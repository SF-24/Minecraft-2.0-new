package net.mineshaft.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBundle;
import net.minecraft.item.ItemStack;

public class SpecialItemModelLocation {

    public static ModelResourceLocation getSpecialResourceLocation(ItemStack stack) {
        ModelResourceLocation modelresourcelocation = null;
        Item item = stack.getItem();
        if(item == Items.bundle && ItemBundle.isFull(stack)) {
            modelresourcelocation = new ModelResourceLocation("bundle_filled", "inventory");
        }
        return modelresourcelocation;
    }

}
