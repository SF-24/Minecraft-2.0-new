package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.List;

public class BlockDungeon extends Block
{

    public static final PropertyEnum<BlockDungeon.EnumType> TYPE = PropertyEnum.create("type", BlockDungeon.EnumType.class);

    public BlockDungeon()
    {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.COMPRESSED_COBBLESTONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public MapColor getMapColor(IBlockState state)
    {
        return MapColor.stoneColor;
    }


    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPE).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return Blocks.compressed_cobblestone.getDefaultState().withProperty(TYPE, BlockDungeon.EnumType.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TYPE).getMetadata();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, TYPE);
    }


    public static enum EnumType implements IStringSerializable {
        COMPRESSED_COBBLESTONE(0, "compressed_cobblestone", "cobblestoneCompressed"),
        MOSSY_COMPRESSED_COBBLESTONE(1, "mossy_compressed_cobblestone", "cobblestoneCompressedMossy");

        private static final BlockDungeon.EnumType[] META_LOOKUP = new BlockDungeon.EnumType[values().length];
        private final int metadata;
        private final String name;
        private final String unlocalizedName;

        EnumType(int meta, String name, String unlocalizedName) {
            this.metadata = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String toString() {
            return this.name;
        }

        public static BlockDungeon.EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            for (BlockDungeon.EnumType element : values()) {
                META_LOOKUP[element.getMetadata()] = element;
            }
        }
    }

}
