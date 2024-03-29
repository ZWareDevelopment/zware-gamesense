package dev.zihasz.zware.api.mixin.mixins;

import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.api.event.events.PlayerMoveEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer{

	public MixinEntityPlayerSP(){
		super(null, null);
	}

	@Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
	public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z){
		PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
		ZWareMod.EVENT_BUS.post(moveEvent);
		if (moveEvent.isCancelled()){
		}
		super.move(type, moveEvent.x, moveEvent.y, moveEvent.z);
	}
}
