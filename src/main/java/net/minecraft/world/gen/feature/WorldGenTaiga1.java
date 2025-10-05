package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga1 extends WorldGenAbstractTree
{
    private static final IBlockState field_181636_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181637_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public WorldGenTaiga1()
    {
        super(false);
    }

    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        int i = rand.nextInt(5) + 7;
        int j = i - rand.nextInt(2) - 3;
        int k = i - j;
        int l = 1 + rand.nextInt(k + 1);
        boolean flag = true;

        if (x.getY() >= 1 && x.getY() + i + 1 <= 256)
        {
            for (int i1 = x.getY(); i1 <= x.getY() + 1 + i && flag; ++i1)
            {
                int j1 = 1;

                if (i1 - x.getY() < j)
                {
                    j1 = 0;
                }
                else
                {
                    j1 = l;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int k1 = x.getX() - j1; k1 <= x.getX() + j1 && flag; ++k1)
                {
                    for (int l1 = x.getZ() - j1; l1 <= x.getZ() + j1 && flag; ++l1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            if (!this.func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.set(k1, i1, l1)).getBlock()))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                Block block = worldIn.getBlockState(x.down()).getBlock();

                if ((block == Blocks.grass || block == Blocks.dirt) && x.getY() < 256 - i - 1)
                {
                    this.func_175921_a(worldIn, x.down());
                    int k2 = 0;

                    for (int l2 = x.getY() + i; l2 >= x.getY() + j; --l2)
                    {
                        for (int j3 = x.getX() - k2; j3 <= x.getX() + k2; ++j3)
                        {
                            int k3 = j3 - x.getX();

                            for (int i2 = x.getZ() - k2; i2 <= x.getZ() + k2; ++i2)
                            {
                                int j2 = i2 - x.getZ();

                                if (Math.abs(k3) != k2 || Math.abs(j2) != k2 || k2 <= 0)
                                {
                                    BlockPos blockpos = new BlockPos(j3, l2, i2);

                                    if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
                                    {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, field_181637_b);
                                    }
                                }
                            }
                        }

                        if (k2 >= 1 && l2 == x.getY() + j + 1)
                        {
                            --k2;
                        }
                        else if (k2 < l)
                        {
                            ++k2;
                        }
                    }

                    for (int i3 = 0; i3 < i - 1; ++i3)
                    {
                        Block block1 = worldIn.getBlockState(x.up(i3)).getBlock();

                        if (block1.getMaterial() == Material.air || block1.getMaterial() == Material.leaves)
                        {
                            this.setBlockAndNotifyAdequately(worldIn, x.up(i3), field_181636_a);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
