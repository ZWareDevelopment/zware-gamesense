package dev.zihasz.zware.api.event.events;

import dev.zihasz.zware.api.event.GameSenseEvent;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenDisplayedEvent extends GameSenseEvent {
	private final GuiScreen guiScreen;
	public GuiScreenDisplayedEvent(GuiScreen screen){
		super();
		guiScreen = screen;
	}

	public GuiScreen getScreen(){
		return guiScreen;
	}
}