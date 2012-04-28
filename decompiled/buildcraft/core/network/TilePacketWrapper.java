package buildcraft.core.network;

import buildcraft.core.ByteBuffer;
import buildcraft.core.ClassMapping;
import net.minecraft.server.TileEntity;

public class TilePacketWrapper
{
    ClassMapping[] rootMappings;

    public TilePacketWrapper(Class var1)
    {
        this(new Class[] {var1});
    }

    public TilePacketWrapper(Class[] var1)
    {
        this.rootMappings = new ClassMapping[var1.length];

        for (int var2 = 0; var2 < var1.length; ++var2)
        {
            this.rootMappings[var2] = new ClassMapping(var1[var2]);
        }
    }

    public PacketPayload toPayload(TileEntity var1)
    {
        int var2 = 0;
        int var3 = 0;

        for (int var4 = 0; var4 < this.rootMappings.length; ++var4)
        {
            int[] var5 = this.rootMappings[var4].getSize();
            var2 += var5[1];
            var3 += var5[2];
        }

        PacketPayload var8 = new PacketPayload(0, var2, var3);
        ByteBuffer var9 = new ByteBuffer();
        var9.writeInt(var1.x);
        var9.writeInt(var1.y);
        var9.writeInt(var1.z);

        try
        {
            this.rootMappings[0].setData(var1, var9, var8.floatPayload, var8.stringPayload, new ClassMapping.Indexes(0, 0));
            var8.intPayload = var9.readIntArray();
            return var8;
        }
        catch (Exception var7)
        {
            var7.printStackTrace();
            return null;
        }
    }

    public PacketPayload toPayload(int var1, int var2, int var3, Object var4)
    {
        return this.toPayload(var1, var2, var3, new Object[] {var4});
    }

    public PacketPayload toPayload(int var1, int var2, int var3, Object[] var4)
    {
        int var5 = 0;
        int var6 = 0;

        for (int var7 = 0; var7 < this.rootMappings.length; ++var7)
        {
            int[] var8 = this.rootMappings[var7].getSize();
            var5 += var8[1];
            var6 += var8[2];
        }

        PacketPayload var12 = new PacketPayload(0, var5, var6);
        ByteBuffer var13 = new ByteBuffer();
        var13.writeInt(var1);
        var13.writeInt(var2);
        var13.writeInt(var3);

        try
        {
            ClassMapping.Indexes var9 = new ClassMapping.Indexes(0, 0);

            for (int var10 = 0; var10 < this.rootMappings.length; ++var10)
            {
                this.rootMappings[var10].setData(var4[var10], var13, var12.floatPayload, var12.stringPayload, var9);
            }

            var12.intPayload = var13.readIntArray();
            return var12;
        }
        catch (Exception var11)
        {
            var11.printStackTrace();
            return null;
        }
    }

    public void fromPayload(TileEntity var1, PacketPayload var2)
    {
        try
        {
            ByteBuffer var3 = new ByteBuffer();
            var3.writeIntArray(var2.intPayload);
            var3.readInt();
            var3.readInt();
            var3.readInt();
            this.rootMappings[0].updateFromData(var1, var3, var2.floatPayload, var2.stringPayload, new ClassMapping.Indexes(0, 0));
            var2.intPayload = var3.readIntArray();
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }

    public void fromPayload(Object var1, PacketPayload var2)
    {
        this.fromPayload(new Object[] {var1}, var2);
    }

    public void fromPayload(Object[] var1, PacketPayload var2)
    {
        try
        {
            ByteBuffer var3 = new ByteBuffer();
            var3.writeIntArray(var2.intPayload);
            var3.readInt();
            var3.readInt();
            var3.readInt();
            ClassMapping.Indexes var4 = new ClassMapping.Indexes(0, 0);

            for (int var5 = 0; var5 < this.rootMappings.length; ++var5)
            {
                this.rootMappings[var5].updateFromData(var1[var5], var3, var2.floatPayload, var2.stringPayload, var4);
            }

            var2.intPayload = var3.readIntArray();
        }
        catch (Exception var6)
        {
            var6.printStackTrace();
        }
    }
}
