package net.minecraft.world.gen.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenRock extends WorldGenAbstractTree {
    private final int size;
    private final IBlockState block;
    private final IBlockState blockAlt;
    private final IBlockState blockRare;
    private final boolean enableRareBlocks;
    private static final BlockStateHelper stateHelper = BlockStateHelper.forBlock(Blocks.gravel);
    private static final BlockStateHelper stateHelper1 = BlockStateHelper.forBlock(Blocks.grass);
    private static final BlockStateHelper stateHelper2 = BlockStateHelper.forBlock(Blocks.stone);

    public WorldGenRock(IBlockState block1, IBlockState block2, int size)
    {
        super(false);
        this.size=size;
        this.enableRareBlocks=false;
        this.block= block1;
        this.blockAlt= block2;
        this.blockRare=null;
    }

    public WorldGenRock(IBlockState block1, IBlockState block2, IBlockState blockRare, int size)
    {
        super(false);
        this.size=size;
        this.enableRareBlocks=true;
        this.block= block1;
        this.blockRare= blockRare;
        this.blockAlt= block2;
    }




    public boolean generate(World worldIn, Random rand, BlockPos pos) {


        while (worldIn.isAirBlock(pos) && pos.getY() > 2)
        {
            pos = pos.down();
        }

        if (!stateHelper.apply(worldIn.getBlockState(pos)) && !stateHelper1.apply(worldIn.getBlockState(pos)) && !stateHelper2.apply(worldIn.getBlockState(pos)))
        {
            return false;
        }
        else {

            for (int x = -size; x <= size; x++) {
                for (int y = -size; y <= size; y++) {
                    for (int z = -size; z <= size; z++) {
                        if (!(Math.abs(z) == size && Math.abs(x) == size) && !(Math.abs(z) == size && Math.abs(y) == size) && !(Math.abs(x) == 2 && Math.abs(y) == 2)) {
                            if(enableRareBlocks && rand.nextInt(7)==0) {
                                worldIn.setBlockState(pos.add(x, y, z), blockRare);
                            } else if (rand.nextInt(2) == 0) {
                                worldIn.setBlockState(pos.add(x, y, z), blockAlt);
                            } else {
                                worldIn.setBlockState(pos.add(x, y, z), block);
                            }
                        }
                    }
                }
            }
            return true;
        }

    }

}
