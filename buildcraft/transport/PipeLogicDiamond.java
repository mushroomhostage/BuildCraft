package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.SafeTimeTracker;
import buildcraft.core.CoreProxy;
import buildcraft.core.PacketIds;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeLogic;
import buildcraft.transport.TransportProxy;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.mod_BuildCraftTransport;

public class PipeLogicDiamond extends PipeLogic {

   ItemStack[] items = new ItemStack[54];
   private SafeTimeTracker tracker = new SafeTimeTracker();


   public boolean blockActivated(EntityHuman var1) {
      if(var1.K() != null && var1.K().id < Block.byId.length && Block.byId[var1.K().id] instanceof BlockGenericPipe) {
         return false;
      } else {
         TransportProxy.displayGUIFilter(var1, this.container);
         return true;
      }
   }

   public int getSizeInventory() {
      return this.items.length;
   }

   public ItemStack getStackInSlot(int var1) {
      return this.items[var1];
   }

   public ItemStack decrStackSize(int var1, int var2) {
      ItemStack var3 = this.items[var1].cloneItemStack();
      var3.count = var2;
      this.items[var1].count -= var2;
      if(this.items[var1].count == 0) {
         this.items[var1] = null;
      }

      if(APIProxy.isServerSide()) {
         CoreProxy.sendToPlayers((Packet230ModLoader)this.getContentsPacket(), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
      }

      return var3;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.items[var1] = var2;
      if(APIProxy.isServerSide()) {
         CoreProxy.sendToPlayers((Packet230ModLoader)this.getContentsPacket(), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
      }

   }

   public void updateEntity() {
      if(this.tracker.markTimeIfDelay(this.worldObj, 200L) && APIProxy.isServerSide()) {
         CoreProxy.sendToPlayers((Packet230ModLoader)this.getContentsPacket(), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
      }

   }

   public String getInvName() {
      return "Filters";
   }

   public int getInventoryStackLimit() {
      return 1;
   }

   public boolean canInteractWith(EntityHuman var1) {
      return true;
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      NBTTagList var2 = var1.l("items");

      for(int var3 = 0; var3 < var2.c(); ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.a(var3);
         int var5 = var4.e("index");
         this.items[var5] = ItemStack.a(var4);
      }

   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.items.length; ++var3) {
         if(this.items[var3] != null && this.items[var3].count > 0) {
            NBTTagCompound var4 = new NBTTagCompound();
            var2.a(var4);
            var4.a("index", var3);
            this.items[var3].b(var4);
         }
      }

      var1.a("items", var2);
   }

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      return false;
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      return null;
   }

   public Packet getContentsPacket() {
      Packet230ModLoader var1 = new Packet230ModLoader();
      var1.modId = mod_BuildCraftTransport.instance.getId();
      var1.packetType = PacketIds.DiamondPipeContents.ordinal();
      var1.k = true;
      var1.dataInt = new int[3 + this.items.length * 2];
      var1.dataInt[0] = this.xCoord;
      var1.dataInt[1] = this.yCoord;
      var1.dataInt[2] = this.zCoord;

      for(int var2 = 0; var2 < this.items.length; ++var2) {
         if(this.items[var2] == null) {
            var1.dataInt[3 + var2 * 2 + 0] = -1;
            var1.dataInt[3 + var2 * 2 + 1] = -1;
         } else {
            var1.dataInt[3 + var2 * 2 + 0] = this.items[var2].id;
            var1.dataInt[3 + var2 * 2 + 1] = this.items[var2].getData();
         }
      }

      return var1;
   }

   public void handleContentsPacket(Packet230ModLoader var1) {
      if(var1.packetType == PacketIds.DiamondPipeContents.ordinal()) {
         for(int var2 = 0; var2 < this.items.length; ++var2) {
            if(var1.dataInt[3 + var2 * 2 + 0] == -1) {
               this.items[var2] = null;
            } else {
               this.items[var2] = new ItemStack(var1.dataInt[3 + var2 * 2 + 0], 1, var1.dataInt[3 + var2 * 2 + 1]);
            }
         }

      }
   }
}
