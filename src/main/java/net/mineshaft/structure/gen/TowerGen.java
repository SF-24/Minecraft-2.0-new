package net.mineshaft.structure.gen;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.mineshaft.structure.LootTableList;

import java.util.Random;

public class TowerGen {

    public static int generateFloor(World worldIn, Random rand, int i, int j, int k, int size, int floorHeight, boolean raiseWindows) {
        return generateFloor(worldIn,rand,i,j,k,size, floorHeight, false,false,raiseWindows);
    }

    // Returns the height at which the next floor will begin to start.
    public static int generateFloor(World worldIn, Random rand, int i, int j, int k, int size, int floorHeight, boolean enforceStairs, boolean addStairHole, boolean raiseWindows) {
        if(floorHeight<7&&enforceStairs) {
            floorHeight = 7;
        } else if(floorHeight>8&&enforceStairs) {
            floorHeight = 8;
        }
        if(enforceStairs&& floorHeight==7&&size<4) size=4;
        if(enforceStairs&& floorHeight==8&&size<5) size=5;

        for(int x = -size; x<=size; x++) {
            for(int z = -size; z<=size; z++) {
                for(int y = -1; y<floorHeight; y++) {
                    if((x==-size || x==size || z==size || z==-size || y==-1)) {
                        if((x==-size || x==size) && (z==-size || z==size)) {
                            worldIn.setBlockPrimitive(x+i,y+j,z+k, Blocks.blackstone);
                        } else {
                            worldIn.setBlockPrimitive(x+i,y+j,z+k, Blocks.nether_brick);
                        }
                    } else {
                        worldIn.setBlockPrimitive(x+i,y+j,z+k, Blocks.air);
                    }
                }
            }
        }

        // Place the window
        placeWindow(worldIn,i+size,j+(raiseWindows?1:0),k);
        placeWindow(worldIn,i-size,j+(raiseWindows?1:0),k);
        placeWindow(worldIn,i,j+(raiseWindows?1:0),k+size);
        placeWindow(worldIn,i,j+(raiseWindows?1:0),k-size);

        // Add the stairs
        if(enforceStairs) {
            if(floorHeight==7) {
                worldIn.setBlockPrimitive(i-size+1,j,k-2, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+1,k-1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+2,k, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+3,k+1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+4,k+2, Blocks.nether_brick);
            } else if(floorHeight==8) {
                worldIn.setBlockPrimitive(i-size+1,j,k-3, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+1,k-2, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+2,k-1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+3,k, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+4,k+1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+5,k+2, Blocks.nether_brick);
            }
        }
        if(addStairHole) {
            worldIn.setBlockPrimitive(i-size+1,j-1,k-2, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k-1, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k+1, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k+2, Blocks.air);
        }

        // Spawn the chest in a random corner
        BlockPos chestPos = new BlockPos(i+(size-1)*(rand.nextBoolean() ? 1 : -1),j,k+(size-1)*(rand.nextBoolean() ? 1 : -1));
        worldIn.setBlockState(chestPos, Blocks.chest.getDefaultState());
        TileEntity tileEntityChest = worldIn.getTileEntity(chestPos);
        if (tileEntityChest instanceof TileEntityChest) {
            WeightedRandomChestContent.generateChestContents(rand, LootTableList.LootNether.NETHER_TOWER, (TileEntityChest) tileEntityChest, LootTableList.LootNether.getNetherTowerLootCount(rand));
        }

        // Spawn guard mobs
        int mobCount = 3 + rand.nextInt(2);
        for (int iteration = 0; iteration < mobCount; iteration++) {
            EntityLiving guard;

            // 50% chance for a Zombie, 50% chance for a Skeleton
            if (rand.nextBoolean()) {
                guard = new EntityPigZombie(worldIn);
            } else {
                guard = new EntitySkeleton(worldIn);
            }

            // Set the mob spawn position
            double spawnX = i + rand.nextInt(size-2)-size*0.5D;
            double spawnY = j + 0.5D; // Just so it won't glitch into a block.
            double spawnZ = k + rand.nextInt(size-2)-size*0.5D;

            guard.setLocationAndAngles(spawnX, spawnY, spawnZ, rand.nextFloat() * 360.0F, 0.0F);

            // Initialize mob equipment and attributes (like weapon holding)
            guard.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(guard)), null);

            guard.enablePersistence();

            if(guard instanceof EntitySkeleton) {
                ((EntitySkeleton)guard).setSkeletonType(0);
                if(rand.nextBoolean()) {
                    guard.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
                    guard.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
                } else {
                    guard.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
                    guard.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
                    guard.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
                }
            }
            for(int it = 0; it<5; it++) guard.setEquipmentDropChance(it,0);

            // Spawn the entity into the world
            worldIn.spawnEntityInWorld(guard);
        }

        return floorHeight+j-1;
    }

    public static void placeWindow(World world, int k, int j, int l) {
        world.setBlockPrimitive(k,j,l,Blocks.air);
        world.setBlockPrimitive(k,j+1,l,Blocks.air);
    }

    public static void placeLadder(World world, int k, int j, int l, int height) {
        for(int h = 0; h<height; h++) {
            world.setBlockPrimitive(k,j+h,l,Blocks.ladder);
        }
    }

    // Later, I will add a random roof
    public static void generateRoof(World worldIn, Random rand, int k, int j, int l, int size) {
        boolean isCharred = rand.nextBoolean();
        if(rand.nextBoolean()) {
            for(int x = -size-1; x<=size+1; x++) {
                for(int z = -size-1; z<=size+1; z++) {
                    if(isCharred) {
                        worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.blackstone);
                    } else {
                        worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.nether_brick);
                    }
                }
            }
        } else {
            for(int x = -size; x<=size; x++) {
                for(int z = -size; z<=size; z++) {
                    if(isCharred) {
                        worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.blackstone);
                    } else {
                        if(x==size||z==size||x==-size||z==-size) {
                            worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.blackstone);
                        } else {
                            worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.nether_brick);
                        }
                    }
                }
            }
        }
    }
}
