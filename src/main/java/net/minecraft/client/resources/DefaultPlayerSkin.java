package net.minecraft.client.resources;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.mineshaft.data.SkinRegistry;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DefaultPlayerSkin
{
    /** The default skin for the Steve model. */
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/player/steve.png");
    /** The default skin for the Alex model. */
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

    private static final ResourceLocation CAPE_B_DAY = new ResourceLocation("textures/entity/cape/special/bday.png");
    private static final ResourceLocation CAPE_APRIL = new ResourceLocation("textures/entity/cape/special/white_eyes.png");
    private static final ResourceLocation CAPE_CHRISTMAS = new ResourceLocation("textures/entity/cape/special/christmas.png");
    private static final ResourceLocation CAPE_NEW_YEAR = new ResourceLocation("textures/entity/cape/special/new_year.png");
    public static final ResourceLocation CAPE_EMPTY = new ResourceLocation("textures/entity/cape/special/empty.png");

    /**
     * Returns the default skind for versions prior to 1.8, which is always the Steve texture.
     */
    public static ResourceLocation getDefaultSkinLegacy()
    {
        System.out.println("! Loading default skin legacy ...");
        return TEXTURE_STEVE;
    }

    /**
     * Retrieves the default skin for this player. Depending on the model used this will be Alex or Steve.
     */

    public static ResourceLocation getDefaultSkin(GameProfile profile) {
        if (Minecraft.getMinecraft().userRenderData.skinResource.containsKey(profile.getName())) {
            return Minecraft.getMinecraft().userRenderData.skinResource.get(profile.getName());
        }
        String name = SkinRegistry.getSkinName(SkinRegistry.getUUID(profile));
        System.out.println("! Loading default skin " + name);
        if (name.equals("steve")) {
            Minecraft.getMinecraft().userRenderData.skinResource.put(profile.getName(), TEXTURE_STEVE);
            return TEXTURE_STEVE;
        }
        // If the skin is slim, add it to the slim skin cache
        if (SkinRegistry.isSkinSlim(SkinRegistry.getUUID(profile))) {
            Minecraft.getMinecraft().userRenderData.slimSkins.add(profile.getName());
        }
        Minecraft.getMinecraft().userRenderData.skinResource.put(profile.getName(), new ResourceLocation("textures/entity/player/skins/"+name+".png"));
        return new ResourceLocation("textures/entity/player/skins/"+name+".png");
    }

    public static ResourceLocation getEventCape() {
        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        if(day==1&&month==4) {
            return CAPE_APRIL;
        }
        if(day>21&&day<30&&month==12) {
            return CAPE_CHRISTMAS;
        }
        if((day>=30&&month==12)||(day<=6&&month==1)) {
            return CAPE_NEW_YEAR;
        }
        return null;
    }

    // TODO: Clean up

    public static ResourceLocation getDefaultCape(GameProfile profile) {
        if(Minecraft.getMinecraft().userRenderData.capeResource.containsKey(profile.getName())) {
            return Minecraft.getMinecraft().userRenderData.capeResource.get(profile.getName());
        }
        if(getEventCape() != null) {
            return getEventCape();
        }
        return getPlayerCape(profile);
//        return new ResourceLocation("textures/entity/cape/empty.png");
//        return ProfileManager.getPlayerCapeResourceLocation(profile);
    }

    public static ResourceLocation getPlayerCape(GameProfile profile) {
        String name = SkinRegistry.getCapeName(SkinRegistry.getUUID(profile));
        Minecraft.getMinecraft().userRenderData.capeResource.put(profile.getName(), new ResourceLocation("textures/entity/cape/"+name+".png"));
        return new ResourceLocation("textures/entity/cape/"+name+".png");
    }

    /**
     * Retrieves the type of skin that a player is using. The Alex model is slim while the Steve model is default.
     */
    public static String getSkinType(GameProfile profile)
    {
        return isSlimSkin(profile) ? "slim" : "default";
    }

    /**
     * Checks if a players skin model is slim or the default. The Alex model is slime while the Steve model is default.
     */
    private static boolean isSlimSkin(GameProfile profile)
    {
        try {
            Field field = profile.getClass().getField("skin");
        } catch (NoSuchFieldException ignored) {
        }
        return Minecraft.getMinecraft().userRenderData.slimSkins.contains(profile.getName());
    }
}
