package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.SafeTimeTracker;
import buildcraft.api.TileNetworkData;
import buildcraft.core.CoreProxy;
import buildcraft.core.IMachine;
import buildcraft.core.Utils;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public class PipeTransportPower extends PipeTransport
{
    public int[] powerQuery = new int[6];
    public int[] nextPowerQuery = new int[6];
    public long currentDate;
    public double[] internalPower = new double[] {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
    public double[] internalNextPower = new double[] {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
    @TileNetworkData(
            staticSize = 6
    )
    public short[] displayPower = new short[] {(short)0, (short)0, (short)0, (short)0, (short)0, (short)0};
    public double powerResitance = 0.01D;
    SafeTimeTracker tracker = new SafeTimeTracker();

    public PipeTransportPower()
    {
        for (int var1 = 0; var1 < 6; ++var1)
        {
            this.powerQuery[var1] = 0;
        }
    }

    public boolean isPipeConnected(TileEntity var1)
    {
        return var1 instanceof TileGenericPipe || var1 instanceof IMachine || var1 instanceof IPowerReceptor;
    }

    public void updateEntity()
    {
        if (!APIProxy.isClient(this.worldObj))
        {
            this.step();
            TileEntity[] var1 = new TileEntity[6];
            int var2;

            for (var2 = 0; var2 < 6; ++var2)
            {
                Position var3 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var2]);
                var3.moveForwards(1.0D);

                if (Utils.checkPipesConnections(this.worldObj, (int)var3.x, (int)var3.y, (int)var3.z, this.xCoord, this.yCoord, this.zCoord))
                {
                    var1[var2] = this.worldObj.getTileEntity((int)var3.x, (int)var3.y, (int)var3.z);
                }
            }

            this.displayPower = new short[] {(short)0, (short)0, (short)0, (short)0, (short)0, (short)0};

            for (var2 = 0; var2 < 6; ++var2)
            {
                if (this.internalPower[var2] > 0.0D)
                {
                    double var12 = 0.0D;

                    for (int var5 = 0; var5 < 6; ++var5)
                    {
                        if (var5 != var2 && this.powerQuery[var5] > 0 && (var1[var5] instanceof TileGenericPipe || var1[var5] instanceof IPowerReceptor))
                        {
                            var12 += (double)this.powerQuery[var5];
                        }
                    }

                    double var18 = this.internalPower[var2];

                    for (int var7 = 0; var7 < 6; ++var7)
                    {
                        if (var7 != var2 && this.powerQuery[var7] > 0)
                        {
                            double var8 = var18 / var12 * (double)this.powerQuery[var7];

                            if (var1[var7] instanceof TileGenericPipe)
                            {
                                TileGenericPipe var10 = (TileGenericPipe)var1[var7];
                                PipeTransportPower var11 = (PipeTransportPower)var10.pipe.transport;
                                var11.receiveEnergy(Orientations.values()[var7].reverse(), var8);
                                this.displayPower[var7] = (short)((int)((double)this.displayPower[var7] + var8 / 2.0D));
                                this.displayPower[var2] = (short)((int)((double)this.displayPower[var2] + var8 / 2.0D));
                                this.internalPower[var2] -= var8;
                            }
                            else if (var1[var7] instanceof IPowerReceptor)
                            {
                                IPowerReceptor var19 = (IPowerReceptor)var1[var7];
                                var19.getPowerProvider().receiveEnergy((int)var8);
                                this.displayPower[var7] = (short)((int)((double)this.displayPower[var7] + var8 / 2.0D));
                                this.displayPower[var2] = (short)((int)((double)this.displayPower[var2] + var8 / 2.0D));
                                this.internalPower[var2] -= var8;
                            }
                        }
                    }
                }
            }

            int var4;

            for (var2 = 0; var2 < 6; ++var2)
            {
                if (var1[var2] instanceof IPowerReceptor && !(var1[var2] instanceof TileGenericPipe))
                {
                    IPowerReceptor var13 = (IPowerReceptor)var1[var2];
                    var4 = var13.powerRequest();

                    if (var4 > 0)
                    {
                        this.requestEnergy(Orientations.values()[var2], var4);
                    }
                }
            }

            int[] var14 = new int[] {0, 0, 0, 0, 0, 0};
            int var15;

            for (var15 = 0; var15 < 6; ++var15)
            {
                var14[var15] = 0;

                for (var4 = 0; var4 < 6; ++var4)
                {
                    if (var4 != var15)
                    {
                        var14[var15] += this.powerQuery[var4];
                    }
                }
            }

            for (var15 = 0; var15 < 6; ++var15)
            {
                if (var14[var15] != 0 && var1[var15] != null)
                {
                    TileEntity var16 = var1[var15];

                    if (var16 instanceof TileGenericPipe)
                    {
                        TileGenericPipe var17 = (TileGenericPipe)var16;
                        PipeTransportPower var6 = (PipeTransportPower)var17.pipe.transport;
                        var6.requestEnergy(Orientations.values()[var15].reverse(), var14[var15]);
                    }
                }
            }

            if (APIProxy.isServerSide() && this.tracker.markTimeIfDelay(this.worldObj, (long)(2 * BuildCraftCore.updateFactor)))
            {
                CoreProxy.sendToPlayers(this.container.getUpdatePacket(), this.xCoord, this.yCoord, this.zCoord, 40, mod_BuildCraftCore.instance);
            }
        }
    }

    public void step()
    {
        if (this.currentDate != this.worldObj.getTime())
        {
            this.currentDate = this.worldObj.getTime();
            this.powerQuery = this.nextPowerQuery;
            this.nextPowerQuery = new int[] {0, 0, 0, 0, 0, 0};
            this.internalPower = this.internalNextPower;
            this.internalNextPower = new double[] {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
        }
    }

    public void receiveEnergy(Orientations var1, double var2)
    {
        this.step();

        if (this.container.pipe instanceof IPipeTransportPowerHook)
        {
            ((IPipeTransportPowerHook)this.container.pipe).receiveEnergy(var1, var2);
        }
        else
        {
            double[] var10000 = this.internalNextPower;
            int var10001 = var1.ordinal();
            var10000[var10001] += var2 * (1.0D - this.powerResitance);

            if (this.internalNextPower[var1.ordinal()] >= 1000.0D)
            {
                this.worldObj.explode((Entity)null, (double)this.xCoord, (double)this.yCoord, (double)this.zCoord, 2.0F);
            }
        }
    }

    public void requestEnergy(Orientations var1, int var2)
    {
        this.step();

        if (this.container.pipe instanceof IPipeTransportPowerHook)
        {
            ((IPipeTransportPowerHook)this.container.pipe).requestEnergy(var1, var2);
        }
        else
        {
            this.step();
            int[] var10000 = this.nextPowerQuery;
            int var10001 = var1.ordinal();
            var10000[var10001] += var2;
        }
    }

    public void initialize()
    {
        this.currentDate = this.worldObj.getTime();
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);

        for (int var2 = 0; var2 < 6; ++var2)
        {
            this.powerQuery[var2] = var1.getInt("powerQuery[" + var2 + "]");
            this.nextPowerQuery[var2] = var1.getInt("nextPowerQuery[" + var2 + "]");
            this.internalPower[var2] = var1.getDouble("internalPower[" + var2 + "]");
            this.internalNextPower[var2] = var1.getDouble("internalNextPower[" + var2 + "]");
        }
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);

        for (int var2 = 0; var2 < 6; ++var2)
        {
            var1.setInt("powerQuery[" + var2 + "]", this.powerQuery[var2]);
            var1.setInt("nextPowerQuery[" + var2 + "]", this.nextPowerQuery[var2]);
            var1.setDouble("internalPower[" + var2 + "]", this.internalPower[var2]);
            var1.setDouble("internalNextPower[" + var2 + "]", this.internalNextPower[var2]);
        }
    }
}
