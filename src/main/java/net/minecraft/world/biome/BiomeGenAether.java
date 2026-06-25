package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeGenAether extends BiomeGenBase
{
    private WorldGenerator theWorldGenerator = new WorldGenMinable(Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 9);
    private WorldGenTaiga2 field_150634_aD = new WorldGenTaiga2(false);
    private int field_150635_aE = 0;
    private int field_150638_aH;

    protected BiomeGenAether(int id)
    {
        super(id);
        this.field_150638_aH = this.field_150635_aE;
        this.hasBeach=false;
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        return rand.nextInt(2) > 0 ? this.field_150634_aD : super.genBigTreeChance(rand);
    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        super.decorate(worldIn, rand, pos);
        int i = 1 + rand.nextInt(6);

        for (i = 0; i < 7; ++i)
        {
            int j1 = rand.nextInt(16);
            int k1 = rand.nextInt(64);
            int l1 = rand.nextInt(16);
            this.theWorldGenerator.generate(worldIn, rand, pos.add(j1, k1, l1));
        }
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        super.genTerrainBlocks(worldIn,rand,chunkPrimerIn,x,z,noiseVal);
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    protected BiomeGenBase createMutatedBiome(int p_180277_1_)
    {
        return (new BiomeGenAether(p_180277_1_));
    }


    public void genWells(World worldIn, Random rand, BlockPos pos) {
        if (rand.nextInt(10) == 0) {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 0, j)).up();

            final List<WeightedRandomChestContent> chestContents = Lists.newArrayList(
                    new WeightedRandomChestContent(Items.glowing_bread, 0, 1, 1, 2),
                    new WeightedRandomChestContent(Items.glowstone_dust, 0, 1, 6, 30),
                    new WeightedRandomChestContent(Items.slime_ball, 0, 1, 2, 5),
                    new WeightedRandomChestContent(Items.ruby, 0, 1, 1, 2),
                    new WeightedRandomChestContent(Items.apple, 0, 1, 2, 15),
                    new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 3),
                    new WeightedRandomChestContent(Items.record_magnetic_circuit, 0, 1, 1, 1),
                    new WeightedRandomChestContent(Items.bread, 0, 1, 2, 5),
                    new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 2, 5),
                    new WeightedRandomChestContent(Items.gold_nugget, 0, 2, 10, 5));

            BlockPos p = worldIn.getPrecipitationHeight(blockpos.add(i, 0, j)).up();
            worldIn.setBlockState(p.down(3), Blocks.chest.getDefaultState());
            TileEntity tileentity1 = worldIn.getTileEntity(p.down(2));
            if (tileentity1 instanceof TileEntityChest) {
                WeightedRandomChestContent.generateChestContents(rand, chestContents, (TileEntityChest) tileentity1, 8);
            }
        }
    }
}
