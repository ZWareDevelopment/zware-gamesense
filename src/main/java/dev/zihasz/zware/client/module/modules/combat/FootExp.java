package dev.zihasz.zware.client.module.modules.combat;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.Objects;

public class FootExp extends Module {
    @EventHandler
    final Listener<PacketEvent.Send> packetListener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayer && FootExp.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            event.cancel();
            packet.pitch = 90.0f;
            packet.moving = false;
            packet.onGround = true;
            Objects.requireNonNull(mc.getConnection()).sendPacket(packet);
        }
    });

    public FootExp() {
        super("FootXp", Category.Combat);
    }
}