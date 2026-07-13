package net.minecraft.world.biome;

import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.mineshaft.NetherConfig;
import net.mineshaft.util.RandomUtil;

import java.util.Random;

public class BiomeGenGravelCrags extends BiomeGenBase {
    public BiomeGenGravelCrags(int id) {
        super(id);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 50, 4, 4));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 100, 4, 4));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityMagmaCube.class, 100 /*was 1*/, 4, 4));
        this.topBlock = Blocks.blackstone.getDefaultState();
        this.fillerBlock = Blocks.blackstone.getDefaultState();
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos) {
        super.decorate(world, rand, pos);
        //  BASALT HEIGHTS
//      for(int k2 = 0; k2 < RandomUtil.range(rand,10, 20);k2++) {
//         int l6 = rand.nextInt(16) + 8;
//         int k10 = rand.nextInt(16) + 8;
//         int depthSignature = 2;
//         for(int y = 110; y > 32; y--) {
//            IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
//             if(depthSignature == 1) {
//                    // heights.generate(world, rand, pos.add(l6, y + 1, k10));
//             }
//
//             if(currentBlock == NetherConfig.basaltBlock.getDefaultState()) {
//                depthSignature++;
//          } else if (currentBlock == Blocks.air.getDefaultState()) {
//                depthSignature = 0;
//           }
//        }
//       }

//        Basalt Flat Parts
        for (int k2 = 0; k2 < RandomUtil.range(rand, 3, 5); k2++) {
            int l6 = rand.nextInt(16) + 8;
            int k10 = rand.nextInt(16) + 8;
            int depthSignature = 2;
            for (int y = 110; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                //                if(depthSignature == 1) {
//                    if(!world.isAirBlock(pos.add(l6, y, k10))) {
//                        NetherConfig.flat_areas.generate(world, rand, pos.add(l6, y, k10));
//                    }
//                }
                BlockPos posAbove = pos.add(l6, y + 1, k10);
                if (currentBlock == NetherConfig.smoothBasaltBlock.getDefaultState() && world.isAirBlock(posAbove)) {
//                    depthSignature++;
                    NetherConfig.flat_areas.generate(world, rand, posAbove);
                    break;
                }
            }
        }

        //  BASALT DELTAS
        for (int k2 = 0; k2 < RandomUtil.range(rand, 2, 4); k2++) {
            int l6 = rand.nextInt(16) + 8;
            int k10 = rand.nextInt(16) + 8;
//            int depthSignature = 2;
            for (int y = 110; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
//                if(depthSignature == 1) {
//                    if(!world.isAirBlock(pos.add(l6 + 8, y, k10 + 8)) && !world.isAirBlock(pos.add(l6 - 8, y, k10 - 8))) {
//                        NetherConfig.delta.generate(world, rand, pos.add(l6, y, k10));
//                    }
//                }

                BlockPos posAbove = pos.add(l6, y + 1, k10);

                if (currentBlock == NetherConfig.basaltBlock.getDefaultState() && world.isAirBlock(posAbove)) {
//                    depthSignature++;
                    NetherConfig.delta.generate(world, rand, posAbove);
                    break; // Skip to next column
                }
//                else if (currentBlock == NetherConfig.smoothBasaltBlock.getDefaultState() ) {
//                    depthSignature = 0;
//                }
            }
        }
    }
}