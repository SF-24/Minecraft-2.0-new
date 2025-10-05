package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator
{
    private Block block;

    public WorldGenLiquids(Block p_i45465_1_)
    {
        this.block = p_i45465_1_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        if (worldIn.getBlockState(x.up()).getBlock() != Blocks.stone)
        {
            return false;
        }
        else if (worldIn.getBlockState(x.down()).getBlock() != Blocks.stone)
        {
            return false;
        }
        else if (worldIn.getBlockState(x).getBlock().getMaterial() != Material.air && worldIn.getBlockState(x).getBlock() != Blocks.stone)
        {
            return false;
        }
        else
        {
            int i = 0;

            if (worldIn.getBlockState(x.west()).getBlock() == Blocks.stone)
            {
                ++i;
            }

            if (worldIn.getBlockState(x.east()).getBlock() == Blocks.stone)
            {
                ++i;
            }

            if (worldIn.getBlockState(x.north()).getBlock() == Blocks.stone)
            {
                ++i;
            }

            if (worldIn.getBlockState(x.south()).getBlock() == Blocks.stone)
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

            if (i == 3 && j == 1)
            {
                worldIn.setBlockState(x, this.block.getDefaultState(), 2);
                worldIn.forceBlockUpdateTick(this.block, x, rand);
            }

            return true;
        }
    }
}
