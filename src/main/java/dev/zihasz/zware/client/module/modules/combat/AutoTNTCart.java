package dev.zihasz.zware.client.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.players.friends.Friends;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.api.util.render.GameSenseTessellator;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

public class AutoTNTCart extends Module {
    private boolean firstSwap;
    private boolean secondSwap;
    private boolean beginPlacing;
    private int lighterSlot;
    private EntityPlayer closestTarget;
    private BlockPos targetPos;
    Setting.Boolean announceUsage;
    Setting.Boolean debug;
    Setting.Double carts;

    int tickDelay = 0;

    public AutoTNTCart() {
        super("5b5t AutoTNTCart", Category.Combat);
        this.firstSwap = true;
        this.secondSwap = true;
        this.beginPlacing = false;
    }

    @Override
    public void setup() {
        announceUsage = registerBoolean(    "AnnounceUsage",    "AutoTNTCartAnnounceUsage", true);
        debug = registerBoolean(            "Debug",            "AutoTNTCartDebug",         false);
        carts = registerDouble(             "PlaceDuration",    "AutoTNTCartPlaceDuration", 60.0, 1.0, 100.0);
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            this.toggle();
            return;
        }
        ZWareMod.EVENT_BUS.subscribe(this);
        MinecraftForge.EVENT_BUS.register(this);
        this.tickDelay = 0;
        try {
            this.findClosestTarget();
        }
        catch (Exception ex) {}
        if (this.closestTarget != null) {
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage("Attempting to TNT cart " + ChatFormatting.BLUE.toString() + this.closestTarget.getName() + ChatFormatting.WHITE.toString() + " ...");
            }
            this.targetPos = new BlockPos(this.closestTarget.getPositionVector());
        }
        else {
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage("No target within range to TNT Cart.");
            }
            this.toggle();
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoTNTCart" + TextFormatting.BLUE + "]" + ChatFormatting.RED.toString() + " Disabled!");
        }
        this.firstSwap = true;
        this.secondSwap = true;
        this.beginPlacing = false;
        this.tickDelay = 0;
        ZWareMod.EVENT_BUS.unsubscribe(this);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        final int tntSlot = this.findTNTCart();
        final int railSlot = this.findRail();
        if (railSlot > -1 && this.firstSwap) {
            mc.player.inventory.currentItem = railSlot;
            this.firstSwap = false;
            this.placeBlock(this.targetPos, EnumFacing.DOWN);
            if (this.debug.getValue()) {
                Command.sendClientMessage("place rail");
            }
        }
        if (tntSlot > -1 && this.secondSwap && !this.firstSwap) {
            mc.player.inventory.currentItem = tntSlot;
            this.secondSwap = false;
            this.beginPlacing = true;
            if (this.debug.getValue()) {
                Command.sendClientMessage("swap tnt & place");
            }
        }
        if (!this.firstSwap && !this.secondSwap && this.beginPlacing) {
            if (this.tickDelay > 0) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.targetPos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            if (this.tickDelay == this.carts.getValue()) {
                final int pickSlot = this.findPick();
                if (pickSlot > -1) {
                    mc.player.inventory.currentItem = pickSlot;
                }
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.targetPos, EnumFacing.DOWN));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.targetPos, EnumFacing.DOWN));
                if (this.debug.getValue()) {
                    Command.sendClientMessage("break rail");
                }
            }
            if (this.tickDelay == this.carts.getValue() + 5) {
                final int flint = this.findFlint();
                if (flint > -1) {
                    mc.player.inventory.currentItem = flint;
                    this.placeBlock(this.targetPos, EnumFacing.DOWN);
                }
                else {
                    this.invGrabFlint();
                    mc.player.inventory.currentItem = 0;
                    this.placeBlock(this.targetPos, EnumFacing.DOWN);
                }
                this.toggle();
            }
        }
    }

    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>) mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!isLiving((Entity) target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (mc.player.getDistance(target) > 6.0f) {
                continue;
            }
            if (this.closestTarget != null) {
                continue;
            }
            this.closestTarget = target;
        }
    }

    public static boolean isLiving(final Entity e) {
        return e instanceof EntityLivingBase;
    }

    private int findTNTCart() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (!(stack.getItem() instanceof ItemBlock)) {
                    final Item item = stack.getItem();
                    if (item instanceof ItemMinecart) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }

    private int findRail() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ( (ItemBlock) stack.getItem() ) .getBlock();
                    if (block instanceof BlockRailBase) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }

    private int findFlint() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (!(stack.getItem() instanceof ItemBlock)) {
                    final Item item = stack.getItem();
                    if (item instanceof ItemFlintAndSteel) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }

    private void invGrabFlint() {
        if ((!(mc.currentScreen instanceof GuiContainer)) && mc.player.inventory.getStackInSlot(0).getItem() != Items.FLINT_AND_STEEL) {
            for (int i = 9; i < 36; ++i) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.FLINT_AND_STEEL) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, mc.player);
                    this.lighterSlot = i;
                    break;
                }
            }
        }
    }

    private int findPick() {
        int pickSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemPickaxe) {
                    pickSlot = i;
                    break;
                }
            }
        }
        return pickSlot;
    }

    private void placeBlock(final BlockPos pos, final EnumFacing side) {
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @Mod.EventHandler
    public void render(final RenderWorldLastEvent event) {
        if (mc.world == null || this.targetPos == null) {
            return;
        }
        try {
            final float posX = (float)this.targetPos.getX();
            final float posY = (float)this.targetPos.getY();
            final float posZ = (float)this.targetPos.getZ();
            GameSenseTessellator.prepare();
            GameSenseTessellator.drawBox(new BlockPos(posX,posY,posZ), 1, GSColor.fromHSB(0,100,100), 6);
        }
        catch (Exception ex) {}
        GameSenseTessellator.release();
    }
}
