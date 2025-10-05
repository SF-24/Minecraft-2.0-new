package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator
{
    private Block baseBlockRequired;

    public WorldGenSpikes(Block p_i45464_1_)
    {
        this.baseBlockRequired = p_i45464_1_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        if (worldIn.isAirBlock(x) && worldIn.getBlockState(x.down()).getBlock() == this.baseBlockRequired)
        {
            int i = rand.nextInt(32) + 6;
            int j = rand.nextInt(4) + 1;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = x.getX() - j; k <= x.getX() + j; ++k)
            {
                for (int l = x.getZ() - j; l <= x.getZ() + j; ++l)
                {
                    int i1 = k - x.getX();
                    int j1 = l - x.getZ();

                    if (i1 * i1 + j1 * j1 <= j * j + 1 && worldIn.getBlockState(blockpos$mutableblockpos.set(k, x.getY() - 1, l)).getBlock() != this.baseBlockRequired)
                    {
                        return false;
                    }
                }
            }

            for (int l1 = x.getY(); l1 < x.getY() + i && l1 < 256; ++l1)
            {
                for (int i2 = x.getX() - j; i2 <= x.getX() + j; ++i2)
                {
                    for (int j2 = x.getZ() - j; j2 <= x.getZ() + j; ++j2)
                    {
                        int k2 = i2 - x.getX();
                        int k1 = j2 - x.getZ();

                        if (k2 * k2 + k1 * k1 <= j * j + 1)
                        {
                            worldIn.setBlockState(new BlockPos(i2, l1, j2), Blocks.obsidian.getDefaultState(), 2);
                        }
                    }
                }
            }

            Entity entity = new EntityEnderCrystal(worldIn);
            entity.setLocationAndAngles((double)((float) x.getX() + 0.5F), (double)(x.getY() + i), (double)((float) x.getZ() + 0.5F), rand.nextFloat() * 360.0F, 0.0F);
            worldIn.spawnEntityInWorld(entity);
            worldIn.setBlockState(x.up(i), Blocks.bedrock.getDefaultState(), 2);
            return true;
        }
        else
        {
            return false;
        }
    }
}
