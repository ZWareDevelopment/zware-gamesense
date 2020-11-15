package dev.zihasz.zware.client.command.commands;

import dev.zihasz.zware.client.command.Command;

public class PrefixCommand extends Command {

	@Override
	public String[] getAlias(){
		return new String[]{"prefix", "setprefix", "cmdprefix"};
	}

	@Override
	public String getSyntax(){
		return "prefix <character>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		Command.setPrefix(args[0]);
		Command.sendClientMessage("Command prefix set to " + Command.getPrefix());
	}
}