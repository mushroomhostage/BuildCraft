package buildcraft.factory;

import buildcraft.factory.TileTank;
import net.minecraft.server.forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;

public class BlockTank extends BlockContainer implements ITextureProvider {

   public BlockTank(int var1) {
      super(var1, Material.ORE);
      this.a(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
      this.c(5.0F);
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean a() {
      return false;
   }

   public boolean b() {
      return false;
   }

   protected TileEntity a_() {
      return new TileTank();
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public int a(int var1) {
      switch(var1) {
      case 0:
      case 1:
         return 98;
      default:
         return 96;
      }
   }

   public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      switch(var5) {
      case 0:
      case 1:
         return 98;
      default:
         return var1.getTypeId(var2, var3 - 1, var4) == this.id?97:96;
      }
   }
}
