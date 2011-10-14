package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.builders.BuildersProxy;
import buildcraft.builders.TileTemplate;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockTemplate extends BlockContainer implements ITextureProvider {

   int blockTextureSides;
   int blockTextureFront;
   int blockTextureTopPos;
   int blockTextureTopNeg;


   public BlockTemplate(int var1) {
      super(var1, Material.ORE);
      this.c(0.5F);
      this.blockTextureSides = 48;
      this.blockTextureTopNeg = 49;
      this.blockTextureTopPos = 50;
      this.blockTextureFront = 52;
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public TileEntity a_() {
      return new TileTemplate();
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var5.K() != null && var5.K().getItem() == BuildCraftCore.wrenchItem) {
         int var7 = var1.getData(var2, var3, var4);
         switch(Orientations.values()[var7]) {
         case XNeg:
            var1.setRawData(var2, var3, var4, Orientations.ZPos.ordinal());
            break;
         case XPos:
            var1.setRawData(var2, var3, var4, Orientations.ZNeg.ordinal());
            break;
         case ZNeg:
            var1.setRawData(var2, var3, var4, Orientations.XNeg.ordinal());
            break;
         case ZPos:
            var1.setRawData(var2, var3, var4, Orientations.XPos.ordinal());
         }

         var1.notify(var2, var3, var4);
         return true;
      } else {
         TileTemplate var6 = (TileTemplate)var1.getTileEntity(var2, var3, var4);
         BuildersProxy.displayGUITemplate(var5, var6);
         return true;
      }
   }

   public void remove(World var1, int var2, int var3, int var4) {
      Utils.preDestroyBlock(var1, var2, var3, var4);
      super.remove(var1, var2, var3, var4);
   }

   public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      Orientations var6 = Utils.get2dOrientation(new Position(var5.locX, var5.locY, var5.locZ), new Position((double)var2, (double)var3, (double)var4));
      var1.setData(var2, var3, var4, var6.reverse().ordinal());
   }

   public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      int var6 = var1.getData(var2, var3, var4);
      if(var5 == 1) {
         boolean var7 = false;
         if(APIProxy.getWorld() == null) {
            return this.a(var5, var6);
         } else {
            var7 = APIProxy.getWorld().isBlockIndirectlyPowered(var2, var3, var4);
            return !var7?this.blockTextureTopPos:this.blockTextureTopNeg;
         }
      } else {
         return this.a(var5, var6);
      }
   }

   public int a(int var1, int var2) {
      return var2 == 0 && var1 == 3?this.blockTextureFront:(var1 == 1?this.blockTextureTopPos:(var1 == var2?this.blockTextureFront:this.blockTextureSides));
   }
}
