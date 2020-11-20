package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.api.util.misc.Text;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.module.ModuleManager;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class PornSuffix extends Module {
    public PornSuffix() {
        super("PornSuffix", Category.Misc);
    }

    boolean disabledCS = false;

    @Override
    protected void onEnable() {
        if (ModuleManager.isModuleEnabled("ChatSuffix")) {
            ModuleManager.getModuleByName("ChatSuffix").disable();
            disabledCS = true;
        }
    }

    @Override
    protected void onDisable() {
        if (disabledCS) {
            ModuleManager.getModuleByName("ChatSuffix").enable();
        }
        disabledCS = false;
    }

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {

            CPacketChatMessage msgPacket = (CPacketChatMessage) event.getPacket();
            String msg = msgPacket.getMessage();
            if (Text.isCommand(msg)) return;

            boolean trigger = false;
            ArrayList<String> hornyWords = new ArrayList<String>();
            hornyWords.add("Horny");
            hornyWords.add("Sex");
            hornyWords.add("Cum");
            hornyWords.add("Daddy");
            hornyWords.add("Sex");

            for (String word : hornyWords) {
                if (msg.contains(word)) {
                    trigger = true;
                }
            }

            if (trigger) {
                msg += Text.suffixBuilder("", "|");

                if (msg.length() >= 256)
                    msg = msg.substring(0,256);
                msgPacket.message = msg;
            }
        }
    });
}
