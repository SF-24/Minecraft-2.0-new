package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenIcePath extends WorldGenerator
{
    private Block block = Blocks.packed_ice;
    private int basePathWidth;

    public WorldGenIcePath(int p_i45454_1_)
    {
        this.basePathWidth = p_i45454_1_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        while (worldIn.isAirBlock(x) && x.getY() > 2)
        {
            x = x.down();
        }

        if (worldIn.getBlockState(x).getBlock() != Blocks.snow)
        {
            return false;
        }
        else
        {
            int i = rand.nextInt(this.basePathWidth - 2) + 2;
            int j = 1;

            for (int k = x.getX() - i; k <= x.getX() + i; ++k)
            {
                for (int l = x.getZ() - i; l <= x.getZ() + i; ++l)
                {
                    int i1 = k - x.getX();
                    int j1 = l - x.getZ();

                    if (i1 * i1 + j1 * j1 <= i * i)
                    {
                        for (int k1 = x.getY() - j; k1 <= x.getY() + j; ++k1)
                        {
                            BlockPos blockpos = new BlockPos(k, k1, l);
                            Block block = worldIn.getBlockState(blockpos).getBlock();

                            if (block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice)
                            {
                                worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
