package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenGlowStone1 extends WorldGenerator
{
    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        if (!worldIn.isAirBlock(x))
        {
            return false;
        }
        else if (worldIn.getBlockState(x.up()).getBlock() != Blocks.netherrack && worldIn.getBlockState(x.up()).getBlock() != Blocks.stone)
        {
            return false;
        }
        else
        {
            worldIn.setBlockState(x, Blocks.glowstone.getDefaultState(), 2);

            for (int i = 0; i < 1500; ++i)
            {
                BlockPos blockpos = x.add(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));

                if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air)
                {
                    int j = 0;

                    for (EnumFacing enumfacing : EnumFacing.values())
                    {
                        if (worldIn.getBlockState(blockpos.offset(enumfacing)).getBlock() == Blocks.glowstone)
                        {
                            ++j;
                        }

                        if (j > 1)
                        {
                            break;
                        }
                    }

                    if (j == 1)
                    {
                        worldIn.setBlockState(blockpos, Blocks.glowstone.getDefaultState(), 2);
                    }
                }
            }

            return true;
        }
    }
}
