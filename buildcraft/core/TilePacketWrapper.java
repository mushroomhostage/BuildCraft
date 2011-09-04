package buildcraft.core;

import buildcraft.core.ClassMapping;
import buildcraft.core.PacketIds;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public class TilePacketWrapper {

   ClassMapping rootMapping;
   PacketIds packetType;


   public TilePacketWrapper(Class var1, PacketIds var2) {
      this.rootMapping = new ClassMapping(var1);
      this.packetType = var2;
   }

   public Packet230ModLoader toPacket(TileEntity var1) {
      Packet230ModLoader var2 = new Packet230ModLoader();
      var2.modId = mod_BuildCraftCore.instance.getId();
      var2.k = true;
      var2.packetType = this.packetType.ordinal();
      int[] var3 = this.rootMapping.getSize();
      var2.dataInt = new int[var3[0] + 3];
      var2.dataFloat = new float[var3[1]];
      var2.dataString = new String[var3[2]];
      var2.dataInt[0] = var1.x;
      var2.dataInt[1] = var1.y;
      var2.dataInt[2] = var1.z;

      try {
         this.rootMapping.setData(var1, var2.dataInt, var2.dataFloat, var2.dataString, new ClassMapping.Indexes(3, 0, 0));
         return var2;
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public void updateFromPacket(TileEntity var1, Packet230ModLoader var2) {
      try {
         this.rootMapping.updateFromData(var1, var2.dataInt, var2.dataFloat, var2.dataString, new ClassMapping.Indexes(3, 0, 0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
