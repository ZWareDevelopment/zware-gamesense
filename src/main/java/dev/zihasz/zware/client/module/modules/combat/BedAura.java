package dev.zihasz.zware.client.module.modules.combat;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.module.Module;

public class BedAura extends Module {
    public BedAura() {
        super("BedAura", Category.Combat);
    }

    Setting.Integer range;
    Setting.Double delay;

    public void setup() {
        range = registerInteger("Range", "BedAuraRange", 5, 1,6);
        delay = registerDouble("Delay","BedAuraDelay",0f,0f,10f);
    }
}
