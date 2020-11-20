package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.client.command.Command;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.module.ModuleManager;
import dev.zihasz.zware.client.module.modules.combat.AutoTotem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class AutoShield extends Module {

    int totems;
    boolean moving = false;
    boolean returnI = false;

    public AutoShield() {
        super("AutoShield", Category.Misc);
    }

    protected void onEnable() {
        if (ModuleManager.isModuleEnabled("SmartOffhand") || ModuleManager.isModuleEnabled("AutoTotem") || ModuleManager.isModuleEnabled("OffhandGap") || ModuleManager.isModuleEnabled("OffhandCrystal"))
            Command.sendClientMessage("Please disable SmartOffhand, AutoTotem, OffhandGap or OffhandCrystal");
    }

    public void onUpdate() {
        // Put shield in offhand
        if (mc.currentScreen instanceof GuiContainer) return;
        if (returnI){
            int t = -1;
            for (int i = 0; i < 45; i++)
                if (mc.player.inventory.getStackInSlot(i).isEmpty()){
                    t = i;
                    break;
                }
            if (t == -1) return;
            mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            returnI = false;
        }
        totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.SHIELD).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.SHIELD) totems++;
        else {
            if (moving){
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                moving = false;
                if (!mc.player.inventory.getItemStack().isEmpty()) returnI = true;
                return;
            }
            if (mc.player.inventory.getItemStack().isEmpty()){
                if (totems == 0) return;
                int t = -1;
                for (int i = 0; i < 45; i++)
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.SHIELD){
                        t = i;
                        break;
                    }
                if (t == -1) return; // Should never happen!
                mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
                moving = true;
            } else {
                int t = -1;
                for (int i = 0; i < 45; i++)
                    if (mc.player.inventory.getStackInSlot(i).isEmpty()){
                        t = i;
                        break;
                    }
                if (t == -1) return;
                mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            }
        }

        // Send "block" packet
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
    }

}
