package buildcraft.core.network;

import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class PacketTileUpdate extends PacketUpdate
{
    public PacketTileUpdate()
    {
        super(0);
    }

    public PacketTileUpdate(ISynchronizedTile var1)
    {
        super(0);
        this.payload = var1.getPacketPayload();
        TileEntity var2 = (TileEntity)var1;
        this.posX = var2.x;
        this.posY = var2.y;
        this.posZ = var2.z;
        this.isChunkDataPacket = true;
    }

    public boolean targetExists(World var1)
    {
        return var1.isLoaded(this.posX, this.posY, this.posZ);
    }

    public TileEntity getTarget(World var1)
    {
        return var1.getTileEntity(this.posX, this.posY, this.posZ);
    }
}
