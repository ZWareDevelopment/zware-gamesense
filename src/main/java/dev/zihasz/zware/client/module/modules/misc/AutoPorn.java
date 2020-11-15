package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.module.Module;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

public class AutoPorn extends Module {
    Setting.Mode mode;

    public AutoPorn() {
        super("AutoPorn", Module.Category.Exploits);
    }

    public void setup() {
        ArrayList<String> modes = new ArrayList<String>();
        modes.add("Straight");
        modes.add("Lesbian");
        modes.add("Step");
        modes.add("MILF");
        modes.add("Hentai");
        modes.add("Feet");
        modes.add("BDSM");
        modes.add("Tease");
        modes.add("Creampie");
        modes.add("Squirt");
        modes.add("Gangbang");
        modes.add("Teen");
        modes.add("Cumshot");
        mode = registerMode("Mode", "AutoPornMode", modes, "Hentai");
    }

    @Override
    public void onEnable() {
        String url = String.format("https://www.pornhub.com/video/search?search=%s", mode.getValue().toLowerCase());
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception e) {}
    }
}
