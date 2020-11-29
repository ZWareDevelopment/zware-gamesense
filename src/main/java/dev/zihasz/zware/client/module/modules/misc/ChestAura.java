package dev.zihasz.zware.client.module.modules.misc;

import java.util.concurrent.CopyOnWriteArrayList;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.misc.Wrapper;
import dev.zihasz.zware.client.module.Module;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

/**
 * @author historian
 * @since 11/29/2020
 */
public class ChestAura extends Module {
	private CopyOnWriteArrayList<TileEntity> ignoredList = new CopyOnWriteArrayList<TileEntity>();
	Setting.Double range = registerDouble("Range", "ChestAuraRange", 4, 0.1, 6);
	Setting.Boolean chests = registerBoolean("Chests", "ChestAuraChests", true);
	Setting.Boolean shulkers = registerBoolean("Shulkers", "ChestAuraShulkers", false);
	Setting.Boolean hoppers = registerBoolean("Hoppers", "ChestAuraShulkers", false);
	Setting.Boolean furnaces = registerBoolean("Furnaces", "ChestAuraShulkers", false);
	Setting.Boolean dispensers = registerBoolean("Dispensers", "ChestAuraDispensers", false);
	Setting.Boolean droppers = registerBoolean("Droppers", "ChestAuraDroppers", false);
	
	public ChestAura() {
		super("ChestAura", Category.Misc);
	}
	
	public void onUpdate() {
		for(TileEntity e : ignoredList) {
			if(!mc.world.loadedTileEntityList.contains(e))ignoredList.remove(e);
		}
		for(TileEntity e : mc.world.loadedTileEntityList) {
			if(ignoredList.contains(e) || mc.player.getDistance(e.getPos().x, e.getPos().y, e.getPos().z) > range.getValue())return;
			
			if((e instanceof TileEntityChest && chests.getValue()) || (e instanceof TileEntityShulkerBox && shulkers.getValue()) ||
			(e instanceof TileEntityHopper && hoppers.getValue()) || (e instanceof TileEntityFurnace && furnaces.getValue()) ||
			(e instanceof TileEntityDispenser && dispensers.getValue()) || (e instanceof TileEntityDropper && droppers.getValue())) {
				mc.playerController.processRightClickBlock(mc.player, mc.world, e.getPos(), EnumFacing.UP, new Vec3d(0, 0, 0), EnumHand.MAIN_HAND);
				mc.player.connection.sendPacket(new CPacketCloseWindow());
				ignoredList.add(e);
			}
		}
	}
}
