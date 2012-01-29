package buildcraft.core;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.PowerProvider;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class RedstonePowerProvider extends PowerProvider
{
    private boolean lastPower = false;

    public RedstonePowerProvider()
    {
        this.powerLoss = 0;
        this.powerLossRegularity = 0;
    }

    public boolean preConditions(IPowerReceptor var1)
    {
        TileEntity var2 = (TileEntity)var1;
        boolean var3 = var2.world.isBlockIndirectlyPowered(var2.x, var2.y, var2.z);
        if (BuildCraftCore.continuousCurrentModel)
        {
            if (var3)
            {
                return true;
            }
        }
        else if (var3 != this.lastPower)
        {
            this.lastPower = var3;
            if (var3)
            {
                return true;
            }
        }

        return false;
    }

    public int useEnergy(int var1, int var2, boolean var3)
    {
        return var1;
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.lastPower = var1.getBoolean("lastPower");
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        var1.setBoolean("lastPower", this.lastPower);
    }

    public void configure(int var1, int var2, int var3, int var4, int var5)
    {
        super.configure(var1, var2, var3, var4, var5);
        this.minActivationEnergy = 0;
        this.energyStored = 1;
    }

    public void configurePowerPerdition(int var1, int var2) {}
}
