package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.client.module.Module;
import io.netty.buffer.Unpooled;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class NoHandshake extends Module {
    public NoHandshake() {
        super("NoHandshake", Category.Misc);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof FMLProxyPacket && !mc.isSingleplayer()) {
            event.cancel();
        }
        try {
            if (event.getPacket() instanceof CPacketCustomPayload) {
                CPacketCustomPayload packet = (CPacketCustomPayload) event.getPacket();
                if (packet.getChannelName().equals("MC|Brand")) {
                    packet.writePacketData(new PacketBuffer(Unpooled.buffer()).writeString("vanilla"));
                }
            }
        } catch (Exception ignored) {}
    });
}
