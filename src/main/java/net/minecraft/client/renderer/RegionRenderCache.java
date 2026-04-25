package net.minecraft.client.renderer;

import java.util.ArrayDeque;
import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.optifine.DynamicLights;

public class RegionRenderCache extends ChunkCache
{
    private static final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
//    private final BlockPos position;
    private final int posX;
    private final int posY;
    private final int posZ;
    private int[] combinedLights;
    private IBlockState[] blockStates;
    private static ArrayDeque<int[]> cacheLights = new ArrayDeque();
    private static ArrayDeque<IBlockState[]> cacheStates = new ArrayDeque();
    private static int maxCacheSize = Config.limit(Runtime.getRuntime().availableProcessors(), 1, 32);

    public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn)
    {
        super(worldIn, posFromIn.getX(),posFromIn.getY(),posFromIn.getZ(), posToIn.getX(),posToIn.getY(),posToIn.getZ(), subIn);
//        this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        posX = posFromIn.getX()-subIn;
        posY = posFromIn.getY()-subIn;
        posZ = posFromIn.getZ()-subIn;
        int i = 8000;
        this.combinedLights = allocateLights(8000);
        Arrays.fill((int[])((int[])this.combinedLights), (int) - 1);
        this.blockStates = allocateStates(8000);
    }

    public RegionRenderCache(World worldIn, int x1, int y1, int z1, int x2, int y2, int z2, int subIn)
    {
        super(worldIn, x1,y1,z1,x2,y2,z2, subIn);
//        this.position = new BlockPos(x1-subIn,y1-subIn,z1-subIn); // posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        posX=x1-subIn;
        posY=y1-subIn;
        posZ=z1-subIn;
        int i = 8000;
        this.combinedLights = allocateLights(8000);
        Arrays.fill((int[])((int[])this.combinedLights), (int) - 1);
        this.blockStates = allocateStates(8000);
    }


    public TileEntity getTileEntity(BlockPos pos)
    {
        int i = (pos.getX() >> 4) - this.chunkX;
        int j = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.QUEUED);
    }

    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        int i = this.getPositionIndex(pos.getX(),pos.getY(),pos.getZ());
        int j = this.combinedLights[i];

        if (j == -1)
        {
            j = super.getCombinedLight(pos, lightValue);

            if (Config.isDynamicLights() && !this.getBlockState(pos).getBlock().isOpaqueCube())
            {
                j = DynamicLights.getCombinedLight(pos, j);
            }

            this.combinedLights[i] = j;
        }

        return j;
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        int i = this.getPositionIndex(pos.getX(),pos.getY(),pos.getZ());
        IBlockState iblockstate = this.blockStates[i];

        if (iblockstate == null)
        {
            iblockstate = this.getBlockStateRaw(pos);
            this.blockStates[i] = iblockstate;
        }

        return iblockstate;
    }

    private IBlockState getBlockStateRaw(BlockPos pos)
    {
        return super.getBlockState(pos);
    }

    // Works now. Mineshaft
    private int getPositionIndex(int xIn, int yIn, int zIn)
    {
        int i = xIn - posX;
        int j = yIn - posY;
        int k = zIn - posZ;
        return i * 400 + j * 20 + k;
    }

    public void freeBuffers()
    {
        freeLights(this.combinedLights);
        freeStates(this.blockStates);
    }

    private static int[] allocateLights(int p_allocateLights_0_)
    {
        synchronized (cacheLights)
        {
            int[] aint = (int[])cacheLights.pollLast();

            if (aint == null || aint.length < p_allocateLights_0_)
            {
                aint = new int[p_allocateLights_0_];
            }

            return aint;
        }
    }

    public static void freeLights(int[] p_freeLights_0_)
    {
        synchronized (cacheLights)
        {
            if (cacheLights.size() < maxCacheSize)
            {
                cacheLights.add(p_freeLights_0_);
            }
        }
    }

    private static IBlockState[] allocateStates(int p_allocateStates_0_)
    {
        synchronized (cacheStates)
        {
            IBlockState[] aiblockstate = (IBlockState[])cacheStates.pollLast();

            if (aiblockstate != null && aiblockstate.length >= p_allocateStates_0_)
            {
                Arrays.fill(aiblockstate, (Object)null);
            }
            else
            {
                aiblockstate = new IBlockState[p_allocateStates_0_];
            }

            return aiblockstate;
        }
    }

    public static void freeStates(IBlockState[] p_freeStates_0_)
    {
        synchronized (cacheStates)
        {
            if (cacheStates.size() < maxCacheSize)
            {
                cacheStates.add(p_freeStates_0_);
            }
        }
    }
}
