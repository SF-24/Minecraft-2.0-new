package net.minecraft.client.renderer.entity;

import net.minecraft.MineshaftLogger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSnowball<T extends Entity> extends Render<T>
{
    protected final Item renderedItem;
    private final RenderItem field_177083_e;

    public RenderSnowball(RenderManager renderManagerIn, Item renderedItem, RenderItem p_i46137_3_)
    {
        super(renderManagerIn);
        this.renderedItem = renderedItem;
        this.field_177083_e = p_i46137_3_;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.depthMask(false);


        // Scale the model based on whether it's riding an entity
        if(entity instanceof EntitySnowball && ((EntitySnowball) entity).getProjectileType()==10) {

            if (entity.riddenByEntity!=null) {
                if(((EntitySnowball) entity).getRenderOffset() == 0f) {
                    // Cache the values.
                    ((EntitySnowball) entity).setRenderScale(1.125f * Math.max(entity.riddenByEntity.height, entity.riddenByEntity.width * 1.2f));
                    ((EntitySnowball) entity).setRenderOffset(entity.riddenByEntity.height / 2.0f);
                }
                float renderScale = ((EntitySnowball) entity).getRenderScale();
                GlStateManager.translate(0, ((EntitySnowball) entity).getRenderOffset(), 0);
                GlStateManager.scale(renderScale, renderScale, renderScale);
            } else {
                GlStateManager.scale(1.0f,1.0f,1.0f);
            }
        } else {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
//        if(entity.riddenByEntity==null||entity.riddenByEntity.isDead) {
//        }
        this.bindTexture(TextureMap.locationBlocksTexture);
        this.field_177083_e.renderItem(this.getITemStack(entity), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.depthMask(true);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public ItemStack getITemStack(T entityIn)
    {
        if(entityIn instanceof EntitySnowball) {
            // Will have more values later.
            switch (((EntitySnowball) entityIn).getProjectileType()) {
                case 1: return new ItemStack(Items.wind_charge, 1, 0);
                case 10: return new ItemStack(Items.display_bubble, 1, 0);
                default: return new ItemStack(this.renderedItem, 1, 0);
            }
        }
        return new ItemStack(this.renderedItem, 1, 0);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
