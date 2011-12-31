package buildcraft.factory;

import buildcraft.api.API;
import buildcraft.api.Orientations;
import buildcraft.factory.TileTank;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockTank extends BlockContainer implements ITextureProvider {

   public BlockTank(int var1) {
      super(var1, Material.SHATTERABLE);
      this.a(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
      this.c(1.0F);
   }

   public boolean b() {
      return false;
   }

   public boolean a() {
      return false;
   }

   public boolean isACube() {
      return false;
   }

   public TileEntity a_() {
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

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var5.P() != null) {
         int var6 = var5.P().id;
         int var7 = API.getLiquidForBucket(var6);
         TileTank var8 = (TileTank)var1.getTileEntity(var2, var3, var4);
         if(var7 != 0) {
            int var9 = var8.fill(Orientations.Unknown, 1000, var7, true);
            if(var9 != 0 && !BuildCraftCore.debugMode) {
               var5.inventory.setItem(var5.inventory.itemInHandIndex, new ItemStack(Item.BUCKET, 1));
            }

            return true;
         }
      }

      return false;
   }
}
