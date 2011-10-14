package buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.transport.TileGenericPipe;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class PipeTransport {

   public int xCoord;
   public int yCoord;
   public int zCoord;
   public World worldObj;
   public TileGenericPipe container;


   public void setPosition(int var1, int var2, int var3) {
      this.xCoord = var1;
      this.yCoord = var2;
      this.zCoord = var3;
   }

   public void setWorld(World var1) {
      this.worldObj = var1;
   }

   public void readFromNBT(NBTTagCompound var1) {}

   public void writeToNBT(NBTTagCompound var1) {}

   public void updateEntity() {}

   public void setTile(TileGenericPipe var1) {
      this.container = var1;
   }

   public boolean isPipeConnected(TileEntity var1) {
      return true;
   }

   public void onNeighborBlockChange() {}

   public void onBlockPlaced() {}

   public void initialize() {}

   public boolean inputOpen(Orientations var1) {
      return true;
   }

   public boolean outputOpen(Orientations var1) {
      return true;
   }

   public boolean acceptItems() {
      return false;
   }

   public void entityEntering(EntityPassiveItem var1, Orientations var2) {}
}
