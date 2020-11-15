package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.io.File;

public class ChatDownloader extends Module {
    public ChatDownloader() {
        super("ChatDownloader", Category.Misc);
    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            
        }
    });
}
