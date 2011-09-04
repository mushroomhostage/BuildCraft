package buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerProvider;
import buildcraft.core.ILiquidContainer;
import buildcraft.core.TileNetworkData;
import buildcraft.core.Utils;
import buildcraft.transport.BlockWoodenPipe;
import buildcraft.transport.TilePipe;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class TileWoodenPipe extends TilePipe implements IPowerReceptor {

   long lastMining = 0L;
   boolean lastPower = false;
   private PowerProvider powerProvider;
   @TileNetworkData
   public int liquidToExtract;


   public TileWoodenPipe() {
      this.powerProvider = BuildCraftCore.powerFramework.createPowerProvider();
      this.powerProvider.configure(50, 1, 64, 1, 64);
      this.powerProvider.configurePowerPerdition(64, 1);
   }

   public void doWork() {
      if(this.powerProvider.energyStored > 0) {
         World var1 = this.world;
         int var2 = this.world.getData(this.x, this.y, this.z);
         if(var2 <= 5) {
            Position var3 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var2]);
            var3.moveForwards(1.0D);
            int var4 = var1.getTypeId((int)var3.x, (int)var3.y, (int)var3.z);
            TileEntity var5 = var1.getTileEntity((int)var3.x, (int)var3.y, (int)var3.z);
            if(var5 != null && (var5 instanceof IInventory || var5 instanceof ILiquidContainer) && !BlockWoodenPipe.isExcludedFromExtraction(Block.byId[var4])) {
               if(var5 instanceof IInventory) {
                  IInventory var6 = (IInventory)var5;
                  ItemStack var7 = this.checkExtract(var6, true, var3.orientation.reverse());
                  if(var7 == null || var7.count == 0) {
                     this.powerProvider.useEnergy(1, 1, false);
                     return;
                  }

                  Position var8 = new Position(var3.x + 0.5D, var3.y + (double)Utils.getPipeFloorOf(var7), var3.z + 0.5D, var3.orientation.reverse());
                  var8.moveForwards(0.5D);
                  EntityPassiveItem var9 = new EntityPassiveItem(var1, var8.x, var8.y, var8.z, var7);
                  this.entityEntering(var9, var8.orientation);
               } else if(var5 instanceof ILiquidContainer && this.liquidToExtract <= 1000) {
                  this.liquidToExtract += this.powerProvider.useEnergy(1, 1, true) * 1000;
                  this.sendNetworkUpdate();
               }

            }
         }
      }
   }

   public ItemStack checkExtract(IInventory var1, boolean var2, Orientations var3) {
      if(var1 instanceof ISpecialInventory) {
         return ((ISpecialInventory)var1).extractItem(var2, var3);
      } else {
         boolean var4;
         ItemStack var5;
         byte var6;
         if(var1.getSize() == 2) {
            var4 = false;
            if(var3 != Orientations.YNeg && var3 != Orientations.YPos) {
               var6 = 1;
            } else {
               var6 = 0;
            }

            var5 = var1.getItem(var6);
            if(var5 != null && var5.count > 0) {
               if(var2) {
                  return var1.splitStack(var6, this.powerProvider.useEnergy(1, var5.count, true));
               }

               return var5;
            }
         } else if(var1.getSize() == 3) {
            var4 = false;
            if(var3 == Orientations.YPos) {
               var6 = 0;
            } else if(var3 == Orientations.YNeg) {
               var6 = 1;
            } else {
               var6 = 2;
            }

            var5 = var1.getItem(var6);
            if(var5 != null && var5.count > 0) {
               if(var2) {
                  return var1.splitStack(var6, this.powerProvider.useEnergy(1, var5.count, true));
               }

               return var5;
            }
         } else {
            IInventory var7 = Utils.getInventory(var1);
            var5 = this.checkExtractGeneric(var7, var2, var3);
            if(var5 != null) {
               return var5;
            }
         }

         return null;
      }
   }

   public ItemStack checkExtractGeneric(IInventory var1, boolean var2, Orientations var3) {
      for(int var4 = 0; var4 < var1.getSize(); ++var4) {
         if(var1.getItem(var4) != null && var1.getItem(var4).count > 0) {
            ItemStack var5 = var1.getItem(var4);
            if(var5 != null && var5.count > 0) {
               if(var2) {
                  return var1.splitStack(var4, this.powerProvider.useEnergy(1, var5.count, true));
               }

               return var5;
            }
         }
      }

      return null;
   }

   public void switchSource() {
      int var1 = this.world.getData(this.x, this.y, this.z);
      int var2 = 6;

      for(int var3 = var1 + 1; var3 <= var1 + 6; ++var3) {
         Orientations var4 = Orientations.values()[var3 % 6];
         Position var5 = new Position((double)this.x, (double)this.y, (double)this.z, var4);
         var5.moveForwards(1.0D);
         Block var6 = Block.byId[this.world.getTypeId((int)var5.x, (int)var5.y, (int)var5.z)];
         TileEntity var7 = this.world.getTileEntity((int)var5.x, (int)var5.y, (int)var5.z);
         if((var7 instanceof IInventory || var7 instanceof ILiquidContainer && !(var7 instanceof TilePipe)) && Utils.checkPipesConnections(this.world, this.x, this.y, this.z, var7.x, var7.y, var7.z) && !BlockWoodenPipe.isExcludedFromExtraction(var6)) {
            var2 = var4.ordinal();
            break;
         }
      }

      if(var2 != var1) {
         this.world.setRawData(this.x, this.y, this.z, var2);
         this.world.notify(this.x, this.y, this.z);
      }

   }

   protected void neighborChange() {
      super.neighborChange();
      int var1 = this.world.getData(this.x, this.y, this.z);
      if(var1 > 5) {
         this.switchSource();
      } else {
         Position var2 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var1]);
         var2.moveForwards(1.0D);
         if(!(this.world.getTileEntity((int)var2.x, (int)var2.y, (int)var2.z) instanceof IInventory)) {
            this.switchSource();
         }
      }

   }

   public void initialize() {
      super.initialize();
      this.scheduleNeighborChange();
   }

   public void setPowerProvider(PowerProvider var1) {
      this.powerProvider = var1;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   public void g_() {
      super.g_();
      int var1 = this.world.getData(this.x, this.y, this.z);
      if(this.liquidToExtract > 0 && var1 < 6) {
         Position var2 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var1]);
         var2.moveForwards(1.0D);
         TileEntity var3 = this.world.getTileEntity((int)var2.x, (int)var2.y, (int)var2.z);
         if(var3 instanceof ILiquidContainer) {
            ILiquidContainer var4 = (ILiquidContainer)var3;
            int var5 = var4.empty(this.liquidToExtract > flowRate?flowRate:this.liquidToExtract, false);
            var5 = this.fill(var2.orientation, var5);
            var4.empty(var5, true);
            this.liquidToExtract -= var5;
         }
      }

   }
}
