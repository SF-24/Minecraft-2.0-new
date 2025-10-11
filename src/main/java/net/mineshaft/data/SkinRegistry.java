package net.mineshaft.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.mineshaft.MineshaftLogger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class SkinRegistry {

    protected static JsonParser jsonParser = new JsonParser();

    protected static TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }
            }
    };


    public static JsonElement parseJson(String json) {
//        System.out.println("parsing json " + json);
        if(json==null) return jsonParser.parse("{}");
        return jsonParser.parse(Objects.requireNonNull(json));
    }

    public static String getRequest(String url) {
        try {
            // Set cert acceptation
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            connection.setSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format(url)).openConnection();

            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                return new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining());
            } else {
                System.out.println("Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
                return null;
            }
        } catch (IOException e) {
            MineshaftLogger.logWarning("request for url " + url + " failed: " + e.getMessage());
            return "{}";
        } catch (NullPointerException ignored) {
            MineshaftLogger.logWarning("request for url " + url + " failed: " + "null");
            return "{}";
        }
    }

    public static UUID getUUID(GameProfile profile) {
        String reply = getRequest(String.format("https://api.mojang.com/users/profiles/minecraft/%s", profile.getName()));
        return UUID.fromString(parseJson(reply).getAsJsonObject().get("id").getAsString().replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
        ));
    }

    public static void loadSkins(String name, UUID uuid) {
        String reply = getRequest(String.format("https://v0-backend-delta-taupe.vercel.app/cape?id=%s", UUIDTypeAdapter.fromUUID(uuid)));
        String skin = parseJson(reply).getAsJsonObject().get("current_skin").getAsString(); // skin
        boolean isSlim = parseJson(reply).getAsJsonObject().get("is_slim").getAsBoolean(); //slim
        String cape = parseJson(reply).getAsJsonObject().get("current_cape").getAsString(); // cape
        if(skin==null||skin.isEmpty()) skin="steve";
        if(cape==null||cape.isEmpty()) cape="empty";
        Minecraft.getMinecraft().userRenderData.capeResource.put(name, new ResourceLocation("textures/entity/cape/"+cape+".png"));
        if(isSlim) {
            Minecraft.getMinecraft().userRenderData.slimSkins.add(name);
        }
        Minecraft.getMinecraft().userRenderData.skinResource.put(name, new ResourceLocation("textures/entity/player/skins/"+skin+".png"));
    }

    public static String getSkinName(UUID uuid) {
        String reply = getRequest(String.format("https://v0-backend-delta-taupe.vercel.app/cape?id=%s", UUIDTypeAdapter.fromUUID(uuid)));
        return parseJson(reply).getAsJsonObject().get("current_skin").getAsString();
    }

    public static boolean isSkinSlim(UUID uuid) {
        String reply = getRequest(String.format("https://v0-backend-delta-taupe.vercel.app/cape?id=%s", UUIDTypeAdapter.fromUUID(uuid)));
        return parseJson(reply).getAsJsonObject().get("is_slim").getAsBoolean();
    }

    public static String getCapeName(UUID uuid) {
        String reply = getRequest(String.format("https://v0-backend-delta-taupe.vercel.app/cape?id=%s", UUIDTypeAdapter.fromUUID(uuid)));
        return parseJson(reply).getAsJsonObject().get("current_cape").getAsString();
    }

    public static boolean setSkin(GameProfile profile, UUID uuid) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                String reply = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                String skin = reply.split("\"value\":\"")[1].split("\"")[0];
                String signature = reply.split("\"signature\":\"")[1].split("\"")[0];
                profile.getProperties().put("textures", new Property("textures", skin, signature));
                MineshaftLogger.logInfo("Skin set: " + profile.getName());
                return true;
            } else {
                System.out.println("Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



}
