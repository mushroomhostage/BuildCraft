package buildcraft.core;

import buildcraft.core.ClassMapping;
import buildcraft.core.PacketIds;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public class TilePacketWrapper {

   ClassMapping[] rootMappings;
   PacketIds packetType;


   public TilePacketWrapper(Class var1, PacketIds var2) {
      this(new Class[]{var1}, var2);
   }

   public TilePacketWrapper(Class[] var1, PacketIds var2) {
      this.rootMappings = new ClassMapping[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.rootMappings[var3] = new ClassMapping(var1[var3]);
      }

      this.packetType = var2;
   }

   public Packet230ModLoader toPacket(TileEntity var1) {
      Packet230ModLoader var2 = new Packet230ModLoader();
      var2.modId = mod_BuildCraftCore.instance.getId();
      var2.k = true;
      var2.packetType = this.packetType.ordinal();
      int[] var3 = this.rootMappings[0].getSize();
      var2.dataInt = new int[var3[0] + 3];
      var2.dataFloat = new float[var3[1]];
      var2.dataString = new String[var3[2]];
      var2.dataInt[0] = var1.x;
      var2.dataInt[1] = var1.y;
      var2.dataInt[2] = var1.z;

      try {
         this.rootMappings[0].setData(var1, var2.dataInt, var2.dataFloat, var2.dataString, new ClassMapping.Indexes(3, 0, 0));
         return var2;
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public Packet230ModLoader toPacket(int var1, int var2, int var3, Object[] var4) {
      Packet230ModLoader var5 = new Packet230ModLoader();
      var5.modId = mod_BuildCraftCore.instance.getId();
      var5.k = true;
      var5.packetType = this.packetType.ordinal();
      int var6 = 3;
      int var7 = 0;
      int var8 = 0;

      for(int var9 = 0; var9 < this.rootMappings.length; ++var9) {
         int[] var10 = this.rootMappings[0].getSize();
         var6 += var10[0];
         var7 += var10[1];
         var8 += var10[2];
      }

      var5.dataInt = new int[var6];
      var5.dataFloat = new float[var7];
      var5.dataString = new String[var8];
      var5.dataInt[0] = var1;
      var5.dataInt[1] = var2;
      var5.dataInt[2] = var3;

      try {
         ClassMapping.Indexes var12 = new ClassMapping.Indexes(3, 0, 0);

         for(int var13 = 0; var13 < this.rootMappings.length; ++var13) {
            this.rootMappings[var13].setData(var4[var13], var5.dataInt, var5.dataFloat, var5.dataString, var12);
         }

         return var5;
      } catch (Exception var11) {
         var11.printStackTrace();
         return null;
      }
   }

   public void updateFromPacket(Object[] var1, Packet230ModLoader var2) {
      try {
         ClassMapping.Indexes var3 = new ClassMapping.Indexes(3, 0, 0);

         for(int var4 = 0; var4 < this.rootMappings.length; ++var4) {
            this.rootMappings[var4].updateFromData(var1[var4], var2.dataInt, var2.dataFloat, var2.dataString, var3);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void updateFromPacket(TileEntity var1, Packet230ModLoader var2) {
      try {
         this.rootMappings[0].updateFromData(var1, var2.dataInt, var2.dataFloat, var2.dataString, new ClassMapping.Indexes(3, 0, 0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
