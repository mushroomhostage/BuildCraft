package buildcraft.core.network;

import forge.packets.ForgePacket;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet250CustomPayload;

public abstract class BuildCraftPacket extends ForgePacket
{
    protected boolean isChunkDataPacket = false;
    protected String channel = "BC";

    public Packet getPacket()
    {
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        DataOutputStream var2 = new DataOutputStream(var1);

        try
        {
            var2.writeByte(this.getID());
            this.writeData(var2);
        }
        catch (IOException var4)
        {
            var4.printStackTrace();
        }

        Packet250CustomPayload var3 = new Packet250CustomPayload();
        var3.tag = this.channel;
        var3.data = var1.toByteArray();
        var3.length = var3.data.length;
        var3.lowPriority = this.isChunkDataPacket;
        return var3;
    }
}
