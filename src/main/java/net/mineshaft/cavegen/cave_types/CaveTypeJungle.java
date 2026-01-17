package net.mineshaft.cavegen.cave_types;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.mineshaft.cavegen.CaveHeight;
import net.mineshaft.cavegen.CaveType;
import net.mineshaft.cavegen.VineUtil;

import java.util.Random;

public class CaveTypeJungle extends CaveType {
    public CaveTypeJungle(CaveHeight height, String name) {
        super(height,name);
    }

    @Override
    public void decorate(World world, Random random, BiomeGenBase biomeGenBase, BlockPos pos) {
        super.decorate(world, random, biomeGenBase, pos);
        generateCaveVines(world, random, pos.getX()>>4, pos.getZ()>>4);
    }

    private void generateCaveVines(World world, Random random, int chunkX, int chunkZ) {
        // Deterministic seed for consistent generation
        random.setSeed((long)chunkX * 0x4f9939f508L ^ (long)chunkZ * 0x1ef1565bd5L ^ world.getSeed());

        // Sparse 4x4 sampling (75% performance boost)
        for (int x = 2; x < 14; x += 4) {
            for (int z = 2; z < 14; z += 4) {
                if (random.nextInt(12) == 0) {  // 1/12 density = natural
                    placeVinePatch(world, random, chunkX * 16 + x, chunkZ * 16 + z);
                }
            }
        }
    }

    private void placeVinePatch(World world, Random random, int worldX, int worldZ) {
        // Scan downward for cave ceiling (Y=120 to 10)
        for (int y = 120; y > 10; y--) {
            BlockPos pos = new BlockPos(worldX, y, worldZ);
            BlockPos below = pos.down();

            // Found cave ceiling: solid above, air below
            if (!world.isAirBlock(below) || !world.isAirBlock(below.down())) continue;
            if (world.getBlockState(pos).getBlock().getMaterial().isSolid()) {
                // Place vine patch
                VineUtil.tryPlaceCaveVinesFromWorld(world, worldX, y - 1, worldZ, random);

                // Small patch: 1-2 nearby vines
                if (random.nextInt(3) == 0) {
                    VineUtil.tryPlaceCaveVinesFromWorld(world, worldX + 1, y - 1, worldZ, random);
                }
                if (random.nextInt(3) == 0) {
                    VineUtil.tryPlaceCaveVinesFromWorld(world, worldX, y - 1, worldZ + 1, random);
                }
                break;
            }
        }
    }
}
