package net.mineshaft.cavegen;

import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class CaveUtil {

    public static void decorateCave(ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random rand) {
        // small chance to place vines here
        if (rand.nextInt(12) == 0) {
            VineUtil.tryPlaceCaveVines(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
        }

    }

    public static boolean isSolid(ChunkPrimer primer, int x, int y, int z) {
        if(!inBounds(x,y,z)) return false;
        return primer.getBlockState(x, y, z).getBlock().getMaterial().isSolid();
    }

    private static boolean inBounds(int x, int y, int z) {
        return x >= 0 && x < 16
                && z >= 0 && z < 16
                && y >= 0 && y < 256;
    }

}
