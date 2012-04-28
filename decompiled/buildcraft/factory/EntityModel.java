package buildcraft.factory;

import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityModel extends Entity
{
    public EntityModel(World var1, double var2, double var4, double var6)
    {
        super(var1);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = var2;
        this.lastY = var4;
        this.lastZ = var6;
        this.setPosition(var2, var4, var6);
    }

    protected void b() {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void a(NBTTagCompound var1) {}

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void b(NBTTagCompound var1) {}
}
