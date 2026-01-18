package net.mineshaft.cavegen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

import java.util.Random;

public class WorldGenCaveShrub extends WorldGenTrees
{
    private final IBlockState leavesMetadata;
    private final IBlockState woodMetadata;

    public WorldGenCaveShrub(IBlockState p_i46450_1_, IBlockState p_i46450_2_)
    {
        super(false);
        this.woodMetadata = p_i46450_1_;
        this.leavesMetadata = p_i46450_2_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        Block block;

        while (((block = worldIn.getBlockState(x).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && x.getY() > 0)
        {
            x = x.down();
        }

        Block block1 = worldIn.getBlockState(x).getBlock();

        if (block1 == Blocks.dirt || block1 == Blocks.grass || block1 == Blocks.stone || block1 == Blocks.gravel || block1 == Blocks.coal_ore || block1 == Blocks.iron_ore || block1 == Blocks.clay)
        {
            x = x.up();
            this.setBlockAndNotifyAdequately(worldIn, x, this.woodMetadata);

            for (int i = x.getY(); i <= x.getY() + 2; ++i)
            {
                int j = i - x.getY();
                int k = 2 - j;

                for (int l = x.getX() - k; l <= x.getX() + k; ++l)
                {
                    int i1 = l - x.getX();

                    for (int j1 = x.getZ() - k; j1 <= x.getZ() + k; ++j1)
                    {
                        int k1 = j1 - x.getZ();

                        if (Math.abs(i1) != k || Math.abs(k1) != k || rand.nextInt(2) != 0)
                        {
                            BlockPos blockpos = new BlockPos(l, i, j1);

                            if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
                            {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
