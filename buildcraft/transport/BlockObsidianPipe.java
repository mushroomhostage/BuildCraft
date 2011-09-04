package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.transport.BlockPipe;
import buildcraft.transport.TileObsidianPipe;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.Entity;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockObsidianPipe extends BlockPipe {

   public BlockObsidianPipe(int var1) {
      super(var1, Material.ORE);
      this.textureId = 28;
   }

   protected TileEntity a_() {
      return new TileObsidianPipe();
   }

   public void a(World var1, int var2, int var3, int var4, Entity var5) {
      if(!var5.dead) {
         TileObsidianPipe var6 = (TileObsidianPipe)var1.getTileEntity(var2, var3, var4);
         if(var6.canSuck(var5, 0)) {
            var6.pullItemIntoPipe(var5, 0);
         }

      }
   }

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      TileEntity var8 = APIProxy.getWorld().getTileEntity(var5, var6, var7);
      return BuildCraftTransport.alwaysConnectPipes?super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7):!(var8 instanceof TileObsidianPipe) && super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7);
   }
}
