package buildcraft.core;

import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;

public interface ISynchronizedTile
{
    void handleDescriptionPacket(Packet230ModLoader var1);

    void handleUpdatePacket(Packet230ModLoader var1);

    void postPacketHandling(Packet230ModLoader var1);

    Packet230ModLoader getUpdatePacket();

    /**
     * Overriden in a sign to provide the text
     */
    Packet d();
}
