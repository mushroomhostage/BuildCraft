package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.Orientations;
import buildcraft.api.SafeTimeTracker;
import buildcraft.api.TileNetworkData;
import buildcraft.core.DefaultProps;
import buildcraft.core.TileBuildCraft;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class TileTank extends TileBuildCraft implements ILiquidContainer
{
    @TileNetworkData
    public int stored = 0;
    @TileNetworkData
    public int liquidId = 0;
    public boolean hasUpdate = false;
    public SafeTimeTracker tracker = new SafeTimeTracker();

    public int fill(Orientations var1, int var2, int var3, boolean var4)
    {
        TileTank var5 = this;

        for (int var6 = this.yCoord - 1; var6 > 1 && this.worldObj.getBlockTileEntity(this.xCoord, var6, this.zCoord) instanceof TileTank; --var6)
        {
            var5 = (TileTank)this.worldObj.getBlockTileEntity(this.xCoord, var6, this.zCoord);
        }

        return var5.actualFill(var1, var2, var3, var4);
    }

    private int actualFill(Orientations var1, int var2, int var3, boolean var4)
    {
        if (this.stored != 0 && var3 != this.liquidId)
        {
            return 0;
        }
        else
        {
            this.liquidId = var3;
            TileEntity var5 = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
            int var6 = 0;

            if (this.stored + var2 <= this.getCapacity())
            {
                if (var4)
                {
                    this.stored += var2;
                    this.hasUpdate = true;
                }

                var6 = var2;
            }
            else if (this.stored <= this.getCapacity())
            {
                var6 = this.getCapacity() - this.stored;

                if (var4)
                {
                    this.stored = this.getCapacity();
                    this.hasUpdate = true;
                }
            }

            if (var6 < var2 && var5 instanceof TileTank)
            {
                var6 += ((TileTank)var5).actualFill(var1, var2 - var6, var3, var4);
            }

            return var6;
        }
    }

    public int getLiquidQuantity()
    {
        return this.stored;
    }

    public int getCapacity()
    {
        return 16000;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.stored = var1.getInteger("stored");
        this.liquidId = var1.getInteger("liquidId");

        if (this.liquidId == 0)
        {
            this.stored = 0;
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("stored", this.stored);
        var1.setInteger("liquidId", this.liquidId);
    }

    public int empty(int var1, boolean var2)
    {
        TileTank var3 = this;

        for (int var4 = this.yCoord + 1; var4 <= DefaultProps.WORLD_HEIGHT && this.worldObj.getBlockTileEntity(this.xCoord, var4, this.zCoord) instanceof TileTank; ++var4)
        {
            var3 = (TileTank)this.worldObj.getBlockTileEntity(this.xCoord, var4, this.zCoord);
        }

        return var3.actualEmtpy(var1, var2);
    }

    private int actualEmtpy(int var1, boolean var2)
    {
        if (this.stored >= var1)
        {
            if (var2)
            {
                this.stored -= var1;
                this.hasUpdate = true;
            }

            return var1;
        }
        else
        {
            int var3 = this.stored;

            if (var2)
            {
                this.stored = 0;
                this.hasUpdate = true;
            }

            TileEntity var4 = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);

            if (var4 instanceof TileTank)
            {
                var3 += ((TileTank)var4).actualEmtpy(var1 - var3, var2);
            }

            return var3;
        }
    }

    public int getLiquidId()
    {
        return this.liquidId;
    }

    @TileNetworkData

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (APIProxy.isServerSide() && this.hasUpdate && this.tracker.markTimeIfDelay(this.worldObj, (long)(2 * BuildCraftCore.updateFactor)))
        {
            this.sendNetworkUpdate();
            this.hasUpdate = false;
        }
    }
}
