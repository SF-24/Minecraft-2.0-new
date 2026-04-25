package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderSpider<T extends EntitySpider> extends RenderLiving<T> {
    private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");
    private static final ResourceLocation brownSpiderTextures = new ResourceLocation("textures/entity/spider/brown_spider.png");
    private static final ResourceLocation jungleSpiderTextures = new ResourceLocation("textures/entity/spider/jungle_spider.png");
    private static final ResourceLocation scorpionTextures = new ResourceLocation("textures/entity/spider/scorpion.png");

    private boolean isScorpion = false;

    public RenderSpider(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSpider(), 1.0F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntitySpider entitylivingbaseIn, float partialTickTime) {
        if (entitylivingbaseIn.getSpiderType() == 2) {
            GlStateManager.scale(0.8F, 0.8F, 0.8F);
        }
        if (entitylivingbaseIn.getSpiderType() == 3) {
            this.isScorpion = true;
        } else {
            this.isScorpion = false;
        }
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(T entity) {
        return entity.getSpiderType() == 0 ? spiderTextures : entity.getSpiderType() == 1 ? brownSpiderTextures : entity.getSpiderType() == 2 ? jungleSpiderTextures : scorpionTextures;
//        return spiderTextures;
    }
}
