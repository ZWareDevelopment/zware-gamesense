package dev.zihasz.zware.client.command.commands;

import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.api.util.font.CFontRenderer;

import java.awt.*;

public class FontCommand extends Command {

	@Override
	public String[] getAlias(){
		return new String[]{
				"font", "setfont", "newfont", "chatfont"
		};
	}

	@Override
	public String getSyntax(){
		return "font <name> <size>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		String font = args[0].replace("_", " ");
		int size = Integer.parseInt(args[1]);
		ZWareMod.fontRenderer = new CFontRenderer(new Font(font, Font.PLAIN, size), true, false);
		ZWareMod.fontRenderer.setFontName(font);
		ZWareMod.fontRenderer.setFontSize(size);
		Command.sendClientMessage("Set the font to " + font + ", size " + size);
	}
}