package dev.zihasz.zware.client.module.modules.hud;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.font.FontUtils;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.module.modules.gui.ColorMain;

public class Overlay extends Module {
    public Overlay(){
        super("Overlay", Category.HUD);
    }

    Setting.Boolean welcomer;
    Setting.Boolean watermark;
    Setting.Integer welcomerX;
    Setting.Integer welcomerY;
    Setting.Integer watermarkX;
    Setting.Integer watermarkY;
    Setting.ColorSetting color;

    public void setup(){
        welcomer = registerBoolean("Welcomer", "Welcomer", true);
        welcomerX = registerInteger("Welcomer X", "WelcomerX", 450, 0, 1000);
        welcomerY = registerInteger("Welcomer Y", "WelcomerY", 0, 0, 1000);
        watermark = registerBoolean("Watermark", "Watermark", true);
        watermarkX = registerInteger("Watermark X", "WatermarkX", 0, 0, 1000);
        watermarkY = registerInteger("Watermark Y", "WatermarkY", 0, 0, 1000);
        color = registerColor("Color", "Color", new GSColor(255, 0, 0, 255));
    }

    public void onRender(){
        if (watermark.getValue()){
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "ZWare.cc " + ZWareMod.MODVER, watermarkX.getValue(), watermarkY.getValue(), color.getValue());
        }

        if (welcomer.getValue()){
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), "Hello " + mc.player.getName() + " :^)", welcomerX.getValue(), welcomerY.getValue(), color.getValue());
        }
    }
}