package dev.zihasz.zware.api.mixin.mixins;

import dev.zihasz.zware.api.event.events.BossbarEvent;
import dev.zihasz.zware.client.ZWareMod;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay{

	@Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
	private void renderBossHealth(CallbackInfo ci){
		BossbarEvent event = new BossbarEvent();
		ZWareMod.EVENT_BUS.post(event);
		if (event.isCancelled()){
			ci.cancel();
		}
	}
}
