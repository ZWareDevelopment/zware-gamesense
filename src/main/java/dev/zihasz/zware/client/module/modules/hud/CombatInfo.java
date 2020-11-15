package dev.zihasz.zware.client.module.modules.hud;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.font.FontUtils;
import dev.zihasz.zware.api.util.players.friends.Friends;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.module.ModuleManager;
import dev.zihasz.zware.client.module.modules.combat.AutoCrystal;
import dev.zihasz.zware.client.module.modules.gui.ColorMain;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CombatInfo extends Module {
    public CombatInfo(){
        super("CombatInfo", Category.HUD);
    }

    Setting.Integer posX;
    Setting.Integer posY;
    Setting.ColorSetting color1;
    Setting.ColorSetting color2;

    public void setup(){
        posX = registerInteger("X", "X", 0, 0, 1000);
        posY = registerInteger("Y", "Y", 150, 0, 1000);
        color1 = registerColor("On","On", new GSColor(0, 255, 0, 255));
        color2 = registerColor("Off", "Off", new GSColor(255, 0, 0, 255));
    }

    private int totems;
    private BlockPos[] surroundOffset;


    public void onRender(){
        GSColor on = new GSColor(color1.getValue());
        GSColor off = new GSColor(color2.getValue());

        if (ModuleManager.isModuleEnabled("AutoCrystal")) {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "AC: ENBL", posX.getValue(), posY.getValue(), on);
        } else {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "AC: DSBL", posX.getValue(), posY.getValue(), off);
        }

        if (ModuleManager.isModuleEnabled("KillAura")) {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "KA: ENBL", posX.getValue(), posY.getValue() + 10, on);
        } else {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "KA: DSBL", posX.getValue(), posY.getValue() + 10, off);
        }

        if (ModuleManager.isModuleEnabled("Surround")) {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "SU: ENBL", posX.getValue(), posY.getValue() + 20, on);
        } else {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "SU: DSBL", posX.getValue(), posY.getValue() + 20, off);
        }

        if (ModuleManager.isModuleEnabled("AutoTrap")) {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "AT: ENBL", posX.getValue(), posY.getValue() + 30, on);
        } else {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "AT: DSBL", posX.getValue(), posY.getValue() + 30, off);
        }

        if (ModuleManager.isModuleEnabled("SelfTrap")) {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "ST: ENBL", posX.getValue(), posY.getValue() + 40, on);
        } else {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "ST: DSBL", posX.getValue(), posY.getValue() + 40, off);
        }

        if (ModuleManager.isModuleEnabled("SmartOffhand")) {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "ST: ENBL", posX.getValue(), posY.getValue() + 50, on);
        } else {
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "ST: DSBL", posX.getValue(), posY.getValue() + 50, off);
        }
    }

    private int getPing () {
        int p = -1;
        if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
            p = -1;
        } else {
            p = mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime();
        }
        return p;
    }
}