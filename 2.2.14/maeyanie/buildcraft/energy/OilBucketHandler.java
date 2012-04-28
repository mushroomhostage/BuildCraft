package buildcraft.energy;

import forge.IBucketHandler;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class OilBucketHandler implements IBucketHandler
{

    public ItemStack fillCustomBucket(World var1, int var2, int var3, int var4)
    {
        if ((var1.getTypeId(var2, var3, var4) == BuildCraftEnergy.oilStill.id || var1.getTypeId(var2, var3, var4) == BuildCraftEnergy.oilMoving.id) && var1.getData(var2, var3, var4) == 0)
        {
            var1.setTypeId(var2, var3, var4, 0);
            return new ItemStack(BuildCraftEnergy.bucketOil);
        }
        else
        {
            return null;
        }
    }
}
