package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.TileEntity;

public class PipeLogicWood extends PipeLogic
{
    public static String[] excludedBlocks = new String[0];

    public void switchSource()
    {
        int var1 = this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);
        int var2 = 6;

        for (int var3 = var1 + 1; var3 <= var1 + 6; ++var3)
        {
            Orientations var4 = Orientations.values()[var3 % 6];
            Position var5 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, var4);
            var5.moveForwards(1.0D);
            Block var6 = Block.byId[this.worldObj.getTypeId((int)var5.x, (int)var5.y, (int)var5.z)];
            TileEntity var7 = this.worldObj.getTileEntity((int)var5.x, (int)var5.y, (int)var5.z);

            if (this.isInput(var7) && !isExcludedFromExtraction(var6))
            {
                var2 = var4.ordinal();
                break;
            }
        }

        if (var2 != var1)
        {
            this.worldObj.setRawData(this.xCoord, this.yCoord, this.zCoord, var2);
            this.worldObj.notify(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public boolean isInput(TileEntity var1)
    {
        return !(var1 instanceof TileGenericPipe) && (var1 instanceof IInventory || var1 instanceof ILiquidContainer) && Utils.checkPipesConnections(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var1.x, var1.y, var1.z);
    }

    public static boolean isExcludedFromExtraction(Block var0)
    {
        if (var0 == null)
        {
            return true;
        }
        else
        {
            String[] var1 = excludedBlocks;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                String var4 = var1[var3];

                if (var4.equals(var0.p()) || var4.equals(Integer.toString(var0.id)))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean blockActivated(EntityHuman var1)
    {
        if (var1.T() != null && var1.T().getItem() == BuildCraftCore.wrenchItem)
        {
            this.switchSource();
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isPipeConnected(TileEntity var1)
    {
        Pipe var2 = null;

        if (var1 instanceof TileGenericPipe)
        {
            var2 = ((TileGenericPipe)var1).pipe;
        }

        return BuildCraftTransport.alwaysConnectPipes ? super.isPipeConnected(var1) : (var2 == null || !(var2.logic instanceof PipeLogicWood)) && super.isPipeConnected(var1);
    }

    public void initialize()
    {
        super.initialize();

        if (!APIProxy.isClient(this.worldObj))
        {
            this.switchSourceIfNeeded();
        }
    }

    private void switchSourceIfNeeded()
    {
        int var1 = this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);

        if (var1 > 5)
        {
            this.switchSource();
        }
        else
        {
            Position var2 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var1]);
            var2.moveForwards(1.0D);
            TileEntity var3 = this.worldObj.getTileEntity((int)var2.x, (int)var2.y, (int)var2.z);

            if (!this.isInput(var3))
            {
                this.switchSource();
            }
        }
    }

    public void onNeighborBlockChange()
    {
        super.onNeighborBlockChange();

        if (!APIProxy.isClient(this.worldObj))
        {
            this.switchSourceIfNeeded();
        }
    }
}
