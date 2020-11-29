package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PlayerJoinEvent;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class AutoQMain extends Module {
    public AutoQMain() {
        super("AutoQMain", Category.Misc);
    }

    Setting.Boolean only2b2t;

    public void setup() {
        only2b2t = registerBoolean("Only2b2t", "AutoQMainOnly2b2t", false);
    }

    @EventHandler
    private Listener<PlayerJoinEvent> playerJoin = new Listener<>(event -> {
        if(mc.player != null && !mc.serverName.isEmpty()) {
            if (only2b2t.getValue() && mc.serverName.equals("2b2t.org")) {
                mc.player.sendChatMessage("/queue main");
            } else if (!only2b2t.getValue()) {
                mc.player.sendChatMessage("/queue main");
            }
        }
    });
}
