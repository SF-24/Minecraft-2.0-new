package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class VboChunkFactory implements IRenderChunkFactory
{
    public RenderChunk makeRenderChunk(World worldIn, RenderGlobal globalRenderer, BlockPos pos, int index)
    {
        return new RenderChunk(worldIn, globalRenderer, pos.getX(),pos.getY(),pos.getZ(), index);
    }

    @Override
    public RenderChunk makeRenderChunk(World worldIn, RenderGlobal globalRenderer, int x, int y, int z, int index) {
        return new RenderChunk(worldIn, globalRenderer, x,y,z, index);
    }
}
