package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerResearch extends Container
{
    /** The crafting matrix inventory. */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public IInventory craftResult = new InventoryCraftResult();

    /** The crafting matrix inventory. */
    public IInventory itemSlot = new InventoryCraftResult();

    /** Determines if inventory manipulation should be handled. */
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;

    public ContainerResearch(final InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player)
    {
        this.isLocalWorld = localWorld;
        this.thePlayer = player;
        this.addSlotToContainer(new Slot(this.itemSlot, 0, 144, 36));

        for (int k = 0; k < 4; ++k)
        {
            final int k_f = k;
            this.addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 1 - k, 80000, 8 + k * 18)
            {
                public int getSlotStackLimit()
                {
                    return 1;
                }
                public boolean isItemValid(ItemStack stack)
                {
                    return stack == null ? false : (stack.getItem() instanceof ItemArmor ? ((ItemArmor)stack.getItem()).armorType == k_f : (stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull ? false : k_f == 0));
                }
                public String getSlotTexture()
                {
                    return ItemArmor.EMPTY_SLOT_NAMES[k_f];
                }
            });
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18 + 26));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 142 + 26));
        }
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        ItemStack itemstack = this.itemSlot.removeStackFromSlot(0);

        if (itemstack != null)
        {
            playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
        }
        this.itemSlot.setInventorySlotContents(0, (ItemStack)null);

    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);


        if (slot != null && slot.getHasStack())
        {
            System.out.println("Clicked: slot: " + slot.slotNumber);
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
//            else if (index >= 1 && index < 5)
//            {
//                if (!this.mergeItemStack(itemstack1, 9, 45, false))
//                {
//                    return null;
//                }
//            }
//            else if (index >= 5 && index < 9)
//            {
//                if (!this.mergeItemStack(itemstack1, 9, 45, false))
//                {
//                    return null;
//                }
//            }
//            else if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack())
//            {
//                int i = 5 + ((ItemArmor)itemstack.getItem()).armorType;
//
//                if (!this.mergeItemStack(itemstack1, i, i + 1, false))
//                {
//                    return null;
//                }
//            }
            else if (index >= 9 && index < 36)
            {
                if (!this.mergeItemStack(itemstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (index >= 36 && index < 45)
            {
                if (!this.mergeItemStack(itemstack1, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 9, 45, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     */
    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return super.canMergeSlot(stack, slotIn);
    }
}
