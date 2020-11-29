package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;

/*
* @Author Zihasz
* @Description  cancel CPacketPlayerTryUseItem packets
*/

public class AntiPacket extends Module {
    public AntiPacket() {
        super("AntiPacket", Category.Exploits);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketPlayerTryUseItem)
            event.cancel();
    });
}
