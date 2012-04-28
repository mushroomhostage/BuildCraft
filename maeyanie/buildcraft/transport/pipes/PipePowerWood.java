package buildcraft.transport.pipes;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.core.Utils;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicWood;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TileGenericPipe;
import net.minecraft.server.TileEntity;

public class PipePowerWood extends Pipe implements IPowerReceptor {

   private PowerProvider powerProvider;
   private int baseTexture = 118;
   private int plainTexture = 31;
   private int nextTexture;


   public PipePowerWood(int var1) {
      super(new PipeTransportPower(), new PipeLogicWood(), var1);
      this.nextTexture = this.baseTexture;
      this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
      this.powerProvider.configure(50, 1, 1000, 1, 1000);
      this.powerProvider.configurePowerPerdition(1, 100);
   }

   public int getBlockTexture() {
      return this.nextTexture;
   }

   public void setPowerProvider(PowerProvider var1) {
      var1 = this.powerProvider;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   public void doWork() {}

   public void updateEntity() {
      super.updateEntity();

      for(int var1 = 0; var1 < 6; ++var1) {
         Position var2 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var1]);
         var2.moveForwards(1.0D);
         if(Utils.checkPipesConnections(this.worldObj, this.xCoord, this.yCoord, this.zCoord, (int)var2.x, (int)var2.y, (int)var2.z)) {
            TileEntity var3 = this.worldObj.getTileEntity((int)var2.x, (int)var2.y, (int)var2.z);
            if(var3 instanceof TileGenericPipe) {
               PipeTransportPower var4 = (PipeTransportPower)((TileGenericPipe)var3).pipe.transport;
               boolean var5 = false;
               int var6;
               if(this.powerProvider.energyStored > 40) {
                  var6 = this.powerProvider.energyStored / 40 + 4;
               } else if(this.powerProvider.energyStored > 10) {
                  var6 = this.powerProvider.energyStored / 10;
               } else {
                  var6 = 1;
               }

               int var7 = this.powerProvider.useEnergy(1, var6, true);
               var4.receiveEnergy(var2.orientation.reverse(), (double)var7);
               ((PipeTransportPower)this.transport).displayPower[var1] = (short)(((PipeTransportPower)this.transport).displayPower[var1] + var7);
            }
         }
      }

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

   public int powerRequest() {
      return this.getPowerProvider().maxEnergyReceived;
   }
}
