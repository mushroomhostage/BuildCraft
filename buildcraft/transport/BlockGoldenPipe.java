package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.transport.BlockPipe;
import buildcraft.transport.TileGoldenPipe;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;

public class BlockGoldenPipe extends BlockPipe {

   private int inactiveTexture = 20;
   private int activeTexture = 30;


   public BlockGoldenPipe(int var1) {
      super(var1, Material.ORE);
   }

   protected TileEntity a_() {
      return new TileGoldenPipe();
   }

   public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      boolean var6 = false;
      if(APIProxy.getWorld() == null) {
         return this.a(var2, var1.getData(var2, var3, var4));
      } else {
         var6 = APIProxy.getWorld().isBlockIndirectlyPowered(var2, var3, var4);
         return var6?this.activeTexture:this.inactiveTexture;
      }
   }

   public int a(int var1, int var2) {
      return this.inactiveTexture;
   }

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      TileEntity var8 = APIProxy.getWorld().getTileEntity(var5, var6, var7);
      return BuildCraftTransport.alwaysConnectPipes?super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7):!(var8 instanceof TileGoldenPipe) && super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7);
   }
}
