package buildcraft.energy;

import buildcraft.api.APIProxy;
import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.World;

public class OilPopulate
{
    public static Random rand = null;

    public static void doPopulate(World var0, int var1, int var2)
    {
        if (BuildCraftCore.modifyWorld)
        {
            if (rand == null)
            {
                rand = APIProxy.createNewRandom(var0);
            }

            BiomeBase var3 = var0.getWorldChunkManager().getBiome(var1, var2);
            int var6;
            int var7;
            int var8;
            if (var3 == BiomeBase.DESERT && (double)rand.nextFloat() > 0.97D)
            {
                int var4 = rand.nextInt(10) + 2;
                int var5 = rand.nextInt(10) + 2;

                for (var6 = 128; var6 > 65; --var6)
                {
                    var7 = var4 + var1;
                    var8 = var5 + var2;
                    if (var0.getTypeId(var7, var6, var8) != 0)
                    {
                        if (var0.getTypeId(var7, var6, var8) == Block.SAND.id)
                        {
                            generateSurfaceDeposit(var0, var7, var6, var8, 3);
                        }
                        break;
                    }
                }
            }

            boolean var15 = rand.nextDouble() <= 0.0015D;
            boolean var16 = rand.nextDouble() <= 5.0E-5D;
            if (BuildCraftCore.debugMode && var1 == 0 && var2 == 0)
            {
                var16 = true;
            }

            if (var15 || var16)
            {
                var6 = var1;
                var7 = 20 + rand.nextInt(10);
                var8 = var2;
                int var9 = 0;
                if (var16)
                {
                    var9 = 8 + rand.nextInt(9);
                }
                else if (var15)
                {
                    var9 = 4 + rand.nextInt(4);
                }

                int var10 = var9 * var9;

                int var12;
                int var13;
                int var14;
                for (int var11 = -var9; var11 <= var9; ++var11)
                {
                    for (var12 = -var9; var12 <= var9; ++var12)
                    {
                        for (var13 = -var9; var13 <= var9; ++var13)
                        {
                            var14 = var11 * var11 + var12 * var12 + var13 * var13;
                            if (var14 <= var10)
                            {
                                var0.setTypeId(var11 + var6, var12 + var7, var13 + var8, BuildCraftEnergy.oilStill.id);
                            }
                        }
                    }
                }

                boolean var17 = false;

                for (var12 = 128; var12 >= var7; --var12)
                {
                    if (!var17 && var0.getTypeId(var6, var12, var8) != 0 && var0.getTypeId(var6, var12, var8) != Block.LEAVES.id && var0.getTypeId(var6, var12, var8) != Block.LOG.id && var0.getTypeId(var6, var12, var8) != Block.GRASS.id)
                    {
                        var17 = true;
                        if (var16)
                        {
                            generateSurfaceDeposit(var0, var6, var12, var8, 20 + rand.nextInt(20));
                        }
                        else if (var15)
                        {
                            generateSurfaceDeposit(var0, var6, var12, var8, 5 + rand.nextInt(5));
                        }

                        var13 = 0;
                        if (var16)
                        {
                            var13 = var12 + 30 < 128 ? var12 + 30 : 128;
                        }
                        else if (var15)
                        {
                            var13 = var12 + 4 < 128 ? var12 + 4 : 128;
                        }

                        for (var14 = var12 + 1; var14 <= var13; ++var14)
                        {
                            var0.setTypeId(var6, var14, var8, BuildCraftEnergy.oilStill.id);
                        }
                    }
                    else if (var17)
                    {
                        var0.setTypeId(var6, var12, var8, BuildCraftEnergy.oilStill.id);
                    }
                }
            }
        }
    }

    public static void generateSurfaceDeposit(World var0, int var1, int var2, int var3, int var4)
    {
        setOilWithProba(var0, 1.0F, var1, var2, var3, true);

        int var5;
        for (var5 = 1; var5 <= var4; ++var5)
        {
            float var6 = (float)(var4 - var5 + 4) / (float)(var4 + 4);

            for (int var7 = -var5; var7 <= var5; ++var7)
            {
                setOilWithProba(var0, var6, var1 + var7, var2, var3 + var5, false);
                setOilWithProba(var0, var6, var1 + var7, var2, var3 - var5, false);
                setOilWithProba(var0, var6, var1 + var5, var2, var3 + var7, false);
                setOilWithProba(var0, var6, var1 - var5, var2, var3 + var7, false);
            }
        }

        for (var5 = var1 - var4; var5 <= var1 + var4; ++var5)
        {
            for (int var8 = var3 - var4; var8 <= var3 + var4; ++var8)
            {
                if (var0.getTypeId(var5, var2 - 1, var8) != BuildCraftEnergy.oilStill.id && isOil(var0, var5 + 1, var2 - 1, var8) && isOil(var0, var5 - 1, var2 - 1, var8) && isOil(var0, var5, var2 - 1, var8 + 1) && isOil(var0, var5, var2 - 1, var8 - 1))
                {
                    setOilWithProba(var0, 1.0F, var5, var2, var8, true);
                }
            }
        }
    }

    private static boolean isOil(World var0, int var1, int var2, int var3)
    {
        return var0.getTypeId(var1, var2, var3) == BuildCraftEnergy.oilStill.id || var0.getTypeId(var1, var2, var3) == BuildCraftEnergy.oilMoving.id;
    }

    public static void setOilWithProba(World var0, float var1, int var2, int var3, int var4, boolean var5)
    {
        if (rand.nextFloat() <= var1 && var0.getTypeId(var2, var3 - 2, var4) != 0 || var5)
        {
            boolean var6 = false;

            for (int var7 = -1; var7 <= 1; ++var7)
            {
                if (isOil(var0, var2 + var7, var3 - 1, var4) || isOil(var0, var2 - var7, var3 - 1, var4) || isOil(var0, var2, var3 - 1, var4 + var7) || isOil(var0, var2, var3 - 1, var4 - var7))
                {
                    var6 = true;
                }
            }

            if (var6 || var5)
            {
                if (var0.getTypeId(var2, var3, var4) != Block.WATER.id && var0.getTypeId(var2, var3, var4) != Block.STATIONARY_WATER.id && !isOil(var0, var2, var3, var4))
                {
                    var0.setTypeId(var2, var3, var4, 0);
                }
                else
                {
                    var0.setTypeId(var2, var3, var4, BuildCraftEnergy.oilStill.id);
                }

                var0.setTypeId(var2, var3 - 1, var4, BuildCraftEnergy.oilStill.id);
            }
        }
    }
}
