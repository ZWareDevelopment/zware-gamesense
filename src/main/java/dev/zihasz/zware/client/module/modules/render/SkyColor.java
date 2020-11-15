package dev.zihasz.zware.client.module.modules.render;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.client.module.Module;

public class SkyColor extends Module {
    public SkyColor() {
        super("SkyColor", Category.Render);
    }

    public static Setting.ColorSetting color;
    public static Setting.Boolean fog;

    @Override
    public void setup(){
        fog = registerBoolean("Fog", "Fog", true);
        color = registerColor("Color", "Color", new GSColor(0, 255, 0, 255));
    }

    //check the event processor to see how this all works...
    // probably a better way to do it, but hey it works.
}