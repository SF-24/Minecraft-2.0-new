package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAetherPillar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeGenAether extends BiomeGenBase {
    //ArrayList<BlockPos> blockIgnore = new ArrayList<>();

    public BiomeGenAether(int id) {
        super(id);
        this.hasBeach=false;
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySheep.class, 10, 1, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 10, 4, 4));
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        this.theBiomeDecorator = new BiomeAetherDecorator();
    }

    //ArrayList<BlockPos> blockedPos = new ArrayList<>();

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        ArrayList<Integer> blockI = new ArrayList<>();
        ArrayList<Integer> blockJ = new ArrayList<>();

        for (int k2 = 0; k2 < 16; ++k2) {
            for (int j3 = 0; j3 < 16; ++j3) {



                BlockPos blockpos1 = worldIn.getPrecipitationHeight(pos.add(k2, 0, j3)); //ERROR
                ArrayList<BlockPos> blockPos = new ArrayList<>();

                blockPos.add(blockpos1.down());
                blockPos.add(blockpos1.down(2));

                Block top = worldIn.getBiomeGenForCoords(blockpos1).topBlock.getBlock();

                //boolean b = worldIn.getPrecipitationHeight(pos.add(k2, 0, j3)).getY() != worldIn.getHeight(pos.add(k2, 0, j3)).getY();
                //worldIn.getPrecipitationHeight(pos.add(k2,0,j3)).getY()!=worldIn.getHeight(pos.add(k2,0,j3)).getY();

                if (/*!b&&*/!top.equals(Blocks.stone_slab) && !top.equals(Blocks.chest) && !top.equals(Blocks.air) && !top.equals(Blocks.water) && !top.equals(Blocks.cobblestone) && !top.equals(Blocks.stonebrick) && !top.equals(Blocks.mossy_cobblestone)) {


                    BlockPos pos1 = blockpos1;

                        /*for (BlockPos position : blockedPos) {
                            if (position.getX() == pos1.getX() && position.getZ() == pos1.getZ()) {
                                return;
                            }
                        }*/

                    int m = 0;
                    while (pos1.equals(Blocks.leaves) || pos.equals(Blocks.leaves2)) {
                        pos1 = pos1.down();
                        m++;
                        if (m >= 12) {
                            pos1 = blockpos1;
                            break;
                        }
                    }

                    Block top1 = worldIn.getBiomeGenForCoords(pos1).topBlock.getBlock();


                    if (!(top1 instanceof BlockLeaves && !top1.equals(Blocks.leaves) && !top1.equals(Blocks.leaves2))) {

                        //if (blockIgnore.contains(pos1)) return;

                        worldIn.setBlockState(pos1, Blocks.grass.getDefaultState());

                        for (BlockPos blockPo : blockPos) {
                            if (!worldIn.getBlockState(blockPo).getBlock().equals(Blocks.air)) {
                                worldIn.setBlockState(blockPo, Blocks.dirt.getDefaultState());

                            }
                        }

                    }
                }
            }
        }
        //System.out.printf("h: " + worldIn.getHeight(pos));


       // genWells(worldIn,rand,pos);

        blockI.clear();
        blockJ.clear();
        //worldIn.setBlockState(pos.add(0,worldIn.getHeight(pos).getY(),0),Blocks.diamond_block.getDefaultSta

    }

    public void genWells(World worldIn, Random rand, BlockPos pos) {
        if (rand.nextInt(10) == 0) {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 0, j)).up();

//            blockI.add(i - 2);
//            blockI.add(i - 1);
//            blockI.add(i);
//            blockI.add(i + 1);
//            blockI.add(i + 2);
//            blockJ.add(j - 2);
//            blockJ.add(j - 1);
//            blockJ.add(j);
//            blockJ.add(j + 1);
//            blockJ.add(j + 2);

            //blockedPos.add(pos);

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    //blockedPos.add(new BlockPos(blockpos.getX() + x, blockpos.getY(), blockpos.getZ() + z));
                    //if (blockedPos.size() > 30) blockedPos.remove(0);
                }
            }


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
            ArrayList<BlockPos> add = (new WorldGenAetherPillar()).generateBlocks(worldIn, rand, p.down());
            //blockIgnore.addAll(add);
            worldIn.setBlockState(p.down(2), Blocks.chest.getDefaultState());
            TileEntity tileentity1 = worldIn.getTileEntity(p.down(2));
            if (tileentity1 instanceof TileEntityChest) {
                WeightedRandomChestContent.generateChestContents(rand, chestContents, (TileEntityChest) tileentity1, 8);
            }
        }
    }
}