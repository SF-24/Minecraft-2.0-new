package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPotato extends BlockCrops
{
    protected Item getSeed()
    {
        return Items.potato;
    }

    protected Item getCrop()
    {
        return Items.potato;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x,y,z, state, chance, fortune);

        if (!worldIn.isRemote)
        {
            if (((Integer)state.getValue(AGE)).intValue() >= 7 && worldIn.rand.nextInt(50) == 0)
            {
                spawnAsEntity(worldIn, x,y,z, new ItemStack(Items.poisonous_potato));
            }
        }
    }
}
