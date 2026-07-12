package net.minecraft.world.gen.feature.nether;

import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.mineshaft.NetherConfig;
import net.mineshaft.util.MathsUtil;
import net.mineshaft.util.RandomUtil;

import java.util.List;
import java.util.Random;

public class WorldGenBasaltDeltas extends WorldGenerator {
    private int minRadius;
    private int maxRadius;
    private int radius;

    public WorldGenBasaltDeltas(int minRadius, int maxRadius) {
        this.minRadius=minRadius;
        this.maxRadius=maxRadius;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        this.radius= RandomUtil.range(rand,minRadius,maxRadius);
        intereperetCircle(position.getX(), position.getY(), position.getZ(), worldIn);
        return true;
    }

    public void intereperetCircle(double x, double y, double z, World world) {


        List<BlockPos> affectedConversionPositions = Lists.newArrayList();
        int radius_int_conversion = (int) Math.ceil(radius);
        for (int dx = -radius_int_conversion; dx < radius_int_conversion + 1; dx++) {
            int y_lim = (int) Math.sqrt(radius_int_conversion*radius_int_conversion-dx*dx);
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int_conversion * radius_int_conversion - dx * dx - dy * dy);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    double power = MathsUtil.power(Math.sqrt(dx * dx + dy * dy + dz * dz), radius);
                    if ((power > 1) || (power > new Random().nextDouble())) {
                        affectedConversionPositions.add(blockPos);
                    }
                }
            }
        }

        for(BlockPos blockPos : affectedConversionPositions) {

            for(int c = (int) y + 9; c >= y - 10; c--) {
                BlockPos findSmooth = new BlockPos(blockPos.getX(), c, blockPos.getZ());
                if(world.getBlockState(findSmooth) == NetherConfig.smoothBasaltBlock.getDefaultState()&& world.getBlockState(findSmooth.add(0,1,0)) == NetherConfig.basaltBlock.getDefaultState()) {
                    y = c + 1;
                }
            }

            BlockPos relPos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
            //Make sure theres air above
            if(world.getBlockState(relPos) != NetherConfig.blackstoneBlockState && !world.isAirBlock(relPos) && world.getBlockState(relPos) != Blocks.lava.getDefaultState()) {

                if (world.isAirBlock(relPos.add(1, 0, 0)) || world.isAirBlock(relPos.add(-1, 0, 0)) || world.isAirBlock(relPos.add(0, 0, -1)) || world.isAirBlock(relPos.add(0, 0, 1)) ||
                        world.isAirBlock(relPos.add(0, -1, 0))) {
                    world.setBlockState(relPos, NetherConfig.blackstoneBlockState);
                } else {
                    if (world.rand.nextInt(4) == 0) {
                        world.setBlockState(relPos, NetherConfig.magmaBlock.getDefaultState());
                        world.setBlockState(relPos.add(0,-1,0), NetherConfig.blackstoneBlockState);
                        for (int b = relPos.getY() + 1; b <= relPos.getY() + 30; b++) {
                            BlockPos adjustY = new BlockPos(relPos.getX(), b, relPos.getZ());
                            if (world.getBlockState(adjustY) == NetherConfig.basaltBlock.getDefaultState() || world.getBlockState(adjustY) == NetherConfig.smoothBasaltBlock.getDefaultState()) {
                                world.setBlockToAir(adjustY);
                            } else {
                                break;
                            }
                        }
                    } else {
                        world.setBlockState(relPos, Blocks.lava.getDefaultState());
                        world.setBlockState(relPos.add(0,-1,0), NetherConfig.blackstoneBlockState);
                        for (int b = relPos.getY() + 1; b <= relPos.getY() + 19; b++) {
                            BlockPos adjustY = new BlockPos(relPos.getX(), b, relPos.getZ());
                            if (world.getBlockState(adjustY) == NetherConfig.basaltBlock.getDefaultState() || world.getBlockState(adjustY) == NetherConfig.smoothBasaltBlock.getDefaultState()) {
                                world.setBlockToAir(adjustY);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

        }
    }

    private int getNetherSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int maxY = max;
        int minY = min;
        int currentY = maxY;

        while(currentY >= minY)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)))
                return currentY;
            currentY--;
        }
        return 0;
    }
}
