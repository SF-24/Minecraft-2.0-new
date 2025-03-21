package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemSoup extends ItemFood
{
    // A LOT OF CUSTOM CODE

    public ItemSoup(int healAmount)
    {
        super(healAmount, false, EnumFoodType.SOUP);
        this.setMaxStackSize(4 /* WAS 1 */);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        super.onItemUseFinish(stack, worldIn, playerIn);
        if(stack.stackSize<=0) {
            return new ItemStack(Items.bowl);
        } else {
            playerIn.inventory.addItemStackToInventory(new ItemStack(Items.bowl));
            return stack;
        }
    }
}
