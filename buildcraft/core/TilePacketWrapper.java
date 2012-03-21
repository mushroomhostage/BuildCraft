package buildcraft.core;

import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public class TilePacketWrapper
{
    ClassMapping[] rootMappings;
    PacketIds packetType;

    public TilePacketWrapper(Class var1, PacketIds var2)
    {
        this(new Class[] {var1}, var2);
    }

    public TilePacketWrapper(Class[] var1, PacketIds var2)
    {
        this.rootMappings = new ClassMapping[var1.length];

        for (int var3 = 0; var3 < var1.length; ++var3)
        {
            this.rootMappings[var3] = new ClassMapping(var1[var3]);
        }

        this.packetType = var2;
    }

    public Packet230ModLoader toPacket(TileEntity var1)
    {
        Packet230ModLoader var2 = new Packet230ModLoader();
        var2.modId = mod_BuildCraftCore.instance.getId();
        var2.lowPriority = true;
        var2.packetType = this.packetType.ordinal();
        int var3 = 0;
        int var4 = 0;

        for (int var5 = 0; var5 < this.rootMappings.length; ++var5)
        {
            int[] var6 = this.rootMappings[var5].getSize();
            var3 += var6[1];
            var4 += var6[2];
        }

        var2.dataFloat = new float[var3];
        var2.dataString = new String[var4];
        ByteBuffer var8 = new ByteBuffer();
        var8.writeInt(var1.x);
        var8.writeInt(var1.y);
        var8.writeInt(var1.z);

        try
        {
            this.rootMappings[0].setData(var1, var8, var2.dataFloat, var2.dataString, new ClassMapping.Indexes(0, 0));
            var2.dataInt = var8.readIntArray();
            return var2;
        }
        catch (Exception var7)
        {
            var7.printStackTrace();
            return null;
        }
    }

    public Packet230ModLoader toPacket(int var1, int var2, int var3, Object var4)
    {
        return this.toPacket(var1, var2, var3, new Object[] {var4});
    }

    public Packet230ModLoader toPacket(int var1, int var2, int var3, Object[] var4)
    {
        Packet230ModLoader var5 = new Packet230ModLoader();
        var5.modId = mod_BuildCraftCore.instance.getId();
        var5.lowPriority = true;
        var5.packetType = this.packetType.ordinal();
        int var6 = 0;
        int var7 = 0;

        for (int var8 = 0; var8 < this.rootMappings.length; ++var8)
        {
            int[] var9 = this.rootMappings[var8].getSize();
            var6 += var9[1];
            var7 += var9[2];
        }

        var5.dataFloat = new float[var6];
        var5.dataString = new String[var7];
        ByteBuffer var12 = new ByteBuffer();
        var12.writeInt(var1);
        var12.writeInt(var2);
        var12.writeInt(var3);

        try
        {
            ClassMapping.Indexes var13 = new ClassMapping.Indexes(0, 0);

            for (int var10 = 0; var10 < this.rootMappings.length; ++var10)
            {
                this.rootMappings[var10].setData(var4[var10], var12, var5.dataFloat, var5.dataString, var13);
            }

            var5.dataInt = var12.readIntArray();
            return var5;
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
            return null;
        }
    }

    public void updateFromPacket(Object var1, Packet230ModLoader var2)
    {
        this.updateFromPacket(new Object[] {var1}, var2);
    }

    public void updateFromPacket(Object[] var1, Packet230ModLoader var2)
    {
        try
        {
            ByteBuffer var3 = new ByteBuffer();
            var3.writeIntArray(var2.dataInt);
            var3.readInt();
            var3.readInt();
            var3.readInt();
            ClassMapping.Indexes var4 = new ClassMapping.Indexes(0, 0);

            for (int var5 = 0; var5 < this.rootMappings.length; ++var5)
            {
                this.rootMappings[var5].updateFromData(var1[var5], var3, var2.dataFloat, var2.dataString, var4);
            }

            var2.dataInt = var3.readIntArray();
        }
        catch (Exception var6)
        {
            var6.printStackTrace();
        }
    }

    public void updateFromPacket(TileEntity var1, Packet230ModLoader var2)
    {
        try
        {
            ByteBuffer var3 = new ByteBuffer();
            var3.writeIntArray(var2.dataInt);
            var3.readInt();
            var3.readInt();
            var3.readInt();
            this.rootMappings[0].updateFromData(var1, var3, var2.dataFloat, var2.dataString, new ClassMapping.Indexes(0, 0));
            var2.dataInt = var3.readIntArray();
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }
}
