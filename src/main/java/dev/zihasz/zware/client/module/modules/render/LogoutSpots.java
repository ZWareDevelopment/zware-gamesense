package dev.zihasz.zware.client.module.modules.render;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.zihasz.zware.api.event.events.PlayerJoinEvent;
import dev.zihasz.zware.api.event.events.PlayerLeaveEvent;
import dev.zihasz.zware.api.event.events.RenderEvent;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.api.util.render.GameSenseTessellator;
import dev.zihasz.zware.client.ZWareMod;
import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.Module;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.world.WorldEvent;

public class LogoutSpots extends Module {
	public LogoutSpots() {super("LogoutSpots", Category.Render);}

	Map<Entity, String> loggedPlayers = new ConcurrentHashMap<>();
	List<Entity> lastTickEntities;
	
	Setting.Integer width;
	Setting.ColorSetting color;
	Setting.ColorSetting nameColor;
	
	public void setup() {
		width=registerInteger("Width","Width",1,1,10);
		color=registerColor("Box Color","Color",new GSColor(0,0,0));
		nameColor=registerColor("Nametag Color","NameColor");
	}

	@EventHandler
	private final Listener<PlayerJoinEvent> listener1 = new Listener<>(event -> {
		loggedPlayers.forEach((e, s) -> {
			try {
				if (e.getName().equalsIgnoreCase(event.getName())) {
					loggedPlayers.remove(e);
					Command.sendClientMessage(event.getName() + " reconnected!");
				}
			} catch(ConcurrentModificationException ex){ex.printStackTrace();}
		});
	});

	@EventHandler
	private final Listener<PlayerLeaveEvent> listener2 = new Listener<>(event -> {
		if (mc.world == null) return;
		lastTickEntities.forEach(e ->{
			if(e.getName().equalsIgnoreCase(event.getName())){
				String date = new SimpleDateFormat("k:mm").format(new Date());
				loggedPlayers.put(e, date);
				String pos = "x" + e.getPosition().getX() + " y" + e.getPosition().getY() + " z" + e.getPosition().getZ();
				Command.sendClientMessage(event.getName() + " disconnected at " + pos + "!");
			}
		});
	});

	public void onUpdate(){
		lastTickEntities = mc.world.loadedEntityList;
	}

	public void onWorldRender(RenderEvent event) {
		loggedPlayers.forEach((e, time) -> {
			if(mc.player.getDistance(e) < 500) {
				GlStateManager.pushMatrix();
				drawLogoutBox(e.getRenderBoundingBox(), width.getValue());
				drawNametag(e, time);
				GlStateManager.popMatrix();
			}
		});
	}

	public void drawLogoutBox(AxisAlignedBB bb, int width){
		GameSenseTessellator.drawBoundingBox(bb, width, color.getValue());
	}

	@EventHandler
	private final Listener<WorldEvent.Unload> listener3 = new Listener<>(event -> {
		lastTickEntities.clear();
		if(mc.player == null)
			loggedPlayers.clear();
		else
		if(!mc.player.isDead)
			loggedPlayers.clear();
	});

	@EventHandler
	private final Listener<WorldEvent.Load> listener4 = new Listener<>(event -> {
		lastTickEntities.clear();
		if (mc.player == null) {
			loggedPlayers.clear();
		} else {
			if (!mc.player.isDead) loggedPlayers.clear();
		}
	});

	public void onEnable(){
		lastTickEntities = new ArrayList<>();
		loggedPlayers.clear();
		ZWareMod.EVENT_BUS.subscribe(this);
	}

	public void onDisable() {
		ZWareMod.EVENT_BUS.unsubscribe(this);
	}

	private void drawNametag(Entity entityIn, String t) {
		String[] text=new String[2];
		text[0] = entityIn.getName() + "  (" + t + ")";
		text[1] = "x" + entityIn.getPosition().getX() + " y" + entityIn.getPosition().getY() + " z" + entityIn.getPosition().getZ();
		GameSenseTessellator.drawNametag(entityIn,text,nameColor.getValue(),0);
	}
}
