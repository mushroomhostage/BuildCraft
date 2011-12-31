package buildcraft.builders;

import buildcraft.api.API;
import buildcraft.api.IBox;
import buildcraft.core.FillerPattern;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;

public class FillerFlattener extends FillerPattern {

   public boolean iteratePattern(TileEntity var1, IBox var2, ItemStack var3) {
      int var4 = (int)var2.p1().x;
      int var5 = (int)var2.p1().y;
      int var6 = (int)var2.p1().z;
      int var7 = (int)var2.p2().x;
      int var8 = (int)var2.p2().z;
      int var9 = var7 - var4 + 1;
      int var10 = var8 - var6 + 1;
      boolean[][] var11 = new boolean[var9][var10];

      int var13;
      for(int var12 = 0; var12 < var11.length; ++var12) {
         for(var13 = 0; var13 < var11[0].length; ++var13) {
            var11[var12][var13] = false;
         }
      }

      boolean var19 = false;
      var13 = Integer.MAX_VALUE;
      int var14 = Integer.MAX_VALUE;
      int var15 = Integer.MAX_VALUE;

      for(int var16 = var5 - 1; var16 >= 0; --var16) {
         var19 = false;

         for(int var17 = var4; var17 <= var7; ++var17) {
            for(int var18 = var6; var18 <= var8; ++var18) {
               if(!var11[var17 - var4][var18 - var6]) {
                  if(!API.softBlock(var1.world.getTypeId(var17, var16, var18))) {
                     var11[var17 - var4][var18 - var6] = true;
                  } else {
                     var19 = true;
                     var13 = var17;
                     var14 = var16;
                     var15 = var18;
                  }
               }
            }
         }

         if(!var19) {
            break;
         }
      }

      if(var13 != Integer.MAX_VALUE && var3 != null) {
         var3.getItem().a(var3, BuildCraftCore.getBuildCraftPlayer(var1.world), var1.world, var13, var14 + 1, var15, 0);
      }

      if(var13 != Integer.MAX_VALUE) {
         return false;
      } else {
         return this.empty(var4, var5, var6, var7, 128, var8, var1.world);
      }
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public int getTextureIndex() {
      return 69;
   }
}
