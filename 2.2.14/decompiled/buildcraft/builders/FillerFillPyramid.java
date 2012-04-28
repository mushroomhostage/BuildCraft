package buildcraft.builders;

import buildcraft.api.IBox;
import buildcraft.core.FillerPattern;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;

public class FillerFillPyramid extends FillerPattern
{
    public boolean iteratePattern(TileEntity var1, IBox var2, ItemStack var3)
    {
        int var4 = (int)var2.p1().x;
        int var5 = (int)var2.p1().y;
        int var6 = (int)var2.p1().z;
        int var7 = (int)var2.p2().x;
        int var8 = (int)var2.p2().y;
        int var9 = (int)var2.p2().z;
        int var10 = var7 - var4 + 1;
        int var11 = var9 - var6 + 1;
        int var12 = 0;
        byte var14;

        if (var1.y <= var5)
        {
            var14 = 1;
        }
        else
        {
            var14 = -1;
        }

        int var13;

        if (var14 == 1)
        {
            var13 = var5;
        }
        else
        {
            var13 = var8;
        }

        while (var12 <= var10 / 2 && var12 <= var11 / 2 && var13 >= var5 && var13 <= var8)
        {
            if (!this.fill(var4 + var12, var13, var6 + var12, var7 - var12, var13, var9 - var12, var3, var1.world))
            {
                return false;
            }

            ++var12;
            var13 += var14;
        }

        return true;
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    public int getTextureIndex()
    {
        return 71;
    }
}
