package net.mineshaft.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.optifine.player.CapeImageBuffer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;
import java.util.UUID;

public class ProfileManager {

    public static ResourceLocation getPlayerCapeResourceLocation(GameProfile profile) {
        if(Minecraft.getMinecraft().userRenderData.capeResource.containsKey(profile.getName())) {return Minecraft.getMinecraft().userRenderData.capeResource.get(profile.getName());}
        ResourceLocation location = new ResourceLocation("textures/entity/cape/" + ProfileManager.getUserCapeName(profile) + ".png");
        Minecraft.getMinecraft().userRenderData.capeResource.put(profile.getName(), location);
        if(ProfileManager.getUserCapeName(profile).equals("empty")) {
            return new ResourceLocation("textures/entity/cape/empty.png");
        }
        return location;
    }

    public static ResourceLocation getPlayerSkinResourceLocation(GameProfile profile) {
        // cache skin slimness data
        isUserSkinSlim(profile);
        if(Minecraft.getMinecraft().userRenderData.skinResource.containsKey(profile.getName())) {return Minecraft.getMinecraft().userRenderData.skinResource.get(profile.getName());}
        ResourceLocation location = new ResourceLocation("textures/entity/player/skins/" + ProfileManager.getUserSkinName(profile) + ".png");
        if(ProfileManager.getUserCapeName(profile).equals("empty") || ProfileManager.getUserCapeName(profile).equals("classic") || ProfileManager.getUserCapeName(profile).equals("default")) {
            return new ResourceLocation("textures/entity/player/steve.png");
        }
        Minecraft.getMinecraft().userRenderData.skinResource.put(profile.getName(), location);
        return location;
    }


    public static boolean checkResourceExists(ResourceLocation location) {
        return Minecraft.getMinecraft().getTextureManager().getTexture(location)==null&& !Minecraft.getMinecraft().getTextureManager().getTexture(location).toString().isEmpty();
    }

    public static ITextureObject checkDownloadCape(ResourceLocation location, String capeName) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(location);
        if (itextureobject == null)
        {
            itextureobject = new ThreadDownloadImageData((File)null, String.format("https://mcreawakened.github.io/request/cape_images/%s.png", new Object[] {StringUtils.stripControlCodes(capeName)}), new ResourceLocation("skins/capes/"+capeName+".png"), new CapeImageBuffer(null,location));
            System.out.println("link: " + String.format("https://mcreawakened.github.io/request/cape_images/%s.png", new Object[] {StringUtils.stripControlCodes(capeName)}));
        //    ITextureObject textureobject = Minecraft.getMinecraft().getTextureManager().getTexture(new ResourceLocation("textures/entity/cape/"+capeName+".png"));
            Minecraft.getMinecraft().getTextureManager().loadTexture(location, itextureobject);
            //            Minecraft.getMinecraft().toLoad.add(new ResourceLocation("textures/entity/cape/"+capeName+".png"));
            System.out.println("location: " + location.toString());
        }
        return /*(ThreadDownloadImageData)*/itextureobject;
    }

    public static String getUserCapeName(GameProfile profile) {
        // check if registry exists
        try {
            if (Minecraft.getMinecraft().userRenderData.capeLocation.containsKey(profile.getName())) {
                return Minecraft.getMinecraft().userRenderData.capeLocation.get(profile.getName());
            }

            String url = "https://v0-backend-delta-taupe.vercel.app/cape?id=" + getUserId(profile);

            if (getUserId(profile) == null || getJson(url) == null) {
                Minecraft.getMinecraft().userRenderData.capeLocation.put(profile.getName(), "empty");
                return "empty";
            }
            String cape = Objects.requireNonNull(getJson(url)).getAsJsonObject().get("current_cape").getAsString();
            if (cape == null || cape.equals("none") || cape.equals("null")) {
                Minecraft.getMinecraft().userRenderData.capeLocation.put(profile.getName(), "empty");
                return "empty";
            }
            Minecraft.getMinecraft().userRenderData.capeLocation.put(profile.getName(), cape);

            System.out.println("cape name: " + cape);
            return cape;
        } catch (Exception ignored) {
            return "empty";
        }
    }

    public static String getUserSkinName(GameProfile profile) {
        // check if registry exists
        if(Minecraft.getMinecraft().userRenderData.skinLocation.containsKey(profile.getName())) {return Minecraft.getMinecraft().userRenderData.skinLocation.get(profile.getName());}

        String url = "https://v0-backend-delta-taupe.vercel.app/cape?id=" + getUserId(profile);

        if(getUserId(profile)==null || getJson(url)==null) return "steve";
        String skin = Objects.requireNonNull(getJson(url)).getAsJsonObject().get("current_skin").getAsString();
        if(skin==null || skin.equals("none") || skin.equals("null")) {return "steve";}
        Minecraft.getMinecraft().userRenderData.skinLocation.put(profile.getName(),skin);
        System.out.println("skin name: " + skin);
        return skin;
    }

    public static boolean isUserSkinSlim(GameProfile profile) {
        // check if registry exists
        if(Minecraft.getMinecraft().userRenderData.skinLocation.containsKey(profile.getName())) {return Minecraft.getMinecraft().userRenderData.slimSkins.contains(profile.getName());}

        String url = "https://v0-backend-delta-taupe.vercel.app/cape?id=" + getUserId(profile);

        boolean isSlim = Objects.requireNonNull(getJson(url)).getAsJsonObject().get("is_slim").getAsBoolean();
        if(isSlim) Minecraft.getMinecraft().userRenderData.slimSkins.add(profile.getName());
        return isSlim;
    }


    public static String getUserId(GameProfile profile) {

        String name = profile.getName();
        JsonElement json = getJson("https://api.mojang.com/users/profiles/minecraft/" + name);
        if(json != null) {
            try {
                String id = json.getAsJsonObject().get("id").getAsString();
                Field field = profile.getClass().getDeclaredField("id");
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                field.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                // Convert to UUID
                field.set(profile,java.util.UUID.fromString(
                                "5231b533ba17478798a3f2df37de2aD7"
                                        .replaceFirst(
                                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                                        )
                        )
                );
                return (id);
            } catch (Exception e) {
                System.out.println("Error loading uuid for user: " + name);
                e.printStackTrace();
                return UUID.randomUUID().toString();
            }
        }
        return "default";
    }

    public static JsonObject getJsonAsObject(String uri) {
        return getJson(uri).getAsJsonObject();
    }

    public static JsonElement getJson(String url) {
        return new JsonParser().parse(Objects.requireNonNull(getRequest(url)));
    }

    public static String getRequest(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);

            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    return EntityUtils.toString(entity);
                }

            } catch (Exception e) {
                System.out.println("fetching json failed. URL: " + url);
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    System.out.println("closing response failed. Error.");
                    return "null";
                }
            }
        } catch (Exception e) {
            System.out.println("fetching json failed. URL: " + url);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                System.out.println("closing http client failed. Error.");
                return "null";
            }
        }
        return null;
    }
}
