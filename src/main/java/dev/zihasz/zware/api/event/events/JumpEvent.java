package dev.zihasz.zware.api.event.events;

import dev.zihasz.zware.api.event.GameSenseEvent;
import dev.zihasz.zware.api.util.world.Location;

public class JumpEvent extends GameSenseEvent {
	private Location location;

	public JumpEvent(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return this.location;
	}

	public void setLocation(Location location){
		this.location = location;
	}

}
