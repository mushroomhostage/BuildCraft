package buildcraft.factory;

import buildcraft.api.IBlockPipe;
import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import buildcraft.core.Utils;
import net.minecraft.server.forge.ITextureProvider;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockFrame extends Block implements IPipeConnection, IBlockPipe, ITextureProvider {

   public BlockFrame(int var1) {
      super(var1, Material.SHATTERABLE);
      this.textureId = 34;
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

   public int a(int var1, Random var2) {
      return -1;
   }

   public int getRenderType() {
      return BuildCraftCore.pipeModel;
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

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return var1.getTypeId(var5, var6, var7) == this.id;
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
}
