package net.minecraft.client.resources;

import java.time.LocalDate;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import net.mineshaft.data.ProfileManager;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

public class DefaultPlayerSkin
{
    /** The default skin for the Steve model. */
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/player/steve.png");
//    private static final ResourceLocation TEXTURE_XPKITTY = new ResourceLocation("textures/entity/player/xpkitty.png");
//    private static final ResourceLocation TEXTURE_BAT89 = new ResourceLocation("textures/entity/player/bat89.png");
//    private static final ResourceLocation TEXTURE_DIAMONDCRAFT = new ResourceLocation("textures/entity/player/diamond_craftk.png");
//    private static final ResourceLocation TEXTURE_MINEBUSTER = new ResourceLocation("textures/entity/player/minebuster_.png");
//    private static final ResourceLocation TEXTURE_CATZEE = new ResourceLocation("textures/entity/player/catzeemeow.png");

    private static final ResourceLocation CAPE_BDAY = new ResourceLocation("textures/entity/cape/special/bday.png");
    private static final ResourceLocation CAPE_APRIL = new ResourceLocation("textures/entity/cape/special/white_eyes.png");
    private static final ResourceLocation CAPE_CHRISTMAS = new ResourceLocation("textures/entity/cape/special/christmas.png");
    private static final ResourceLocation CAPE_NEW_YEAR = new ResourceLocation("textures/entity/cape/special/new_year.png");
    public static final ResourceLocation CAPE_EMPTY = new ResourceLocation("textures/entity/cape/special/empty.png");

    /** The default skin for the Alex model. */
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/player/alex.png");

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

    public static ResourceLocation getDefaultSkin(String name) {
        if(ProfileManager.getPlayerSkinResourceLocation(name)!=null) {
            return ProfileManager.getPlayerSkinResourceLocation(name);
        } else {
            return TEXTURE_STEVE;
        }
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

    public static ResourceLocation getDefaultCape(String name) {
        if(getEventCape() != null) {
            return getEventCape();
        }
        return ProfileManager.getPlayerCapeResourceLocation(name);
    }



    /**
     * Retrieves the type of skin that a player is using. The Alex model is slim while the Steve model is default.
     */
    public static String getSkinType(String name)
    {
        return isSlimSkin(name) ? "slim" : "default";
    }

    /**
     * Checks if a players skin model is slim or the default. The Alex model is slime while the Steve model is default.
     */
    private static boolean isSlimSkin(String name)
    {
        return ProfileManager.isUserSkinSlim(name);
    }
}
