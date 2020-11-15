package dev.zihasz.zware.client.module.modules.render;

import java.util.ArrayList;
import java.util.List;


import dev.zihasz.zware.client.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class Freecam extends Module
{

	public Freecam()
	{
		super("Freecam", Category.Render);
	}

	private static Vec3d pos = Vec3d.ZERO;

	private static Vec2f pitchyaw = Vec2f.ZERO;

	private static boolean isRidingEntity;

	public static boolean enabled = false;

	private static Entity ridingEntity;

	private static EntityOtherPlayerMP originalPlayer;

	@Override
	public void onEnable()
	{
		if (mc.player == null || mc.world == null)
			return;

		super.onEnable();

		enabled = true;
		if (isRidingEntity = mc.player.isRiding())
		{
			ridingEntity = mc.player.getRidingEntity();
			mc.player.dismountRidingEntity();
		}
		pos = mc.player.getPositionVector();
		pitchyaw = mc.player.getPitchYaw();
		originalPlayer = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
		originalPlayer.copyLocationAndAnglesFrom((Entity) mc.player);
		originalPlayer.rotationYawHead = mc.player.rotationYawHead;
		originalPlayer.inventory = mc.player.inventory;
		originalPlayer.inventoryContainer = mc.player.inventoryContainer;
		mc.world.addEntityToWorld(-100, (Entity) originalPlayer);

		if (isRidingEntity)
		{
			originalPlayer.startRiding(ridingEntity, true);
		}
	}

	@Override
	public void onDisable()
	{
		super.onDisable();

		PacketsToIgnore.clear();

		mc.addScheduledTask(() ->
		{
			EntityPlayerSP entityPlayerSP = mc.player;
			if (entityPlayerSP == null || entityPlayerSP.capabilities == null)
				return;
			PlayerCapabilities gmCaps = new PlayerCapabilities();
			mc.playerController.getCurrentGameType().configurePlayerCapabilities(gmCaps);
			PlayerCapabilities capabilities = entityPlayerSP.capabilities;
			capabilities.allowFlying	 = gmCaps.allowFlying	;
			capabilities.isFlying = (gmCaps.allowFlying	 && capabilities.isFlying);
			capabilities.setFlySpeed(gmCaps.getFlySpeed());
		});

		if (mc.player == null || originalPlayer == null)
			return;

		enabled = false;
		originalPlayer.dismountRidingEntity();
		mc.world.removeEntityFromWorld(-100);
		originalPlayer = null;
		mc.player.noClip = false;
		mc.player.setVelocity(0.0D, 0.0D, 0.0D);
		if (isRidingEntity)
		{
			mc.player.startRiding(ridingEntity, true);
			ridingEntity = null;
			isRidingEntity = false;
		}
	}

	@Override
	public void onUpdate()
	{
		mc.addScheduledTask(() ->
		{
			if (mc.player == null || mc.player.capabilities == null)
				return;
			mc.player.capabilities.allowFlying   = true;
			mc.player.capabilities.isFlying = true;
		});
		mc.player.capabilities.setFlySpeed(0.5F);
		mc.player.noClip = true;
		mc.player.onGround = false;
		mc.player.fallDistance = 0.0F;
		if (!mc.gameSettings.keyBindForward.isPressed() && !mc.gameSettings.keyBindBack.isPressed() && !mc.gameSettings.keyBindLeft.isPressed()
				&& !mc.gameSettings.keyBindRight.isPressed() && !mc.gameSettings.keyBindJump.isPressed() && !mc.gameSettings.keyBindSneak.isPressed())
			mc.player.setVelocity(0.0D, 0.0D, 0.0D);
	}

	private List<CPacketPlayer> PacketsToIgnore = new ArrayList<CPacketPlayer>();

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if (!enabled || originalPlayer == null || mc.player == null)
			return;
		pos = mc.player.getPositionVector();
	}

	@SubscribeEvent
	public void onEntityRender(RenderLivingEvent.Pre<?> event)
	{
		if (originalPlayer != null && mc.player != null && mc.player

				.equals(event.getEntity()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onRenderTag(RenderLivingEvent.Specials.Pre<?> event)
	{
		if (originalPlayer != null && mc.player != null && mc.player

				.equals(event.getEntity()))
			event.setCanceled(true);
	}
}
