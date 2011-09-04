package buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.core.Utils;
import buildcraft.transport.TilePipe;

public class TileGoldenPipe extends TilePipe {

   public void entityEntering(EntityPassiveItem var1, Orientations var2) {
      if(this.world.isBlockIndirectlyPowered(this.x, this.y, this.z)) {
         var1.speed = Utils.pipeNormalSpeed * 20.0F;
      }

      super.entityEntering(var1, var2);
   }
}
