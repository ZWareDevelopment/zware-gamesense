package dev.zihasz.zware.api.event.events;

import dev.zihasz.zware.api.event.GameSenseEvent;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends GameSenseEvent {

	private final Entity entity;

	public TotemPopEvent(Entity entity){
		super();
		this.entity = entity;
	}

	public Entity getEntity(){
		return entity;
	}
}
