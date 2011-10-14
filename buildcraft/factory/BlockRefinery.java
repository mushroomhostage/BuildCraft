package buildcraft.factory;

import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import buildcraft.factory.TileRefinery;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockRefinery extends BlockContainer {

   public BlockRefinery(int var1) {
      super(var1, Material.ORE);
      this.c(0.5F);
   }

   public boolean a() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean b() {
      return false;
   }

   public int getRenderType() {
      return BuildCraftCore.blockByEntityModel;
   }

   public TileEntity a_() {
      return new TileRefinery();
   }

   public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      Orientations var6 = Utils.get2dOrientation(new Position(var5.locX, var5.locY, var5.locZ), new Position((double)var2, (double)var3, (double)var4));
      var1.setData(var2, var3, var4, var6.reverse().ordinal());
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var5.K() != null) {
         int var6;
         if(var5.K().getItem() == BuildCraftCore.wrenchItem) {
            var6 = var1.getData(var2, var3, var4);
            switch(BlockRefinery.NamelessClass1531034163.$SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.values()[var6].ordinal()]) {
            case 1:
               var1.setRawData(var2, var3, var4, Orientations.ZPos.ordinal());
               break;
            case 2:
               var1.setRawData(var2, var3, var4, Orientations.ZNeg.ordinal());
               break;
            case 3:
               var1.setRawData(var2, var3, var4, Orientations.XNeg.ordinal());
               break;
            case 4:
               var1.setRawData(var2, var3, var4, Orientations.XPos.ordinal());
            }

            var1.notify(var2, var3, var4);
         } else {
            var6 = BuildCraftCore.getLiquidForBucket(var5.K().id);
            if(var6 != 0) {
               int var7 = ((TileRefinery)var1.getTileEntity(var2, var3, var4)).fill(Orientations.Unknown, 1000, var6, true);
               if(var7 != 0 && !BuildCraftCore.debugMode) {
                  var5.inventory.setItem(var5.inventory.itemInHandIndex, new ItemStack(Item.BUCKET, 1));
               }

               return true;
            }
         }
      }

      return false;
   }

   // $FF: synthetic class
   static class NamelessClass1531034163 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$src$buildcraft$api$Orientations = new int[Orientations.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.XNeg.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.XPos.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.ZNeg.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.ZPos.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
