package dev.zihasz.zware.client.module.modules.misc;
import dev.zihasz.zware.client.module.Module;

import java.util.Random;

public class ZihaszDupe extends Module {
	public ZihaszDupe(){
		super("ZihaszDupe", Category.Misc);
	}

	public void onEnable(){
		if (mc.player != null)
			mc.player.sendChatMessage("I just used the Zihasz Dupe and got " + (new Random().nextInt(31) + 1) + " shulkers thanks to ZWare.cc!");
		disable();
	}
}