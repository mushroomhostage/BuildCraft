package buildcraft.core;

import net.minecraft.server.TileEntity;

public abstract class PersistentTile
{
    public TileEntity tile;
    public BlockIndex index;

    public void setTile(TileEntity var1)
    {
        this.tile = var1;
        this.index = new BlockIndex(var1.xCoord, var1.yCoord, var1.zCoord);
    }

    public void destroy() {}

    public boolean isValid()
    {
        return this.tile != null && !this.tile.isInvalid();
    }
}
