package dev.zihasz.zware.api.mixin.mixins;

import dev.zihasz.zware.api.event.events.DamageBlockEvent;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.api.event.events.DestroyBlockEvent;
import dev.zihasz.zware.client.module.ModuleManager;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP{

	@Inject(method = "onPlayerDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V"), cancellable = true)
	private void onPlayerDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info){
		ZWareMod.EVENT_BUS.post(new DestroyBlockEvent(pos));
	}

	@Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
	private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir){
		DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
		ZWareMod.EVENT_BUS.post(event);
		if (event.isCancelled()){
			cir.setReturnValue(false);
		}
	}

	//credit cookiedragon234
	@Inject(method = "resetBlockRemoving", at = @At("HEAD"), cancellable = true)
	private void resetBlock(CallbackInfo ci){
		if (ModuleManager.isModuleEnabled("MultiTask")) ci.cancel();
	}
}

