package net.mineshaft.cavegen;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class VineUtil {

    public static void tryPlaceCaveVines(ChunkPrimer primer, int x, int y, int z, Random rand) {
        // Cheaper air check
        if (primer.getBlock(x,y,z) != 0) return;

        // Try to attach to walls first
        IBlockState state = null;

        if (CaveUtil.isSolid(primer, x + 1, y, z)) {
            state = Blocks.vine.getDefaultState()
                    .withProperty(BlockVine.WEST, true);
        } else if (CaveUtil.isSolid(primer, x - 1, y, z)) {
            state = Blocks.vine.getDefaultState()
                    .withProperty(BlockVine.EAST, true);
        } else if (CaveUtil.isSolid(primer, x, y, z + 1)) {
            state = Blocks.vine.getDefaultState()
                    .withProperty(BlockVine.NORTH, true);
        } else if (CaveUtil.isSolid(primer, x, y, z - 1)) {
            state = Blocks.vine.getDefaultState()
                    .withProperty(BlockVine.SOUTH, true);
        } else {
            // No supporting wall
            return;
        }

        if (state == null) return;

        // Place first vine block
        primer.setBlockState(x, y, z, state);

        // Optionally extend downwards a bit
        int maxLength = 1 + rand.nextInt(3);
        for (int dy = 1; dy <= maxLength; dy++) {
            int yy = y - dy;
            if (yy <= 0) break;
            if (primer.getBlockState(x, yy, z).getBlock() != Blocks.air) break;
            primer.setBlockState(x, yy, z, state);
        }
    }

    public static void cleanupFloatingVines(ChunkPrimer primer) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    IBlockState state = primer.getBlockState(x, y, z);
                    if (state.getBlock() == Blocks.vine) {
                        if (!hasValidVineSupport(primer, x, y, z, state)) {
                            // Remove unsupported vine
                            primer.setBlockState(x, y, z, Blocks.air.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    private static boolean hasValidVineSupport(ChunkPrimer primer, int x, int y, int z, IBlockState vine) {
        // Check the sides that this vine actually uses
        if (vine.getValue(BlockVine.WEST)  && CaveUtil.isSolid(primer, x + 1, y, z)) return true;
        if (vine.getValue(BlockVine.EAST)  && CaveUtil.isSolid(primer, x - 1, y, z)) return true;
        if (vine.getValue(BlockVine.NORTH) && CaveUtil.isSolid(primer, x, y, z + 1)) return true;
        if (vine.getValue(BlockVine.SOUTH) && CaveUtil.isSolid(primer, x, y, z - 1)) return true;
        // If you allow ceiling vines, also check above.
        return false;
    }

}
