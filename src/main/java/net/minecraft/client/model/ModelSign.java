package net.minecraft.client.model;

public class ModelSign extends ModelBase
{
    /** The board on a sign that has the writing on it. */
    public ModelRenderer signBoard;

    /** The stick a sign stands on. */
    public ModelRenderer signStick;

    public ModelSign(boolean isStanding, boolean isVisible) {
        if (isStanding) {
            this.signStick = new ModelRenderer(this, 0, 14);
            this.signStick.addBox(-1.0F, -2.0F, -1.0F, 2, 14, 2, 0.0F);
        }
        if(isVisible) {
            this.signBoard = new ModelRenderer(this, 0, 0);
            this.signBoard.addBox(-12.0F, -14.0F, -1.0F, 24, 12, 2, 0.0F);
        }
    }

    /**
     * Renders the sign model through TileEntitySignRenderer
     */
    public void renderSign(boolean isStanding, boolean isVisible) {
        if(isVisible) {
            this.signBoard.render(0.0625F);
        }
        if (isStanding) {
            this.signStick.render(0.0625F);
        }
    }
}
