package dev.zihasz.zware.client.module.modules.misc;

import java.util.UUID;

import dev.zihasz.zware.client.module.Module;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;

public class FakePlayer extends Module {
    public FakePlayer(){
        super("FakePlayer", Category.Misc);
    }

    private EntityOtherPlayerMP clonedPlayer;

    public void onEnable(){

        if (mc.player == null || mc.player.isDead){
            disable();
            return;
        }

    	clonedPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"), "Fit"));
        clonedPlayer.copyLocationAndAnglesFrom(mc.player);
        clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
        clonedPlayer.rotationYaw = mc.player.rotationYaw;
        clonedPlayer.rotationPitch = mc.player.rotationPitch;
        clonedPlayer.setGameType(GameType.SURVIVAL);
        clonedPlayer.setHealth(20);
        mc.world.addEntityToWorld(-1234, clonedPlayer);
        clonedPlayer.onLivingUpdate();
    }

    public void onDisable(){
        if (mc.world != null) {
            mc.world.removeEntityFromWorld(-1234);
        }
    }
}