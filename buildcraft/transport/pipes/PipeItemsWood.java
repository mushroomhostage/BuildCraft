package buildcraft.transport.pipes;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.core.Utils;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicWood;
import buildcraft.transport.PipeTransportItems;
import forge.ISidedInventory;
import net.minecraft.server.Block;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class PipeItemsWood extends Pipe implements IPowerReceptor {

   private PowerProvider powerProvider;
   private int baseTexture = 16;
   private int plainTexture = 31;
   private int nextTexture;


   public PipeItemsWood(int var1) {
      super(new PipeTransportItems(), new PipeLogicWood(), var1);
      this.nextTexture = this.baseTexture;
      this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
      this.powerProvider.configure(50, 1, 64, 1, 64);
      this.powerProvider.configurePowerPerdition(64, 1);
   }

   public int getBlockTexture() {
      return this.nextTexture;
   }

   public void prepareTextureFor(Orientations var1) {
      if(var1 == Orientations.Unknown) {
         this.nextTexture = this.baseTexture;
      } else {
         int var2 = this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);
         if(var2 == var1.ordinal()) {
            this.nextTexture = this.plainTexture;
         } else {
            this.nextTexture = this.baseTexture;
         }
      }

   }

   public void setPowerProvider(PowerProvider var1) {
      var1 = this.powerProvider;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   public void doWork() {
      if(this.powerProvider.energyStored > 0) {
         World var1 = this.worldObj;
         int var2 = this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);
         if(var2 <= 5) {
            Position var3 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var2]);
            var3.moveForwards(1.0D);
            int var4 = var1.getTypeId((int)var3.x, (int)var3.y, (int)var3.z);
            TileEntity var5 = var1.getTileEntity((int)var3.x, (int)var3.y, (int)var3.z);
            if(var5 != null && (var5 instanceof IInventory || var5 instanceof ILiquidContainer) && !PipeLogicWood.isExcludedFromExtraction(Block.byId[var4])) {
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
                  ((PipeTransportItems)this.transport).entityEntering(var9, var8.orientation);
               }

            }
         }
      }
   }

   public ItemStack checkExtract(IInventory var1, boolean var2, Orientations var3) {
      if(var1 instanceof ISpecialInventory) {
         return ((ISpecialInventory)var1).extractItem(var2, var3);
      } else {
         if(var1 instanceof ISidedInventory) {
            ISidedInventory var4 = (ISidedInventory)var1;
            int var5 = var4.getStartInventorySide(var3.ordinal());
            int var6 = var5 + var4.getSizeInventorySide(var3.ordinal()) - 1;
            IInventory var7 = Utils.getInventory(var1);
            ItemStack var8 = this.checkExtractGeneric(var7, var2, var3, var5, var6);
            if(var8 != null) {
               return var8;
            }
         } else {
            boolean var9;
            byte var10;
            ItemStack var11;
            if(var1.getSize() == 2) {
               var9 = false;
               if(var3 != Orientations.YNeg && var3 != Orientations.YPos) {
                  var10 = 1;
               } else {
                  var10 = 0;
               }

               var11 = var1.getItem(var10);
               if(var11 != null && var11.count > 0) {
                  if(var2) {
                     return var1.splitStack(var10, this.powerProvider.useEnergy(1, var11.count, true));
                  }

                  return var11;
               }
            } else if(var1.getSize() == 3) {
               var9 = false;
               if(var3 == Orientations.YPos) {
                  var10 = 0;
               } else if(var3 == Orientations.YNeg) {
                  var10 = 1;
               } else {
                  var10 = 2;
               }

               var11 = var1.getItem(var10);
               if(var11 != null && var11.count > 0) {
                  if(var2) {
                     return var1.splitStack(var10, this.powerProvider.useEnergy(1, var11.count, true));
                  }

                  return var11;
               }
            } else {
               IInventory var12 = Utils.getInventory(var1);
               var11 = this.checkExtractGeneric(var12, var2, var3, 0, var12.getSize() - 1);
               if(var11 != null) {
                  return var11;
               }
            }
         }

         return null;
      }
   }

   public ItemStack checkExtractGeneric(IInventory var1, boolean var2, Orientations var3, int var4, int var5) {
      for(int var6 = var4; var6 <= var5; ++var6) {
         if(var1.getItem(var6) != null && var1.getItem(var6).count > 0) {
            ItemStack var7 = var1.getItem(var6);
            if(var7 != null && var7.count > 0) {
               if(var2) {
                  return var1.splitStack(var6, this.powerProvider.useEnergy(1, var7.count, true));
               }

               return var7;
            }
         }
      }

      return null;
   }

   public int powerRequest() {
      return this.getPowerProvider().maxEnergyReceived;
   }
}
