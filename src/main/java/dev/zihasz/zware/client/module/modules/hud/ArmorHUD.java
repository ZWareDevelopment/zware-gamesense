package dev.zihasz.zware.client.module.modules.hud;

import dev.zihasz.zware.api.util.font.FontUtils;
import dev.zihasz.zware.api.util.render.GSColor;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.module.modules.gui.ColorMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends Module {
    public ArmorHUD(){
        super("ArmorHUD", Category.HUD);
    }

    private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

    public void onRender(){
        GlStateManager.enableTexture2D();

        ScaledResolution resolution = new ScaledResolution(mc);
        int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
        for (ItemStack is : mc.player.inventory.armorInventory) {
            iteration++;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();

            itemRender.zLevel = 200F;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
            itemRender.zLevel = 0F;

            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, new GSColor(255,255,255).getRGB());
            float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
            float red = 1 - green;
            int dmg = 100 - (int) (red * 100);
            FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, new GSColor((int) (red * 255), (int) (green * 255), 0));
        }

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}