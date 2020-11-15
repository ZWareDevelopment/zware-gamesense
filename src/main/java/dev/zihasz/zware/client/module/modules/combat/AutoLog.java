package dev.zihasz.zware.client.module.modules.combat;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class AutoLog extends Module {
    Setting.Integer health;
    private boolean shouldLog = false;
    long lastLog = System.currentTimeMillis();

    public AutoLog () {
        super("AutoLog", Category.Combat);
    }

    public void setup() {
        health = registerInteger("Health", "AutoLogHealth", 16,0,36);
    }

    @Override
    public void onUpdate() {
        if (shouldLog) {
            shouldLog = false;
            if (System.currentTimeMillis() - lastLog < 2000) return;
            Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("AutoLogged")));
        }
    }

    @EventHandler
    private Listener<LivingDamageEvent> livingDamageEventListener = new Listener<>(event -> {
        if (mc.player == null) return;
        if (event.getEntity() == mc.player) {
            if (mc.player.getHealth() - event.getAmount() < health.getValue()) {
                log();
            }
        }
    });

    @EventHandler
    private Listener<EntityJoinWorldEvent> entityJoinWorldEventListener = new Listener<>(event -> {
        if (mc.player == null) return;
        if (event.getEntity() instanceof EntityEnderCrystal) {
            log();
        }
    });

    private void log() {
        shouldLog = true;
        lastLog = System.currentTimeMillis();
    }
}
