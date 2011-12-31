package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.SafeTimeTracker;
import buildcraft.api.TileNetworkData;
import buildcraft.core.IMachine;
import buildcraft.factory.RefineryRecipe;
import buildcraft.factory.TileMachine;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class TileRefinery extends TileMachine implements ILiquidContainer, IPowerReceptor, IInventory, IMachine {

   public static LinkedList recipes = new LinkedList();
   public static int LIQUID_PER_SLOT = 4000;
   @TileNetworkData
   public TileRefinery.Slot slot1 = new TileRefinery.Slot();
   @TileNetworkData
   public TileRefinery.Slot slot2 = new TileRefinery.Slot();
   @TileNetworkData
   public TileRefinery.Slot result = new TileRefinery.Slot();
   @TileNetworkData
   public float animationSpeed = 1.0F;
   private int animationStage = 0;
   SafeTimeTracker time = new SafeTimeTracker();
   SafeTimeTracker updateNetworkTime = new SafeTimeTracker();
   PowerProvider powerProvider;
   private boolean isActive;


   public TileRefinery() {
      this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
      this.powerProvider.configure(20, 25, 25, 25, 1000);
   }
   
   public ItemStack[] getContents() {
      return new ItemStack[0];
   }

   public int fill(Orientations var1, int var2, int var3, boolean var4) {
      int var5 = this.slot1.fill(var1, var2, var3, var4);
      var5 += this.slot2.fill(var1, var2 - var5, var3, var4);
      if(var4 && var5 > 0) {
         this.updateNetworkTime.markTime(this.world);
         this.sendNetworkUpdate();
      }

      return var5;
   }

   public int empty(int var1, boolean var2) {
      boolean var3 = false;
      int var4;
      if(this.result.quantity >= var1) {
         var4 = var1;
         if(var2) {
            this.result.quantity -= var1;
         }
      } else {
         var4 = this.result.quantity;
         if(var2) {
            this.result.quantity = 0;
         }
      }

      if(var2 && var4 > 0) {
         this.updateNetworkTime.markTime(this.world);
         this.sendNetworkUpdate();
      }

      return var4;
   }

   public int getLiquidQuantity() {
      return this.result.quantity;
   }

   public int getCapacity() {
      return 3000;
   }

   public int getLiquidId() {
      return this.result.liquidId;
   }

   public int getSize() {
      return 0;
   }

   public ItemStack getItem(int var1) {
      return null;
   }

   public ItemStack splitStack(int var1, int var2) {
      return null;
   }

   public void setItem(int var1, ItemStack var2) {}

   public String getName() {
      return null;
   }

   public int getMaxStackSize() {
      return 0;
   }

   public boolean a(EntityHuman var1) {
      return false;
   }

   public void setPowerProvider(PowerProvider var1) {
      this.powerProvider = var1;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   public void doWork() {}

   public void l_() {
      if(APIProxy.isClient(this.world)) {
         this.simpleAnimationIterate();
      } else if(APIProxy.isServerSide() && this.updateNetworkTime.markTimeIfDelay(this.world, (long)(2 * BuildCraftCore.updateFactor))) {
         this.sendNetworkUpdate();
      }

      this.isActive = false;
      RefineryRecipe var1 = null;
      TileRefinery.Slot var2 = null;
      TileRefinery.Slot var3 = null;
      Iterator var4 = recipes.iterator();

      while(var4.hasNext()) {
         RefineryRecipe var5 = (RefineryRecipe)var4.next();
         if(var5.sourceId1 == this.slot1.liquidId && this.slot1.quantity >= var5.sourceQty1) {
            var2 = this.slot1;
            var3 = this.slot2;
         } else if(var5.sourceId1 == this.slot2.liquidId && this.slot2.quantity >= var5.sourceQty1) {
            var2 = this.slot2;
            var3 = this.slot1;
         }

         if(var2 != null) {
            if(var5.sourceQty2 > 0) {
               if(var5.sourceId2 != var3.liquidId || var3.quantity < var5.sourceQty2) {
                  continue;
               }
            } else {
               var3 = null;
            }

            var1 = var5;
            break;
         }
      }

      if(var1 == null) {
         this.decreaseAnimation();
      } else if(this.result.quantity != 0 && this.result.liquidId != var1.resultId) {
         this.decreaseAnimation();
      } else if(this.result.quantity + var1.resultQty > LIQUID_PER_SLOT) {
         this.decreaseAnimation();
      } else {
         this.isActive = true;
         if(this.powerProvider.energyStored >= var1.energy) {
            this.increaseAnimation();
         } else {
            this.decreaseAnimation();
         }

         if(this.time.markTimeIfDelay(this.world, (long)var1.delay)) {
            int var6 = this.powerProvider.useEnergy(var1.energy, var1.energy, true);
            if(var6 != 0) {
               this.result.liquidId = var1.resultId;
               this.result.quantity += var1.resultQty;
               var2.quantity -= var1.sourceQty1;
               if(var3 != null) {
                  var3.quantity -= var1.sourceQty2;
               }
            }

         }
      }
   }

   public boolean isActive() {
      return this.isActive;
   }

   public boolean manageLiquids() {
      return true;
   }

   public boolean manageSolids() {
      return true;
   }

   public static void addRecipe(RefineryRecipe var0) {
      recipes.add(var0);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKey("slot1")) {
         this.slot1.readFromNBT(var1.getCompound("slot1"));
         this.slot2.readFromNBT(var1.getCompound("slot2"));
         this.result.readFromNBT(var1.getCompound("result"));
      }

      this.animationStage = var1.getInt("animationStage");
      this.animationSpeed = var1.getFloat("animationSpeed");
      PowerFramework.currentFramework.loadPowerProvider(this, var1);
      this.powerProvider.configure(20, 25, 25, 25, 1000);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      NBTTagCompound var2 = new NBTTagCompound();
      NBTTagCompound var3 = new NBTTagCompound();
      NBTTagCompound var4 = new NBTTagCompound();
      this.slot1.writeFromNBT(var2);
      this.slot2.writeFromNBT(var3);
      this.result.writeFromNBT(var4);
      var1.set("slot1", var2);
      var1.set("slot2", var3);
      var1.set("result", var4);
      var1.setInt("animationStage", this.animationStage);
      var1.setFloat("animationSpeed", this.animationSpeed);
      PowerFramework.currentFramework.savePowerProvider(this, var1);
   }

   public int getAnimationStage() {
      return this.animationStage;
   }

   public void simpleAnimationIterate() {
      if(this.animationSpeed > 1.0F) {
         this.animationStage = (int)((float)this.animationStage + this.animationSpeed);
         if(this.animationStage > 300) {
            this.animationStage = 100;
         }
      } else if(this.animationStage > 0) {
         --this.animationStage;
      }

   }

   public void increaseAnimation() {
      if(this.animationSpeed < 2.0F) {
         this.animationSpeed = 2.0F;
      } else if(this.animationSpeed <= 5.0F) {
         this.animationSpeed = (float)((double)this.animationSpeed + 0.1D);
      }

      this.animationStage = (int)((float)this.animationStage + this.animationSpeed);
      if(this.animationStage > 300) {
         this.animationStage = 100;
      }

   }

   public void decreaseAnimation() {
      if(this.animationSpeed >= 1.0F) {
         this.animationSpeed = (float)((double)this.animationSpeed - 0.1D);
         this.animationStage = (int)((float)this.animationStage + this.animationSpeed);
         if(this.animationStage > 300) {
            this.animationStage = 100;
         }
      } else if(this.animationStage > 0) {
         --this.animationStage;
      }

   }

   public void f() {}

   public void g() {}


   public static class Slot {

      @TileNetworkData
      public int liquidId = 0;
      @TileNetworkData
      public int quantity = 0;


      public int fill(Orientations var1, int var2, int var3, boolean var4) {
         if(this.quantity != 0 && this.liquidId != var3) {
            return 0;
         } else if(this.quantity + var2 <= TileRefinery.LIQUID_PER_SLOT) {
            if(var4) {
               this.quantity += var2;
            }

            this.liquidId = var3;
            return var2;
         } else {
            int var5 = TileRefinery.LIQUID_PER_SLOT - this.quantity;
            if(var4) {
               this.quantity = TileRefinery.LIQUID_PER_SLOT;
            }

            this.liquidId = var3;
            return var5;
         }
      }

      public void writeFromNBT(NBTTagCompound var1) {
         var1.setInt("liquidId", this.liquidId);
         var1.setInt("quantity", this.quantity);
      }

      public void readFromNBT(NBTTagCompound var1) {
         this.liquidId = var1.getInt("liquidId");
         if(this.liquidId != 0) {
            this.quantity = var1.getInt("quantity");
         } else {
            this.quantity = 0;
         }

      }
   }
}
