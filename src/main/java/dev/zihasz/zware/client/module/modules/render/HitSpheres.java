package dev.zihasz.zware.client.module.modules.render;

import dev.zihasz.zware.api.event.events.RenderEvent;
import dev.zihasz.zware.api.util.players.friends.Friends;
import dev.zihasz.zware.api.util.misc.Wrapper;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.api.util.render.GameSenseTessellator;
import dev.zihasz.zware.client.module.Module;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class HitSpheres extends Module{

	public HitSpheres(){super("HitSpheres", Category.Render);}

	public void onWorldRender(RenderEvent event){
		for (Entity ep : Wrapper.getWorld().loadedEntityList){
			if (ep instanceof EntityPlayerSP){
				continue;
			}
			if (ep instanceof EntityPlayer){
				double posX = ep.lastTickPosX + (ep.posX - ep.lastTickPosX) * (double)Wrapper.getMinecraft().timer.renderPartialTicks;
				double posY = ep.lastTickPosY + (ep.posY - ep.lastTickPosY) * (double)Wrapper.getMinecraft().timer.renderPartialTicks;
				double posZ = ep.lastTickPosZ + (ep.posZ - ep.lastTickPosZ) * (double)Wrapper.getMinecraft().timer.renderPartialTicks;
				if (Friends.isFriend(ep.getName())){
					new GSColor(38,38,255).glColor();
				} else{
					if (Wrapper.getPlayer().getDistanceSq(ep) >= 64){
						new GSColor(0,255,0).glColor();
					} else{
						new GSColor(255,(int)(Wrapper.getPlayer().getDistance(ep)*255/150f),0).glColor();
					}
				}
				GameSenseTessellator.drawSphere(posX, posY, posZ, 6, 20, 15);
			}
		}
	}
}