package net.minecraft.world.gen.feature.nether;

import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.mineshaft.NetherConfig;
import net.mineshaft.util.MathsUtil;
import net.mineshaft.util.RandomUtil;

import java.util.List;
import java.util.Random;

public class WorldGenBasaltHeights extends WorldGenerator {

    private int radius;

    public WorldGenBasaltHeights(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        intereperetCircle(position.getX(), position.getY(), position.getZ(), worldIn,rand);
        return true;
    }

    public void intereperetCircle(double x, double y, double z, World world, Random rand) {
        List<BlockPos> affectedConversionPositions = Lists.newArrayList();
        int radius_int_conversion = (int) Math.ceil(radius);
        for (int dx = -radius_int_conversion; dx < radius_int_conversion + 1; dx++) {
            int y_lim = (int) Math.sqrt(radius_int_conversion * radius_int_conversion - dx * dx);
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

        for (BlockPos blockPos : affectedConversionPositions) {
            int findSurface = getNetherSurfaceHeight(world, blockPos, blockPos.getY() - 2, blockPos.getY() + 5);
            BlockPos relPos = new BlockPos(blockPos.getX(), findSurface, blockPos.getZ());
            if (!world.isAirBlock(relPos.down()) && findSurface != 0 && world.getBlockState(relPos).getBlock() == NetherConfig.basaltBlock && world.isAirBlock(relPos.up())) {

                for (int t = 0; t < RandomUtil.range(rand,1, 5); t++) {
                    world.setBlockState(relPos.add(0, t, 0), NetherConfig.basaltBlock.getDefaultState());
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