package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenerator
{
    /**
     * Sets wither or not the generator should notify blocks of blocks it changes. When the world is first generated,
     * this is false, when saplings grow, this is true.
     */
    private final boolean doBlockNotify;

    public WorldGenerator()
    {
        this(false);
    }

    public WorldGenerator(boolean notify)
    {
        this.doBlockNotify = notify;
    }

    public abstract boolean generate(World worldIn, Random rand, BlockPos position);

    public void func_175904_e()
    {
    }

    protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state)
    {
        this.state=state;
        if (this.doBlockNotify)
        {
            worldIn.setBlockState(pos, state, 3);
        }
        else
        {
            worldIn.setBlockState(pos, state, 2);
        }
    }

    private IBlockState state = null;

    public Block getBlock() {
        return state.getBlock();
    }
}
