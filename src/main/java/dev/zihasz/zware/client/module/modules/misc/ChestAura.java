package dev.zihasz.zware.client.module.modules.misc;

import java.util.concurrent.CopyOnWriteArrayList;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.module.Module;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

/**
 * @author historian
 * @since 11/29/2020
 */
public class ChestAura extends Module {
	private static CopyOnWriteArrayList<TileEntity> ignoredList = new CopyOnWriteArrayList<>();
	Setting.Double 	range;
	Setting.Boolean chests;
	Setting.Boolean shulkers;
	Setting.Boolean hoppers;
	Setting.Boolean furnaces;
	Setting.Boolean dispensers;
	Setting.Boolean droppers;
	
	public ChestAura() {
		super("ChestAura", Category.Misc);
	}

	public void setup() {
		range = 		registerDouble("Range", "ChestAuraRange", 4, 0.1, 6);
		chests = 		registerBoolean("Chests", "ChestAuraChests", true);
		shulkers = 		registerBoolean("Shulkers", "ChestAuraShulkers", false);
		hoppers = 		registerBoolean("Hoppers", "ChestAuraShulkers", false);
		furnaces =		registerBoolean("Furnaces", "ChestAuraShulkers", false);
		dispensers =	registerBoolean("Dispensers", "ChestAuraDispensers", false);
		droppers = 		registerBoolean("Droppers", "ChestAuraDroppers", false);
	}

	public void onUpdate() {
		ignoredList.removeIf(e -> !mc.world.loadedTileEntityList.contains(e));

		for(TileEntity e : mc.world.loadedTileEntityList) {
			if(ignoredList.contains(e) || mc.player.getDistance(e.getPos().x, e.getPos().y, e.getPos().z) > range.getValue())return;
			
			if ( (e instanceof TileEntityChest && chests.getValue()) || (e instanceof TileEntityShulkerBox && shulkers.getValue()) ||
				 (e instanceof TileEntityHopper && hoppers.getValue()) || (e instanceof TileEntityFurnace && furnaces.getValue()) ||
				 (e instanceof TileEntityDispenser && dispensers.getValue()) || (e instanceof TileEntityDropper && droppers.getValue()))
			{
				mc.playerController.processRightClickBlock(mc.player, mc.world, e.getPos(), EnumFacing.UP, new Vec3d(0, 0, 0), EnumHand.MAIN_HAND);
				mc.player.connection.sendPacket(new CPacketCloseWindow());
				ignoredList.add(e);
			}
		}
	}
}
