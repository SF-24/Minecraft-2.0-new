package net.minecraft.client.resources;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin
{
    /** The default skin for the Steve model. */
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/player/steve.png");
    private static final ResourceLocation TEXTURE_XPKITTY = new ResourceLocation("textures/entity/player/xpkitty.png");
    private static final ResourceLocation TEXTURE_BAT89 = new ResourceLocation("textures/entity/player/bat89.png");
    private static final ResourceLocation TEXTURE_DIAMONDCRAFT = new ResourceLocation("textures/entity/player/diamond_craftk.png");
    private static final ResourceLocation TEXTURE_MINEBUSTER = new ResourceLocation("textures/entity/player/minebuster_.png");
    private static final ResourceLocation TEXTURE_CATZEE = new ResourceLocation("textures/entity/player/catzeemeow.png");

    private static final ResourceLocation CAPE_EMPTY = new ResourceLocation("textures/entity/cape/empty.png");
    private static final ResourceLocation CAPE_BDAY = new ResourceLocation("textures/entity/cape/bday.png");
    private static final ResourceLocation CAPE_APRIL = new ResourceLocation("textures/entity/cape/april_fool.png");
    private static final ResourceLocation CAPE_CHRISTMAS = new ResourceLocation("textures/entity/cape/christmas.png");
    private static final ResourceLocation CAPE_NEW_YEAR = new ResourceLocation("textures/entity/cape/new_year.png");
    private static final ResourceLocation CAPE_XPKITTY = new ResourceLocation("textures/entity/cape/xpkitty.png");
    private static final ResourceLocation CAPE_BAT89 = new ResourceLocation("textures/entity/cape/bat89.png");
    private static final ResourceLocation CAPE_DIAMONDCRAFT = new ResourceLocation("textures/entity/cape/diamond_craftk.png");
    private static final ResourceLocation CAPE_MINEBUSTER = new ResourceLocation("textures/entity/cape/minebuster_.png");
    private static final ResourceLocation CAPE_CATZEE = new ResourceLocation("textures/entity/cape/catzeemeow.png");

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
        switch (name) {
            case "XpKitty":
                return TEXTURE_XPKITTY;
            case "Bat89":
                return TEXTURE_BAT89;
            case "CatzeeMeow":
                return TEXTURE_CATZEE;
            case "diamond_craftK":
                return TEXTURE_DIAMONDCRAFT;
            case "Minebuster_":
                return TEXTURE_MINEBUSTER;
            default:
                return TEXTURE_STEVE;
        }
    }

    @Deprecated
    public static ResourceLocation getDefaultSkin(UUID playerUUID) {
        String uuid = playerUUID.toString();
        //System.out.println(uuid);
        switch (playerUUID.toString()) {
            case "d86f9151-d68b-3763-a550-ba936b4320ce":
            case "7e8a85ee3bb04158a1075a6886ffe438":
                return TEXTURE_XPKITTY;
            case "ad34c1b6-438f-3d28-b52f-0467fe71a04a":
            case "844252ff-ee25-45cc-8e88-9dc7fc8fb7c4":
            case "8de57200-4b6e-3740-9cb1-d60a76d58756":
                return TEXTURE_BAT89;
            case "8a64bfc0-0a73-3784-82d6-d70e99309066":
            case "f936a77a-e678-34f7-a229-d42b9b89744c":
            case "b5649d05-9bd6-454a-b2e6-4e0be019db16":
                return TEXTURE_CATZEE;
            case "bc0e6e5b-8117-34ed-bbee-72d4215a8ade":
            case "d51d3191-6286-406f-aa62-fcc2602f5f70":
            case "bfd1c099-d6e5-3e42-8569-24960aa04b19":
                return TEXTURE_DIAMONDCRAFT;
            case "c1d1e709-1bab-4de2-889e-31ef18300097":
            case "5bbc4f2e-1457-318c-b6a4-c4493bcae2a1":
            case "fafdae87-1703-3ffe-84dd-05a342145ec4":
                return TEXTURE_MINEBUSTER;
            default:
                return isSlimSkin(playerUUID) ? TEXTURE_ALEX : TEXTURE_STEVE;
        }
    }

    public static ResourceLocation getEventCape(boolean isXp) {
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
        if(((day==25&&month==8)||(day==7&&month==7)) && isXp) return CAPE_BDAY;
        return null;
    }

    public static ResourceLocation getDefaultCape(UUID playerUUID) {
        String uuid = playerUUID.toString();
        boolean isXpKitty = uuid.equals("d86f9151-d68b-3763-a550-ba936b4320ce")||uuid.equals("7e8a85ee3bb04158a1075a6886ffe438");

        if(getEventCape(isXpKitty) != null) {
            return getEventCape(isXpKitty);
        }
        //System.out.println(uuid);
        switch (playerUUID.toString()) {
            case "d86f9151-d68b-3763-a550-ba936b4320ce":
            case "7e8a85ee3bb04158a1075a6886ffe438":
                return CAPE_XPKITTY;
            case "ad34c1b6-438f-3d28-b52f-0467fe71a04a":
            case "844252ff-ee25-45cc-8e88-9dc7fc8fb7c4":
            case "8de57200-4b6e-3740-9cb1-d60a76d58756":
                return CAPE_BAT89;
            case "8a64bfc0-0a73-3784-82d6-d70e99309066":
            case "f936a77a-e678-34f7-a229-d42b9b89744c":
            case "b5649d05-9bd6-454a-b2e6-4e0be019db16":
                return CAPE_CATZEE;
            case "bc0e6e5b-8117-34ed-bbee-72d4215a8ade":
            case "d51d3191-6286-406f-aa62-fcc2602f5f70":
            case "bfd1c099-d6e5-3e42-8569-24960aa04b19":
                return CAPE_DIAMONDCRAFT;
            case "c1d1e709-1bab-4de2-889e-31ef18300097":
            case "5bbc4f2e-1457-318c-b6a4-c4493bcae2a1":
            case "fafdae87-1703-3ffe-84dd-05a342145ec4":
                return CAPE_MINEBUSTER;
            default:
                return CAPE_EMPTY;
        }
    }


    public static ResourceLocation getDefaultCape(String name) {
        boolean isXpKitty = name.equalsIgnoreCase("xpkitty");

        if(getEventCape(isXpKitty) != null) {
            return getEventCape(isXpKitty);
        }

        //System.out.println(uuid);
        switch (name) {
            case "XpKitty":
                return CAPE_XPKITTY;
            case "Bat89":
                return CAPE_BAT89;
            case "CatzeeMeow":
                return CAPE_CATZEE;
            case "diamond_craftK":
                return CAPE_DIAMONDCRAFT;
            case "Minebuster_":
                return CAPE_MINEBUSTER;
            default:
                return CAPE_EMPTY;
        }
    }


    /**
     * Retrieves the type of skin that a player is using. The Alex model is slim while the Steve model is default.
     */
    public static String getSkinType(UUID playerUUID)
    {
        switch (playerUUID.toString()) {
            case "d86f9151-d68b-3763-a550-ba936b4320ce":
            case "7e8a85ee3bb04158a1075a6886ffe438":
                // xp
                return "default";
            case "ad34c1b6-438f-3d28-b52f-0467fe71a04a":
            case "844252ff-ee25-45cc-8e88-9dc7fc8fb7c4":
                // bat89
                return "default";
            case "8a64bfc0-0a73-3784-82d6-d70e99309066":
            case "f936a77a-e678-34f7-a229-d42b9b89744c":
            case "b5649d05-9bd6-454a-b2e6-4e0be019db16":
                // catzee
                return "slim";
            case "bc0e6e5b-8117-34ed-bbee-72d4215a8ade":
            case "d51d3191-6286-406f-aa62-fcc2602f5f70":
                // diamondcraft
                return "default";
            case "c1d1e709-1bab-4de2-889e-31ef18300097":
            case "5bbc4f2e-1457-318c-b6a4-c4493bcae2a1":
            case "fafdae87-1703-3ffe-84dd-05a342145ec4":
                // minebuster
                return "slim";
            default:
                return isSlimSkin(playerUUID) ? "slim" : "default";
        }
    }

    /**
     * Checks if a players skin model is slim or the default. The Alex model is slime while the Steve model is default.
     */
    private static boolean isSlimSkin(UUID playerUUID)
    {
        return (playerUUID.hashCode() & 1) == 1;
    }
}
