package dev.zihasz.zware.client.command.commands;

import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.modules.misc.AutoGG;

public class AutoGgCommand extends Command {

	@Override
	public String[] getAlias(){
		return new String[]{"autogg", "autoez"};
	}

	@Override
	public String getSyntax(){
		return "autogg <add | del> <message> (use \"{name}\" for the player's name, use \"_\" for spaces)";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		String s = args[1].replace("_", " ");
		if (args[0].equalsIgnoreCase("add")){
			if (!AutoGG.getAutoGgMessages().contains(s)){
				AutoGG.addAutoGgMessage(s);
				Command.sendClientMessage("Added AutoGG message: " + s);
			} else{
				Command.sendClientMessage("AutoGG list doesn't contain " + s);
			}
		} else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")){
			AutoGG.getAutoGgMessages().remove(s);
			Command.sendClientMessage("Removed AutoGG message: " + s);
		}
		else{
			Command.sendClientMessage(getSyntax());
		}
	}
}