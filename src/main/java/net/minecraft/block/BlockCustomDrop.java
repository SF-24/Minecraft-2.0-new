package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockCustomDrop extends Block
{
    int dropItemId;
    public BlockCustomDrop(int dropItemId, Material material) {
        super(material);
        this.dropItemId = dropItemId;
    }

    /**
    * Spawns this Block's drops into the World as EntityItems.
    */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemById(dropItemId);
    }
}
