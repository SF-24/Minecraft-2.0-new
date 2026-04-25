package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IRenderChunkFactory
{
//    RenderChunk makeRenderChunk(World worldIn, RenderGlobal globalRenderer, BlockPos pos, int index);
    RenderChunk makeRenderChunk(World worldIn, RenderGlobal globalRenderer, int x, int y, int z, int index);
}
