package buildcraft.transport;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogic;
import buildcraft.transport.PipeLogicCobblestone;
import buildcraft.transport.TileGenericPipe;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.TileEntity;

public class PipeLogicStone extends PipeLogic {

   public boolean isPipeConnected(TileEntity var1) {
      Pipe var2 = null;
      if(var1 instanceof TileGenericPipe) {
         var2 = ((TileGenericPipe)var1).pipe;
      }

      return BuildCraftTransport.alwaysConnectPipes?super.isPipeConnected(var1):(var2 == null || !(var2.logic instanceof PipeLogicCobblestone)) && super.isPipeConnected(var1);
   }
}
