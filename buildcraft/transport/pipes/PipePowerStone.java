package buildcraft.transport.pipes;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicStone;
import buildcraft.transport.PipeTransportPower;

public class PipePowerStone extends Pipe {

   public PipePowerStone(int var1) {
      super(new PipeTransportPower(), new PipeLogicStone(), var1);
   }

   public int getBlockTexture() {
      return 120;
   }
}
