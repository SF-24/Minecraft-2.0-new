package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

import java.util.Random;

public class EntityEnderCrystal extends Entity
{
    /** Used to create the rotation animation when rendering the crystal. */
    public int innerRotation;
    public int health;

    public EntityEnderCrystal(World worldIn)
    {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(2.0F, 2.0F);
        this.health = 5;
        this.innerRotation = this.rand.nextInt(100000);
    }

    public EntityEnderCrystal(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(8, Integer.valueOf(this.health));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;
        this.dataWatcher.updateObject(8, Integer.valueOf(this.health));
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.worldObj.provider instanceof WorldProviderEnd && this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock() != Blocks.fire)
        {
            this.worldObj.setBlockState(new BlockPos(i, j, k), Blocks.fire.getDefaultState());
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.health = 0;

                //if (this.health <= 0)
                //{
                this.setDead();

                if (!this.worldObj.isRemote)
                {
                    this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 6.0F, true);
                    Random random = new Random();
                    int val = random.nextInt(3);
                    EntityItem item = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.amethyst, val));
                    if (val > 0) this.worldObj.spawnEntityInWorld(item);

                    int xMultiplier = 1;
                    int zMultiplier = 1;
                    if(random.nextInt(2)==1) xMultiplier=-1;
                    if(random.nextInt(2)==1) zMultiplier=-1;

                    item.setVelocity(1*xMultiplier, 1, 1*zMultiplier);
                }
                //}
            }

            return true;
        }
    }
}
