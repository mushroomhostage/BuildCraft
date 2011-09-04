package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.core.ILiquidContainer;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.TileNetworkData;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class TileTank extends TileBuildCraft implements ILiquidContainer {

   @TileNetworkData
   public int stored = 0;


   public int fill(Orientations var1, int var2) {
      TileTank var3 = this;

      for(int var4 = this.y - 1; var4 > 1 && this.world.getTileEntity(this.x, var4, this.z) instanceof TileTank; --var4) {
         var3 = (TileTank)this.world.getTileEntity(this.x, var4, this.z);
      }

      return var3.actualFill(var1, var2);
   }

   private int actualFill(Orientations var1, int var2) {
      TileEntity var3 = this.world.getTileEntity(this.x, this.y + 1, this.z);
      int var4 = 0;
      if(this.stored + var2 <= this.getCapacity()) {
         this.stored += var2;
         var4 = var2;
      } else if(this.stored <= this.getCapacity()) {
         var4 = this.getCapacity() - this.stored;
         this.stored = this.getCapacity();
      }

      if(APIProxy.isServerSide() && var4 > 0) {
         this.sendNetworkUpdate();
      }

      if(var4 < var2 && var3 instanceof TileTank) {
         var4 += ((TileTank)var3).actualFill(var1, var2 - var4);
      }

      return var4;
   }

   public int getLiquidQuantity() {
      return this.stored;
   }

   public int getCapacity() {
      return 16000;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.stored = var1.e("stored");
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.a("stored", this.stored);
   }

   public int empty(int var1, boolean var2) {
      TileTank var3 = this;

      for(int var4 = this.y + 1; var4 <= 128 && this.world.getTileEntity(this.x, var4, this.z) instanceof TileTank; ++var4) {
         var3 = (TileTank)this.world.getTileEntity(this.x, var4, this.z);
      }

      return var3.actualEmtpy(var1, var2);
   }

   private int actualEmtpy(int var1, boolean var2) {
      if(this.stored >= var1) {
         if(var2) {
            this.stored -= var1;
         }

         return var1;
      } else {
         int var3 = this.stored;
         if(var2) {
            this.stored = 0;
         }

         TileEntity var4 = this.world.getTileEntity(this.x, this.y - 1, this.z);
         if(var4 instanceof TileTank) {
            var3 += ((TileTank)var4).actualEmtpy(var1 - var3, var2);
         }

         return var3;
      }
   }
}
