package dev.zihasz.zware.client.module.modules.combat;


import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.players.friends.Friends;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.api.util.render.GameSenseTessellator;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.item.ItemBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import java.util.Comparator;
import net.minecraft.tileentity.TileEntityBed;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;

public class BedAura extends Module
{
    Setting.Double range;
    Setting.Double placedelay;
    Setting.Boolean placeesp;
    Setting.Boolean announceUsage;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private EntityPlayer closestTarget;
    private String lastTickTargetName;
    private int bedSlot;
    private BlockPos placeTarget;
    private float rotVar;
    private int blocksPlaced;
    private double diffXZ;
    private boolean firstRun;
    private boolean nowTop;

    public BedAura() {
        super("BedAura", Category.Combat);
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.bedSlot = -1;
        this.nowTop = false;
    }

    @Override
    public void setup() {
        range = registerDouble("Range", "BedAuraRange", 7.0, 0.0,9.0);
        placedelay = registerDouble("Delay", "BedAuraDelay", 15.0, 8.0, 20.0);
        placeesp = registerBoolean("Render","BedAuraRender", true);
        announceUsage = registerBoolean("Announce","BedAuraAnnounce", true);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            this.toggle();
            return;
        }
        MinecraftForge.EVENT_BUS.register(this);
        this.firstRun = true;
        this.blocksPlaced = 0;
        this.playerHotbarSlot = mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }
        MinecraftForge.EVENT_BUS.unregister(this);
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (announceUsage.getValue()) {
            Command.sendClientMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + ChatFormatting.RED.toString() + " Disabled" + ChatFormatting.RESET.toString() + "!");
        }
        this.blocksPlaced = 0;
    }

    @Override
    public void onUpdate() {
        if (mc.player == null) {
            return;
        }
        if (mc.player.dimension == 0) {
            Command.sendClientMessage("You are in the overworld!");
            this.toggle();
        }
        try {
            this.findClosestTarget();
        }
        catch (NullPointerException ex) {}
        if (this.closestTarget == null && mc.player.dimension != 0 && this.firstRun) {
            this.firstRun = false;
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " enabled, " + TextFormatting.WHITE + "waiting for target.");
            }
        }
        if (this.firstRun && this.closestTarget != null && mc.player.dimension != 0) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.getName();
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " enabled" + TextFormatting.WHITE + ", target: " + ChatFormatting.BLUE.toString() + this.lastTickTargetName);
            }
        }
        if (this.closestTarget != null && this.lastTickTargetName != null && !this.lastTickTargetName.equals(this.closestTarget.getName())) {
            this.lastTickTargetName = this.closestTarget.getName();
            if (this.announceUsage.getValue()) {
                Command.sendClientMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " New target: " + ChatFormatting.BLUE.toString() + this.lastTickTargetName);
            }
        }
        try {
            this.diffXZ = mc.player.getPositionVector().distanceTo(this.closestTarget.getPositionVector());
        }
        catch (NullPointerException ex2) {}
        try {
            if (this.closestTarget != null) {
                this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(1.0, 1.0, 0.0));
                this.nowTop = false;
                this.rotVar = 90.0f;
                final BlockPos block1 = this.placeTarget;
                if (!this.canPlaceBed(block1)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(-1.0, 1.0, 0.0));
                    this.rotVar = -90.0f;
                    this.nowTop = false;
                }
                final BlockPos block2 = this.placeTarget;
                if (!this.canPlaceBed(block2)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 1.0, 1.0));
                    this.rotVar = 180.0f;
                    this.nowTop = false;
                }
                final BlockPos block3 = this.placeTarget;
                if (!this.canPlaceBed(block3)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 1.0, -1.0));
                    this.rotVar = 0.0f;
                    this.nowTop = false;
                }
                final BlockPos block4 = this.placeTarget;
                if (!this.canPlaceBed(block4)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 2.0, -1.0));
                    this.rotVar = 0.0f;
                    this.nowTop = true;
                }
                final BlockPos blockt1 = this.placeTarget;
                if (this.nowTop && !this.canPlaceBed(blockt1)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(-1.0, 2.0, 0.0));
                    this.rotVar = -90.0f;
                }
                final BlockPos blockt2 = this.placeTarget;
                if (this.nowTop && !this.canPlaceBed(blockt2)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0, 2.0, 1.0));
                    this.rotVar = 180.0f;
                }
                final BlockPos blockt3 = this.placeTarget;
                if (this.nowTop && !this.canPlaceBed(blockt3)) {
                    this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(1.0, 2.0, 0.0));
                    this.rotVar = 90.0f;
                }
            }
            mc.world.loadedTileEntityList.stream().filter(e -> e instanceof TileEntityBed).filter(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) <= this.range.getValue()).sorted(Comparator.comparing(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()))).forEach(bed -> {
                if (mc.player.dimension != 0) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bed.getPos(), EnumFacing.UP, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f));
                }
                return;
            });
            if (mc.player.ticksExisted % this.placedelay.getValue() == 0 && this.closestTarget != null) {
                this.findBeds();
                final EntityPlayerSP player = mc.player;
                ++player.ticksExisted;
                this.doDaMagic();
            }
        }
        catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void doDaMagic() {
        if (this.diffXZ <= this.range.getValue()) {
            int i = 0;
            while (i < 9) {
                if (this.bedSlot != -1) {
                    break;
                }
                final ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() instanceof ItemBed) {
                    if ((this.bedSlot = i) != -1) {
                        mc.player.inventory.currentItem = this.bedSlot;
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
            this.bedSlot = -1;
            if (this.blocksPlaced == 0 && mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() instanceof ItemBed) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(this.rotVar, 0.0f, mc.player.onGround));
                this.placeBlock(new BlockPos(this.placeTarget), EnumFacing.DOWN);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.blocksPlaced = 1;
                this.nowTop = false;
            }
            this.blocksPlaced = 0;
        }
    }

    private void findBeds() {
        if ((mc.currentScreen == null || !(mc.currentScreen instanceof GuiContainer)) && mc.player.inventory.getStackInSlot(0).getItem() != Items.BED) {
            for (int i = 9; i < 36; ++i) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, mc.player);
                    break;
                }
            }
        }
    }

    private boolean canPlaceBed(final BlockPos pos) {
        return (mc.world.getBlockState(pos).getBlock() == Blocks.AIR || mc.world.getBlockState(pos).getBlock() == Blocks.BED) && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).isEmpty();
    }

    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)mc.world.playerEntities;
        this.closestTarget = null;
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
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (mc.player.getDistance(target) >= mc.player.getDistance(this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }

    private void placeBlock(final BlockPos pos, final EnumFacing side) {
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
    }

    public static boolean isLiving(final Entity e) {
        return e instanceof EntityLivingBase;
    }

    @SubscribeEvent
    public void render(final RenderWorldLastEvent event) {
        if (this.placeTarget == null || mc.world == null || this.closestTarget == null) {
            return;
        }
        if (this.placeesp.getValue()) {
            try {
                final float posx = (float)this.placeTarget.getX();
                final float posy = (float)this.placeTarget.getY();
                final float posz = (float)this.placeTarget.getZ();
                GameSenseTessellator.prepare();
                if (this.rotVar == 90.0f) {
                    GameSenseTessellator.drawBox(new BlockPos(posx - 1.0f, posy, posz), 1, new GSColor(GSColor.WHITE), 6);
                }
                if (this.rotVar == 0.0f) {
                    GameSenseTessellator.drawBox(new BlockPos(posx, posy, posz + 1.0f), 1, new GSColor(GSColor.WHITE), 6);
                }
                if (this.rotVar == -90.0f) {
                    GameSenseTessellator.drawBox(new BlockPos(posx + 1.0f, posy, posz), 1, new GSColor(GSColor.WHITE), 6);
                }
                if (this.rotVar == 180.0f) {
                    GameSenseTessellator.drawBox(new BlockPos(posx, posy, posz - 1.0f), 1, new GSColor(GSColor.WHITE), 6);
                }
            }
            catch (Exception ignored) {}
            GameSenseTessellator.release();
        }
    }
}
