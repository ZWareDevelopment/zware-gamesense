package dev.zihasz.zware.api.util.font;

import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.client.ZWareMod;
import net.minecraft.client.Minecraft;

public class FontUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static float drawStringWithShadow(boolean customFont, String text, int x, int y, GSColor color){
		if(customFont) return ZWareMod.fontRenderer.drawStringWithShadow(text, x, y, color);
		else return mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
	}

	public static int getStringWidth(boolean customFont, String str){
		if (customFont) return ZWareMod.fontRenderer.getStringWidth(str);
		else return mc.fontRenderer.getStringWidth(str);
	}

	public static int getFontHeight(boolean customFont){
		if (customFont) return ZWareMod.fontRenderer.getHeight();
		else return mc.fontRenderer.FONT_HEIGHT;
	}
}