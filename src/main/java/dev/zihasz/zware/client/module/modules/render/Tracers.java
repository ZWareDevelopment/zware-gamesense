package dev.zihasz.zware.client.module.modules.render;

import java.util.ArrayList;

import dev.zihasz.zware.api.event.events.RenderEvent;
import dev.zihasz.zware.api.util.players.enemy.Enemies;
import dev.zihasz.zware.api.util.players.friends.Friends;
import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.api.util.render.GameSenseTessellator;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.module.modules.gui.ColorMain;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

/**
 * Made by Hoosiers on 8/12/20, some GL from Osiris/KAMI was referenced.
 */

public class Tracers extends Module {
	public Tracers(){
		super("Tracers", Category.Render);
	}

	Setting.Integer renderDistance;
	Setting.Mode pointsTo;
	Setting.ColorSetting nearColor;
	Setting.ColorSetting midColor;
	Setting.ColorSetting farColor;

	public void setup(){
		renderDistance = registerInteger("Distance", "Distance", 100, 10, 260);

		ArrayList<String> link = new ArrayList<>();
		link.add("Head");
		link.add("Feet");

		pointsTo = registerMode("Draw To", "DrawTo", link, "Feet");
		nearColor=registerColor("Near Color","NearColor",new GSColor(255,0,0, 255));
		midColor=registerColor("Middle Color","MidColor",new GSColor(255,255,0, 255));
		farColor=registerColor("Far Color","FarColor",new GSColor(0,255,0, 255));
	}

	GSColor tracerColor;

	public void onWorldRender(RenderEvent event){
		mc.world.loadedEntityList.stream()
				.filter(e->e instanceof EntityPlayer)
				.filter(e->e != mc.player)
				.forEach(e->{
					if (mc.player.getDistance(e) > renderDistance.getValue()){
						return;
					} else {
						if (Friends.isFriend(e.getName())) {
							tracerColor = ColorMain.getFriendGSColor();
						} else if (Enemies.isEnemy(e.getName())) {
							tracerColor = ColorMain.getEnemyGSColor();
						} else {
							if (mc.player.getDistance(e) < 20) {
								tracerColor = nearColor.getValue();
							}
							if (mc.player.getDistance(e) >= 20 && mc.player.getDistance(e) < 50) {
								tracerColor = midColor.getValue();
							}
							if (mc.player.getDistance(e) >= 50) {
								tracerColor = farColor.getValue();
							}
						}
					}
					GlStateManager.pushMatrix();
					drawLineToEntityPlayer(e, tracerColor);
					GlStateManager.popMatrix();
				});
	}

	public void drawLineToEntityPlayer(Entity e, GSColor color){
		double[] xyz = interpolate(e);
		drawLine1(xyz[0],xyz[1],xyz[2], e.height, color);
	}

	public static double[] interpolate(Entity entity) {
		double posX = interpolate(entity.posX, entity.lastTickPosX);
		double posY = interpolate(entity.posY, entity.lastTickPosY);
		double posZ = interpolate(entity.posZ, entity.lastTickPosZ);
		return new double[] { posX, posY, posZ };
	}

	public static double interpolate(double now, double then) {
		return then + (now - then) * mc.getRenderPartialTicks();
	}

	public void drawLine1(double posx, double posy, double posz, double up, GSColor color){
		Vec3d eyes=ActiveRenderInfo.getCameraPosition().add(mc.getRenderManager().viewerPosX,mc.getRenderManager().viewerPosY,mc.getRenderManager().viewerPosZ);
		if (pointsTo.getValue().equalsIgnoreCase("Head")) {
			GameSenseTessellator.drawLine(eyes.x, eyes.y, eyes.z, posx, posy+up, posz, color);
		} else {
			GameSenseTessellator.drawLine(eyes.x, eyes.y, eyes.z, posx, posy, posz, color);
		}
	}
}
