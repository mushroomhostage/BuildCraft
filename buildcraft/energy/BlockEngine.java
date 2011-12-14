package buildcraft.energy;

import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import buildcraft.energy.EnergyProxy;
import buildcraft.energy.EngineIron;
import buildcraft.energy.EngineStone;
import buildcraft.energy.EngineWood;
import buildcraft.energy.TileEngine;
import java.util.Random;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockEngine extends BlockContainer implements IPipeConnection {

   public BlockEngine(int var1) {
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

   public int c() {
      return BuildCraftCore.blockByEntityModel;
   }

   public TileEntity a_() {
      return new TileEngine();
   }

   public void remove(World var1, int var2, int var3, int var4) {
      ((TileEngine)var1.getTileEntity(var2, var3, var4)).delete();
      super.remove(var1, var2, var3, var4);
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      TileEngine var6 = (TileEngine)var1.getTileEntity(var2, var3, var4);
      if(var5.P() != null && var5.P().getItem() == BuildCraftCore.wrenchItem) {
         var6.switchOrientation();
         return true;
      } else if(var6.engine instanceof EngineStone) {
         EnergyProxy.displayGUISteamEngine(var5, var6);
         return true;
      } else if(var6.engine instanceof EngineIron) {
         EnergyProxy.displayGUICombustionEngine(var5, var6);
         return true;
      } else {
         return true;
      }
   }

   public void postPlace(World var1, int var2, int var3, int var4, int var5) {
      TileEngine var6 = (TileEngine)var1.getTileEntity(var2, var3, var4);
      var6.orientation = Orientations.YPos.ordinal();
      var6.switchOrientation();
   }

   protected int getDropData(int var1) {
      return var1;
   }

   public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
      TileEngine var6 = (TileEngine)var1.getTileEntity(var2, var3, var4);
      if(var6.isBurning()) {
         float var7 = (float)var2 + 0.5F;
         float var8 = (float)var3 + 0.0F + var5.nextFloat() * 6.0F / 16.0F;
         float var9 = (float)var4 + 0.5F;
         float var10 = 0.52F;
         float var11 = var5.nextFloat() * 0.6F - 0.3F;
         var1.a("reddust", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
         var1.a("reddust", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
         var1.a("reddust", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
         var1.a("reddust", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
      }
   }

   public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      TileEngine var8 = (TileEngine)var1.getTileEntity(var2, var3, var4);
      if(var8 == null) {
         return false;
      } else if(var8.engine instanceof EngineWood) {
         return false;
      } else {
         switch(Orientations.values()[var8.orientation]) {
         case YPos:
            return var3 - var6 != -1;
         case YNeg:
            return var3 - var6 != 1;
         case ZPos:
            return var4 - var7 != -1;
         case ZNeg:
            return var4 - var7 != 1;
         case XPos:
            return var2 - var5 != -1;
         case XNeg:
            return var2 - var5 != 1;
         default:
            return true;
         }
      }
   }
}
