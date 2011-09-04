package buildcraft.transport;

import buildcraft.api.IBlockPipe;
import buildcraft.api.IPipeConnection;
import buildcraft.api.IPipeEntry;
import buildcraft.api.Orientations;
import buildcraft.core.ILiquidContainer;
import buildcraft.core.IMachine;
import buildcraft.core.Utils;
import buildcraft.transport.TilePipe;
import net.minecraft.server.forge.ITextureProvider;
import java.util.ArrayList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IInventory;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public abstract class BlockPipe extends BlockContainer implements IPipeConnection, IBlockPipe, ITextureProvider {

   public BlockPipe(int var1, Material var2) {
      super(var1, var2);
      this.c(0.5F);
   }

   public int getRenderType() {
      return BuildCraftCore.pipeModel;
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

   protected abstract TileEntity a_();

   public void a(World var1, int var2, int var3, int var4, AxisAlignedBB var5, ArrayList var6) {
      this.a(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
      super.a(var1, var2, var3, var4, var5, var6);
      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2 - 1, var3, var4)) {
         this.a(0.0F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2 + 1, var3, var4)) {
         this.a(0.25F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 - 1, var4)) {
         this.a(0.25F, 0.0F, 0.25F, 0.75F, 0.75F, 0.75F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 + 1, var4)) {
         this.a(0.25F, 0.25F, 0.25F, 0.75F, 1.0F, 0.75F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 - 1)) {
         this.a(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.75F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 + 1)) {
         this.a(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 1.0F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public AxisAlignedBB e(World var1, int var2, int var3, int var4) {
      float var5 = 0.25F;
      float var6 = 0.75F;
      float var7 = 0.25F;
      float var8 = 0.75F;
      float var9 = 0.25F;
      float var10 = 0.75F;
      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2 - 1, var3, var4)) {
         var5 = 0.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2 + 1, var3, var4)) {
         var6 = 1.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 - 1, var4)) {
         var7 = 0.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 + 1, var4)) {
         var8 = 1.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 - 1)) {
         var9 = 0.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 + 1)) {
         var10 = 1.0F;
      }

      return AxisAlignedBB.b((double)var2 + (double)var5, (double)var3 + (double)var7, (double)var4 + (double)var9, (double)var2 + (double)var6, (double)var3 + (double)var8, (double)var4 + (double)var10);
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      return this.e(var1, var2, var3, var4);
   }

   public MovingObjectPosition a(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
      float var7 = 0.25F;
      float var8 = 0.75F;
      float var9 = 0.25F;
      float var10 = 0.75F;
      float var11 = 0.25F;
      float var12 = 0.75F;
      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2 - 1, var3, var4)) {
         var7 = 0.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2 + 1, var3, var4)) {
         var8 = 1.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 - 1, var4)) {
         var9 = 0.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 + 1, var4)) {
         var10 = 1.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 - 1)) {
         var11 = 0.0F;
      }

      if(Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 + 1)) {
         var12 = 1.0F;
      }

      this.a(var7, var9, var11, var8, var10, var12);
      MovingObjectPosition var13 = super.a(var1, var2, var3, var4, var5, var6);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return var13;
   }

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      TileEntity var8 = var1.getTileEntity(var5, var6, var7);
      return var8 instanceof IPipeEntry || var8 instanceof IInventory || var8 instanceof IMachine || var8 instanceof ILiquidContainer;
   }

   public void remove(World var1, int var2, int var3, int var4) {
      Utils.preDestroyBlock(var1, var2, var3, var4);
      super.remove(var1, var2, var3, var4);
   }

   public int getTextureForConnection(Orientations var1, int var2) {
      return this.textureId;
   }

   public float getHeightInPipe() {
      return 0.5F;
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public void doPhysics(World var1, int var2, int var3, int var4, int var5) {
      ((TilePipe)var1.getTileEntity(var2, var3, var4)).scheduleNeighborChange();
   }
}
