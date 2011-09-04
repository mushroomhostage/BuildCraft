package buildcraft.builders;

import buildcraft.api.FillerPattern;
import buildcraft.api.IBox;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;

public class FillerRemover extends FillerPattern {

   public boolean iteratePattern(TileEntity var1, IBox var2, ItemStack var3) {
      int var4 = (int)var2.p1().x;
      int var5 = (int)var2.p1().y;
      int var6 = (int)var2.p1().z;
      int var7 = (int)var2.p2().x;
      int var8 = (int)var2.p2().y;
      int var9 = (int)var2.p2().z;
      return this.empty(var4, var5, var6, var7, var8, var9, var1.world);
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public int getTextureIndex() {
      return 68;
   }
}
