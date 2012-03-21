package buildcraft.core;

import buildcraft.api.IPowerReceptor;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public abstract class TileBuildCraft extends TileEntity implements ISynchronizedTile
{
    private static Map updateWrappers = new HashMap();
    private static Map descriptionWrappers = new HashMap();
    private TilePacketWrapper descriptionPacket;
    private TilePacketWrapper updatePacket;
    private boolean init = false;

    public TileBuildCraft()
    {
        if (!updateWrappers.containsKey(this.getClass()))
        {
            updateWrappers.put(this.getClass(), new TilePacketWrapper(this.getClass(), PacketIds.TileUpdate));
        }

        if (!descriptionWrappers.containsKey(this.getClass()))
        {
            descriptionWrappers.put(this.getClass(), new TilePacketWrapper(this.getClass(), PacketIds.TileDescription));
        }

        this.updatePacket = (TilePacketWrapper)updateWrappers.get(this.getClass());
        this.descriptionPacket = (TilePacketWrapper)descriptionWrappers.get(this.getClass());
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void q_()
    {
        if (!this.init)
        {
            this.initialize();
            this.init = true;
        }

        if (this instanceof IPowerReceptor)
        {
            IPowerReceptor var1 = (IPowerReceptor)this;
            var1.getPowerProvider().update(var1);
        }
    }

    public void initialize()
    {
        Utils.handleBufferedDescription(this);
    }

    public void destroy() {}

    public void sendNetworkUpdate()
    {
        if (this instanceof ISynchronizedTile)
        {
            CoreProxy.sendToPlayers(this.getUpdatePacket(), this.x, this.y, this.z, 50, mod_BuildCraftCore.instance);
        }
    }

    /**
     * Overriden in a sign to provide the text
     */
    public Packet d()
    {
        return this.descriptionPacket.toPacket(this);
    }

    public Packet230ModLoader getUpdatePacket()
    {
        return this.updatePacket.toPacket(this);
    }

    public void handleDescriptionPacket(Packet230ModLoader var1)
    {
        this.descriptionPacket.updateFromPacket((TileEntity)this, var1);
    }

    public void handleUpdatePacket(Packet230ModLoader var1)
    {
        this.updatePacket.updateFromPacket((TileEntity)this, var1);
    }

    public void postPacketHandling(Packet230ModLoader var1) {}
}
