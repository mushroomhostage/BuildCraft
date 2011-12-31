package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.SafeTimeTracker;
import buildcraft.api.TileNetworkData;
import buildcraft.core.CoreProxy;
import buildcraft.core.PacketIds;
import buildcraft.core.TilePacketWrapper;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.PipeLogic;
import buildcraft.transport.TransportProxy;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.mod_BuildCraftTransport;

public class PipeLogicDiamond extends PipeLogic {

   ItemStack[] items = new ItemStack[54];
   private static TilePacketWrapper networkPacket;
   private SafeTimeTracker tracker = new SafeTimeTracker();


   public PipeLogicDiamond() {
      if(networkPacket == null) {
         networkPacket = new TilePacketWrapper(new Class[]{PipeLogicDiamond.PacketStack.class}, PacketIds.DiamondPipeContents);
      }

   }

   public boolean blockActivated(EntityHuman var1) {
      if(var1.P() != null && var1.P().id < Block.byId.length && Block.byId[var1.P().id] instanceof BlockGenericPipe) {
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
         for(int var4 = 0; var4 < 6; ++var4) {
            CoreProxy.sendToPlayers((Packet230ModLoader)this.getContentsPacket(var4), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
         }
      }

      return var3;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      if(this.items[var1] != null || var2 != null) {
         if(this.items[var1] == null || var2 == null || !this.items[var1].c(var2)) {
            if(var2 != null) {
               this.items[var1] = var2.cloneItemStack();
            } else {
               this.items[var1] = null;
            }

            if(APIProxy.isServerSide()) {
               for(int var3 = 0; var3 < 6; ++var3) {
                  CoreProxy.sendToPlayers((Packet230ModLoader)this.getContentsPacket(var3), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
               }
            }

         }
      }
   }

   public void updateEntity() {
      if(this.tracker.markTimeIfDelay(this.worldObj, (long)(20 * BuildCraftCore.updateFactor)) && APIProxy.isServerSide()) {
         for(int var1 = 0; var1 < 6; ++var1) {
            CoreProxy.sendToPlayers((Packet230ModLoader)this.getContentsPacket(var1), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
         }
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
      NBTTagList var2 = var1.getList("items");

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.get(var3);
         int var5 = var4.getInt("index");
         this.items[var5] = ItemStack.a(var4);
      }

   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.items.length; ++var3) {
         if(this.items[var3] != null && this.items[var3].count > 0) {
            NBTTagCompound var4 = new NBTTagCompound();
            var2.add(var4);
            var4.setInt("index", var3);
            this.items[var3].b(var4);
         }
      }

      var1.set("items", var2);
   }

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      return false;
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      return null;
   }

   public Packet getContentsPacket(int var1) {
      PipeLogicDiamond.PacketStack var2 = new PipeLogicDiamond.PacketStack();
      var2.num = var1;

      for(int var3 = 0; var3 < 9; ++var3) {
         if(this.items[var3 + var1 * 9] == null) {
            var2.ids[var3] = -1;
            var2.dmg[var3] = -1;
         } else {
            var2.ids[var3] = (short)this.items[var3 + var1 * 9].id;
            var2.dmg[var3] = this.items[var3 + var1 * 9].getData();
         }
      }

      return networkPacket.toPacket(this.xCoord, this.yCoord, this.zCoord, (Object)var2);
   }

   public void handleContentsPacket(Packet230ModLoader var1) {
      PipeLogicDiamond.PacketStack var2 = new PipeLogicDiamond.PacketStack();
      networkPacket.updateFromPacket((Object)var2, var1);
      int var3 = var2.num;

      for(int var4 = 0; var4 < 9; ++var4) {
         if(var2.ids[var4] == -1) {
            this.items[var3 * 9 + var4] = null;
         } else {
            this.items[var3 * 9 + var4] = new ItemStack(var2.ids[var4], 1, var2.dmg[var4]);
         }
      }

   }

   public class PacketStack {

      @TileNetworkData(
         intKind = 1
      )
      public int num;
      @TileNetworkData(
         staticSize = 9
      )
      public short[] ids = new short[9];
      @TileNetworkData(
         staticSize = 9,
         intKind = 1
      )
      public int[] dmg = new int[9];


   }
}
