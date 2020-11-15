package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.util.misc.Discord;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.module.ModuleManager;
// import com.gamesense.client.module.modules.movement.ElytraFly;

public class DiscordRPCModule extends Module {
    public DiscordRPCModule(){
        super("DiscordRPC", Category.Misc);
        setDrawn(false);
    }

    @Override
    public void onUpdate() {
        if(ModuleManager.isModuleEnabled("AutoCrystal"))
        {
            Discord.updateRPCState("PvPing");
        }
    }

    public void onEnable(){
        Discord.startRPC();
        if (mc.player != null || mc.world != null){
            Command.sendClientMessage("Discord RPC started!");
        }
    }
    public void onDisable(){
        Discord.stopRPC();
        if (mc.player != null || mc.world != null) {
            Command.sendClientMessage("Discord RPC stopped!");
        }
    }
}