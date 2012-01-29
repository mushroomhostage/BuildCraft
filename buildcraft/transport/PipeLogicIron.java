package buildcraft.transport;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPipeEntry;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.transport.PipeLogic;
import buildcraft.transport.PipeLogicWood;
import buildcraft.transport.TileGenericPipe;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.TileEntity;

public class PipeLogicIron extends PipeLogic
{

    boolean lastPower = false;


    public void switchPower()
    {
        boolean var1 = this.worldObj.isBlockIndirectlyPowered(this.xCoord, this.yCoord, this.zCoord);
        if (var1 != this.lastPower)
        {
            this.switchPosition();
            this.lastPower = var1;
        }

    }

    public void switchPosition()
    {
        int var1 = this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);
        int var2 = var1;

        for (int var3 = 0; var3 < 6; ++var3)
        {
            ++var2;
            if (var2 > 5)
            {
                var2 = 0;
            }

            Position var4 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var2]);
            var4.moveForwards(1.0D);
            TileEntity var5 = this.worldObj.getTileEntity((int)var4.x, (int)var4.y, (int)var4.z);
            if ((!(var5 instanceof TileGenericPipe) || !(((TileGenericPipe)var5).pipe.logic instanceof PipeLogicWood)) && (var5 instanceof IPipeEntry || var5 instanceof IInventory || var5 instanceof ILiquidContainer || var5 instanceof TileGenericPipe))
            {
                this.worldObj.setRawData(this.xCoord, this.yCoord, this.zCoord, var2);
                return;
            }
        }

    }

    public void initialize()
    {
        super.initialize();
        this.lastPower = this.worldObj.isBlockIndirectlyPowered(this.xCoord, this.yCoord, this.zCoord);
    }

    public void onBlockPlaced()
    {
        super.onBlockPlaced();
        this.worldObj.setRawData(this.xCoord, this.yCoord, this.zCoord, 1);
        this.switchPosition();
    }

    public boolean blockActivated(EntityHuman var1)
    {
        super.blockActivated(var1);
        if (var1.P() != null && var1.P().getItem() == BuildCraftCore.wrenchItem)
        {
            this.switchPosition();
            this.worldObj.notify(this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void onNeighborBlockChange()
    {
        super.onNeighborBlockChange();
        this.switchPower();
    }

    public boolean outputOpen(Orientations var1)
    {
        return var1.ordinal() == this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);
    }
}
