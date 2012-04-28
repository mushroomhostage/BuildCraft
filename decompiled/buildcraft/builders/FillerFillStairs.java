package buildcraft.builders;

import buildcraft.api.IBox;
import buildcraft.core.FillerPattern;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;

public class FillerFillStairs extends FillerPattern
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
        int var14 = 0;
        int var15 = 0;
        int var12;
        byte var13;

        if (var1.yCoord <= var5)
        {
            var12 = var5;
            var13 = 1;
        }
        else
        {
            var12 = var8;
            var13 = -1;
        }

        boolean var16 = false;
        int[] var17 = new int[] {0, 0, 0, 0};
        int var18 = 0;
        int var19 = 0;
        byte var20 = 0;
        byte var21 = 0;

        if (var1.xCoord == var4 - 1)
        {
            var17[0] = 1;
        }
        else if (var1.xCoord == var7 + 1)
        {
            var17[1] = 1;
        }
        else if (var1.zCoord == var6 - 1)
        {
            var17[2] = 1;
        }
        else if (var1.zCoord == var9 + 1)
        {
            var17[3] = 1;
        }
        else
        {
            var16 = true;

            if (var1.xCoord <= var4)
            {
                var18 = var4;
            }
            else if (var1.xCoord >= var7)
            {
                var18 = var7;
            }

            if (var1.zCoord <= var6)
            {
                var19 = var6;
            }
            else if (var1.zCoord >= var9)
            {
                var19 = var9;
            }

            if (var13 == 1)
            {
                var20 = -1;
                var14 = var10 - 1;
                var21 = -1;
                var15 = var11 - 1;
            }
            else
            {
                var20 = 1;
                var14 = 0;
                var21 = 1;
                var15 = 0;
            }
        }

        boolean var22 = false;
        boolean var23 = false;
        boolean var24 = false;
        boolean var25 = false;
        int var26 = var4;
        int var27 = var7;
        int var29 = var6;
        int var28 = var9;

        if (var13 == -1)
        {
            if (var17[0] == 1)
            {
                var26 = var7 - var10 + 1;
                var27 = var26;
            }

            if (var17[1] == 1)
            {
                var27 = var4 + var10 - 1;
                var26 = var27;
            }

            if (var17[2] == 1)
            {
                var29 = var9 - var11 + 1;
                var28 = var29;
            }

            if (var17[3] == 1)
            {
                var28 = var6 + var11 - 1;
                var29 = var28;
            }
        }

        if (!var16)
        {
            for (; var27 - var26 + 1 > 0 && var28 - var29 + 1 > 0 && var27 - var26 < var10 && var28 - var29 < var11 && var12 >= var5 && var12 <= var8; var12 += var13)
            {
                if (!this.fill(var26, var12, var29, var27, var12, var28, var3, var1.worldObj))
                {
                    return false;
                }

                if (var13 == 1)
                {
                    var26 += var17[0];
                    var27 -= var17[1];
                    var29 += var17[2];
                    var28 -= var17[3];
                }
                else
                {
                    var27 += var17[0];
                    var26 -= var17[1];
                    var28 += var17[2];
                    var29 -= var17[3];
                }
            }
        }
        else if (var16)
        {
            while (var14 >= 0 && var14 < var10 && var15 >= 0 && var15 < var11 && var12 >= var5 && var12 <= var8)
            {
                if (var13 == 1)
                {
                    if (var1.xCoord >= var7)
                    {
                        var26 = var18 - var10 + 1;
                        var27 = var26 + var14;
                    }
                    else
                    {
                        var27 = var18 + var10 - 1;
                        var26 = var27 - var14;
                    }

                    if (var1.zCoord >= var9)
                    {
                        var29 = var19 - var11 + 1;
                        var28 = var29 + var15;
                    }
                    else
                    {
                        var28 = var19 + var11 - 1;
                        var29 = var28 - var15;
                    }
                }
                else if (var13 == -1)
                {
                    if (var1.xCoord <= var4)
                    {
                        var26 = var18;
                        var27 = var18 + var14;
                    }
                    else
                    {
                        var27 = var18;
                        var26 = var18 - var14;
                    }

                    if (var1.zCoord <= var6)
                    {
                        var29 = var19;
                        var28 = var19 + var15;
                    }
                    else
                    {
                        var28 = var19;
                        var29 = var19 - var15;
                    }
                }

                if (!this.fill(var26, var12, var29, var27, var12, var28, var3, var1.worldObj))
                {
                    return false;
                }

                var14 += var20;
                var15 += var21;
                var12 += var13;
            }
        }

        return true;
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    public int getTextureIndex()
    {
        return 72;
    }
}
