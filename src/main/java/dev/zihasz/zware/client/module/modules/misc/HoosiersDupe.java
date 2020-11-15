package dev.zihasz.zware.client.module.modules.misc;
import dev.zihasz.zware.client.module.Module;

import java.util.Random;

public class HoosiersDupe extends Module {
	public HoosiersDupe(){
		super("HoosiersDupe", Category.Misc);
	}

	public void onEnable(){
		if (mc.player != null)
			mc.player.sendChatMessage("I just used the Go_Hoosiers Dupe and got " + (new Random().nextInt(31) + 1) + " shulkers thanks to GameSense!");
		disable();
	}
}