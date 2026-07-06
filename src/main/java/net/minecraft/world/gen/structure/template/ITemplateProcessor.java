package net.minecraft.world.gen.structure.template;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ITemplateProcessor
{
    @Nullable
    Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn);
}