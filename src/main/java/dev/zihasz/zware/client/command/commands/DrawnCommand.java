package dev.zihasz.zware.client.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.ModuleManager;

public class DrawnCommand extends Command {
	boolean found;

	@Override
	public String[] getAlias(){
		return new String[]{"drawn", "visible", "d", "seen"};
	}

	@Override
	public String getSyntax(){
		return "drawn <module>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		found = false;
		ModuleManager.getModules().forEach(m -> {
			if (m.getName().equalsIgnoreCase(args[0])){
				if (m.isDrawn()){
					m.setDrawn(false);
					found = true;
					Command.sendClientMessage(m.getName() + ChatFormatting.RED + " drawn = false");
				} else if (!m.isDrawn()){
					m.setDrawn(true);
					found = true;
					Command.sendClientMessage(m.getName() + ChatFormatting.GREEN + " drawn = true");
				}
			}
		});
		if (!found && args.length == 1) Command.sendClientMessage(ChatFormatting.GRAY + "Module not found!");
	}
}