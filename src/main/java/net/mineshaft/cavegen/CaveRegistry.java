package net.mineshaft.cavegen;

import net.minecraft.world.biome.BiomeGenBase;

public class CaveRegistry {

    public static CaveType getCaveType(BiomeGenBase biome, int height){
        CaveType caveType = null;
        if(height>23) {
            // Biome dependant

        } else {
            // Separate placement
            return null;
        }
        return caveType;
    }

}
