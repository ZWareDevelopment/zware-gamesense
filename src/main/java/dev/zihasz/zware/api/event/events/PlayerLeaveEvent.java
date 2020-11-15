package dev.zihasz.zware.api.event.events;

import dev.zihasz.zware.api.event.GameSenseEvent;

public class PlayerLeaveEvent extends GameSenseEvent {

	private final String name;

	public PlayerLeaveEvent(String n){
		super();
		name = n;
	}

	public String getName(){
		return name;
	}
}
