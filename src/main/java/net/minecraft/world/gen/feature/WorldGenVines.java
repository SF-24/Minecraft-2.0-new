package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenVines extends WorldGenerator
{
    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        for (; x.getY() < 128; x = x.up())
        {
            if (worldIn.isAirBlock(x))
            {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings())
                {
                    if (Blocks.vine.canPlaceBlockOnSide(worldIn, x, enumfacing))
                    {
                        IBlockState iblockstate = Blocks.vine.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(enumfacing == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(enumfacing == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(enumfacing == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(enumfacing == EnumFacing.WEST));
                        worldIn.setBlockState(x, iblockstate, 2);
                        break;
                    }
                }
            }
            else
            {
                x = x.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            }
        }

        return true;
    }
}
