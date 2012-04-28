package buildcraft.core;

import java.util.Iterator;
import java.util.LinkedList;

public class ByteBuffer
{

    LinkedList bytes = new LinkedList();


    public void writeUnsignedByte(int var1)
    {
        this.bytes.add(Integer.valueOf(var1 - 127));
    }

    public void writeUnsignedShort(int var1)
    {
        this.writeUnsignedByte(var1 & 255);
        this.writeUnsignedByte(var1 >> 8 & 255);
    }

    public void writeShort(short var1)
    {
        this.writeUnsignedByte(var1 & 255);
        this.writeUnsignedByte(var1 >> 8 & 255);
    }

    public void writeInt(int var1)
    {
        this.writeUnsignedByte(var1 & 255);
        this.writeUnsignedByte(var1 >> 8 & 255);
        this.writeUnsignedByte(var1 >> 16 & 255);
        this.writeUnsignedByte(var1 >> 24 & 255);
    }

    public short readUnsignedByte()
    {
        byte var1 = 0;
        if (this.bytes.size() == 0)
        {
            return (short)var1;
        }
        else
        {
            short var2 = (short)(var1 + (short)(((Integer)this.bytes.removeFirst()).intValue() + 127));
            return var2;
        }
    }

    public int readUnsignedShort()
    {
        byte var1 = 0;
        if (this.bytes.size() == 0)
        {
            return var1;
        }
        else
        {
            int var2 = var1 + this.readUnsignedByte();
            var2 += this.readUnsignedByte() << 8;
            return var2;
        }
    }

    public short readShort()
    {
        byte var1 = 0;
        if (this.bytes.size() == 0)
        {
            return (short)var1;
        }
        else
        {
            short var2 = (short)(var1 + this.readUnsignedByte());
            var2 = (short)(var2 + (this.readUnsignedByte() << 8));
            return var2;
        }
    }

    public int readInt()
    {
        byte var1 = 0;
        if (this.bytes.size() == 0)
        {
            return var1;
        }
        else
        {
            int var2 = var1 + this.readUnsignedByte();
            var2 += this.readUnsignedByte() << 8;
            var2 += this.readUnsignedByte() << 16;
            var2 += this.readUnsignedByte() << 24;
            return var2;
        }
    }

    public int[] readIntArray()
    {
        LinkedList var1 = new LinkedList();

        while (this.bytes.size() > 0)
        {
            var1.add(Integer.valueOf(this.readInt()));
        }

        int[] var2 = new int[var1.size()];
        int var3 = 0;

        for (Iterator var4 = var1.iterator(); var4.hasNext(); ++var3)
        {
            Integer var5 = (Integer)var4.next();
            var2[var3] = var5.intValue();
        }

        return var2;
    }

    public void writeIntArray(int[] var1)
    {
        int[] var2 = var1;
        int var3 = var1.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            int var5 = var2[var4];
            this.writeInt(var5);
        }

    }
}
