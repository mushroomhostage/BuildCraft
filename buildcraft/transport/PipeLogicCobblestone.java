package buildcraft.transport;

import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.TileEntity;

public class PipeLogicCobblestone extends PipeLogic
{
    public boolean isPipeConnected(TileEntity var1)
    {
        Pipe var2 = null;

        if (var1 instanceof TileGenericPipe)
        {
            var2 = ((TileGenericPipe)var1).pipe;
        }

        return BuildCraftTransport.alwaysConnectPipes ? super.isPipeConnected(var1) : (var2 == null || !(var2.logic instanceof PipeLogicStone)) && super.isPipeConnected(var1);
    }
}
