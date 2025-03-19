package net.mineshaft.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.CustomCapeImageBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
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
import java.util.Objects;

public class ProfileManager {

    public static ResourceLocation getPlayerCapeResourceLocation(String name) {
        if(Minecraft.getMinecraft().userRenderData.capeResource.containsKey(name)) {return Minecraft.getMinecraft().userRenderData.capeResource.get(name);}
        ResourceLocation location = new ResourceLocation("skins/capes/"+ProfileManager.getUserCapeName(name)+".png");
        if(ProfileManager.getUserCapeName(name).equals("empty")) {
            return new ResourceLocation("textures/entity/cape/special/empty.png");
        }
//        if(!checkResourceExists(location) && checkResourceExists(new ResourceLocation("textures/entity/cape/"+ProfileManager.getUserCapeName(name)+".png"))) {
//            return new ResourceLocation("textures/entity/cape/" + ProfileManager.getUserCapeName(name) + ".png");
//        }
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

    public static String getUserCapeName(String name) {
        // check if registry exists
        if(Minecraft.getMinecraft().userRenderData.capeLocation.containsKey(name)) {return Minecraft.getMinecraft().userRenderData.capeLocation.get(name);}

        if(getUserId(name)==null || getJson("https://mcreawakened.github.io/request/cape/"+ getUserId(name) +".json")==null) return "empty";
        String cape = Objects.requireNonNull(getJson("https://mcreawakened.github.io/request/cape/" + getUserId(name) + ".json")).getAsJsonObject().get("current_cape").getAsString();
        if(cape==null || cape.equals("none") || cape.equals("null")) return "empty";

        Minecraft.getMinecraft().userRenderData.capeLocation.put(name,cape);
        System.out.println("cape name: " + cape);
        return cape;
    }

    public static String getUserId(String name) {
        JsonElement json = getJson("https://api.mojang.com/users/profiles/minecraft/" + name);
        if(json != null) {
            return (json.getAsJsonObject().get("id").getAsString());
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
