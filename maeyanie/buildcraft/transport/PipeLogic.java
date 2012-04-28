package buildcraft.transport;

import buildcraft.api.Orientations;
import buildcraft.transport.TileGenericPipe;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class PipeLogic {

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

   public boolean blockActivated(EntityHuman var1) {
      return false;
   }

   public void writeToNBT(NBTTagCompound var1) {}

   public void readFromNBT(NBTTagCompound var1) {}

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

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      return false;
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      return null;
   }

   public int getSizeInventory() {
      return 0;
   }

   public ItemStack getStackInSlot(int var1) {
      return null;
   }

   public ItemStack decrStackSize(int var1, int var2) {
      return null;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {}

   public String getInvName() {
      return null;
   }

   public int getInventoryStackLimit() {
      return 0;
   }

   public boolean canInteractWith(EntityHuman var1) {
      return true;
   }

   public void updateEntity() {}
}
