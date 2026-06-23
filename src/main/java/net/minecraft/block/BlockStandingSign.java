package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSign extends BlockSign
{
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

    public BlockStandingSign()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, Integer.valueOf(0)));
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.getBlockState(x,y-1,z).getBlock().getMaterial().isSolid())
        {
            this.dropBlockAsItem(worldIn, x,y,z, state, 0);
            worldIn.setBlockToAir(x,y,z);
        }

        super.onNeighborBlockChange(worldIn, x,y,z, state, neighborBlock);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ROTATION, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(ROTATION)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {ROTATION});
    }
}
