package buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPipeEntry;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.PowerProvider;
import buildcraft.core.BlockIndex;
import buildcraft.core.ISynchronizedTile;
import buildcraft.core.PacketIds;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public class TileGenericPipe extends TileEntity implements IPowerReceptor, ILiquidContainer, ISpecialInventory, IPipeEntry, ISynchronizedTile {

   public Pipe pipe;
   private boolean blockNeighborChange = false;
   private boolean initialized = false;


   public void b(NBTTagCompound var1) {
      super.b(var1);
      if(this.pipe != null) {
         var1.a("pipeId", this.pipe.itemID);
         this.pipe.writeToNBT(var1);
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.pipe = BlockGenericPipe.createPipe(this.x, this.y, this.z, var1.e("pipeId"));
      this.pipe.setTile(this);
      this.pipe.readFromNBT(var1);
   }
   
   public ItemStack[] getContents() {
      return new ItemStack[0];
   }

   public void n() {
      super.n();
      if(this.pipe == null) {
         this.pipe = (Pipe)BlockGenericPipe.pipeBuffer.get(new BlockIndex(this.x, this.y, this.z));
      }

      if(BlockGenericPipe.pipeBuffer.containsKey(new BlockIndex(this.x, this.y, this.z))) {
         BlockGenericPipe.pipeBuffer.remove(new BlockIndex(this.x, this.y, this.z));
      }

      if(this.pipe != null) {
         this.pipe.setTile(this);
         this.pipe.setWorld(this.world);
      }

   }

   public void h_() {
      if(BlockGenericPipe.isValid(this.pipe)) {
         if(!this.initialized) {
            this.pipe.initialize();
            this.pipe.setWorld(this.world);
            this.initialized = true;
         }

         if(this.blockNeighborChange) {
            this.pipe.onNeighborBlockChange();
            this.blockNeighborChange = false;
         }

         PowerProvider var1 = this.getPowerProvider();
         if(var1 != null) {
            var1.update(this);
         }

         this.pipe.updateEntity();
      }
   }

   public void setPowerProvider(PowerProvider var1) {
      if(this.pipe instanceof IPowerReceptor) {
         ((IPowerReceptor)this.pipe).setPowerProvider(var1);
      }

   }

   public PowerProvider getPowerProvider() {
      return this.pipe instanceof IPowerReceptor?((IPowerReceptor)this.pipe).getPowerProvider():null;
   }

   public void doWork() {
      if(this.pipe instanceof IPowerReceptor) {
         ((IPowerReceptor)this.pipe).doWork();
      }

   }

   public int fill(Orientations var1, int var2, int var3, boolean var4) {
      return this.pipe.transport instanceof ILiquidContainer?((ILiquidContainer)this.pipe.transport).fill(var1, var2, var3, var4):0;
   }

   public int empty(int var1, boolean var2) {
      return this.pipe.transport instanceof ILiquidContainer?((ILiquidContainer)this.pipe.transport).empty(var1, var2):0;
   }

   public int getLiquidQuantity() {
      return this.pipe.transport instanceof ILiquidContainer?((ILiquidContainer)this.pipe.transport).getLiquidQuantity():0;
   }

   public int getCapacity() {
      return this.pipe.transport instanceof ILiquidContainer?((ILiquidContainer)this.pipe.transport).getCapacity():0;
   }

   public int getLiquidId() {
      return this.pipe.transport instanceof ILiquidContainer?((ILiquidContainer)this.pipe.transport).getLiquidId():0;
   }

   public void scheduleNeighborChange() {
      this.blockNeighborChange = true;
   }

   public int getSize() {
      return this.pipe.logic.getSizeInventory();
   }

   public ItemStack getItem(int var1) {
      return this.pipe.logic.getStackInSlot(var1);
   }

   public ItemStack splitStack(int var1, int var2) {
      return this.pipe.logic.decrStackSize(var1, var2);
   }

   public void setItem(int var1, ItemStack var2) {
      this.pipe.logic.setInventorySlotContents(var1, var2);
   }

   public String getName() {
      return this.pipe.logic.getInvName();
   }

   public int getMaxStackSize() {
      return this.pipe.logic.getInventoryStackLimit();
   }

   public boolean a(EntityHuman var1) {
      return this.pipe.logic.canInteractWith(var1);
   }

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      return this.pipe.logic.addItem(var1, var2, var3);
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      return this.pipe.logic.extractItem(var1, var2);
   }

   public void entityEntering(EntityPassiveItem var1, Orientations var2) {
      this.pipe.transport.entityEntering(var1, var2);
   }

   public boolean acceptItems() {
      return this.pipe.transport.acceptItems();
   }

   public void handleDescriptionPacket(Packet230ModLoader var1) {
      if(this.pipe == null) {
         this.pipe = BlockGenericPipe.createPipe(this.x, this.y, this.z, var1.dataInt[3]);
         this.pipe.setTile(this);
         this.pipe.setWorld(this.world);
      }

   }

   public void handleUpdatePacket(Packet230ModLoader var1) {
      if(BlockGenericPipe.isValid(this.pipe)) {
         this.pipe.handlePacket(var1);
      }

   }

   public void postPacketHandling(Packet230ModLoader var1) {}

   public Packet230ModLoader getUpdatePacket() {
      return this.pipe.getNetworkPacket();
   }

   public Packet l() {
      Packet230ModLoader var1 = new Packet230ModLoader();
      var1.modId = mod_BuildCraftCore.instance.getId();
      var1.k = true;
      var1.packetType = PacketIds.TileDescription.ordinal();
      var1.dataInt = new int[4];
      var1.dataInt[0] = this.x;
      var1.dataInt[1] = this.y;
      var1.dataInt[2] = this.z;
      var1.dataInt[3] = this.pipe.itemID;
      return var1;
   }

   public void e() {}

   public void t_() {}

   public int powerRequest() {
      return this.getPowerProvider().maxEnergyReceived;
   }
}
