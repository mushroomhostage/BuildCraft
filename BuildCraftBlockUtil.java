package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class BuildCraftBlockUtil {

   public static int damageDropped(World var0, int var1, int var2, int var3) {
      Block var4 = Block.byId[var0.getTypeId(var1, var2, var3)];
      return var4.a_(var0.getData(var1, var2, var3));
   }

   public static ItemStack getItemStackFromBlock(World var0, int var1, int var2, int var3) {
      Block var4 = Block.byId[var0.getTypeId(var1, var2, var3)];
      if(var4 == null) {
         return null;
      } else {
         int var5 = var0.getData(var1, var2, var3);
         int var6 = var4.a(var5, var0.random);
         int var7 = var4.quantityDropped(var5, var0.random);
         int var8 = var4.a_(var5);
         return var6 > 0 && var7 != 0?new ItemStack(var6, var7, var8):null;
      }
   }
}
