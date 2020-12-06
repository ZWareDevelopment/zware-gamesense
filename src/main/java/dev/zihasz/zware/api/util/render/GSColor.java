package dev.zihasz.zware.api.util.render;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;

/**
* @author lukflug
*/
// Why would anyone ever need to use JavaDoc properly?

public class GSColor extends Color {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GSColor (int rgb) {
		super(rgb);
	}
	
	public GSColor (int rgba, boolean hasalpha) {
		super(rgba,hasalpha);
	}
	
	public GSColor (int r, int g, int b) {
		super(r,g,b);
	}
	
	public GSColor (int r, int g, int b, int a) {
		super(r,g,b,a);
	}
	
	public GSColor (Color color) {
		super(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
	}
	
	public GSColor (GSColor color, int a) {
		super(color.getRed(),color.getGreen(),color.getBlue(),a);
	}
	
	public static GSColor fromHSB (float hue, float saturation, float brightness) {
		return new GSColor(Color.getHSBColor(hue,saturation,brightness));
	}
	
	public float getHue() {
		return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[0];
	}
	
	public float getSaturation() {
		return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[1];
	}
	
	public float getBrightness() {
		return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[2];
	}
	
	public void glColor() {
		GlStateManager.color(getRed()/255.0f,getGreen()/255.0f,getBlue()/255.0f,getAlpha()/255.0f);
	}

	public static int GenRainbow() {
		final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
		final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
		final int red = rgb >> 16 & 0xFF;
		final int green = rgb >> 8 & 0xFF;
		final int blue = rgb & 0xFF;
		final int color = toRGBA(red, green, blue, 255);
		return color;
	}

	public static int toRGBA(final double r, final double g, final double b, final double a) {
		return toRGBA((float)r, (float)g, (float)b, (float)a);
	}
}
