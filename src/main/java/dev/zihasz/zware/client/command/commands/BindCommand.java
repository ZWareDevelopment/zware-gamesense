package dev.zihasz.zware.client.command.commands;

import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.ModuleManager;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

	@Override
	public String[] getAlias(){
		return new String[]{"bind", "b"};
	}

	@Override
	public String getSyntax(){
		return "bind <module> <key>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		int key = Keyboard.getKeyIndex(args[1].toUpperCase());
		ModuleManager.getModules().forEach(m -> {
			if (args[0].equalsIgnoreCase(m.getName())){
				m.setBind(key);
				Command.sendClientMessage(args[0] + " bound to " + args[1].toUpperCase());
			}
		});
	}
}