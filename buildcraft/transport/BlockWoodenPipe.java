package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.transport.BlockPipe;
import buildcraft.transport.TileWoodenPipe;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockWoodenPipe extends BlockPipe {

   public static String[] excludedBlocks;
   int plainWoodenPipeTexture;


   public BlockWoodenPipe(int var1) {
      super(var1, Material.WOOD);
      this.textureId = 16;
      this.plainWoodenPipeTexture = 31;
   }

   protected TileEntity a_() {
      return new TileWoodenPipe();
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var5.G() != null && var5.G().getItem() == BuildCraftCore.wrenchItem) {
         TileWoodenPipe var6 = (TileWoodenPipe)var1.getTileEntity(var2, var3, var4);
         var6.switchSource();
         return true;
      } else {
         return false;
      }
   }

   public void doPhysics(World var1, int var2, int var3, int var4, int var5) {
      super.doPhysics(var1, var2, var3, var4, var5);
      TileWoodenPipe var6 = (TileWoodenPipe)var1.getTileEntity(var2, var3, var4);
      var6.scheduleNeighborChange();
   }

   public int getTextureForConnection(Orientations var1, int var2) {
      return var2 == var1.ordinal()?this.plainWoodenPipeTexture:this.textureId;
   }

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      TileEntity var8 = APIProxy.getWorld().getTileEntity(var5, var6, var7);
      return BuildCraftTransport.alwaysConnectPipes?super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7):!(var8 instanceof TileWoodenPipe) && super.isPipeConnected(var1, var2, var3, var4, var5, var6, var7);
   }

   public static boolean isExcludedFromExtraction(Block var0) {
      if(var0 == null) {
         return true;
      } else {
         String[] var1 = excludedBlocks;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            if(var4.equals(var0.l()) || var4.equals(Integer.toString(var0.id))) {
               return true;
            }
         }

         return false;
      }
   }
}
