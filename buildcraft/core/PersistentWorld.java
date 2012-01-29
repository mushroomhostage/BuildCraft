package buildcraft.core;

import buildcraft.core.BlockIndex;
import buildcraft.core.CoreProxy;
import buildcraft.core.PersistentTile;
import java.util.HashMap;
import java.util.TreeMap;
import net.minecraft.server.IBlockAccess;

public class PersistentWorld
{
    private static HashMap worlds = new HashMap();
    private static Long lastBlockAccess = null;
    private static PersistentWorld lastWorld = null;
    private TreeMap tiles = new TreeMap();

    public PersistentTile createTile(PersistentTile var1, BlockIndex var2)
    {
        PersistentTile var3 = null;
        if (!this.tiles.containsKey(var2))
        {
            this.tiles.put(var2, var1);
            var3 = var1;
        }
        else
        {
            var3 = (PersistentTile)this.tiles.get(var2);
            if (var3 != var1)
            {
                if (!var3.getClass().equals(var1.getClass()))
                {
                    this.tiles.remove(var2);
                    this.tiles.put(var2, var1);
                    var3.destroy();
                    var3 = var1;
                }
                else
                {
                    var1.destroy();
                }
            }
        }

        return var3;
    }

    public void storeTile(PersistentTile var1, BlockIndex var2)
    {
        if (this.tiles.containsKey(var2))
        {
            PersistentTile var3 = (PersistentTile)this.tiles.get(var2);
            if (var3 == var1)
            {
                return;
            }

            ((PersistentTile)this.tiles.remove(var2)).destroy();
        }

        this.tiles.put(var2, var1);
    }

    public PersistentTile getTile(BlockIndex var1)
    {
        return (PersistentTile)this.tiles.get(var1);
    }

    public void removeTile(BlockIndex var1)
    {
        if (this.tiles.containsKey(var1))
        {
            ((PersistentTile)this.tiles.remove(var1)).destroy();
        }
    }

    public static PersistentWorld getWorld(IBlockAccess var0)
    {
        Long var1 = Long.valueOf(CoreProxy.getHash(var0));
        if (!var1.equals(lastBlockAccess))
        {
            if (!worlds.containsKey(var1))
            {
                worlds.put(var1, new PersistentWorld());
            }

            lastBlockAccess = var1;
            lastWorld = (PersistentWorld)worlds.get(var1);
        }

        return lastWorld;
    }
}
