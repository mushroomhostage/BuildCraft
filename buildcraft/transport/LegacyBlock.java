package buildcraft.transport;

import buildcraft.transport.LegacyTile;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;

public class LegacyBlock extends BlockContainer {

   public int newPipeId;


   public LegacyBlock(int var1, int var2) {
      super(var1, Material.SHATTERABLE);
      this.newPipeId = var2;
   }

   public TileEntity a_() {
      return new LegacyTile();
   }
}
