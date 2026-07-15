package net.mineshaft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemUtil {

    public static void decreaseStackSizeIfNotCreativeMode(EntityPlayer playerIn,  ItemStack itemStack) {
        if(!playerIn.capabilities.isCreativeMode) {
            decreaseStackSizeInHotbar(playerIn, itemStack);
        }
    }

    public static void decreaseStackSizeInHotbar(EntityPlayer playerIn, ItemStack itemStack) {
        // Only if not in creative
        --itemStack.stackSize;
        if(itemStack.stackSize <= 0) {
            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
        }

    }
}
