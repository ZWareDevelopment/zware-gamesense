package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.command.Command;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class DeathCoords extends Module {
    public DeathCoords() {
        super("DeathCoords", Category.Misc);
    }

    private final Listener<LivingDeathEvent> onLivingDeath = new Listener<>(event -> {
        if(event.getEntity() == mc.player)
            Command.sendClientMessage("Died at: " + event.getEntity().posX + event.getEntity().posY + event.getEntity().posZ);
    });
}
