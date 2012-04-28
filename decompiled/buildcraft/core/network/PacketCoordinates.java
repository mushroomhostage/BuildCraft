package buildcraft.core.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketCoordinates extends BuildCraftPacket
{
    private int id;
    public int posX;
    public int posY;
    public int posZ;

    public PacketCoordinates() {}

    public PacketCoordinates(int var1, int var2, int var3, int var4)
    {
        this.id = var1;
        this.posX = var2;
        this.posY = var3;
        this.posZ = var4;
    }

    public void writeData(DataOutputStream var1) throws IOException
    {
        var1.writeInt(this.posX);
        var1.writeInt(this.posY);
        var1.writeInt(this.posZ);
    }

    public void readData(DataInputStream var1) throws IOException
    {
        this.posX = var1.readInt();
        this.posY = var1.readInt();
        this.posZ = var1.readInt();
    }

    public int getID()
    {
        return this.id;
    }
}
