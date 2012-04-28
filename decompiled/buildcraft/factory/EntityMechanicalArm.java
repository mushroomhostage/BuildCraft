package buildcraft.factory;

import buildcraft.core.EntityBlock;
import net.minecraft.server.BuildCraftFactory;
import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityMechanicalArm extends Entity
{
    double sizeX;
    double sizeZ;
    EntityBlock xArm;
    EntityBlock yArm;
    EntityBlock zArm;
    EntityBlock head;
    double angle;
    public double targetX;
    public double targetY;
    public double targetZ;
    public double headPosX;
    public double headPosY;
    public double headPosZ;
    public double speed = 0.03D;
    double baseY;
    IArmListener listener;
    boolean inProgressionXZ = false;
    boolean inProgressionY = false;

    public EntityMechanicalArm(World var1)
    {
        super(var1);
    }

    public EntityMechanicalArm(World var1, double var2, double var4, double var6, double var8, double var10)
    {
        super(var1);
        this.setPosition(var2, var4, var6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = var2;
        this.prevPosY = var4;
        this.prevPosZ = var6;
        this.sizeX = var10;
        this.sizeZ = var8;
        this.noClip = true;
        this.baseY = var4;
        this.headPosX = var2;
        this.headPosY = var4 - 2.0D;
        this.headPosZ = var6;
        this.setTarget(this.headPosX, this.headPosY, this.headPosZ);
        this.inProgressionXZ = false;
        this.inProgressionY = false;
        this.xArm = new EntityBlock(var1, var2, var4, var6, var8, 0.5D, 0.5D);
        this.xArm.texture = BuildCraftFactory.drillTexture;
        var1.spawnEntityInWorld(this.xArm);
        this.yArm = new EntityBlock(var1, var2, var4, var6, 0.5D, 1.0D, 0.5D);
        this.yArm.texture = BuildCraftFactory.drillTexture;
        var1.spawnEntityInWorld(this.yArm);
        this.zArm = new EntityBlock(var1, var2, var4, var6, 0.5D, 0.5D, var10);
        this.zArm.texture = BuildCraftFactory.drillTexture;
        var1.spawnEntityInWorld(this.zArm);
        this.head = new EntityBlock(var1, var2, var4, var6, 0.2D, 1.0D, 0.2D);
        this.head.texture = 42;
        var1.spawnEntityInWorld(this.head);
        this.head.shadowSize = 1.0F;
        this.updatePosition();
    }

    protected void entityInit() {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
        this.sizeX = var1.getDouble("sizeX");
        this.sizeZ = var1.getDouble("sizeZ");
        this.targetX = var1.getDouble("targetX");
        this.targetY = var1.getDouble("targetY");
        this.targetZ = var1.getDouble("targetZ");
        this.angle = var1.getDouble("angle");
        this.headPosX = var1.getDouble("headPosX");
        this.headPosY = var1.getDouble("headPosY");
        this.headPosZ = var1.getDouble("headPosZ");
        this.baseY = var1.getDouble("baseY");
        this.speed = var1.getDouble("speed");
        this.inProgressionXZ = var1.getBoolean("progressionXY");
        this.inProgressionY = var1.getBoolean("progressionY");
        NBTTagCompound var2 = var1.getCompoundTag("xArm");
        NBTTagCompound var3 = var1.getCompoundTag("yArm");
        NBTTagCompound var4 = var1.getCompoundTag("zArm");
        NBTTagCompound var5 = var1.getCompoundTag("head");
        this.xArm = new EntityBlock(this.worldObj);
        this.yArm = new EntityBlock(this.worldObj);
        this.zArm = new EntityBlock(this.worldObj);
        this.head = new EntityBlock(this.worldObj);
        this.xArm.texture = BuildCraftFactory.drillTexture;
        this.yArm.texture = BuildCraftFactory.drillTexture;
        this.zArm.texture = BuildCraftFactory.drillTexture;
        this.head.texture = 42;
        this.xArm.readFromNBT(var2);
        this.yArm.readFromNBT(var3);
        this.zArm.readFromNBT(var4);
        this.head.readFromNBT(var5);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setDouble("sizeX", this.sizeX);
        var1.setDouble("sizeZ", this.sizeZ);
        var1.setDouble("targetX", this.targetX);
        var1.setDouble("targetY", this.targetY);
        var1.setDouble("targetZ", this.targetZ);
        var1.setDouble("angle", this.angle);
        var1.setDouble("headPosX", this.headPosX);
        var1.setDouble("headPosY", this.headPosY);
        var1.setDouble("headPosZ", this.headPosZ);
        var1.setDouble("baseY", this.baseY);
        var1.setDouble("speed", this.speed);
        var1.setBoolean("progressionXY", this.inProgressionXZ);
        var1.setBoolean("progressionY", this.inProgressionY);
        NBTTagCompound var2 = new NBTTagCompound();
        NBTTagCompound var3 = new NBTTagCompound();
        NBTTagCompound var4 = new NBTTagCompound();
        NBTTagCompound var5 = new NBTTagCompound();
        var1.setTag("xArm", var2);
        var1.setTag("yArm", var3);
        var1.setTag("zArm", var4);
        var1.setTag("head", var5);
        this.xArm.writeToNBT(var2);
        this.yArm.writeToNBT(var3);
        this.zArm.writeToNBT(var4);
        this.head.writeToNBT(var5);
    }

    public void setTarget(double var1, double var3, double var5)
    {
        this.targetX = var1;
        this.targetY = var3;
        this.targetZ = var5;
        double var7 = this.targetX - this.headPosX;
        double var9 = this.targetZ - this.headPosZ;
        this.angle = Math.atan2(var9, var7);
        this.inProgressionXZ = true;
        this.inProgressionY = true;
    }

    public double[] getTarget()
    {
        return new double[] {this.targetX, this.targetY, this.targetZ};
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.speed > 0.0D)
        {
            this.doMove(this.speed);
        }
    }

    public void doMove(double var1)
    {
        super.onUpdate();

        if (this.inProgressionXZ)
        {
            if (Math.abs(this.targetX - this.headPosX) < var1 * 2.0D && Math.abs(this.targetZ - this.headPosZ) < var1 * 2.0D)
            {
                this.headPosX = this.targetX;
                this.headPosZ = this.targetZ;
                this.inProgressionXZ = false;

                if (this.listener != null && !this.inProgressionY)
                {
                    this.listener.positionReached(this);
                }
            }
            else
            {
                this.headPosX += Math.cos(this.angle) * var1;
                this.headPosZ += Math.sin(this.angle) * var1;
            }
        }

        if (this.inProgressionY)
        {
            if (Math.abs(this.targetY - this.headPosY) < var1 * 2.0D)
            {
                this.headPosY = this.targetY;
                this.inProgressionY = false;

                if (this.listener != null && !this.inProgressionXZ)
                {
                    this.listener.positionReached(this);
                }
            }
            else if (this.targetY > this.headPosY)
            {
                this.headPosY += var1 / 2.0D;
            }
            else
            {
                this.headPosY -= var1 / 2.0D;
            }
        }

        this.updatePosition();
    }

    public void updatePosition()
    {
        this.xArm.setPosition(this.xArm.posX, this.xArm.posY, this.headPosZ + 0.25D);
        this.yArm.jSize = this.baseY - this.headPosY - 1.0D;
        this.yArm.setPosition(this.headPosX + 0.25D, this.headPosY + 1.0D, this.headPosZ + 0.25D);
        this.zArm.setPosition(this.headPosX + 0.25D, this.zArm.posY, this.zArm.posZ);
        this.head.setPosition(this.headPosX + 0.4D, this.headPosY, this.headPosZ + 0.4D);
    }

    public void joinToWorld(World var1)
    {
        super.worldObj = var1;
        this.xArm.worldObj = var1;
        this.yArm.worldObj = var1;
        this.zArm.worldObj = var1;
        this.head.worldObj = var1;
        var1.spawnEntityInWorld(this);
        var1.spawnEntityInWorld(this.xArm);
        var1.spawnEntityInWorld(this.yArm);
        var1.spawnEntityInWorld(this.zArm);
        var1.spawnEntityInWorld(this.head);
    }

    public void setEntityDead()
    {
        this.xArm.setDead();
        this.yArm.setDead();
        this.zArm.setDead();
        this.head.setDead();
        super.setDead();
    }

    public double[] getHeadPosition()
    {
        return new double[] {this.headPosX, this.headPosY, this.headPosZ};
    }

    public void setHeadPosition(double var1, double var3, double var5)
    {
        this.headPosX = var1;
        this.headPosY = var3;
        this.headPosZ = var5;
    }
}
