package buildcraft.factory;

import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityModel extends Entity
{
    public EntityModel(World var1, double var2, double var4, double var6)
    {
        super(var1);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = var2;
        this.prevPosY = var4;
        this.prevPosZ = var6;
        this.setPosition(var2, var4, var6);
    }

    protected void entityInit() {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound var1) {}

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound var1) {}
}
