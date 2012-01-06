package buildcraft.factory;

import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import buildcraft.factory.BlockMachineRoot;
import buildcraft.factory.TileQuarry;
import forge.ITextureProvider;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockQuarry extends BlockMachineRoot implements ITextureProvider, IPipeConnection {

   int textureTop;
   int textureFront;
   int textureSide;


   public BlockQuarry(int var1) {
      super(var1, Material.ORE);
      this.c(1.5F);
      this.b(10.0F);
      this.a(h);
      this.textureSide = 41;
      this.textureFront = 39;
      this.textureTop = 40;
   }

   // MaeEdit start
   public void onPlace(World world, int i, int j, int k)
   {
      super.onPlace(world, i, j, k);
      TileEntity te = world.getTileEntity(i, j, k);
      te.world = world;
   }
   // MaeEdit end

   public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      Orientations var6 = Utils.get2dOrientation(new Position(var5.locX, var5.locY, var5.locZ), new Position((double)var2, (double)var3, (double)var4));
      var1.setData(var2, var3, var4, var6.reverse().ordinal());
   }

   public int a(int var1, int var2) {
      if(var2 == 0 && var1 == 3) {
         return this.textureFront;
      } else if(var1 == var2) {
         return this.textureFront;
      } else {
         switch(var1) {
         case 1:
            return this.textureTop;
         default:
            return this.textureSide;
         }
      }
   }

   public TileEntity a_() {
      return new TileQuarry();
   }

   public void remove(World var1, int var2, int var3, int var4) {
      Utils.preDestroyBlock(var1, var2, var3, var4);
      super.remove(var1, var2, var3, var4);
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return true;
   }
}
