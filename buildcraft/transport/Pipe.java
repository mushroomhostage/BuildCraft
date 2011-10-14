package buildcraft.transport;

import buildcraft.api.Orientations;
import buildcraft.core.PacketIds;
import buildcraft.core.TilePacketWrapper;
import buildcraft.transport.PipeLogic;
import buildcraft.transport.PipeTransport;
import buildcraft.transport.TileGenericPipe;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public abstract class Pipe {

   public int xCoord;
   public int yCoord;
   public int zCoord;
   public World worldObj;
   public TileGenericPipe container;
   public final PipeTransport transport;
   public final PipeLogic logic;
   public final int itemID;
   private TilePacketWrapper networkPacket;
   private static Map networkWrappers = new HashMap();


   public Pipe(PipeTransport var1, PipeLogic var2, int var3) {
      this.transport = var1;
      this.logic = var2;
      this.itemID = var3;
      if(!networkWrappers.containsKey(this.getClass())) {
         networkWrappers.put(this.getClass(), new TilePacketWrapper(new Class[]{this.transport.getClass(), this.logic.getClass()}, PacketIds.TileUpdate));
      }

      this.networkPacket = (TilePacketWrapper)networkWrappers.get(this.getClass());
   }

   public void setPosition(int var1, int var2, int var3) {
      this.xCoord = var1;
      this.yCoord = var2;
      this.zCoord = var3;
      this.transport.setPosition(var1, var2, var3);
      this.logic.setPosition(var1, var2, var3);
   }

   public void setWorld(World var1) {
      if(var1 != null && this.worldObj == null) {
         this.worldObj = var1;
         this.transport.setWorld(var1);
         this.logic.setWorld(var1);
      }

   }

   public void setTile(TileGenericPipe var1) {
      this.container = var1;
      this.transport.setTile(var1);
      this.logic.setTile(var1);
   }

   public boolean blockActivated(World var1, int var2, int var3, int var4, EntityHuman var5) {
      return this.logic.blockActivated(var5);
   }

   public void onBlockPlaced() {
      this.logic.onBlockPlaced();
      this.transport.onBlockPlaced();
   }

   public void onNeighborBlockChange() {
      this.logic.onNeighborBlockChange();
      this.transport.onNeighborBlockChange();
   }

   public boolean isPipeConnected(TileEntity var1) {
      return this.logic.isPipeConnected(var1) && this.transport.isPipeConnected(var1);
   }

   public int getBlockTexture() {
      return 16;
   }

   public void prepareTextureFor(Orientations var1) {}

   public void updateEntity() {
      this.transport.updateEntity();
      this.logic.updateEntity();
   }

   public void writeToNBT(NBTTagCompound var1) {
      this.transport.writeToNBT(var1);
      this.logic.writeToNBT(var1);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.transport.readFromNBT(var1);
      this.logic.readFromNBT(var1);
   }

   public void initialize() {
      this.transport.initialize();
      this.logic.initialize();
   }

   public boolean inputOpen(Orientations var1) {
      return this.transport.inputOpen(var1) && this.logic.inputOpen(var1);
   }

   public boolean outputOpen(Orientations var1) {
      return this.transport.outputOpen(var1) && this.logic.outputOpen(var1);
   }

   public void onEntityCollidedWithBlock(Entity var1) {}

   public Packet230ModLoader getNetworkPacket() {
      return this.networkPacket.toPacket(this.xCoord, this.yCoord, this.zCoord, new Object[]{this.transport, this.logic});
   }

   public void handlePacket(Packet230ModLoader var1) {
      this.networkPacket.updateFromPacket(new Object[]{this.transport, this.logic}, var1);
   }

}
