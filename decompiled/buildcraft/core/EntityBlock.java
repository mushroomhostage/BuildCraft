package buildcraft.core;

import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityBlock extends Entity
{
    public int texture;
    public float shadowSize;
    public float rotationX;
    public float rotationY;
    public float rotationZ;
    public double iSize;
    public double jSize;
    public double kSize;

    public EntityBlock(World var1)
    {
        super(var1);
        this.texture = -1;
        this.shadowSize = 0.0F;
        this.rotationX = 0.0F;
        this.rotationY = 0.0F;
        this.rotationZ = 0.0F;
        this.preventEntitySpawning = false;
        this.noClip = true;
        this.isImmuneToFire = true;
    }

    public EntityBlock(World var1, double var2, double var4, double var6, double var8, double var10, double var12)
    {
        this(var1);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = var2;
        this.prevPosY = var4;
        this.prevPosZ = var6;
        this.iSize = var8;
        this.jSize = var10;
        this.kSize = var12;
        this.setPosition(var2, var4, var6);
    }

    public EntityBlock(World var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14)
    {
        this(var1, var2, var4, var6, var8, var10, var12);
        this.texture = var14;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double var1, double var3, double var5)
    {
        this.posX = var1;
        this.posY = var3;
        this.posZ = var5;
        this.boundingBox.minX = this.posX;
        this.boundingBox.minY = this.posY;
        this.boundingBox.minZ = this.posZ;
        this.boundingBox.maxX = this.posX + this.iSize;
        this.boundingBox.maxY = this.posY + this.jSize;
        this.boundingBox.maxZ = this.posZ + this.kSize;
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    public void moveEntity(double var1, double var3, double var5)
    {
        this.setPosition(this.posX + var1, this.posY + var3, this.posZ + var5);
    }

    protected void entityInit() {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
        this.iSize = var1.getDouble("iSize");
        this.jSize = var1.getDouble("jSize");
        this.kSize = var1.getDouble("kSize");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setDouble("iSize", this.iSize);
        var1.setDouble("jSize", this.jSize);
        var1.setDouble("kSize", this.kSize);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
}
