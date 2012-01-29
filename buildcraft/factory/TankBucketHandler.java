package buildcraft.factory;

import buildcraft.api.API;
import buildcraft.factory.TileTank;
import forge.IBucketHandler;
import net.minecraft.server.BuildCraftFactory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class TankBucketHandler implements IBucketHandler
{
    public ItemStack fillCustomBucket(World var1, int var2, int var3, int var4)
    {
        if (var1.getTypeId(var2, var3, var4) == BuildCraftFactory.tankBlock.id)
        {
            TileTank var5 = (TileTank)var1.getTileEntity(var2, var3, var4);
            int var6 = var5.empty(1000, false);
            int var7 = API.getBucketForLiquid(var5.getLiquidId());
            if (var6 >= 1000 && var7 > 0)
            {
                var5.empty(1000, true);
                return new ItemStack(Item.byId[var7], 1);
            }
        }

        return null;
    }
}
