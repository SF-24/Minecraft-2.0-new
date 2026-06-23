package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class WorldGenNetherWells extends WorldGenerator
{
    private static final BlockStateHelper stateHelper = BlockStateHelper.forBlock(Blocks.netherrack);
    private static final BlockStateHelper stateHelper1 = BlockStateHelper.forBlock(Blocks.soul_sand);
    private final IBlockState sandstoneSlab = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.NETHERBRICK).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
    private final IBlockState sandstone = Blocks.nether_brick.getDefaultState();
    private final IBlockState water = Blocks.soul_sand.getDefaultState();

    private static final List<WeightedRandomChestContent> itemsToGenerate = Lists.newArrayList(
            new WeightedRandomChestContent(Items.gold_nugget, 0, 8, 25, 2),
            new WeightedRandomChestContent(Items.glass_bottle, 0, 1, 3, 2),
            new WeightedRandomChestContent(Items.flint, 0, 1, 2, 2),
            new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 2, 1),
            new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 1),
            new WeightedRandomChestContent(Items.bone, 0, 1, 12, 3),
            new WeightedRandomChestContent(Items.cooked_porkchop, 0, 1, 4, 3),
            new WeightedRandomChestContent(Items.netherbrick, 0, 3, 15, 3));


    public boolean generate(World worldIn, Random rand, int x, int y, int z)
    {
        while (worldIn.isAirBlock(x,y,z) && y > 2)
        {
            y--;
        }

        if (!stateHelper.apply(worldIn.getBlockState(x,y,z)) && !stateHelper1.apply(worldIn.getBlockState(x,y,z)))
        {
            return false;
        }
        else
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (worldIn.isAirBlock(x,y-1,z) && worldIn.isAirBlock(x,y-2,z))
                    {
                        return false;
                    }
                }
            }

            worldIn.setBlockState(x,y,z, water, 2);
            worldIn.setBlockState(x+1,y,z, water, 2);
            worldIn.setBlockState(x-1,y,z, water, 2);
            worldIn.setBlockState(x,y,z+1, water, 2);
            worldIn.setBlockState(x,y,z-1, water, 2);

            for (int i1 = -2; i1 <= 2; ++i1)
            {
                for (int i2 = -2; i2 <= 2; ++i2)
                {
                    if (i1 == -2 || i1 == 2 || i2 == -2 || i2 == 2)
                    {
                        // sides of well
                        worldIn.setBlockState(x+i1, y+1, z+i2, this.sandstone, 2);
                    }
                }
            }

            worldIn.setBlockState(x+2, y+1, z, this.sandstoneSlab, 2);
            worldIn.setBlockState(x-2, y+1, z, this.sandstoneSlab, 2);
            worldIn.setBlockState(x, y+1, z+2, this.sandstoneSlab, 2);
            worldIn.setBlockState(x, 1, z-2, this.sandstoneSlab, 2);

            for (int j1 = -1; j1 <= 1; ++j1)
            {
                for (int j2 = -1; j2 <= 1; ++j2)
                {
                    if (j1 == 0 && j2 == 0)
                    {
                        // Spawn Chest
                        IBlockState chest = Blocks.chest.getDefaultState();
                        int j = -1;
                        System.out.println("well generating.... : " + (x+j1) + ";" + (y+j) + ";" + (z+j2));

                        worldIn.setBlockState(x+j1,y+j,z+j2, chest, 2);
                        TileEntity tileentity = worldIn.getTileEntity(new BlockPos(x+j1,j+y,z+j2));
                        if(tileentity instanceof TileEntityChest) {
                            WeightedRandomChestContent.generateChestContents(rand,itemsToGenerate,(TileEntityChest) tileentity, 5);
                        }

                        worldIn.setBlockState(x+j1, y+4,z+j2, this.sandstone, 2);
                    }
                    else
                    {
                        worldIn.setBlockState(x+j1, y+4, z+j2, this.sandstoneSlab, 2);
                    }
                }
            }

            for (int k1 = 1; k1 <= 3; ++k1)
            {
                // pillars of well
                worldIn.setBlockState(x-1, y+k1, z-1, this.sandstone, 2);
                worldIn.setBlockState(x-1, y+k1, z+1, this.sandstone, 2);
                worldIn.setBlockState(x+1, y+k1, z-1, this.sandstone, 2);
                worldIn.setBlockState(x+1, y+k1, z+1, this.sandstone, 2);
            }

            return true;
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        return generate(worldIn,rand,position.getX(),position.getY(),position.getZ());
    }
}
