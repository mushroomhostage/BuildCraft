package buildcraft.builders;

import net.minecraft.server.Block;
import net.minecraft.server.World;

public class BuildersProxy
{

    public static boolean canPlaceTorch(World var0, int var1, int var2, int var3)
    {
        Block var4 = Block.byId[var0.getTypeId(var1, var2, var3)];
        return var4 != null && var4.b();
    }
}
