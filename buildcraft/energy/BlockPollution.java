package buildcraft.energy;

import buildcraft.energy.TilePollution;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;

public class BlockPollution extends BlockContainer implements ITextureProvider {

   public BlockPollution(int var1) {
      super(var1, Material.AIR);
      this.textureId = 80;
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public boolean b() {
      return false;
   }

   public boolean a() {
      return false;
   }

   public TileEntity a_() {
      return new TilePollution();
   }

   public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return 80 + var1.getData(var2, var3, var4);
   }
}
