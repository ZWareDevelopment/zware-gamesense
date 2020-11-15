package dev.zihasz.zware.client.module.modules.combat;

import dev.zihasz.zware.client.module.Module;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author linustouchtips
 * wip module
 */

public class PacketEXP extends Module {
    public PacketEXP() {
        super("PacketEXP", Category.Combat);
    }

    @Override
    public void onUpdate() {
        int ArmorDurability = getArmorDurability();

        if (mc.player.isSneaking() && 0 < ArmorDurability) {
            mc.player.inventory.currentItem = findExpInHotbar();
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, 90, true));
            mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
        }
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private int getArmorDurability() {
        int TotalDurability = 0;

        for (ItemStack itemStack : mc.player.inventory.armorInventory) {
            TotalDurability = TotalDurability + itemStack.getItemDamage();
        }
        return TotalDurability;
    }
}
