package buildcraft.core;

import buildcraft.api.API;
import buildcraft.api.IBox;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public abstract class FillerPattern
{

    public int id;


    public abstract boolean iteratePattern(TileEntity var1, IBox var2, ItemStack var3);

    public abstract String getTextureFile();

    public abstract int getTextureIndex();

    public boolean fill(int var1, int var2, int var3, int var4, int var5, int var6, ItemStack var7, World var8)
    {
        boolean var9 = false;
        int var10 = 0;
        int var11 = 0;
        int var12 = 0;

        for (int var13 = var2; var13 <= var5 && !var9; ++var13)
        {
            for (int var14 = var1; var14 <= var4 && !var9; ++var14)
            {
                for (int var15 = var3; var15 <= var6 && !var9; ++var15)
                {
                    if (API.softBlock(var8.getTypeId(var14, var13, var15)))
                    {
                        var10 = var14;
                        var11 = var13;
                        var12 = var15;
                        var9 = true;
                    }
                }
            }
        }

        if (var9 && var7 != null)
        {
            var7.getItem().interactWith(var7, BuildCraftCore.getBuildCraftPlayer(var8), var8, var10, var11 + 1, var12, 0);
        }

        return !var9;
    }

    public boolean empty(int var1, int var2, int var3, int var4, int var5, int var6, World var7)
    {
        boolean var8 = false;
        int var9 = Integer.MAX_VALUE;
        int var10 = Integer.MAX_VALUE;
        int var11 = Integer.MAX_VALUE;

        for (int var12 = var2; var12 <= var5; ++var12)
        {
            var8 = false;

            for (int var13 = var1; var13 <= var4; ++var13)
            {
                for (int var14 = var3; var14 <= var6; ++var14)
                {
                    if (!API.softBlock(var7.getTypeId(var13, var12, var14)) && !API.unbreakableBlock(var7.getTypeId(var13, var12, var14)))
                    {
                        var8 = true;
                        var9 = var13;
                        var10 = var12;
                        var11 = var14;
                    }
                }
            }

            if (var8)
            {
                break;
            }
        }

        if (var9 != Integer.MAX_VALUE)
        {
            API.breakBlock(var7, var9, var10, var11);
        }

        return var9 == Integer.MAX_VALUE;
    }
}
