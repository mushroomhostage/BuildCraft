package buildcraft.api;

import buildcraft.api.LiquidData;
import buildcraft.core.FakePlayer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.Block;
import net.minecraft.server.World;
import org.bukkit.event.block.BlockBreakEvent;

public class API
{

    public static boolean[] softBlocks = new boolean[Block.byId.length];
    public static LinkedList liquids = new LinkedList();
    public static final int BUCKET_VOLUME = 1000;
    public static HashMap ironEngineFuel = new HashMap();


    public static int getLiquidForBucket(int var0)
    {
        Iterator var1 = liquids.iterator();

        while (var1.hasNext())
        {
            LiquidData var2 = (LiquidData)var1.next();
            if (var2.filledBucketId == var0)
            {
                return var2.liquidId;
            }
        }

        return 0;
    }

    public static int getBucketForLiquid(int var0)
    {
        Iterator var1 = liquids.iterator();

        while (var1.hasNext())
        {
            LiquidData var2 = (LiquidData)var1.next();
            if (var2.liquidId == var0)
            {
                return var2.filledBucketId;
            }
        }

        return 0;
    }

    public static boolean softBlock(int var0)
    {
        return var0 == 0 || softBlocks[var0] || Block.byId[var0] == null;
    }

    public static boolean unbreakableBlock(int var0)
    {
        return var0 == Block.BEDROCK.id || var0 == Block.STATIONARY_LAVA.id || var0 == Block.LAVA.id;
    }

    public static void breakBlock(World var0, int var1, int var2, int var3)
    {
        int var4 = var0.getTypeId(var1, var2, var3);
        if (var4 != 0)
        {
            org.bukkit.block.Block var5 = var0.getWorld().getBlockAt(var1, var2, var3);
            BlockBreakEvent var6 = new BlockBreakEvent(var5, FakePlayer.getBukkitEntity(var0));
            var0.getServer().getPluginManager().callEvent(var6);
            if (var6.isCancelled())
            {
                return;
            }

            Block.byId[var4].b(var0, var1, var2, var3, var0.getData(var1, var2, var3), 0);
        }

        var0.setTypeId(var1, var2, var3, 0);
    }

    static
    {
        for (int var0 = 0; var0 < softBlocks.length; ++var0)
        {
            softBlocks[var0] = false;
        }

    }
}
