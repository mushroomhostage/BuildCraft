package buildcraft.builders;

import buildcraft.builders.BuildersProxy;
import buildcraft.builders.TileMarker;
import buildcraft.core.Utils;
import net.minecraft.server.forge.ITextureProvider;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftBuilders;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class BlockMarker extends BlockContainer implements ITextureProvider {

   public BlockMarker(int var1) {
      super(var1, Material.ORIENTABLE);
      this.textureId = 57;
      this.a(0.5F);
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      int var5 = var1.getData(var2, var3, var4);
      double var6 = 0.15D;
      double var8 = 0.65D;
      switch(var5) {
      case 0:
         return AxisAlignedBB.b((double)var2 + 0.5D - var6, (double)(var3 + 1) - var8, (double)var4 + 0.5D - var6, (double)var2 + 0.5D + var6, (double)(var3 + 1), (double)var4 + 0.5D + var6);
      case 1:
         return AxisAlignedBB.b((double)var2, (double)var3 + 0.5D - var6, (double)var4 + 0.5D - var6, (double)var2 + var8, (double)var3 + 0.5D + var6, (double)var4 + 0.5D + var6);
      case 2:
      default:
         return AxisAlignedBB.b((double)(var2 + 1) - var8, (double)var3 + 0.5D - var6, (double)var4 + 0.5D - var6, (double)(var2 + 1), (double)var3 + 0.5D + var6, (double)var4 + 0.5D + var6);
      case 3:
         return AxisAlignedBB.b((double)var2 + 0.5D - var6, (double)var3 + 0.5D - var6, (double)var4, (double)var2 + 0.5D + var6, (double)var3 + 0.5D + var6, (double)var4 + var8);
      case 4:
         return AxisAlignedBB.b((double)var2 + 0.5D - var6, (double)var3 + 0.5D - var6, (double)(var4 + 1) - var8, (double)var2 + 0.5D + var6, (double)var3 + 0.5D + var6, (double)(var4 + 1));
      case 5:
         return AxisAlignedBB.b((double)var2 + 0.5D - var6, (double)var3, (double)var4 + 0.5D - var6, (double)var2 + 0.5D + var6, (double)var3 + var8, (double)var4 + 0.5D + var6);
      }
   }

   public int getRenderType() {
      return BuildCraftCore.markerModel;
   }

   public boolean b() {
      return false;
   }

   protected TileEntity a_() {
      return new TileMarker();
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      ((TileMarker)var1.getTileEntity(var2, var3, var4)).tryConnection();
      return true;
   }

   public void remove(World var1, int var2, int var3, int var4) {
      Utils.preDestroyBlock(var1, var2, var3, var4);
      super.remove(var1, var2, var3, var4);
   }

   public AxisAlignedBB e(World var1, int var2, int var3, int var4) {
      return null;
   }

   public boolean a() {
      return Block.TORCH.a();
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void doPhysics(World var1, int var2, int var3, int var4, int var5) {
      ((TileMarker)var1.getTileEntity(var2, var3, var4)).switchSignals();
      if(this.dropTorchIfCantStay(var1, var2, var3, var4)) {
         int var6 = var1.getData(var2, var3, var4);
         boolean var7 = false;
         if(!BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4) && var6 == 1) {
            var7 = true;
         }

         if(!BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4) && var6 == 2) {
            var7 = true;
         }

         if(!BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1) && var6 == 3) {
            var7 = true;
         }

         if(!BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1) && var6 == 4) {
            var7 = true;
         }

         if(!BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4) && var6 == 5) {
            var7 = true;
         }

         if(!BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4) && var6 == 0) {
            var7 = true;
         }

         if(var7) {
            this.g(var1, var2, var3, var4, BuildCraftBuilders.markerBlock.id);
            var1.setTypeId(var2, var3, var4, 0);
         }
      }

   }

   public MovingObjectPosition a(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
      return Block.TORCH.a(var1, var2, var3, var4, var5, var6);
   }

   public boolean canPlace(World var1, int var2, int var3, int var4) {
      return BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4)?true:(BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4)?true:(BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1)?true:(BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1)?true:(BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4)?true:BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4)))));
   }

   public void postPlace(World var1, int var2, int var3, int var4, int var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      int var6 = var1.getData(var2, var3, var4);
      if(var5 == 1 && BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4)) {
         var6 = 5;
      }

      if(var5 == 2 && BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1)) {
         var6 = 4;
      }

      if(var5 == 3 && BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1)) {
         var6 = 3;
      }

      if(var5 == 4 && BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4)) {
         var6 = 2;
      }

      if(var5 == 5 && BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4)) {
         var6 = 1;
      }

      if(var5 == 0 && BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4)) {
         var6 = 0;
      }

      var1.setData(var2, var3, var4, var6);
   }

   public void c(World var1, int var2, int var3, int var4) {
      super.c(var1, var2, var3, var4);
      if(BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4)) {
         var1.setData(var2, var3, var4, 1);
      } else if(BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4)) {
         var1.setData(var2, var3, var4, 2);
      } else if(BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1)) {
         var1.setData(var2, var3, var4, 3);
      } else if(BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1)) {
         var1.setData(var2, var3, var4, 4);
      } else if(BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4)) {
         var1.setData(var2, var3, var4, 5);
      } else if(BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4)) {
         var1.setData(var2, var3, var4, 0);
      }

      this.dropTorchIfCantStay(var1, var2, var3, var4);
   }

   private boolean dropTorchIfCantStay(World var1, int var2, int var3, int var4) {
      if(!this.canPlace(var1, var2, var3, var4)) {
         this.g(var1, var2, var3, var4, BuildCraftBuilders.markerBlock.id);
         var1.setTypeId(var2, var3, var4, 0);
         return false;
      } else {
         return true;
      }
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }
}
