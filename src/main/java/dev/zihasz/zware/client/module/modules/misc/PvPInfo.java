package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.api.event.events.TotemPopEvent;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Darki for popcounter
 * @src https://github.com/DarkiBoi/CliNet/blob/master/src/main/java/me/zeroeightsix/kami/module/modules/combat/TotemPopCounter.java
 **/

public class PvPInfo extends Module {
	public PvPInfo(){super("PvPInfo", Category.Misc);}

	List<Entity> knownPlayers = new ArrayList<>();
	List<Entity> antipearlspamplz = new ArrayList<>();
	List<Entity> players;
	List<Entity> pearls;
	List<Entity> strengthedPlayers = new ArrayList<>();
	private HashMap<String, Integer> popCounterHashMap = new HashMap<>();

	Setting.Boolean visualRange;
	Setting.Boolean pearlAlert;
	Setting.Boolean strengthDetect;
	Setting.Boolean popCounter;
	Setting.Mode ChatColor;

	public void setup(){
		ArrayList<String> colors = new ArrayList<>();
		colors.add("Black");
		colors.add("Dark Green");
		colors.add("Dark Red");
		colors.add("Gold");
		colors.add("Dark Gray");
		colors.add("Green");
		colors.add("Red");
		colors.add("Yellow");
		colors.add("Dark Blue");
		colors.add("Dark Aqua");
		colors.add("Dark Purple");
		colors.add("Gray");
		colors.add("Blue");
		colors.add("Aqua");
		colors.add("Light Purple");
		colors.add("White");
		visualRange = registerBoolean("Visual Range", "VisualRange", false);
		pearlAlert = registerBoolean("Pearl Alert", "PearlAlert",false);
		strengthDetect = registerBoolean("Strength Detect", "StrengthDetect", false);
		popCounter = registerBoolean("Pop Counter", "PopCounter", false);
		ChatColor = registerMode("Color", "Color", colors, "Light Purple");
	}

	public void onUpdate() {
		if (visualRange.getValue()) {
			if (mc.player == null) return;
			players = mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer).collect(Collectors.toList());
			try {
				for (Entity e : players) {
					if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
						if (!knownPlayers.contains(e)) {
							knownPlayers.add(e);
							Command.sendClientMessage(getTextColor() + e.getName() + " has been spotted thanks to GameSense!");
						}
					}
				}
			} catch (Exception e) {
			} // ez no crasherino
			try {
				for (Entity e : knownPlayers) {
					if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
						if (!players.contains(e)) {
							knownPlayers.remove(e);
						}
					}
				}
			} catch (Exception e) {
			} // ez no crasherino pt.2
		}
		if (pearlAlert.getValue()) {
			pearls = mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderPearl).collect(Collectors.toList());
			try {
				for (Entity e : pearls) {
					if (e instanceof EntityEnderPearl) {
						if (!antipearlspamplz.contains(e)) {
							antipearlspamplz.add(e);
							Command.sendClientMessage(getTextColor() + e.getEntityWorld().getClosestPlayerToEntity(e, 3).getName() + " has just thrown a pearl!");
						}
					}
				}
			} catch (Exception e) {
			}
		}
		if (strengthDetect.getValue() && mc.player != null && mc.world != null) {
			for (EntityPlayer player : mc.world.playerEntities){
				if (player.isPotionActive(MobEffects.STRENGTH) && !(strengthedPlayers.contains(player))){
					Command.sendClientMessage(getTextColor() + player.getName() + " has (drank) strength!");
					strengthedPlayers.add(player);
				}
				if (!(player.isPotionActive(MobEffects.STRENGTH)) && strengthedPlayers.contains(player)){
					Command.sendClientMessage(getTextColor() + player.getName() + " no longer has strength!");
					strengthedPlayers.remove(player);
				}
			}
		}
		if (popCounter.getValue() && mc.world != null && mc.player != null) {
			for (EntityPlayer player : mc.world.playerEntities) {
				if (player.getHealth() <= 0) {
					if (popCounterHashMap.containsKey(player.getDisplayNameString())) {
						Command.sendClientMessage(getTextColor() + player.getName() + " died after popping " + ChatFormatting.GREEN + popCounterHashMap.get(player.getName()) + getTextColor() + " totems!");
						popCounterHashMap.remove(player.getName(), popCounterHashMap.get(player.getName()));
					}
				}
			}
		}
	}

	@EventHandler
	private final Listener<PacketEvent.Receive> packetEventListener = new Listener<>(event -> {

		if (mc.world == null || mc.player == null){
			return;
		}
		if (event.getPacket() instanceof SPacketEntityStatus){
			SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
			if (packet.getOpCode() == 35){
				Entity entity = packet.getEntity(mc.world);
				ZWareMod.EVENT_BUS.post(new TotemPopEvent(entity));
			}
		}
	});

	@EventHandler
	private final Listener<TotemPopEvent> totemPopEventListener = new Listener<>(event -> {
		if (popCounterHashMap == null){
			popCounterHashMap = new HashMap<>();
		}

		if (popCounterHashMap.get(event.getEntity().getName()) == null){
			popCounterHashMap.put(event.getEntity().getName(), 1);
			Command.sendClientMessage(getTextColor() + event.getEntity().getName() + " popped " + ChatFormatting.RED + 1 + getTextColor() + " totem!");
		}
		else if (popCounterHashMap.get(event.getEntity().getName()) != null){
			int popCounter = popCounterHashMap.get(event.getEntity().getName());
			int newPopCounter = popCounter += 1;
			popCounterHashMap.put(event.getEntity().getName(), newPopCounter);
			Command.sendClientMessage(getTextColor() + event.getEntity().getName() + " popped " + ChatFormatting.RED + newPopCounter + getTextColor() + " totems!");
		}
	});

	public ChatFormatting getTextColor(){
		if (ChatColor.getValue().equalsIgnoreCase("Black")){
			return ChatFormatting.BLACK;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Dark Green")){
			return ChatFormatting.DARK_GREEN;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Dark Red")){
			return ChatFormatting.DARK_RED;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Gold")){
			return ChatFormatting.GOLD;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Dark Gray")){
			return ChatFormatting.DARK_GRAY;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Green")){
			return ChatFormatting.GREEN;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Red")){
			return ChatFormatting.RED;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Yellow")){
			return ChatFormatting.YELLOW;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Dark Blue")){
			return ChatFormatting.DARK_BLUE;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Dark Aqua")){
			return ChatFormatting.DARK_AQUA;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Dark Purple")){
			return ChatFormatting.DARK_PURPLE;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Gray")){
			return ChatFormatting.GRAY;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Blue")){
			return ChatFormatting.BLUE;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Light Purple")){
			return ChatFormatting.LIGHT_PURPLE;
		}
		if (ChatColor.getValue().equalsIgnoreCase("White")){
			return ChatFormatting.WHITE;
		}
		if (ChatColor.getValue().equalsIgnoreCase("Aqua")){
			return ChatFormatting.AQUA;
		}
		return null;
	}

	public void onEnable(){
		ZWareMod.EVENT_BUS.subscribe(this);
		popCounterHashMap = new HashMap<>();
	}

	public void onDisable(){
		knownPlayers.clear();
		ZWareMod.EVENT_BUS.unsubscribe(this);
	}
}