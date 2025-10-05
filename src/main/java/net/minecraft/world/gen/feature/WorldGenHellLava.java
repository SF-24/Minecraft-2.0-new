package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator
{
    private final Block field_150553_a;
    private final boolean field_94524_b;

    public WorldGenHellLava(Block p_i45453_1_, boolean p_i45453_2_)
    {
        this.field_150553_a = p_i45453_1_;
        this.field_94524_b = p_i45453_2_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        if (worldIn.getBlockState(x.up()).getBlock() != Blocks.netherrack)
        {
            return false;
        }
        else if (worldIn.getBlockState(x).getBlock().getMaterial() != Material.air && worldIn.getBlockState(x).getBlock() != Blocks.netherrack)
        {
            return false;
        }
        else
        {
            int i = 0;

            if (worldIn.getBlockState(x.west()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(x.east()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(x.north()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(x.south()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(x.down()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            int j = 0;

            if (worldIn.isAirBlock(x.west()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(x.east()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(x.north()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(x.south()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(x.down()))
            {
                ++j;
            }

            if (!this.field_94524_b && i == 4 && j == 1 || i == 5)
            {
                worldIn.setBlockState(x, this.field_150553_a.getDefaultState(), 2);
                worldIn.forceBlockUpdateTick(this.field_150553_a, x, rand);
            }

            return true;
        }
    }
}
