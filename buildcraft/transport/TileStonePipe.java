package buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.core.Utils;
import buildcraft.transport.TilePipe;

public class TileStonePipe extends TilePipe {

   public void readjustSpeed(EntityPassiveItem var1) {
      if(var1.speed > Utils.pipeNormalSpeed) {
         var1.speed -= Utils.pipeNormalSpeed / 2.0F;
      }

      if(var1.speed < Utils.pipeNormalSpeed) {
         var1.speed = Utils.pipeNormalSpeed;
      }

   }
}
