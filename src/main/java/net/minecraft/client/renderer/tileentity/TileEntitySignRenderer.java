package net.minecraft.client.renderer.tileentity;

import net.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Point3d;
import java.util.List;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign>
{
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");

    private static final ModelSign standingModel = new ModelSign(true);
    private static final ModelSign wallModel = new ModelSign(false);
    private static final float f = 0.6666667F;


    /** The ModelSign instance for use in this renderer */
//    private ModelSign model = new ModelSign(false);
//    private static double textRenderDistanceSq = Constants.signRenderDistanceSquared;

    // Distance check: (te.getPos().getX()-entity.posX)*(te.getPos().getX()-entity.posX)+(te.getPos().getY()-entity.posY)*(te.getPos().getY()-entity.posY)+(te.getPos().getZ()-entity.posZ)*(te.getPos().getZ()-entity.posZ)<Config.getSignRenderDistanceSquared()

    public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if((te.getPos().getX() - Config.getMinecraft().getRenderViewEntity().posX) * (te.getPos().getX() - Config.getMinecraft().getRenderViewEntity().posX) + (te.getPos().getY() - Config.getMinecraft().getRenderViewEntity().posY) * (te.getPos().getY() - Config.getMinecraft().getRenderViewEntity().posY) + (te.getPos().getZ() - Config.getMinecraft().getRenderViewEntity().posZ) * (te.getPos().getZ() - Config.getMinecraft().getRenderViewEntity().posZ) >= Config.getSignRenderDistanceSquared()) {
            return;
        }
        GlStateManager.pushMatrix();

//        if(block == Blocks.standing_sign && !(Config.getMinecraft().currentScreen instanceof GuiEditSign)) {
////            model = new ModelSign(block == Blocks.standing_sign); //Constants.signRenderDistanceSquared);
//            model.setStanding();
//        }

//        if (Config.getMinecraft().currentScreen instanceof GuiEditSign) {
////            this.model = new ModelSign(true, true);
//            this.model.signStick.showModel=false;
//        }

        if (te.getBlockType() == Blocks.standing_sign)
        {
            GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
            // Rotation
            GlStateManager.rotate(-((float)te.getBlockMetadata() * 360) / 16.0F, 0.0F, 1.0F, 0.0F);
//            this.model.signStick.showModel = true;
        }
        else
        {
            int k = te.getBlockMetadata();
            float f2 = 0.0F;

            if (k == 2)
            {
                f2 = 180.0F;
            }

            if (k == 4)
            {
                f2 = 90.0F;
            }

            if (k == 5)
            {
                f2 = -90.0F;
            }

            GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
            GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
            //this.model.signStick.showModel = false;
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(SIGN_TEXTURE);
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(f, -f, -f);
        if (te.getBlockType() == Blocks.standing_sign) {
            standingModel.renderSign();//new Point3d(te.getPos().getX(),te.getPos().getY(),te.getPos().getZ()).distanceSquared(new Point3d(entity.posX,entity.posY,entity.posZ))<Config.getSignRenderDistanceSquared());//Constants.signRenderDistanceSquared);
        } else {
            wallModel.renderSign();//new Point3d(te.getPos().getX(),te.getPos().getY(),te.getPos().getZ()).distanceSquared(new Point3d(entity.posX,entity.posY,entity.posZ))<Config.getSignRenderDistanceSquared());//Constants.signRenderDistanceSquared);
        }

        GlStateManager.popMatrix();

        if (isRenderText(te))
        {
            FontRenderer fontrenderer = this.getFontRenderer();
            float f3 = 0.015625F * f;
            GlStateManager.translate(0.0F, 0.5F * f, 0.07F * f);
            GlStateManager.scale(f3, -f3, f3);
            GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
            GlStateManager.depthMask(false);
            int i = 0;

            if (Config.isCustomColors())
            {
                i = CustomColors.getSignTextColor(i);
            }

            if (destroyStage < 0 && (te.getPos().getX() - Config.getMinecraft().getRenderViewEntity().posX) * (te.getPos().getX() - Config.getMinecraft().getRenderViewEntity().posX) + (te.getPos().getY() - Config.getMinecraft().getRenderViewEntity().posY) * (te.getPos().getY() - Config.getMinecraft().getRenderViewEntity().posY) + (te.getPos().getZ() - Config.getMinecraft().getRenderViewEntity().posZ) * (te.getPos().getZ() - Config.getMinecraft().getRenderViewEntity().posZ)*2 < Config.getSignRenderDistanceSquared() && te.signText.length>0) {
                // Render text:
                for (int j = 0; j < te.signText.length; ++j)
                {
                    if (te.signText[j] != null)
                    {
//                        IChatComponent ichatcomponent = te.signText[j];
                        List<IChatComponent> list = GuiUtilRenderComponents.splitText(te.signText[j], 90, fontrenderer, false, true);
                        String s = !list.isEmpty() ? ((IChatComponent)list.get(0)).getFormattedText() : "";

                        if (j == te.lineBeingEdited)
                        {
                            s = "> " + s + " <";
//                            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                        }
//                        else
//                        {
//                        }
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                    }
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private static boolean isRenderText(TileEntitySign p_isRenderText_0_)
    {
        if (Shaders.isShadowPass)
        {
            return false;
        }
        else if (Config.getMinecraft().currentScreen instanceof GuiEditSign)
        {
            return true;
        }
        else
        {
            if (!Config.zoomMode && p_isRenderText_0_.lineBeingEdited < 0)
            {
                Entity entity = Config.getMinecraft().getRenderViewEntity();
                double d0 = p_isRenderText_0_.getDistanceSq(entity.posX, entity.posY, entity.posZ);


                if (d0 > Config.getSignRenderDistanceSquared())//Constants.signRenderDistanceSquared)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public static void updateTextRenderDistance()
    {
//        Minecraft minecraft = Config.getMinecraft();
//        // double d0 = (double)Config.limit(minecraft.gameSettings.gammaSetting, 1.0F, 120.0F);
//        //double d1 = Math.max(1.5D * (double)minecraft.displayHeight / d0, 16.0D);
//        //Constants.signRenderDistanceSquared = d1 * d1;
//        if(!minecraft.gameSettings.fancyGraphics) {
//            Constants.signRenderDistanceSquared=Constants.shortSignDistanceSquared;
//        } else {
//            Constants.signRenderDistanceSquared=Constants.longSignDistanceSquared;
//        }
    }
}
