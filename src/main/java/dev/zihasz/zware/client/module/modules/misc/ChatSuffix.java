package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.misc.Text;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;

public class ChatSuffix extends Module {
	public ChatSuffix(){
		super("ChatSuffix", Category.Misc);
	}

	Setting.Mode Mode;
	Setting.Mode Type;
	Setting.Mode Separator;

	public void setup() {
		ArrayList<String> Modes = new ArrayList<String>();
		Modes.add("ZWare.cc");
		Modes.add("On Top!");

		ArrayList<String> Types = new ArrayList<String>();
		Types.add("Normal");
		Types.add("Unicode");
		Types.add("Small");

		ArrayList<String> Separators = new ArrayList<String>();
		Separators.add("|");
		Separators.add(">>");
		Separators.add("<<");

		Mode = registerMode("Mode","ChatSuffixMode", Modes, "ZWare.cc");
		Type = registerMode("Type", "ChatSuffixType", Types, "Unicode");
		Separator = registerMode("Separator", "ChatSuffixSeparator", Separators, "|");
	}

	@EventHandler
	private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
		if (event.getPacket() instanceof CPacketChatMessage) {
			CPacketChatMessage msgPacket = (CPacketChatMessage) event.getPacket();
			String msg = msgPacket.getMessage();
			if (Text.isCommand(msg)) return;

			String suffix = "";
			switch (Type.getValue()) {
				case "Normal":
					if (Mode.getValue() == "ZWare.cc") {
						suffix = "ZWare.cc";
					} else if (Mode.getValue() == "On Top!") {
						suffix = mc.session.getUsername() + " on top!";
					}
					break;
				case "Unicode":
					if (Mode.getValue() == "ZWare.cc") {
						suffix = "ZWare.cc";
					} else if (Mode.getValue() == "On Top!") {
						suffix = mc.session.getUsername() + " on top!";
					}
					suffix = Text.toUnicode(suffix);
					break;
				case "Small":
					if (Mode.getValue() == "ZWare.cc") {
						suffix = "ZWare.cc";
					} else if (Mode.getValue() == "On Top!") {
						suffix = mc.session.getUsername() + " on top!";
					}
					suffix = Text.toSmallCapsText(suffix);
					break;
			}

			msg += Text.suffixBuilder(suffix, Separator.getValue());

			if (msg.length() >= 256)
				msg = msg.substring(0,256);
			msgPacket.message = msg;
		}
	});

	public void onEnable(){
		ZWareMod.EVENT_BUS.subscribe(this);
	}
	public void onDisable(){
		ZWareMod.EVENT_BUS.unsubscribe(this);
	}
}