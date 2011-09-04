package buildcraft.energy;

import net.minecraft.server.forge.ITextureProvider;
import net.minecraft.server.BlockStationary;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Material;

public class BlockOilStill extends BlockStationary implements ITextureProvider {

   public BlockOilStill(int var1, Material var2) {
      super(var1, var2);
   }

   public int getRenderType() {
      return BuildCraftCore.oilModel;
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }
}
