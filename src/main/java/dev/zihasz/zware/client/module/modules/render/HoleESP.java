package dev.zihasz.zware.client.module.modules.render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import dev.zihasz.zware.api.event.events.RenderEvent;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.api.util.render.GameSenseTessellator;
import dev.zihasz.zware.api.util.world.GeometryMasks;
import dev.zihasz.zware.client.module.Module;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleESP extends Module{

	public HoleESP(){
		super("HoleESP", Category.Render);
	}

	//settings
	public static Setting.Integer rangeS;
	Setting.Boolean hideOwn;
	Setting.Boolean flatOwn;
	Setting.Mode mode;
	Setting.Mode type;
	Setting.Double slabHeight;
	Setting.Integer width;
	Setting.ColorSetting bedrockColor;
	Setting.ColorSetting otherColor;

	//load settings
	public void setup(){
		rangeS = registerInteger("Range", "Range", 5, 1, 20);
		hideOwn = registerBoolean("Hide Own", "HideOwn", false);
		flatOwn = registerBoolean("Flat Own", "FlatOwn", false);

		ArrayList<String> render = new ArrayList<>();
		render.add("Outline");
		render.add("Fill");
		render.add("Both");

		ArrayList<String> modes = new ArrayList<>();
		modes.add("Air");
		modes.add("Ground");
		modes.add("Flat");
		modes.add("Slab");
		modes.add("Double");
		
		type = registerMode("Render", "Render", render, "Both");
		mode = registerMode("Mode", "Mode", modes, "Air");

		slabHeight = registerDouble("Slab Height", "SlabHeight", 0.5, 0.1, 1.5);
		width=registerInteger("Width","Width",1,1,10);
		bedrockColor=registerColor("Bedrock Color","BedrockColor",new GSColor(0,255,0));
		otherColor=registerColor("Obsidian Color","ObsidianColor",new GSColor(255,0,0));
	}

	//defines the render borders
	private final BlockPos[] surroundOffset ={
			new BlockPos(0, -1, 0), // down
			new BlockPos(0, 0, -1), // north
			new BlockPos(1, 0, 0), // east
			new BlockPos(0, 0, 1), // south
			new BlockPos(-1, 0, 0) // west
	};

	//used to register safe holes for rendering
	private ConcurrentHashMap<BlockPos, Boolean> safeHoles;

	//defines the area for the client to search
	public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y){
		List<BlockPos> circleblocks = new ArrayList<>();
		int cx = loc.getX();
		int cy = loc.getY();
		int cz = loc.getZ();
		for (int x = cx - (int) r; x <= cx + r; x++){
			for (int z = cz - (int) r; z <= cz + r; z++){
				for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++){
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
					if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))){
						BlockPos l = new BlockPos(x, y + plus_y, z);
						circleblocks.add(l);
					}
				}
			}
		}
		return circleblocks;
	}

	//gets the players location
	public static BlockPos getPlayerPos(){
		return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
	}

	//finds safe holes to render
	@Override
	public void onUpdate(){
		if (safeHoles == null){
			safeHoles = new ConcurrentHashMap<>();
		}
		else{
			safeHoles.clear();
		}

		int range = (int) Math.ceil(rangeS.getValue());

		List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, range, false, true, 0);
		for (BlockPos pos : blockPosList){

			if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)){
				continue;
			}
			if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)){
				continue;
			}
			if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)){
				continue;
			}
			if (this.hideOwn.getValue() && pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))){
				continue;
			}

			boolean isSafe = true;
			boolean isBedrock = true;

			for (BlockPos offset : surroundOffset){
				Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
				if (block != Blocks.BEDROCK){
					isBedrock = false;
				}
				if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL){
					isSafe = false;
					break;
				}
			}
			if (isSafe){
				safeHoles.put(pos, isBedrock);
			}
		}
	}

	//renders safe holes
	@Override
	public void onWorldRender(final RenderEvent event){
		if (mc.player == null || safeHoles == null){
			return;
		}
		if (safeHoles.isEmpty()){
			return;
		}
		
		safeHoles.forEach((blockPos, isBedrock) -> {
			if (mode.getValue().equalsIgnoreCase("Air")) drawBox(blockPos,isBedrock);
			else if (mode.getValue().equalsIgnoreCase("Ground")) drawBox(blockPos.down(),isBedrock);
			else if (mode.getValue().equalsIgnoreCase("Flat")) drawBox(blockPos,isBedrock);
			else if (mode.getValue().equalsIgnoreCase("Slab")) drawBox(blockPos, isBedrock);
			else if (mode.getValue().equalsIgnoreCase("Double")) drawBox(blockPos, isBedrock);
		});
		safeHoles.forEach((blockPos, isBedrock) -> {
			drawOutline(blockPos,width.getValue(),isBedrock);
		});
	}

	private GSColor getColor (boolean isBedrock, int alpha) {
		GSColor c;
		if (isBedrock) c=bedrockColor.getValue();
		else c=otherColor.getValue();
		return new GSColor(c,alpha);
	}

	//renders fill
	private void drawBox(BlockPos blockPos, boolean isBedrock) {
		if (type.getValue().equalsIgnoreCase("Fill") || type.getValue().equalsIgnoreCase("Both")) {
			GSColor color=getColor(isBedrock,50);
			if (mode.getValue().equalsIgnoreCase("Air")) {
				if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
					GameSenseTessellator.drawBox(blockPos, 1, color, GeometryMasks.Quad.DOWN);
				}
				else {
					GameSenseTessellator.drawBox(blockPos, 1, color, GeometryMasks.Quad.ALL);
				}
			}
			else if (mode.getValue().equalsIgnoreCase("Double")) {
				if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
					GameSenseTessellator.drawBox(blockPos, 1, color, GeometryMasks.Quad.DOWN);
				}
				else {
					GameSenseTessellator.drawBox(blockPos, 2, color, GeometryMasks.Quad.ALL);
				}
			}
			else if (mode.getValue().equalsIgnoreCase("Ground")) {
				GameSenseTessellator.drawBox(blockPos, 1, color, GeometryMasks.Quad.ALL);
			}
			else if (mode.getValue().equalsIgnoreCase("Slab")) {
				if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
					GameSenseTessellator.drawBox(blockPos, 1, color, GeometryMasks.Quad.DOWN);
				}
				else {
					GameSenseTessellator.drawBox(blockPos, slabHeight.getValue(), color, GeometryMasks.Quad.ALL);
				}
			}
			else if (mode.getValue().equalsIgnoreCase("Flat")) {
				GameSenseTessellator.drawBox(blockPos, 1, color, GeometryMasks.Quad.DOWN);
			}
		}
	}

	//renders outline
	private void drawOutline(BlockPos blockPos, int width, boolean isBedrock) {
		GSColor color=getColor(isBedrock,255);
		if (type.getValue().equalsIgnoreCase("Outline") || type.getValue().equalsIgnoreCase("Both")) {
			if (mode.getValue().equalsIgnoreCase("Air")) {
				if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
					GameSenseTessellator.drawBoundingBoxWithSides(blockPos, width, color, GeometryMasks.Quad.DOWN);
				} else {
					GameSenseTessellator.drawBoundingBox(blockPos, 1, width, color);
				}
			}
			else if (mode.getValue().equalsIgnoreCase("Flat")) {
				GameSenseTessellator.drawBoundingBoxWithSides(blockPos, width, color, GeometryMasks.Quad.DOWN);
			}
			else if (mode.getValue().equalsIgnoreCase("Ground")) {
				GameSenseTessellator.drawBoundingBox(blockPos.down(), 1, width, color);
			}
			else if (mode.getValue().equalsIgnoreCase("Double")) {
				if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
					GameSenseTessellator.drawBoundingBoxWithSides(blockPos, width, color, GeometryMasks.Quad.DOWN);
				} else {
					GameSenseTessellator.drawBoundingBox(blockPos, 2, width, color);
				}
			}
			else if (mode.getValue().equalsIgnoreCase("Slab")) {
				if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
					GameSenseTessellator.drawBoundingBoxWithSides(blockPos, width, color, GeometryMasks.Quad.DOWN);
				} else {
					GameSenseTessellator.drawBoundingBox(blockPos, slabHeight.getValue(), width, color);
				}
			}
		}
	}
}