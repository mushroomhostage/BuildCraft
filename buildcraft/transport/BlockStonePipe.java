package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.transport.BlockPipe;
import buildcraft.transport.TileCobblestonePipe;
import buildcraft.transport.TileStonePipe;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;

public class BlockStonePipe extends BlockPipe {

   public BlockStonePipe(int var1) {
      super(var1, Material.STONE);
      this.textureId = 29;
   }

   protected TileEntity a_() {
      return new TileStonePipe();
   }

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      TileEntity var8 = APIProxy.getWorld().getTileEntity(var5, var6, var7);
      return BuildCraftTransport.alwaysConnectPipes?super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7):!(var8 instanceof TileCobblestonePipe) && super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7);
   }
}
