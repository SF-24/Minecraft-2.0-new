package net.mineshaft.data;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRenderData {

    public HashMap<String, String> capeLocation = new HashMap<>();
    public HashMap<String, ResourceLocation> capeResource = new HashMap<>();
    public HashMap<String, String> skinLocation = new HashMap<>();
    public HashMap<String, Boolean> loadedSkin = new HashMap<>();
    public HashMap<String, ResourceLocation> skinResource = new HashMap<>();
    public ArrayList<String> slimSkins = new ArrayList<>();

}
