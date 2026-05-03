package net.minecraft.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container
{
    /**
     * NOTE:
     * <p>
     * Item - Initial Item
     * Item1- Output
     * Item2 - Repair Item/Enchanted Book
     * */

    private static final Logger logger = LogManager.getLogger();
    private final boolean isDiamondAnvil;

    /** Here comes out item you merged and/or renamed. */
    private IInventory outputSlot;

    /**
     * The 2slots where you put your items in that you want to merge and/or rename.
     */
    private IInventory inputSlots;
    private World theWorld;
    private BlockPos selfPosition;

    /** The maximum cost of repairing/renaming in the anvil. */
    public int maximumCost;

    /** determined by damage of input item and stackSize of repair materials */
    private int materialCost;
    private String repairedItemName;

    /** The player that has this container open. */
    private final EntityPlayer thePlayer;

    public ContainerRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player)
    {
        this(playerInventory, worldIn, BlockPos.ORIGIN, player);
    }

    public ContainerRepair(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player)
    {
        this(playerInventory, worldIn, blockPosIn, player, false);

    }

    public ContainerRepair(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player, boolean isDiamondAnvil)
    {
        this.isDiamondAnvil=isDiamondAnvil;
        this.outputSlot = new InventoryCraftResult();
        this.inputSlots = new InventoryBasic("Repair", true, 2)
        {
            public void markDirty()
            {
                super.markDirty();
                ContainerRepair.this.onCraftMatrixChanged(this);
            }
        };
        this.selfPosition = blockPosIn;
        this.theWorld = worldIn;
        this.thePlayer = player;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
            public boolean canTakeStack(EntityPlayer playerIn)
            {
                return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
            }
            public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
            {
                if (!playerIn.capabilities.isCreativeMode)
                {
                    playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }

                ContainerRepair.this.inputSlots.setInventorySlotContents(0, (ItemStack)null);

                if (ContainerRepair.this.materialCost > 0)
                {
                    ItemStack itemstack = ContainerRepair.this.inputSlots.getStackInSlot(1);

                    if (itemstack != null && itemstack.stackSize > ContainerRepair.this.materialCost)
                    {
                        itemstack.stackSize -= ContainerRepair.this.materialCost;
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack);
                    }
                    else
                    {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
                    }
                }
                else
                {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
                }

                ContainerRepair.this.maximumCost = 0;
                IBlockState iblockstate = worldIn.getBlockState(blockPosIn);

                if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12F)
                {
                    int l = ((Integer)iblockstate.getValue(BlockAnvil.DAMAGE)).intValue();
                    ++l;

                    if (l > 2)
                    {
                        worldIn.setBlockToAir(blockPosIn);
                        worldIn.playAuxSFX(1020, blockPosIn, 0);
                    }
                    else
                    {
                        worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(l)), 2);
                        worldIn.playAuxSFX(1021, blockPosIn, 0);
                    }
                }
                else if (!worldIn.isRemote)
                {
                    worldIn.playAuxSFX(1021, blockPosIn, 0);
                }
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);

        if (inventoryIn == this.inputSlots)
        {
            this.updateRepairOutput();
        }
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput()
    {
        ItemStack firstInput = this.inputSlots.getStackInSlot(0);
//        int repairPenalty = 0;
        int levelRepairCost = 0; // was l1.

        int extraRenameCost = 0;
        int rawRepairCost = 0;
        int enchantmentCost = 0;
        int justAppliedSuperEnchantCount = 0;
//        int previousEnchantInheritedCost = 0;
        int inheritedRepairCost = 0;

        this.maximumCost = 1; // Cap

        if (firstInput == null)
        {
            this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
            this.maximumCost = 0;
        }
        else
        {
            ItemStack output = firstInput.copy();
            ItemStack secondInput = this.inputSlots.getStackInSlot(1);
            Map<Integer, Integer> enchMap = EnchantmentHelper.getEnchantments(output);
            boolean isEnchBook = false;
            // Makes repair cost increase by a fixed amount each repair.
            // Count repair penalty
            inheritedRepairCost = firstInput.getRepairCost() + ((secondInput!=null)?secondInput.getRepairCost():0);
//            repairPenalty = repairPenalty + firstInput.getRepairCost() + (secondInput == null ? 0 : secondInput.getRepairCost());
            this.materialCost = 0;

            // If it has a second input
            if (secondInput != null)
            {
                isEnchBook = secondInput.getItem() == Items.enchanted_book && Items.enchanted_book.getEnchantments(secondInput).tagCount() > 0;

                // If both the output is repairable. - material repair
                if (output.isItemStackDamageable() && output.getItem().getIsRepairable(firstInput, secondInput))
                {
                    /**
                     * Repairing with material items.
                     * */

                    int j4 = Math.min(output.getItemDamage(), output.getMaxDamage() / 4);

                    if (j4 <= 0)
                    {
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
                        return;
                    }

                    int repairItemCount;

                    for (repairItemCount = 0; j4 > 0 && repairItemCount < secondInput.stackSize; ++repairItemCount)
                    {
                        int j5 = output.getItemDamage() - j4;
                        output.setItemDamage(j5);
                        j4 = Math.min(output.getItemDamage(), output.getMaxDamage() / 4);
                    }
                    if(repairItemCount>0) {
                        ++rawRepairCost; // Increase raw repair cost by 1
                    }
                    this.materialCost = repairItemCount;
                }
                else
                {
                    // If not an enchanted book
                    if (!isEnchBook && (output.getItem() != secondInput.getItem() || !output.isItemStackDamageable()))
                    {
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
                        return;
                    }

                    /**
                     * Repairing with a second item.
                     * */

                    // Durability calculations. If it's not being repaired via a material?
                    if (output.isItemStackDamageable() && !isEnchBook)
                    {
                        int k2 = firstInput.getMaxDamage() - firstInput.getItemDamage();
                        int l2 = secondInput.getMaxDamage() - secondInput.getItemDamage();
                        int i3 = l2 + output.getMaxDamage() * 12 / 100;
                        int j3 = k2 + i3;
                        int k3 = output.getMaxDamage() - j3;

                        if (k3 < 0)
                        {
                            k3 = 0;
                        }

                        if (k3 < output.getMetadata())
                        {
                            output.setItemDamage(k3);
                            rawRepairCost += 1; // ?
                        }
                    }

                    /**
                     * Enchantment Merging
                     * */

                    Map<Integer, Integer> secondItemEnchants = EnchantmentHelper.getEnchantments(secondInput);

                    for (int enchantId : secondItemEnchants.keySet()) {
                        Enchantment enchantment = Enchantment.getEnchantmentById(enchantId);

                        if (enchantment != null) {
                            int firstEnchantLevel = enchMap.getOrDefault(enchantId, 0);
                            int secondEnchantLevel = secondItemEnchants.get(enchantId);
                            int outputLevel;

                            if (firstEnchantLevel == secondEnchantLevel) {
                                outputLevel = secondEnchantLevel+1;
                            } else {
                                outputLevel = Math.max(secondEnchantLevel, firstEnchantLevel);
                            }
                            boolean canApplyEnchant = enchantment.canApply(firstInput);

                            if (this.thePlayer.capabilities.isCreativeMode || firstInput.getItem() == Items.enchanted_book) {
                                canApplyEnchant = true;
                            }

                            for (int otherId : enchMap.keySet()) {
                                if (otherId != enchantId && !enchantment.canApplyTogether(Enchantment.getEnchantmentById(otherId))) {
                                    canApplyEnchant = false;
//                                    ++levelRepairCost;
                                }
                            }

                            if (canApplyEnchant) {
                                if (outputLevel > enchantment.getMaxLevel()) {
                                    if(outputLevel>secondEnchantLevel) {
                                        outputLevel = Math.max(enchantment.getMaxLevel(), Math.max(firstEnchantLevel,secondEnchantLevel));
                                    } else {
                                        outputLevel = enchantment.getMaxExtraLevel();
                                    }
                                }
                                if (outputLevel > enchantment.getMaxLevel()) {
                                    if(!isDiamondAnvil) {
                                        this.maximumCost=0;
                                        return;
                                    }
                                    justAppliedSuperEnchantCount++;
                                }
                                enchMap.put(enchantId, outputLevel);
//                                int extraWeightRepairCost = 0;

//                                switch (enchantment.getWeight()) {
//                                    case 1:
//                                        extraWeightRepairCost = 8;
//                                        break;
//
//                                    case 2:
//                                        extraWeightRepairCost = 4;
//
//                                    case 3:
//                                    case 4:
//                                    case 6:
//                                    case 7:
//                                    case 8:
//                                    case 9:
//                                    default:
//                                        break;
//
//                                    case 5:
//                                        extraWeightRepairCost = 2;
//                                        break;
//
//                                    case 10:
//                                        extraWeightRepairCost = 1;
//                                }
//
//                                if (isEnchBook) {
//                                    extraWeightRepairCost = Math.max(1, extraWeightRepairCost / 2);
//                                }
//
//                                levelRepairCost += extraWeightRepairCost * outputLevel;
                            }
                        }
                    }
                }
            }

            /**
             * Rename cost.
             * */

            // Renaming.
            if (StringUtils.isBlank(this.repairedItemName))
            {
                if (firstInput.hasDisplayName())
                {
                    extraRenameCost = 1;
//                    levelRepairCost += extraRenameCost;
                    output.clearCustomName();
                }
            }
            else if (!this.repairedItemName.equals(firstInput.getDisplayName()))
            {
                extraRenameCost = 1;
//                levelRepairCost += extraRenameCost;
                output.setStackDisplayName(this.repairedItemName);
            }

            /**
             * Calculate final cost
             * */

            if(rawRepairCost<=0 && extraRenameCost<=0 && output.getEnchantmentTagList()!=null && output.getEnchantmentTagList().equals(firstInput.getEnchantmentTagList())) {
                maximumCost = 0;
                output=null;
            } else if(!isDiamondAnvil && output.hasSpecialGlint()) {
                maximumCost = 0;
                output=null;
            } else {

                int currentSuperEnchants=0;
                // If enchants applied
                Map<Integer, Integer> enchantmentsApplied = new HashMap<>();
                Map<Integer, Integer> otherEnchantments = new HashMap<>();

                Map<Integer, Integer> firstEnchants = EnchantmentHelper.getEnchantments(firstInput);
                Map<Integer, Integer> outputEnchants = EnchantmentHelper.getEnchantments(output);

                for (Integer id : outputEnchants.keySet()) {
                    if (outputEnchants.get(id) > firstEnchants.getOrDefault(id, 0)) {
                        enchantmentsApplied.put(id, outputEnchants.get(id));
                        System.out.println("Enchant id " + id + " level " + outputEnchants.get(id));
                    }
                    if(outputEnchants.get(id)>Enchantment.getEnchantmentById(id).getMaxExtraLevel()) {
                        currentSuperEnchants++;
                    }
                }

                // Cycle through output enchants
                if (!enchantmentsApplied.isEmpty()) {
                    for (Integer id : enchantmentsApplied.keySet()) {
                        enchantmentCost += EnchantmentHelper.getEnchantCost(id, enchantmentsApplied.get(id));
                    }
                    // TODO: Cycle through enchants and find cost.
                } else {
                    // If not enchants are added, it is a repair.
                    // TODO:
                    enchantmentCost = 0;
                }

                // TODO: Cycle through other enchants and apply a penalty based on them.


                // Count max cost.
                this.maximumCost = currentSuperEnchants*5 + ((rawRepairCost>0)?Math.min(20, inheritedRepairCost+rawRepairCost):0) + enchantmentCost + (rawRepairCost==0?extraRenameCost:0);

                /**
                 * Limitations
                 * */

                // Cannot add more than 3 enchants to an item.
                // Too expensive
                if ((enchMap.size() > (3 + ((output.getItem() instanceof ItemTool && ((ItemTool) output.getItem()).getToolMaterialName().equals("GOLD")) ? 1 : 0)) && (enchMap.size() > EnchantmentHelper.getEnchantments(firstInput).size()))) {
                    maximumCost = 0;
                    output = null;
                }


                /**
                 * Bandaid fix:
                 * */

                // Disables too expensive when repairing
                // Lowers cost if above 20 levels and repairing
                if (/*j2 == l1 && j2 > 0   && */ this.maximumCost > 20 && secondInput != null && !secondInput.isItemEnchanted()) {
//                System.out.println("above 20");
//                System.out.println("item0 " + itemstack.toString());
//                System.out.println("item1 " + itemstack1.toString());
//                System.out.println("item2 " + itemstack2.toString());

                    // Super enchants raise the cap.
                    this.maximumCost = Math.min(maximumCost, 20+Math.min(10,currentSuperEnchants*5));
                } else if(secondInput == null && extraRenameCost>0) {
                    this.maximumCost=1;
                }
            }


            // Cap enchant costs.
//            if (extraRenameCost == levelRepairCost && extraRenameCost > 0 && this.maximumCost >= 36)
//            {
//                this.maximumCost = 35+superEnchantCount*5;
//            }
//
//            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode)
//            {
//                itemstack1 = null;
//            }

            if (output != null)
            {
                int repairCost = inheritedRepairCost;

//                if (secondInput != null && repairCost < secondInput.getRepairCost())
//                {
//                    repairCost = secondInput.getRepairCost();
//                }

                // Make the repair cost increase by a fixed amount every time
//                if(secondInput!=null&&secondInput.isItemEnchanted()) {
//                    k4 = k4 /* * 2*/ + 4; // TODO: Make better
//                } else

                // Increase and charge cost based on number of enchantments on the item.
                if(output.isItemEnchanted() && secondInput != null && !secondInput.isItemEnchanted()) {
                    int specialEnchants = 0;
                    int enchantsAboveThirdLevel = 0;
                    int lowEnchantments = 0;
                    for(int enchantment : EnchantmentHelper.getEnchantments(output).keySet()) {
                        if(EnchantmentHelper.getEnchantments(output).get(enchantment)>Enchantment.getEnchantmentById(enchantment).getMaxExtraLevel()) {
                            specialEnchants++;
                        } else if(EnchantmentHelper.getEnchantments(output).get(enchantment)>=3 && EnchantmentHelper.getEnchantments(output).get(enchantment)<Enchantment.getEnchantmentById(enchantment).getMaxExtraLevel()) {
                            enchantsAboveThirdLevel++;
                        } else {
                            lowEnchantments++;
                        }
                    }
                    repairCost = repairCost + 1 + Math.max(4,(lowEnchantments / 2 + enchantsAboveThirdLevel + specialEnchants*2));
                    System.out.println("Cost: " + repairCost + " | risen: " + 1 + Math.max(4,(lowEnchantments / 2 + enchantsAboveThirdLevel + specialEnchants*2)));
                }
                output.setRepairCost(Math.max(20,repairCost));
                EnchantmentHelper.setEnchantments(enchMap, output);
            }

            this.outputSlot.setInventorySlotContents(0, output);
            this.detectAndSendChanges();
        }
    }

    public void onCraftGuiOpened(ICrafting listener)
    {
        super.onCraftGuiOpened(listener);
        listener.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    public void updateProgressBar(int id, int data)
    {
        if (id == 0)
        {
            this.maximumCost = data;
        }
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.theWorld.isRemote)
        {
            for (int i = 0; i < this.inputSlots.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.inputSlots.removeStackFromSlot(i);

                if (itemstack != null)
                {
                    playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.theWorld.getBlockState(this.selfPosition).getBlock() != Blocks.anvil ? false : playerIn.getDistanceSq((double)this.selfPosition.getX() + 0.5D, (double)this.selfPosition.getY() + 0.5D, (double)this.selfPosition.getZ() + 0.5D) <= 64.0D;
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
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 0 && index != 1)
            {
                if (index >= 3 && index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
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
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    public void updateItemName(String newName)
    {
        this.repairedItemName = newName;

        if (this.getSlot(2).getHasStack())
        {
            ItemStack itemstack = this.getSlot(2).getStack();

            if (StringUtils.isBlank(newName))
            {
                itemstack.clearCustomName();
            }
            else
            {
                itemstack.setStackDisplayName(this.repairedItemName);
            }
        }

        this.updateRepairOutput();
    }
}
