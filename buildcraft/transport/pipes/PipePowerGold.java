package buildcraft.transport.pipes;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicGold;
import buildcraft.transport.PipeTransportPower;

public class PipePowerGold extends Pipe {

   public PipePowerGold(int var1) {
      super(new PipeTransportPower(), new PipeLogicGold(), var1);
      ((PipeTransportPower)this.transport).powerResitance = 0.001D;
   }

   public int getBlockTexture() {
      return 122;
   }
}
