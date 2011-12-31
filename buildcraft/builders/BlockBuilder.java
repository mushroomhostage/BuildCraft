package buildcraft.builders;

import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.builders.BuildersProxy;
import buildcraft.builders.TileBuilder;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockBuilder extends BlockContainer implements ITextureProvider {

   int blockTextureTop = 54;
   int blockTextureSide = 53;
   int blockTextureFront = 55;


   public BlockBuilder(int var1) {
      super(var1, Material.ORE);
      this.c(0.7F);
   }

   public TileEntity a_() {
      return new TileBuilder();
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public int a(int var1, int var2) {
      if(var2 == 0 && var1 == 3) {
         return this.blockTextureFront;
      } else if(var1 == var2) {
         return this.blockTextureFront;
      } else {
         switch(var1) {
         case 1:
            return this.blockTextureTop;
         default:
            return this.blockTextureSide;
         }
      }
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var5.P() != null && var5.P().getItem() == BuildCraftCore.wrenchItem) {
         int var7 = var1.getData(var2, var3, var4);
         switch (Orientations.values()[var7]) {
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
         TileBuilder var6 = (TileBuilder)var1.getTileEntity(var2, var3, var4);
         BuildersProxy.displayGUIBuilder(var5, var6);
         return true;
      }
   }

   public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      Orientations var6 = Utils.get2dOrientation(new Position(var5.locX, var5.locY, var5.locZ), new Position((double)var2, (double)var3, (double)var4));
      var1.setData(var2, var3, var4, var6.reverse().ordinal());
   }

   public void remove(World var1, int var2, int var3, int var4) {
      Utils.preDestroyBlock(var1, var2, var3, var4);
      super.remove(var1, var2, var3, var4);
   }
}
