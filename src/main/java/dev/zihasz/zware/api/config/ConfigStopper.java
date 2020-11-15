package dev.zihasz.zware.api.config;

import dev.zihasz.zware.client.ZWareMod;

import java.io.IOException;

public class ConfigStopper extends Thread {

    @Override
    public void run(){
        saveConfig();
    }

    public static void saveConfig(){
        try {
            ZWareMod.getInstance().saveConfig.saveConfig();
            ZWareMod.getInstance().saveConfig.saveModules();
            ZWareMod.getInstance().saveConfig.saveEnabledModules();
            ZWareMod.getInstance().saveConfig.saveModuleKeybinds();
            ZWareMod.getInstance().saveConfig.saveDrawnModules();
            ZWareMod.getInstance().saveConfig.saveCommandPrefix();
            ZWareMod.getInstance().saveConfig.saveCustomFont();
            ZWareMod.getInstance().saveConfig.saveFriendsList();
            ZWareMod.getInstance().saveConfig.saveEnemiesList();
            ZWareMod.getInstance().saveConfig.saveClickGUIPositions();
            ZWareMod.getInstance().saveConfig.saveAutoGG();
            ZWareMod.getInstance().saveConfig.saveAutoReply();
            ZWareMod.log.info("Saved Config!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}