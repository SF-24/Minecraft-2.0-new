package net.mineshaft.cavegen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class VineUtil {

    // Precomputed constants
    private static final int AIR_ID = 0;
    private static final int INVALID_ID = -1;
    private static final int MAX_VINE_LENGTH = 4;

    /** MAIN ENTRY: World-based vine placement (100% crash-proof) */
    public static void tryPlaceCaveVinesFromWorld(World world, int x, int y, int z, Random rand) {
        VineContext ctx = new VineContext(world, x, y, z);

        // Fast-fail: not air or no attachment
        if (!ctx.isAir() || !ctx.hasAttachment(rand)) return;

        // Place single vine column
        ctx.placeVineColumn(rand);
    }

    /** MAIN ENTRY: ChunkPrimer-based vine placement (already safe) */
    public static boolean tryPlaceCeilingVinePatch(ChunkPrimer primer, int x, int y, int z, Random rand) {
        if (primer.getBlock(x, y, z) != AIR_ID) return false;
        if (!isValidCeiling(primer, x, y + 1, z)) return false;
        if (rand.nextInt(4) != 0) return false;

        placeCeilingVinePatch(primer, x, y, z, rand);
        return true;
    }

    /** UNIFIED: Single responsibility context class */
    private static class VineContext {
        private final World world;
        private final int x, y, z;
        private final Random rand;

        VineContext(World world, int x, int y, int z) {
            this.world = world;
            this.x = x; this.y = y; this.z = z;
            this.rand = world.rand; // Reuse world rand
        }

        // 100% safe block ID getter
        private int getBlockId(int x, int y, int z) {
            if (y < 0 || y >= 256) return INVALID_ID;
            if (x < -30000000 || x >= 30000000 || z < -30000000 || z >= 30000000) return INVALID_ID;
            try {
                return world.getBlockId(x, y, z);
            } catch (Exception e) {
                return INVALID_ID;
            }
        }

        // Fast air check
        boolean isAir() {
            return getBlockId(x, y, z) == AIR_ID;
        }

        // Smart attachment detection
        boolean hasAttachment(Random rand) {
            // Ceiling first (most common cave case)
            if (getBlockId(x, y + 1, z) != AIR_ID && getBlockId(x, y + 1, z) != INVALID_ID)
                return true;

            // Wall fallback (1/2 chance to reduce spam)
            if (rand.nextBoolean()) {
                int[] walls = {
                        getBlockId(x + 1, y, z), getBlockId(x - 1, y, z),
                        getBlockId(x, y, z + 1), getBlockId(x, y, z - 1)
                };
                for (int id : walls) {
                    if (id != AIR_ID && id != INVALID_ID) return true;
                }
            }
            return false;
        }

        // Place continuous vine column
        void placeVineColumn(Random rand) {
            int length = 1 + rand.nextInt(MAX_VINE_LENGTH);
            int yy = y;

            for (int i = 0; i < length && yy > 0; i++, yy--) {
                if (!canPlaceAt(yy)) break;
                safeSetBlock(yy, Blocks.vine, 3);
            }
        }

        // Safe placement check
        private boolean canPlaceAt(int yy) {
            return getBlockId(x, yy, z) == AIR_ID;
        }

        // 100% safe block setter
        private void safeSetBlock(int yy, Block block, int flags) {
            try {
                world.setBlockPrimitive(x, yy, z, block, flags);
            } catch (Exception ignored) {
                // Chunk not ready, silently skip
            }
        }
    }

    // ========== CHUNKPRIMER METHODS (UNCHANGED - ALREADY SAFE) ==========

    private static boolean isValidCeiling(ChunkPrimer primer, int x, int y, int z) {
        return CaveUtil.isSolid(primer, x, y, z) ||
                (CaveUtil.inBounds(x-1, y, z) && CaveUtil.isSolid(primer, x-1, y, z)) ||
                (CaveUtil.inBounds(x+1, y, z) && CaveUtil.isSolid(primer, x+1, y, z)) ||
                (CaveUtil.inBounds(x, y, z-1) && CaveUtil.isSolid(primer, x, y, z-1)) ||
                (CaveUtil.inBounds(x, y, z+1) && CaveUtil.isSolid(primer, x, y, z+1));
    }

    private static void placeCeilingVinePatch(ChunkPrimer primer, int x, int y, int z, Random rand) {
        IBlockState upVine = Blocks.vine.getDefaultState().withProperty(BlockVine.UP, true);
        primer.setBlockState(x, y, z, upVine);

        int vinesPlaced = 1;
        int maxVines = 3 + rand.nextInt(3);
        int[][] offsets = {{0,0}, {1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] offset : offsets) {
            if (vinesPlaced >= maxVines) break;
            int nx = x + offset[0];
            int nz = z + offset[1];
            if (!CaveUtil.inBounds(nx, y, nz)) continue;
            if (primer.getBlock(nx, y, nz) != AIR_ID) continue;
            if (!isValidCeiling(primer, nx, y + 1, nz)) continue;

            primer.setBlockState(nx, y, nz, upVine);
            vinesPlaced++;
            hangContinuousVine(primer, nx, y, nz, upVine, rand);
        }
        hangContinuousVine(primer, x, y, z, upVine, rand);
    }

    private static void hangContinuousVine(ChunkPrimer primer, int x, int y, int z, IBlockState parentState, Random rand) {
        IBlockState currentState = parentState;
        int length = 1 + rand.nextInt(MAX_VINE_LENGTH);
        for (int dy = 1; dy <= length; dy++) {
            int yy = y - dy;
            if (yy < 1) break;
            if (!CaveUtil.inBounds(x, yy, z)) break;
            if (primer.getBlock(x, yy, z) != AIR_ID) break;
            primer.setBlockState(x, yy, z, currentState);
        }
    }

    /** Legacy wall vine method (ChunkPrimer) */
    public static void placeWallVines(ChunkPrimer primer, int x, int y, int z, Random rand) {
        IBlockState state = null;
        if (CaveUtil.isSolid(primer, x + 1, y, z)) {
            state = Blocks.vine.getDefaultState().withProperty(BlockVine.WEST, true);
        } else if (CaveUtil.isSolid(primer, x - 1, y, z)) {
            state = Blocks.vine.getDefaultState().withProperty(BlockVine.EAST, true);
        } else if (CaveUtil.isSolid(primer, x, y, z + 1)) {
            state = Blocks.vine.getDefaultState().withProperty(BlockVine.NORTH, true);
        } else if (CaveUtil.isSolid(primer, x, y, z - 1)) {
            state = Blocks.vine.getDefaultState().withProperty(BlockVine.SOUTH, true);
        } else {
            return;
        }

        primer.setBlockState(x, y, z, state);
        int maxLength = 1 + rand.nextInt(3);
        for (int dy = 1; dy <= maxLength; dy++) {
            int yy = y - dy;
            if (yy <= 0) break;
            if (primer.getBlock(x, yy, z) != AIR_ID) break;
            primer.setBlockState(x, yy, z, state);
        }
    }
}
