package dev.zihasz.zware.client.module.modules.combat;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.misc.Wrapper;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.module.Module;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public class BedBomb extends Module {
    public BedBomb() { super("BedBomb", Category.Combat); }

    Setting.Integer range;
    Setting.Mode swing;

    public void setup() {
        ArrayList<String> swingModes = new ArrayList<String>();
        swingModes.add("None");
        swingModes.add("MainHand");
        swingModes.add("OffHand");

        range = registerInteger("Range", "BedBombRange", 4,1,5);
        swing = registerMode("Swing","BedBombSwing",swingModes, "MainHand");
    }

    @Override
    protected void onEnable() {
        ZWareMod.EVENT_BUS.subscribe(this);
    }

    @Override
    protected void onDisable() {
        ZWareMod.EVENT_BUS.unsubscribe(this);
    }

    @Override
    public void onUpdate() {
        TileEntityBed bed = mc.world.loadedTileEntityList.stream()
                .filter(entity -> entity instanceof TileEntityBed)
                .filter(e -> mc.player.getDistance(e.getPos().x, e.getPos().y, e.getPos().z) <= range.getValue())
                .map(entity -> (TileEntityBed) entity)
                .min(Comparator.comparing(c -> mc.player.getDistance(c.getPos().x, c.getPos().y, c.getPos().z) <= range.getValue()))
                .orElse(null);

        if (bed != null) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));

            switch (swing.getValue()) {
                case "None":
                    mc.playerController.processRightClickBlock(Wrapper.getPlayer(), mc.world, bed.getPos(), EnumFacing.DOWN, new Vec3d(0,0,0), EnumHand.MAIN_HAND);
                    break;

                case "MainHand":
                    mc.playerController.processRightClickBlock(Wrapper.getPlayer(), mc.world, bed.getPos(), EnumFacing.DOWN, new Vec3d(0,0,0), EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    break;

                case "OffHand":
                    mc.playerController.processRightClickBlock(Wrapper.getPlayer(), mc.world, bed.getPos(), EnumFacing.DOWN, new Vec3d(0,0,0), EnumHand.OFF_HAND);
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
                    break;
            }

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    private void /*Block*/ findNearestBed() {

    }
}
