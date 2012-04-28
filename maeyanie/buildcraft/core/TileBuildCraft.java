package buildcraft.core;

import buildcraft.api.IPowerReceptor;
import buildcraft.core.CoreProxy;
import buildcraft.core.Utils;
import buildcraft.core.network.ISynchronizedTile;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketTileUpdate;
import buildcraft.core.network.PacketUpdate;
import buildcraft.core.network.TilePacketWrapper;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.Packet;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public abstract class TileBuildCraft extends TileEntity implements ISynchronizedTile {

   private static Map updateWrappers = new HashMap();
   private static Map descriptionWrappers = new HashMap();
   private TilePacketWrapper descriptionPacket;
   private TilePacketWrapper updatePacket;
   private boolean init = false;


   public TileBuildCraft() {
      if(!updateWrappers.containsKey(this.getClass())) {
         updateWrappers.put(this.getClass(), new TilePacketWrapper(this.getClass()));
      }

      if(!descriptionWrappers.containsKey(this.getClass())) {
         descriptionWrappers.put(this.getClass(), new TilePacketWrapper(this.getClass()));
      }

      this.updatePacket = (TilePacketWrapper)updateWrappers.get(this.getClass());
      this.descriptionPacket = (TilePacketWrapper)descriptionWrappers.get(this.getClass());
   }

   public void q_() {
      if(!this.init) {
         this.initialize();
         this.init = true;
      }

      if(this instanceof IPowerReceptor) {
         IPowerReceptor var1 = (IPowerReceptor)this;
         var1.getPowerProvider().update(var1);
      }

   }

   public void initialize() {
      Utils.handleBufferedDescription(this);
   }

   public void destroy() {}

   public void sendNetworkUpdate() {
      if(this instanceof ISynchronizedTile) {
         CoreProxy.sendToPlayers(this.getUpdatePacket(), this.x, this.y, this.z, 50, mod_BuildCraftCore.instance);
      }

   }

   public Packet d() {
      return (new PacketTileUpdate(this)).getPacket();
   }

   public PacketPayload getPacketPayload() {
      return this.updatePacket.toPayload(this);
   }

   public Packet getUpdatePacket() {
      return (new PacketTileUpdate(this)).getPacket();
   }

   public void handleDescriptionPacket(PacketUpdate var1) {
      this.descriptionPacket.fromPayload((TileEntity)this, var1.payload);
   }

   public void handleUpdatePacket(PacketUpdate var1) {
      this.updatePacket.fromPayload((TileEntity)this, var1.payload);
   }

   public void postPacketHandling(PacketUpdate var1) {}

}
