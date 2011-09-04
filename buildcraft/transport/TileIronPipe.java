package buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.IPipeEntry;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.transport.TilePipe;
import buildcraft.transport.TileWoodenPipe;
import java.util.LinkedList;
import net.minecraft.server.IInventory;
import net.minecraft.server.TileEntity;

public class TileIronPipe extends TilePipe {

   boolean lastPower = false;


   public void switchPower() {
      boolean var1 = this.world.isBlockIndirectlyPowered(this.x, this.y, this.z);
      if(var1 != this.lastPower) {
         this.switchPosition();
         this.lastPower = var1;
      }

   }

   public void switchPosition() {
      int var1 = this.world.getData(this.x, this.y, this.z);
      int var2 = var1;

      for(int var3 = 0; var3 < 6; ++var3) {
         ++var2;
         if(var2 > 5) {
            var2 = 0;
         }

         Position var4 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var2]);
         var4.moveForwards(1.0D);
         TileEntity var5 = this.world.getTileEntity((int)var4.x, (int)var4.y, (int)var4.z);
         if(var5 instanceof IPipeEntry && !(var5 instanceof TileWoodenPipe) || var5 instanceof IInventory) {
            this.world.setRawData(this.x, this.y, this.z, var2);
            return;
         }
      }

   }

   public LinkedList getPossibleMovements(Position var1, EntityPassiveItem var2) {
      LinkedList var3 = new LinkedList();
      int var4 = this.world.getData(this.x, this.y, this.z);
      if(var4 != -1) {
         Position var5 = new Position(var1);
         var5.orientation = Orientations.values()[var4];
         var5.moveForwards(1.0D);
         TileEntity var6 = this.world.getTileEntity((int)var5.x, (int)var5.y, (int)var5.z);
         if(var6 instanceof IPipeEntry || var6 instanceof IInventory) {
            var3.add(var5.orientation);
         }
      }

      return var3;
   }

   public void initialize() {
      super.initialize();
      this.lastPower = this.world.isBlockIndirectlyPowered(this.x, this.y, this.z);
   }

   public int fill(Orientations var1, int var2) {
      return var1.ordinal() == this.world.getData(this.x, this.y, this.z)?0:super.fill(var1, var2);
   }

   public boolean canReceiveLiquid(Position var1) {
      return var1.orientation.ordinal() == this.world.getData(this.x, this.y, this.z)?super.canReceiveLiquid(var1):false;
   }
}
