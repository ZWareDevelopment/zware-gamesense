package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.GuiScreenDisplayedEvent;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module {
	public AutoRespawn(){
		super("AutoRespawn", Category.Misc);
	}

	Setting.Boolean coords;

	public void setup(){
		coords = registerBoolean("Coords", "Coords", false);
	}

	@EventHandler
	private final Listener<GuiScreenDisplayedEvent> listener = new Listener<>(event -> {
		if (event.getScreen() instanceof GuiGameOver){
			if (coords.getValue())
				Command.sendClientMessage(String.format("You died at x%d y%d z%d", (int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ));
			if (mc.player != null)
				mc.player.respawnPlayer();
			mc.displayGuiScreen(null);
		}
	});

	public void onEnable(){
		ZWareMod.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		ZWareMod.EVENT_BUS.unsubscribe(this);
	}
}