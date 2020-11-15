package dev.zihasz.zware.api.event.events;

import dev.zihasz.zware.api.event.GameSenseEvent;

public class RenderEvent extends GameSenseEvent {
		private final float partialTicks;

		public RenderEvent(float ticks){
			super();
			partialTicks = ticks;
		}

		public float getPartialTicks(){
			return partialTicks;
		}
}
