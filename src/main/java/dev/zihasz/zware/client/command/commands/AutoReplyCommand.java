package dev.zihasz.zware.client.command.commands;

import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.modules.misc.AutoReply;

public class AutoReplyCommand extends Command {

	@Override
	public String[] getAlias(){
		return new String[]{
				"autoreply", "reply"
		};
	}

	@Override
	public String getSyntax(){
		return "autoreply <message (use \"_\" for spaces)>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		if (args[0] != null && !args[0].equalsIgnoreCase("")){
			AutoReply.setReply(args[0].replace("_", " "));
			Command.sendClientMessage("AutoReply message set to " + AutoReply.getReply());
		} else{
			Command.sendClientMessage(getSyntax());
		}
	}
}