package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture
{
    public ItemAnvilBlock(Block block)
    {
        super(block, block, new String[] {
                "intact",           // 0
                "intact",           // 1 unused
                "slightlyDamaged",  // 2
                "intact",           // 3 unused
                "veryDamaged",      // 4
                "intact",           // 5 unused
                "intact",           // 6 unused
                "intact",           // 7 unused
                "diamond_intact",   // 8
                "intact",           // 9 unused
                "diamond_slightlyDamaged", // 10
                "intact",           // 11 unused
                "diamond_veryDamaged"      // 12
        });
//        super(block, block, new String[] {"intact", "slightlyDamaged", "veryDamaged"});
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return damage;
    }
//    public int getMetadata(int damage)
//    {
//        return damage << 2;
//    }
}
