package buildcraft.factory;

import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.StackUtil;
import buildcraft.core.Utils;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.Container;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;

public class TileAutoWorkbench extends TileEntity implements ISpecialInventory {

   private ItemStack[] stackList = new ItemStack[9];

   public ItemStack[] getContents() {
      return stackList;
   }

   public int getSize() {
      return this.stackList.length;
   }

   public ItemStack getItem(int var1) {
      return this.stackList[var1];
   }

   public ItemStack splitStack(int var1, int var2) {
      ItemStack var3 = this.stackList[var1].cloneItemStack();
      var3.count = var2;
      this.stackList[var1].count -= var2;
      if(this.stackList[var1].count == 0) {
         this.stackList[var1] = null;
      }

      return var3;
   }

   public void setItem(int var1, ItemStack var2) {
      this.stackList[var1] = var2;
   }

   public String getName() {
      return "";
   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean a(EntityHuman var1) {
      return true;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.l("stackList");
      this.stackList = new ItemStack[var2.c()];

      for(int var3 = 0; var3 < this.stackList.length; ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.a(var3);
         if(!var4.m("isNull")) {
            this.stackList[var3] = ItemStack.a(var4);
         }
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.stackList.length; ++var3) {
         NBTTagCompound var4 = new NBTTagCompound();
         var2.a(var4);
         if(this.stackList[var3] == null) {
            var4.a("isNull", true);
         } else {
            var4.a("isNull", false);
            this.stackList[var3].b(var4);
         }
      }

      var1.a("stackList", var2);
   }

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      StackUtil var4 = new StackUtil(var1);
      int var5 = Integer.MAX_VALUE;
      int var6 = -1;

      for(int var7 = 0; var7 < this.getSize(); ++var7) {
         ItemStack var8 = this.getItem(var7);
         if(var8 != null && var8.count > 0 && var8.id == var1.id && var8.getData() == var1.getData() && var8.count < var5) {
            var5 = var8.count;
            var6 = var7;
         }
      }

      if(var6 != -1) {
         if(var4.tryAdding(this, var6, var2, false)) {
            if(var2 && var1.count != 0) {
               this.addItem(var1, var2, var3);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      InventoryCrafting var3 = new InventoryCrafting(new Container() {
         public boolean isUsableByPlayer(EntityHuman var1) {
            return false;
         }
         public boolean b(EntityHuman var1) {
            return false;
         }
      }, 3, 3);
      LinkedList var4 = new LinkedList();

      TileAutoWorkbench.StackPointer var7;
      for(int var5 = 0; var5 < this.getSize(); ++var5) {
         ItemStack var6 = this.getItem(var5);
         if(var6 != null) {
            if(var6.count <= 1) {
               var7 = this.getNearbyItem(var6.id, var6.getData());
               if(var7 == null) {
                  this.resetPointers(var4);
                  return null;
               }

               var4.add(var7);
            } else {
               var7 = new TileAutoWorkbench.StackPointer();
               var7.inventory = this;
               var7.item = this.splitStack(var5, 1);
               var7.index = var5;
               var4.add(var7);
            }
         }

         var3.setItem(var5, var6);
      }

      ItemStack var9 = CraftingManager.getInstance().craft(var3);
      if(var9 != null && var1) {
         Iterator var10 = var4.iterator();

         while(var10.hasNext()) {
            var7 = (TileAutoWorkbench.StackPointer)var10.next();
            if(var7.item.getItem().h() != null) {
               ItemStack var8 = new ItemStack(var7.item.getItem().h(), 1);
               var7.inventory.setItem(var7.index, var8);
            }
         }
      } else {
         this.resetPointers(var4);
      }

      return var9;
   }

   public void resetPointers(LinkedList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         TileAutoWorkbench.StackPointer var3 = (TileAutoWorkbench.StackPointer)var2.next();
         ItemStack var4 = var3.inventory.getItem(var3.index);
         if(var4 == null) {
            var3.inventory.setItem(var3.index, var3.item);
         } else {
            ++var3.inventory.getItem(var3.index).count;
         }
      }

   }

   public TileAutoWorkbench.StackPointer getNearbyItem(int var1, int var2) {
      TileAutoWorkbench.StackPointer var3 = null;
      var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.XNeg);
      if(var3 == null) {
         var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.XPos);
      }

      if(var3 == null) {
         var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.YNeg);
      }

      if(var3 == null) {
         var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.YPos);
      }

      if(var3 == null) {
         var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.ZNeg);
      }

      if(var3 == null) {
         var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.ZPos);
      }

      return var3;
   }

   public TileAutoWorkbench.StackPointer getNearbyItemFromOrientation(int var1, int var2, Orientations var3) {
      Position var4 = new Position((double)this.x, (double)this.y, (double)this.z, var3);
      var4.moveForwards(1.0D);
      TileEntity var5 = this.world.getTileEntity((int)var4.x, (int)var4.y, (int)var4.z);
      if(!(var5 instanceof ISpecialInventory) && var5 instanceof IInventory) {
         IInventory var6 = Utils.getInventory((IInventory)var5);

         for(int var7 = 0; var7 < var6.getSize(); ++var7) {
            ItemStack var8 = var6.getItem(var7);
            if(var8 != null && var8.count > 0 && var8.id == var1 && var8.getData() == var2) {
               var6.splitStack(var7, 1);
               TileAutoWorkbench.StackPointer var9 = new TileAutoWorkbench.StackPointer();
               var9.inventory = var6;
               var9.index = var7;
               var9.item = var8;
               return var9;
            }
         }
      }

      return null;
   }

   public void e() {}

   public void t_() {}

   class StackPointer {

      IInventory inventory;
      int index;
      ItemStack item;


   }
}
