package net.minecraft.world.gen.feature.nether;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.mineshaft.structure.gen.TowerGen;

import java.util.Random;

public class WorldGenNetherTower extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        // Generate the tower
        int size = rand.nextInt(2)+4;
        int yPosition = position.getY();
        for(int c = 0; c<2; c++) {
            yPosition = TowerGen.generateFloor(worldIn,rand,position.getX(),yPosition,position.getZ(),size,7,true,c!=0,c!=0);
        }
        if(rand.nextBoolean()) {
            yPosition = TowerGen.generateFloor(worldIn,rand,position.getX(),yPosition,position.getZ(),size,7,false,true,true);
            TowerGen.generateRoof(worldIn,rand,position.getX(),yPosition,position.getZ(),size);
        } else {
            TowerGen.generateFloor(worldIn,rand,position.getX(),yPosition,position.getZ(),size,1,false,true,true);
        }

        return false;
    }


}
