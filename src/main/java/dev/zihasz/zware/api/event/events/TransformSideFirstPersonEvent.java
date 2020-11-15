package dev.zihasz.zware.api.event.events;

import dev.zihasz.zware.api.event.GameSenseEvent;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent extends GameSenseEvent {
	private final EnumHandSide handSide;

	public TransformSideFirstPersonEvent(EnumHandSide handSide){
		this.handSide = handSide;
	}

	public EnumHandSide getHandSide(){
		return handSide;
	}
}
