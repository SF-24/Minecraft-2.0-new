package net.mineshaft;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.nether.WorldGenBasaltDeltas;
import net.minecraft.world.gen.feature.nether.WorldGenBasaltFlats;

import static net.minecraft.block.BlockStone.VARIANT;

public class NetherConfig {

    public static int netherBiomeScale = 256; // Was 128

    public static Block magmaBlock = Blocks.obsidian;
    public static Block basaltBlock = Blocks.obsidian;
    public static IBlockState blackstoneBlockState = Blocks.stone.getDefaultState().withProperty(VARIANT, BlockStone.EnumType.BLACKSTONE);
    public static Block smoothBasaltBlock = Blocks.stone;

    public static final WorldGenBasaltDeltas delta = new WorldGenBasaltDeltas(7,9);
    public static final WorldGenBasaltFlats flat_areas = new WorldGenBasaltFlats(7, 9);

}
