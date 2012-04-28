package buildcraft.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.Block;
import net.minecraft.server.World;

public class API
{
    public static boolean[] softBlocks = new boolean[Block.blocksList.length];
    public static LinkedList liquids = new LinkedList();
    public static final int BUCKET_VOLUME = 1000;
    public static HashMap ironEngineFuel = new HashMap();

    public static int getLiquidForBucket(int var0)
    {
        Iterator var1 = liquids.iterator();
        LiquidData var2;

        do
        {
            if (!var1.hasNext())
            {
                return 0;
            }

            var2 = (LiquidData)var1.next();
        }
        while (var2.filledBucketId != var0);

        return var2.liquidId;
    }

    public static int getBucketForLiquid(int var0)
    {
        Iterator var1 = liquids.iterator();
        LiquidData var2;

        do
        {
            if (!var1.hasNext())
            {
                return 0;
            }

            var2 = (LiquidData)var1.next();
        }
        while (var2.liquidId != var0);

        return var2.filledBucketId;
    }

    public static boolean softBlock(int var0)
    {
        return var0 == 0 || softBlocks[var0] || Block.blocksList[var0] == null;
    }

    public static boolean unbreakableBlock(int var0)
    {
        return var0 == Block.bedrock.blockID || var0 == Block.lavaStill.blockID || var0 == Block.lavaMoving.blockID;
    }

    public static void breakBlock(World var0, int var1, int var2, int var3)
    {
        int var4 = var0.getBlockId(var1, var2, var3);

        if (var4 != 0)
        {
            Block.blocksList[var4].dropBlockAsItem(var0, var1, var2, var3, var0.getBlockMetadata(var1, var2, var3), 0);
        }

        var0.setBlockWithNotify(var1, var2, var3, 0);
    }

    static
    {
        for (int var0 = 0; var0 < softBlocks.length; ++var0)
        {
            softBlocks[var0] = false;
        }
    }
}
