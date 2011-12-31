package buildcraft.energy;

import buildcraft.core.ILiquid;
import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockOilFlowing extends BlockFluids {

   int numAdjacentSources = 0;
   boolean[] isOptimalFlowDirection = new boolean[4];
   int[] flowCost = new int[4];


   public BlockOilFlowing(int var1, Material var2) {
      super(var1, var2);
   }

   public int c() {
      return BuildCraftCore.oilModel;
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   private void func_30003_j(World var1, int var2, int var3, int var4) {
      int var5 = var1.getData(var2, var3, var4);
      var1.setRawTypeIdAndData(var2, var3, var4, this.id + 1, var5);
      var1.b(var2, var3, var4, var2, var3, var4);
      var1.notify(var2, var3, var4);
   }

   public void a(World var1, int var2, int var3, int var4, Random var5) {
      int var6 = this.g(var1, var2, var3, var4);
      byte var7 = 1;
      boolean var8 = true;
      int var10;
      if(var6 > 0) {
         byte var9 = -100;
         this.numAdjacentSources = 0;
         int var12 = this.getSmallestFlowDecay(var1, var2 - 1, var3, var4, var9);
         var12 = this.getSmallestFlowDecay(var1, var2 + 1, var3, var4, var12);
         var12 = this.getSmallestFlowDecay(var1, var2, var3, var4 - 1, var12);
         var12 = this.getSmallestFlowDecay(var1, var2, var3, var4 + 1, var12);
         var10 = var12 + var7;
         if(var10 >= 8 || var12 < 0) {
            var10 = -1;
         }

         if(this.g(var1, var2, var3 + 1, var4) >= 0) {
            int var11 = this.g(var1, var2, var3 + 1, var4);
            if(var11 >= 8) {
               var10 = var11;
            } else {
               var10 = var11 + 8;
            }
         }

         if(var10 != var6) {
            var6 = var10;
            if(var10 < 0) {
               var1.setTypeId(var2, var3, var4, 0);
            } else {
               var1.setData(var2, var3, var4, var10);
               var1.c(var2, var3, var4, this.id, this.d());
               var1.applyPhysics(var2, var3, var4, this.id);
            }
         } else if(var8) {
            this.func_30003_j(var1, var2, var3, var4);
         }
      } else {
         this.func_30003_j(var1, var2, var3, var4);
      }

      if(this.liquidCanDisplaceBlock(var1, var2, var3 - 1, var4)) {
         if(var6 >= 8) {
            var1.setTypeIdAndData(var2, var3 - 1, var4, this.id, var6);
         } else {
            var1.setTypeIdAndData(var2, var3 - 1, var4, this.id, var6 + 8);
         }
      } else if(var6 >= 0 && (var6 == 0 || this.blockBlocksFlow(var1, var2, var3 - 1, var4))) {
         boolean[] var13 = this.getOptimalFlowDirections(var1, var2, var3, var4);
         var10 = var6 + var7;
         if(var6 >= 8) {
            var10 = 1;
         }

         if(var10 >= 8) {
            return;
         }

         if(var13[0]) {
            this.flowIntoBlock(var1, var2 - 1, var3, var4, var10);
         }

         if(var13[1]) {
            this.flowIntoBlock(var1, var2 + 1, var3, var4, var10);
         }

         if(var13[2]) {
            this.flowIntoBlock(var1, var2, var3, var4 - 1, var10);
         }

         if(var13[3]) {
            this.flowIntoBlock(var1, var2, var3, var4 + 1, var10);
         }
      }

   }

   private void flowIntoBlock(World var1, int var2, int var3, int var4, int var5) {
      if(this.liquidCanDisplaceBlock(var1, var2, var3, var4)) {
         int var6 = var1.getTypeId(var2, var3, var4);
         if(var6 > 0) {
            Block.byId[var6].b(var1, var2, var3, var4, var1.getData(var2, var3, var4), 0);
         }

         var1.setTypeIdAndData(var2, var3, var4, this.id, var5);
      }

   }

   private int calculateFlowCost(World var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = 1000;

      for(int var8 = 0; var8 < 4; ++var8) {
         if((var8 != 0 || var6 != 1) && (var8 != 1 || var6 != 0) && (var8 != 2 || var6 != 3) && (var8 != 3 || var6 != 2)) {
            int var9 = var2;
            int var11 = var4;
            if(var8 == 0) {
               var9 = var2 - 1;
            }

            if(var8 == 1) {
               ++var9;
            }

            if(var8 == 2) {
               var11 = var4 - 1;
            }

            if(var8 == 3) {
               ++var11;
            }

            if(!this.blockBlocksFlow(var1, var9, var3, var11) && (var1.getMaterial(var9, var3, var11) != this.material || var1.getData(var9, var3, var11) != 0)) {
               if(!this.blockBlocksFlow(var1, var9, var3 - 1, var11)) {
                  return var5;
               }

               if(var5 < 4) {
                  int var12 = this.calculateFlowCost(var1, var9, var3, var11, var5 + 1, var8);
                  if(var12 < var7) {
                     var7 = var12;
                  }
               }
            }
         }
      }

      return var7;
   }

   private boolean[] getOptimalFlowDirections(World var1, int var2, int var3, int var4) {
      int var5;
      int var6;
      for(var5 = 0; var5 < 4; ++var5) {
         this.flowCost[var5] = 1000;
         var6 = var2;
         int var8 = var4;
         if(var5 == 0) {
            var6 = var2 - 1;
         }

         if(var5 == 1) {
            ++var6;
         }

         if(var5 == 2) {
            var8 = var4 - 1;
         }

         if(var5 == 3) {
            ++var8;
         }

         if(!this.blockBlocksFlow(var1, var6, var3, var8) && (var1.getMaterial(var6, var3, var8) != this.material || var1.getData(var6, var3, var8) != 0)) {
            if(!this.blockBlocksFlow(var1, var6, var3 - 1, var8)) {
               this.flowCost[var5] = 0;
            } else {
               this.flowCost[var5] = this.calculateFlowCost(var1, var6, var3, var8, 1, var5);
            }
         }
      }

      var5 = this.flowCost[0];

      for(var6 = 1; var6 < 4; ++var6) {
         if(this.flowCost[var6] < var5) {
            var5 = this.flowCost[var6];
         }
      }

      for(var6 = 0; var6 < 4; ++var6) {
         this.isOptimalFlowDirection[var6] = this.flowCost[var6] == var5;
      }

      return this.isOptimalFlowDirection;
   }

   private boolean blockBlocksFlow(World var1, int var2, int var3, int var4) {
      int var5 = var1.getTypeId(var2, var3, var4);
      if(var5 != Block.WOODEN_DOOR.id && var5 != Block.IRON_DOOR_BLOCK.id && var5 != Block.SIGN_POST.id && var5 != Block.LADDER.id && var5 != Block.SUGAR_CANE_BLOCK.id) {
         if(var5 == 0) {
            return false;
         } else {
            Material var6 = Block.byId[var5].material;
            return var6.isSolid();
         }
      } else {
         return true;
      }
   }

   protected int getSmallestFlowDecay(World var1, int var2, int var3, int var4, int var5) {
      int var6 = this.g(var1, var2, var3, var4);
      if(var6 < 0) {
         return var5;
      } else {
         if(var6 >= 8) {
            var6 = 0;
         }

         return var5 >= 0 && var6 >= var5?var5:var6;
      }
   }

   private boolean liquidCanDisplaceBlock(World var1, int var2, int var3, int var4) {
      Material var5 = var1.getMaterial(var2, var3, var4);
      return var5 == this.material?false:!this.blockBlocksFlow(var1, var2, var3, var4);
   }

   public void onPlace(World var1, int var2, int var3, int var4) {
      super.onPlace(var1, var2, var3, var4);
      if(var1.getTypeId(var2, var3, var4) == this.id) {
         var1.c(var2, var3, var4, this.id, this.d());
      }

   }

   public int stillLiquidId() {
      return BuildCraftEnergy.oilStill.id;
   }

   public boolean isBlockReplaceable(World var1, int var2, int var3, int var4) {
      return true;
   }
}
