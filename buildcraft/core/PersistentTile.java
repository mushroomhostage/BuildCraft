package buildcraft.core;

import buildcraft.core.BlockIndex;
import net.minecraft.server.TileEntity;

public abstract class PersistentTile
{
    public TileEntity tile;
    public BlockIndex index;

    public void setTile(TileEntity var1)
    {
        this.tile = var1;
        this.index = new BlockIndex(var1.x, var1.y, var1.z);
    }

    public void destroy() {}

    public boolean isValid()
    {
        return this.tile != null && !this.tile.l();
    }
}
