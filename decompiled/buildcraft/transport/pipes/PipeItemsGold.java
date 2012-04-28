package buildcraft.transport.pipes;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicGold;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TileGenericPipe;
import java.util.LinkedList;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.TileEntity;

public class PipeItemsGold extends Pipe implements IPipeTransportItemsHook
{
    public PipeItemsGold(int var1)
    {
        super(new PipeTransportItems(), new PipeLogicGold(), var1);
    }

    public int getBlockTexture()
    {
        return this.worldObj != null && this.worldObj.isBlockIndirectlyPowered(this.xCoord, this.yCoord, this.zCoord) ? 30 : 20;
    }

    public boolean isPipeConnected(TileEntity var1)
    {
        if (!super.isPipeConnected(var1))
        {
            return false;
        }
        else
        {
            Pipe var2 = null;

            if (var1 instanceof TileGenericPipe)
            {
                var2 = ((TileGenericPipe)var1).pipe;
            }

            return BuildCraftTransport.alwaysConnectPipes ? super.isPipeConnected(var1) : (var2 == null || !(var2.logic instanceof PipeLogicGold)) && super.isPipeConnected(var1);
        }
    }

    public LinkedList filterPossibleMovements(LinkedList var1, Position var2, EntityPassiveItem var3)
    {
        return var1;
    }

    public void entityEntered(EntityPassiveItem var1, Orientations var2)
    {
        if (this.worldObj.isBlockIndirectlyPowered(this.xCoord, this.yCoord, this.zCoord))
        {
            var1.speed = Utils.pipeNormalSpeed * 20.0F;
        }
    }

    public void readjustSpeed(EntityPassiveItem var1)
    {
        ((PipeTransportItems)this.transport).defaultReajustSpeed(var1);
    }
}
