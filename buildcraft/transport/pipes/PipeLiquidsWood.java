package buildcraft.transport.pipes;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.TileNetworkData;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicWood;
import buildcraft.transport.PipeTransportLiquids;
import net.minecraft.server.Block;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class PipeLiquidsWood extends Pipe implements IPowerReceptor {

   @TileNetworkData
   public int liquidToExtract;
   private PowerProvider powerProvider;
   private int baseTexture = 112;
   private int plainTexture = 31;
   private int nextTexture;
   long lastMining;
   boolean lastPower;


   public PipeLiquidsWood(int var1) {
      super(new PipeTransportLiquids(), new PipeLogicWood(), var1);
      this.nextTexture = this.baseTexture;
      this.lastMining = 0L;
      this.lastPower = false;
      this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
      this.powerProvider.configure(50, 1, 1, 1, 1);
      this.powerProvider.configurePowerPerdition(1, 1);
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
            if(var5 != null && var5 instanceof ILiquidContainer && !PipeLogicWood.isExcludedFromExtraction(Block.byId[var4])) {
               if(var5 instanceof ILiquidContainer && this.liquidToExtract <= 1000) {
                  this.liquidToExtract += this.powerProvider.useEnergy(1, 1, true) * 1000;
               }

            }
         }
      }
   }

   public void setPowerProvider(PowerProvider var1) {
      this.powerProvider = var1;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   public void updateEntity() {
      super.updateEntity();
      int var1 = this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);
      if(this.liquidToExtract > 0 && var1 < 6) {
         Position var2 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var1]);
         var2.moveForwards(1.0D);
         TileEntity var3 = this.worldObj.getTileEntity((int)var2.x, (int)var2.y, (int)var2.z);
         if(var3 instanceof ILiquidContainer) {
            ILiquidContainer var4 = (ILiquidContainer)var3;
            int var5 = ((PipeTransportLiquids)this.transport).flowRate;
            int var6 = var4.empty(this.liquidToExtract > var5?var5:this.liquidToExtract, false);
            var6 = ((PipeTransportLiquids)this.transport).fill(var2.orientation, var6, var4.getLiquidId(), true);
            var4.empty(var6, true);
            this.liquidToExtract -= var6;
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

   public int getBlockTexture() {
      return this.nextTexture;
   }

   public int powerRequest() {
      return this.getPowerProvider().maxEnergyReceived;
   }
}
