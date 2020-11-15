package dev.zihasz.zware.client.command.commands;

import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.clickgui.ClickGUI;
import dev.zihasz.zware.client.command.Command;

/**
 * @Author Hoosiers on 10/03/20
 */

public class FixGuiCommand extends Command {

    @Override
    public String[] getAlias(){
        return new String[] {
                "fixgui", "fixhud", "fix"
        };
    }

    @Override
    public String getSyntax(){
        return "fixgui";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception{
        try {
            //resets ClickGui to default positions
            ZWareMod.getInstance().clickGUI = new ClickGUI();
            Command.sendClientMessage("ClickGui positions reset!");
        }
        catch (Exception e){
            Command.sendClientMessage("There was an error in resetting ClickGui positions!");
        }
    }
}