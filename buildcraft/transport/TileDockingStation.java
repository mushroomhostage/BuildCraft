package buildcraft.transport;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.StackUtil;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMinecart;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;

public class TileDockingStation extends TileEntity implements ILiquidContainer, ISpecialInventory {

   public int getSize() {
      return 1;
   }
   
   public ItemStack[] getContents() {
      return new ItemStack[0];
   }

   public ItemStack getItem(int var1) {
      return null;
   }

   public ItemStack splitStack(int var1, int var2) {
      return null;
   }

   public void setItem(int var1, ItemStack var2) {}

   public String getName() {
      return "DockingStation";
   }

   public int getMaxStackSize() {
      return 0;
   }

   public boolean a(EntityHuman var1) {
      return true;
   }

   public void f() {}

   public void g() {}

   private AxisAlignedBB getCheckBox(Orientations var1, int var2) {
      if(var1 == Orientations.Unknown) {
         return null;
      } else {
         Position var3 = new Position((double)this.x, (double)this.y, (double)this.z, var1);
         Position var4 = new Position((double)this.x, (double)this.y, (double)this.z, var1);
         switch(TileDockingStation.NamelessClass379028053.$SwitchMap$net$minecraft$src$buildcraft$api$Orientations[var1.ordinal()]) {
         case 1:
            var3.x += (double)var2;
            var4.x += (double)(1 + var2);
            break;
         case 2:
            var3.x -= (double)(var2 - 1);
            var4.x -= (double)var2;
            break;
         case 3:
         case 4:
            var3.x += (double)(var2 + 1);
            var4.x -= (double)var2;
            var3.z += (double)(var2 + 1);
            var4.z -= (double)var2;
            break;
         case 5:
            var3.z += (double)var2;
            var4.z += (double)(var2 + 1);
            break;
         case 6:
            var3.z -= (double)(var2 - 1);
            var4.z -= (double)var2;
         }

         switch(TileDockingStation.NamelessClass379028053.$SwitchMap$net$minecraft$src$buildcraft$api$Orientations[var1.ordinal()]) {
         case 1:
         case 2:
            var3.y += (double)(var2 + 1);
            var4.y -= (double)var2;
            var3.z += (double)(var2 + 1);
            var4.z -= (double)var2;
            break;
         case 3:
            var3.y += (double)(var2 + 1);
            var4.y += (double)var2;
            break;
         case 4:
            var3.y -= (double)(var2 - 1);
            var4.y -= (double)var2;
            break;
         case 5:
         case 6:
            var3.y += (double)(var2 + 1);
            var4.y -= (double)var2;
            var3.x += (double)(var2 + 1);
            var4.x -= (double)var2;
         }

         Position var5 = var3.min(var4);
         Position var6 = var3.max(var4);
         return AxisAlignedBB.b(var5.x, var5.y, var5.z, var6.x, var6.y, var6.z);
      }
   }

   private EntityMinecart getCart() {
      AxisAlignedBB var1 = this.getCheckBox(Orientations.YPos, 1);
      if(var1 == null) {
         return null;
      } else {
         List var2 = this.world.a(Entity.class, var1);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            if(var2.get(var3) instanceof EntityMinecart) {
               return (EntityMinecart)var2.get(var3);
            }
         }

         return null;
      }
   }

   public ItemStack checkExtractGeneric(IInventory var1, boolean var2, Orientations var3) {
      for(int var4 = 0; var4 < var1.getSize(); ++var4) {
         if(var1.getItem(var4) != null && var1.getItem(var4).count > 0) {
            ItemStack var5 = var1.getItem(var4);
            if(var5 != null && var5.count > 0) {
               if(var2) {
                  return var1.splitStack(var4, 1);
               }

               return var5;
            }
         }
      }

      return null;
   }

   public int fill(Orientations var1, int var2, int var3, boolean var4) {
      return 0;
   }

   public int empty(int var1, boolean var2) {
      return 0;
   }

   public int getLiquidQuantity() {
      return 0;
   }

   public int getCapacity() {
      return 0;
   }

   public int getLiquidId() {
      return 0;
   }

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      EntityMinecart var4 = this.getCart();
      if(var4 != null && !var4.dead && var4.type == 1) {
         StackUtil var5 = new StackUtil(var1);
         return var5.checkAvailableSlot(var4, var2, var3);
      } else {
         return false;
      }
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      EntityMinecart var3 = this.getCart();
      return var3 != null && !var3.dead && var3.type == 1?this.checkExtractGeneric(var3, var1, var2):null;
   }

   // $FF: synthetic class
   static class NamelessClass379028053 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$src$buildcraft$api$Orientations = new int[Orientations.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.XPos.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.XNeg.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.YPos.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.YNeg.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.ZPos.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$api$Orientations[Orientations.ZNeg.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
