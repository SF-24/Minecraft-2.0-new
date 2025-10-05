package net.minecraft.client.resources;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.ResourceLocation;
import net.mineshaft.data.ProfileManager;

import java.time.LocalDate;
import java.util.UUID;

public class DefaultPlayerSkin
{
    /** The default skin for the Steve model. */
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/player/steve.png");
    /** The default skin for the Alex model. */
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

    private static final ResourceLocation CAPE_BDAY = new ResourceLocation("textures/entity/cape/special/bday.png");
    private static final ResourceLocation CAPE_APRIL = new ResourceLocation("textures/entity/cape/special/white_eyes.png");
    private static final ResourceLocation CAPE_CHRISTMAS = new ResourceLocation("textures/entity/cape/special/christmas.png");
    private static final ResourceLocation CAPE_NEW_YEAR = new ResourceLocation("textures/entity/cape/special/new_year.png");
    public static final ResourceLocation CAPE_EMPTY = new ResourceLocation("textures/entity/cape/special/empty.png");

    /**
     * Returns the default skind for versions prior to 1.8, which is always the Steve texture.
     */
    public static ResourceLocation getDefaultSkinLegacy()
    {
        return TEXTURE_STEVE;
    }

    /**
     * Retrieves the default skin for this player. Depending on the model used this will be Alex or Steve.
     */

    public static ResourceLocation getDefaultSkin(GameProfile profile) {
            return TEXTURE_STEVE;
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
        if(getEventCape() != null) {
            return getEventCape();
        }
        return null;
//        return new ResourceLocation("textures/entity/cape/empty.png");
//        return ProfileManager.getPlayerCapeResourceLocation(profile);
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
        return false;
//        return ProfileManager.isUserSkinSlim(profile);
    }
}
