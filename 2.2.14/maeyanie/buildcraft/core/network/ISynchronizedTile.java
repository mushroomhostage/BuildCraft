package buildcraft.core.network;

import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.server.Packet;

public interface ISynchronizedTile
{

    void handleDescriptionPacket(PacketUpdate var1);

    void handleUpdatePacket(PacketUpdate var1);

    void postPacketHandling(PacketUpdate var1);

    Packet getUpdatePacket();

    Packet d();

    PacketPayload getPacketPayload();
}
