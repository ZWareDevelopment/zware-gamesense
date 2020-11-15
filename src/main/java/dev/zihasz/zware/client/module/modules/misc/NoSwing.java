package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;

public class NoSwing extends Module {
    public NoSwing() {
        super("NoSwing", Category.Misc);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if(event.getPacket() instanceof CPacketAnimation)
            event.cancel();
    });
}
