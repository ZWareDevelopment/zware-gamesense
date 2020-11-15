package dev.zihasz.zware.client.command.commands;

import dev.zihasz.zware.api.config.ConfigStopper;
import dev.zihasz.zware.client.command.Command;

public class SaveConfigCommand extends Command {

	@Override
	public String[] getAlias(){
		return new String[]{"saveconfig", "savecfg"};
	}

	@Override
	public String getSyntax(){
		return "saveconfig";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		ConfigStopper.saveConfig();
		Command.sendClientMessage("Config saved!");
	}
}