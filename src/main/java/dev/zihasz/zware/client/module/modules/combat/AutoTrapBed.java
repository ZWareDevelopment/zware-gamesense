package dev.zihasz.zware.client.module.modules.combat;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.players.friends.Friends;
import dev.zihasz.zware.client.module.Module;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;

public class AutoTrapBed extends Module
{
    private int obiSlot;
    private int lastSlot;
    private int tickDelay;
    private boolean firstRun;
    private String lastTickTargetName;
    private EntityPlayer closestTarget;
    private BlockPos target;

    Setting.Double range;

    public AutoTrapBed() {
        super("AutoTrapBed", Category.Combat);
    }

    @Override
    public void setup() {
        range = registerDouble("Range", "AutoBedTrapRange", 7.0, 0.0, 9.0);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            toggle();
            return;
        }
        MinecraftForge.EVENT_BUS.register(this);
        lastSlot = mc.player.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }
        if (lastSlot != -1) {
            mc.player.inventory.currentItem = lastSlot;
        }
        tickDelay = 0;
    }

    @Override
    public void onUpdate() {
        if (mc.player == null) {
            toggle();
            return;
        }
        try {
            findClosestTarget();
        }
        catch (NullPointerException ex) {}
        if (closestTarget == null && firstRun) {
            firstRun = false;
        }
        if (firstRun && closestTarget != null) {
            firstRun = false;
            lastTickTargetName = closestTarget.getName();
        }
        if (closestTarget != null && lastTickTargetName != null && !lastTickTargetName.equals(closestTarget.getName())) {
            lastTickTargetName = closestTarget.getName();
        }
        if (closestTarget != null && mc.player.getPositionVector().distanceTo(closestTarget.getPositionVector()) <= range.getValue()) {
            ++tickDelay;
            obiSlot = findObby();
            if (tickDelay == 1 && obiSlot != -1) {
                mc.player.inventory.currentItem = obiSlot;
            }
            target = new BlockPos(closestTarget.getPositionVector().add(0.0, 3.0, 0.0));
            if (tickDelay == 2) {
                placeBlock(target, EnumFacing.DOWN);
            }
            if (tickDelay == 3) {
                toggle();
            }
        }
    }

    private void placeBlock(final BlockPos pos, final EnumFacing side) {
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private boolean canPlace(final BlockPos pos) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        return block instanceof BlockAir || block instanceof BlockLiquid;
    }

    private int findObby() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockObsidian) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }

    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)mc.world.playerEntities;
        closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!isLiving(target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (closestTarget == null) {
                closestTarget = target;
            }
            else {
                if (mc.player.getDistance(target) >= mc.player.getDistance(closestTarget)) {
                    continue;
                }
                closestTarget = target;
            }
        }
    }

    public static boolean isLiving(final Entity e) {
        return e instanceof EntityLivingBase;
    }
}
