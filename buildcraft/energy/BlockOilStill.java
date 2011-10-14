package buildcraft.energy;

import buildcraft.core.ILiquid;
import forge.ITextureProvider;
import net.minecraft.server.BlockStationary;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.Material;

public class BlockOilStill extends BlockStationary implements ITextureProvider, ILiquid {

   public BlockOilStill(int var1, Material var2) {
      super(var1, var2);
   }

   public int getRenderType() {
      return BuildCraftCore.oilModel;
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public int stillLiquidId() {
      return BuildCraftEnergy.oilStill.id;
   }
}
