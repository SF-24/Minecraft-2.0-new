package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class BlockGlowstone extends Block
{
    public BlockGlowstone(Material materialIn)
    {
        super(materialIn);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        int clampedFortune = 10-(2*fortune);
        if(clampedFortune<0) clampedFortune=0;
        int modifier = 0;
        int randomVal = random.nextInt(clampedFortune);
        if(randomVal<=1 && fortune>0) {
            modifier=1;
            System.out.println("duplication");
        }
        int returnVal = MathHelper.clamp_int(this.quantityDropped(random) + modifier, 0, 4);
        System.out.println("return " + returnVal);
        return returnVal;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;//1 + random.nextInt(3);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.glowstone_dust;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state)
    {
        return MapColor.sandColor;
    }
}
