package net.minecraft.world.gen;

import com.google.common.base.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.mineshaft.cavegen.CaveConstants;
import net.mineshaft.cavegen.CaveUtil;

import java.util.Random;


public class MapGenCaves extends MapGenBase {

//    final Random random = new Random();

//    final List<Integer> desertIds = Arrays.asList(2,130,17);

    protected void addRoom(long randomSeed, int originX, int originZ, ChunkPrimer chunkPrimer, double tunnelCentreX, double tunnelCentreY, double tunnelCentreZ)
    {
        this.addTunnel(randomSeed, originX, originZ, chunkPrimer, tunnelCentreX, tunnelCentreY, tunnelCentreZ, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    // gen room !!!
    // Add a cave type definition.
    // Step index: increment in cavegen
    // Step count: total number of steps
    protected void addTunnel(long randomSeed, int originX, int originZ, ChunkPrimer chunkPrimer, double tunnelCentreX, double tunnelCentreY, double tunnelCentreZ, float baseRadius, float tunnelYaw, float tunnelPitch, int stepIndex, int stepCount, double ellipsoidStretchFactor)
    {
        double d0 = originX * 16 + 8;
        double d1 = originZ * 16 + 8;
        float f = 0.0F;
        float f1 = 0.0F;
        // Gen existing random variable to avoid recreating it
        // warning. may override default random
        Random random = new Random(randomSeed);
//        random.setSeed(randomSeed);

        // Cache chunk origins ONCE outside all loops
        int chunkOriginX = originX * 16;
        int chunkOriginZ = originZ * 16;

        if (stepCount <= 0)
        {
            int i = this.range * 16 - 16;
            stepCount = i - random.nextInt(i / 4);
        }

        boolean flag2 = false;

        if (stepIndex == -1)
        {
            stepIndex = stepCount / 2;
            flag2 = true;
        }

        int j = random.nextInt(stepCount / 2) + stepCount / 4;

        for (boolean flag = random.nextInt(6) == 0; stepIndex < stepCount; ++stepIndex)
        {
            double d2 = 1.5D + (double)(MathHelper.sin((float)stepIndex * (float)Math.PI / (float)stepCount) * baseRadius * 1.0F);
            double d3 = d2 * ellipsoidStretchFactor;
            float f2 = MathHelper.cos(tunnelPitch);
            float f3 = MathHelper.sin(tunnelPitch);
            tunnelCentreX += MathHelper.cos(tunnelYaw) * f2;
            tunnelCentreY += f3;
            tunnelCentreZ += MathHelper.sin(tunnelYaw) * f2;

            if (flag)
            {
                tunnelPitch = tunnelPitch * 0.92F;
            }
            else
            {
                tunnelPitch = tunnelPitch * 0.7F;
            }

            tunnelPitch = tunnelPitch + f1 * 0.1F;
            tunnelYaw += f * 0.1F;
            f1 = f1 * 0.9F;
            f = f * 0.75F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!flag2 && stepIndex == j && baseRadius > 1.0F && stepCount > 0)
            {
                this.addTunnel(random.nextLong(), originX, originZ, chunkPrimer, tunnelCentreX, tunnelCentreY, tunnelCentreZ, random.nextFloat() * 0.5F + 0.5F, tunnelYaw - ((float)Math.PI / 2F), tunnelPitch / 3.0F, stepIndex, stepCount, 1.0D);
                this.addTunnel(random.nextLong(), originX, originZ, chunkPrimer, tunnelCentreX, tunnelCentreY, tunnelCentreZ, random.nextFloat() * 0.5F + 0.5F, tunnelYaw + ((float)Math.PI / 2F), tunnelPitch / 3.0F, stepIndex, stepCount, 1.0D);
                return;
            }

            if (flag2 || random.nextInt(4) != 0)
            {
                double d4 = tunnelCentreX - d0;
                double d5 = tunnelCentreZ - d1;
                double d6 = stepCount - stepIndex;
                double d7 = baseRadius + 2.0F + 16.0F;

                if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7)
                {
                    return;
                }

                if (tunnelCentreX >= d0 - 16.0D - d2 * 2.0D && tunnelCentreZ >= d1 - 16.0D - d2 * 2.0D && tunnelCentreX <= d0 + 16.0D + d2 * 2.0D && tunnelCentreZ <= d1 + 16.0D + d2 * 2.0D)
                {
                    int k2 = MathHelper.floor_double(tunnelCentreX - d2) - originX * 16 - 1;
                    int k = MathHelper.floor_double(tunnelCentreX + d2) - originX * 16 + 1;
                    int l2 = MathHelper.floor_double(tunnelCentreY - d3) - 1;
                    int l = MathHelper.floor_double(tunnelCentreY + d3) + 1;
                    int i3 = MathHelper.floor_double(tunnelCentreZ - d2) - originZ * 16 - 1;
                    int i1 = MathHelper.floor_double(tunnelCentreZ + d2) - originZ * 16 + 1;

                    if (k2 < 0)
                    {
                        k2 = 0;
                    }

                    if (k > 16)
                    {
                        k = 16;
                    }

                    if (l2 < 1)
                    {
                        l2 = 1;
                    }

                    if (l > 248)
                    {
                        l = 248;
                    }

                    if (i3 < 0)
                    {
                        i3 = 0;
                    }

                    if (i1 > 16)
                    {
                        i1 = 16;
                    }

                    boolean flag3 = false;
//                    waterCheck:
                    for (int j1 = k2; !flag3 && j1 < k; ++j1)
                    {
                        for (int k1 = i3; !flag3 && k1 < i1; ++k1)
                        {
                            for (int l1 = l + 1; !flag3 && l1 >= l2 - 1; --l1)
                            {
                                if (l1 >= 0 && l1 < 256)
                                {
                                    IBlockState iblockstate = chunkPrimer.getBlockState(j1, l1, k1);

                                    if (iblockstate.getBlock() == Blocks.flowing_water || iblockstate.getBlock() == Blocks.water)
                                    {
                                        flag3 = true;
                                        // Added optimisation to end the loop earlier and immediately complete the water check.
                                        // check for errors
//                                        break waterCheck;
                                    }

                                    if (l1 != l2 - 1 && j1 != k2 && j1 != k - 1 && k1 != i3 && k1 != i1 - 1)
                                    {
                                        l1 = l2;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag3)
                    {
                        /**
                         * Block removal:
                         * */

                        for (int localChunkX = k2; localChunkX < k; ++localChunkX)
                        {
//                            double d10 = ((double)(localChunkX + originX * 16) + 0.5D - tunnelCentreX) / d2;

                            for (int localChunkZ = i3; localChunkZ < i1; ++localChunkZ)
                            {

                                // NEW - precompute distXZ, early Y-exit
                                double dx = (localChunkX + chunkOriginX + 0.5D - tunnelCentreX) / d2;
                                double dz = (localChunkZ + chunkOriginZ + 0.5D - tunnelCentreZ) / d2;
                                double distXZSq = dx * dx + dz * dz;

//                                double d8 = ((double)(localChunkZ + originZ * 16) + 0.5D - tunnelCentreZ) / d2;

                                boolean flag1 = false;

                                if (distXZSq < 1.0D)
                                {
                                    for (int localChunkY = l; localChunkY > l2; --localChunkY)
                                    {
                                        double dy = ((double)(localChunkY - 1) + 0.5D - tunnelCentreY) / d3;
                                        double distSq = distXZSq + dy * dy;

                                        if (dy > -0.7D && distSq < 1.0D)
                                        {
                                            // Optimised:
                                            int id1=chunkPrimer.getBlock(localChunkX,localChunkY,localChunkZ);
                                            int id2=chunkPrimer.getBlock(localChunkX,localChunkY+1,localChunkZ);
                                            //IBlockState iblockstate1 = p_180702_5_.getBlockState(j3, j2, i2);
                                            IBlockState stateAbove = Objects.firstNonNull(chunkPrimer.getBlockState(localChunkX, localChunkY + 1, localChunkZ), Blocks.air.getDefaultState());

                                            flag1 = (id1 == 32 || id1 == 1760);

                                            if (this.isCarvableBlock(id1, id2))
                                            {

                                                // generate lava under a certain y level. Default: 10 -> changed to 4
                                                if (localChunkY - 1 < 4)
                                                {
                                                    chunkPrimer.setBlockState(localChunkX, localChunkY, localChunkZ, Blocks.lava.getDefaultState());
                                                }
                                                else
                                                {
                                                    // test for certain block
                                                    // set primitive air
                                                    chunkPrimer.setBlockFromId(localChunkX, localChunkY, localChunkZ, (short) 0);

                                                    // Cave decoration logic, optimised
                                                    // Only 1/15 blocks attempt to decorate
                                                    // And function will only be parsed if the block is near a wall
                                                    // 0.0-> centre, 0.3-> near wall, 1.0-> edge of tunnel
                                                    if(distXZSq < 0.3) {
                                                        // Check which random var should be used.
                                                        if (random.nextInt(15) == 0) {
                                                            CaveUtil.decorateCave(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
                                                        }
                                                    }

                                                    // replace stone with ice in snowy biomes
//                                                    if(j2+1>40 && this.worldObj.getBiomeGenForCoords(blockpos$mutableblockpos).isSnowyBiome() && iblockstate2.getBlock() == Blocks.stone) {
//                                                        iblockstate2=Blocks.snow.getDefaultState();
//                                                        // TODO:
//                                                    }

                                                    if (id2 == Block.getMultipliedIdFromBlock(Blocks.sand))
                                                    {
                                                        chunkPrimer.setBlockState(localChunkX, localChunkY + 1, localChunkZ, stateAbove.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
                                                    }

                                                    if (flag1 && chunkPrimer.getBlock(localChunkX, localChunkY - 1, localChunkZ) == 48)
                                                    {
                                                        int x = localChunkX + originX * 16;
                                                        int z = localChunkZ + originZ * 16;
                                                        // was block state
                                                        chunkPrimer.setBlockState(localChunkX, localChunkY - 1, localChunkZ, this.worldObj.getBiomeGenForCoords(x,z).topBlock.getBlock().getDefaultState());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (flag2)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected boolean isCarvableBlock(int idOfBlockToRemove, int idOfBlockAbove)
    {
        return (idOfBlockToRemove == 16 || (idOfBlockToRemove == 48 || (idOfBlockToRemove == 32 || (idOfBlockToRemove == Block.getMultipliedIdFromBlock(Blocks.hardened_clay) || (idOfBlockToRemove == Block.getMultipliedIdFromBlock(Blocks.stained_hardened_clay) || (idOfBlockToRemove == 384) || (idOfBlockToRemove == 2864) || (idOfBlockToRemove == 1760) || (idOfBlockToRemove == 1248) || (idOfBlockToRemove == 192) || idOfBlockToRemove == 208)) &&
                (idOfBlockAbove != Block.getMultipliedIdFromBlock(Blocks.water) && idOfBlockAbove != Block.getMultipliedIdFromBlock(Blocks.flowing_water)))));
    }

    /**
     * Recursively called by generate()
     */
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int originalX, int originalZ, ChunkPrimer chunkPrimerIn)
    {
        // density
        int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(CaveConstants.caveSize /*was 15*/) + 1) + 1);

        // amount
        if (this.rand.nextInt(CaveConstants.caveChance /*was 7*/) != 0)
        {
            i = 0;
        }

        for (int j = 0; j < i; ++j)
        {
            double d0 = chunkX * 16 + this.rand.nextInt(16);
            double d1 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            double d2 = chunkZ * 16 + this.rand.nextInt(16);
            int k = 1;

            if (this.rand.nextInt(4) == 0)
            {
                this.addRoom(this.rand.nextLong(), originalX, originalZ, chunkPrimerIn, d0, d1, d2);
                k += this.rand.nextInt(4);
            }

            for (int l = 0; l < k; ++l)
            {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0)
                {
                    f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.addTunnel(this.rand.nextLong(), originalX, originalZ, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }
}


