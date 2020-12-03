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
	private CopyOnWriteArrayList<TileEntity> ignoredList = new CopyOnWriteArrayList<>();
	Setting.Double range = registerDouble("Range", "ChestAuraRange", 4, 0.1, 6);
	Setting.Boolean chests = registerBoolean("Chests", "ChestAuraChests", true);
	Setting.Boolean shulkers = registerBoolean("Shulkers", "ChestAuraShulkers", false);
	Setting.Boolean hoppers = registerBoolean("Hoppers", "ChestAuraHoppers", false);
	Setting.Boolean furnaces = registerBoolean("Furnaces", "ChestAuraFurnaces", false);
	Setting.Boolean dispensers = registerBoolean("Dispensers", "ChestAuraDispensers", false);
    Setting.Boolean droppers = registerBoolean("Droppers", "ChestAuraDroppers", false);
	
	public ChestAura() {
		super("ChestAura", Category.Misc);
	}

	public void onUpdate() {
		ignoredList.removeIf(e -> !mc.world.loadedTileEntityList.contains(e));

		for(TileEntity e : mc.world.loadedTileEntityList) {
			if(ignoredList.contains(e) || mc.player.getDistance(e.getPos().x, e.getPos().y, e.getPos().z) > range.getValue())return;
			
			if ( (e instanceof TileEntityChest && chests.getValue()) || (e instanceof TileEntityShulkerBox && shulkers.getValue()) ||
				 (e instanceof TileEntityHopper && hoppers.getValue()) || (e instanceof TileEntityFurnace && furnaces.getValue()) ||
				 (e instanceof TileEntityDispenser && dispensers.getValue()) || (e instanceof TileEntityDropper && droppers.getValue()))
			{
				mc.playerController.processRightClickBlock(mc.player, mc.world, e.getPos(), EnumFacing.UP, new Vec3d(990, 0, 0), EnumHand.MAIN_HAND);
				mc.player.connection.sendPacket(new CPacketCloseWindow());
				ignoredList.add(e);
			}
		}
	}
}
