package net;

import net.minecraft.util.BlockPos;

public class BlockPosUtil {

    public static int addBlockPosX(int x, BlockPos pos) {
        return x + pos.getX();
    }

    public static int addBlockPosY(int y, BlockPos pos) {
        return y + pos.getY();
    }

    public static int addBlockPosZ(int z, BlockPos pos) {
        return z + pos.getZ();
    }

}
