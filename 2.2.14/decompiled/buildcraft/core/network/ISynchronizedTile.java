package buildcraft.core.network;

import net.minecraft.server.Packet;

public interface ISynchronizedTile
{
    void handleDescriptionPacket(PacketUpdate var1);

    void handleUpdatePacket(PacketUpdate var1);

    void postPacketHandling(PacketUpdate var1);

    Packet getUpdatePacket();

    /**
     * Overriden in a sign to provide the text
     */
    Packet d();

    PacketPayload getPacketPayload();
}
