package buildcraft.transport;

import buildcraft.api.Orientations;
import buildcraft.transport.BlockPipe;
import buildcraft.transport.TileDiamondPipe;
import buildcraft.transport.TransportProxy;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockDiamondPipe extends BlockPipe {

   public BlockDiamondPipe(int var1) {
      super(var1, Material.ORE);
      this.textureId = 21;
   }

   protected TileEntity a_() {
      return new TileDiamondPipe();
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      TileDiamondPipe var6 = (TileDiamondPipe)var1.getTileEntity(var2, var3, var4);
      TransportProxy.displayGUIFilter(var5, var6);
      return true;
   }

   public int getTextureForConnection(Orientations var1, int var2) {
      return BuildCraftTransport.diamondTextures[var1.ordinal()];
   }
}
