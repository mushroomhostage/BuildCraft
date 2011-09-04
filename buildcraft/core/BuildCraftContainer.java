package buildcraft.core;

import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Slot;

public class BuildCraftContainer extends Container {

   private int inventorySize;


   public BuildCraftContainer(int var1) {
      this.inventorySize = var1;
   }

   public final ItemStack a(int var1) {
      ItemStack var2 = null;
      Slot var3 = (Slot)this.e.get(var1);
      if(var3 != null && var3.b()) {
         ItemStack var4 = var3.getItem();
         var2 = var4.cloneItemStack();
         if(var1 < this.inventorySize) {
            this.a(var4, this.inventorySize, this.e.size(), true);
         } else {
            this.a(var4, 0, this.inventorySize, false);
         }

         if(var4.count == 0) {
            var3.c((ItemStack)null);
         } else {
            var3.c();
         }
      }

      return var2;
   }

   public final boolean b(EntityHuman var1) {
      return true;
   }
}
