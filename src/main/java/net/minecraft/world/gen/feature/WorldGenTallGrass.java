package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTallGrass extends WorldGenerator
{
    private final IBlockState tallGrassState;

    public WorldGenTallGrass(BlockTallGrass.EnumType p_i45629_1_)
    {
        this.tallGrassState = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, p_i45629_1_);
    }

    public boolean generate(World worldIn, Random rand, BlockPos x)
    {
        Block block;

        while (((block = worldIn.getBlockState(x).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && x.getY() > 0)
        {
            x = x.down();
        }

        // reduced tall grass amount
        for (int i = 0; i < 72/*was 128, then 56*/; ++i)
        {
            BlockPos blockpos = x.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && Blocks.tallgrass.canBlockStay(worldIn, blockpos, this.tallGrassState))
            {
                worldIn.setBlockState(blockpos, this.tallGrassState, 2);
            }
        }

        return true;
    }
}
