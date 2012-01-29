package buildcraft.core;

import buildcraft.core.FillerPattern;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.Block;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;

public class FillerRegistry
{
    static LinkedList recipes = new LinkedList();

    public static void addRecipe(FillerPattern var0, Object[] var1)
    {
        String var2 = "";
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        if (var1[var3] instanceof String[])
        {
            String[] var6 = (String[])((String[])var1[var3++]);

            for (int var7 = 0; var7 < var6.length; ++var7)
            {
                String var8 = var6[var7];
                ++var5;
                var4 = var8.length();
                var2 = var2 + var8;
            }
        }
        else
        {
            while (var1[var3] instanceof String)
            {
                String var11 = (String)var1[var3++];
                ++var5;
                var4 = var11.length();
                var2 = var2 + var11;
            }
        }

        HashMap var10;
        for (var10 = new HashMap(); var3 < var1.length; var3 += 2)
        {
            Character var13 = (Character)var1[var3];
            ItemStack var14 = null;
            if (var1[var3 + 1] instanceof Item)
            {
                var14 = new ItemStack((Item)var1[var3 + 1]);
            }
            else if (var1[var3 + 1] instanceof Block)
            {
                var14 = new ItemStack((Block)var1[var3 + 1], 1, -1);
            }
            else if (var1[var3 + 1] instanceof ItemStack)
            {
                var14 = (ItemStack)var1[var3 + 1];
            }

            var10.put(var13, var14);
        }

        ItemStack[] var12 = new ItemStack[var4 * var5];

        for (int var15 = 0; var15 < var4 * var5; ++var15)
        {
            char var9 = var2.charAt(var15);
            if (var10.containsKey(Character.valueOf(var9)))
            {
                var12[var15] = ((ItemStack)var10.get(Character.valueOf(var9))).cloneItemStack();
            }
            else
            {
                var12[var15] = null;
            }
        }

        recipes.add(new FillerRegistry.ShapedPatternRecipe(var4, var5, var12, var0));
        var0.id = recipes.size();
    }

    public static FillerPattern findMatchingRecipe(IInventory var0)
    {
        for (int var1 = 0; var1 < recipes.size(); ++var1)
        {
            FillerRegistry.ShapedPatternRecipe var2 = (FillerRegistry.ShapedPatternRecipe)recipes.get(var1);
            if (var2.matches(var0))
            {
                return var2.recipeOutput;
            }
        }

        return null;
    }

    public static int getPatternNumber(FillerPattern var0)
    {
        int var1 = 0;

        for (Iterator var2 = recipes.iterator(); var2.hasNext(); ++var1)
        {
            FillerRegistry.ShapedPatternRecipe var3 = (FillerRegistry.ShapedPatternRecipe)var2.next();
            if (var3.recipeOutput == var0)
            {
                return var1;
            }
        }

        return -1;
    }

    public static FillerPattern getPattern(int var0)
    {
        return var0 <= 0 ? null : ((FillerRegistry.ShapedPatternRecipe)recipes.get(var0 - 1)).recipeOutput;
    }

    static class ShapedPatternRecipe
    {
        private int recipeWidth;
        private int recipeHeight;
        private ItemStack[] recipeItems;
        private FillerPattern recipeOutput;

        public ShapedPatternRecipe(int var1, int var2, ItemStack[] var3, FillerPattern var4)
        {
            this.recipeWidth = var1;
            this.recipeHeight = var2;
            this.recipeItems = var3;
            this.recipeOutput = var4;
        }

        public boolean matches(IInventory var1)
        {
            for (int var2 = 0; var2 <= 3 - this.recipeWidth; ++var2)
            {
                for (int var3 = 0; var3 <= 3 - this.recipeHeight; ++var3)
                {
                    if (this.func_21137_a(var1, var2, var3, true))
                    {
                        return true;
                    }

                    if (this.func_21137_a(var1, var2, var3, false))
                    {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean func_21137_a(IInventory var1, int var2, int var3, boolean var4)
        {
            for (int var5 = 0; var5 < 3; ++var5)
            {
                for (int var6 = 0; var6 < 3; ++var6)
                {
                    int var7 = var5 - var2;
                    int var8 = var6 - var3;
                    ItemStack var9 = null;
                    if (var7 >= 0 && var8 >= 0 && var7 < this.recipeWidth && var8 < this.recipeHeight)
                    {
                        if (var4)
                        {
                            var9 = this.recipeItems[this.recipeWidth - var7 - 1 + var8 * this.recipeWidth];
                        }
                        else
                        {
                            var9 = this.recipeItems[var7 + var8 * this.recipeWidth];
                        }
                    }

                    ItemStack var10 = var1.getItem(var5 + var6 * 3);
                    if (var10 != null || var9 != null)
                    {
                        if (var10 == null && var9 != null || var10 != null && var9 == null)
                        {
                            return false;
                        }

                        if (var9.id != var10.id)
                        {
                            return false;
                        }

                        if (var9.getData() != -1 && var9.getData() != var10.getData())
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }
}
