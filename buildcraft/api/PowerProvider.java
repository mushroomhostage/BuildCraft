package buildcraft.api;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.SafeTimeTracker;
import buildcraft.api.TileNetworkData;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public abstract class PowerProvider {

   public int latency;
   public int minEnergyReceived;
   public int maxEnergyReceived;
   public int maxEnergyStored;
   public int minActivationEnergy;
   @TileNetworkData
   public int energyStored = 0;
   protected int powerLoss = 1;
   protected int powerLossRegularity = 100;
   public SafeTimeTracker timeTracker = new SafeTimeTracker();
   public SafeTimeTracker energyLossTracker = new SafeTimeTracker();


   public void configure(int var1, int var2, int var3, int var4, int var5) {
      this.latency = var1;
      this.minEnergyReceived = var2;
      this.maxEnergyReceived = var3;
      this.maxEnergyStored = var5;
      this.minActivationEnergy = var4;
   }

   public void configurePowerPerdition(int var1, int var2) {
      this.powerLoss = var1;
      this.powerLossRegularity = var2;
   }

   public final boolean update(IPowerReceptor var1) {
      if(!this.preConditions(var1)) {
         return false;
      } else {
         TileEntity var2 = (TileEntity)var1;
         boolean var3 = false;
         if(this.energyStored >= this.minActivationEnergy) {
            if(this.latency == 0) {
               var1.doWork();
               var3 = true;
            } else if(this.timeTracker.markTimeIfDelay(var2.world, (long)this.latency)) {
               var1.doWork();
               var3 = true;
            }
         }

         if(this.powerLoss > 0 && this.energyLossTracker.markTimeIfDelay(var2.world, (long)this.powerLossRegularity)) {
            this.energyStored -= this.powerLoss;
            if(this.energyStored < 0) {
               this.energyStored = 0;
            }
         }

         return var3;
      }
   }

   public boolean preConditions(IPowerReceptor var1) {
      return true;
   }

   public int useEnergy(int var1, int var2, boolean var3) {
      int var4 = 0;
      if(this.energyStored >= var1) {
         if(this.energyStored <= var2) {
            var4 = this.energyStored;
            if(var3) {
               this.energyStored = 0;
            }
         } else {
            var4 = var2;
            if(var3) {
               this.energyStored -= var2;
            }
         }
      }

      return var4;
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.latency = var1.getInt("latency");
      this.minEnergyReceived = var1.getInt("minEnergyReceived");
      this.maxEnergyReceived = var1.getInt("maxEnergyReceived");
      this.maxEnergyStored = var1.getInt("maxStoreEnergy");
      this.minActivationEnergy = var1.getInt("minActivationEnergy");
      this.energyStored = var1.getInt("storedEnergy");
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.setInt("latency", this.latency);
      var1.setInt("minEnergyReceived", this.minEnergyReceived);
      var1.setInt("maxEnergyReceived", this.maxEnergyReceived);
      var1.setInt("maxStoreEnergy", this.maxEnergyStored);
      var1.setInt("minActivationEnergy", this.minActivationEnergy);
      var1.setInt("storedEnergy", this.energyStored);
   }

   public void receiveEnergy(int var1) {
      this.energyStored += var1;
      if(this.energyStored > this.maxEnergyStored) {
         this.energyStored = this.maxEnergyStored;
      }

   }
}
