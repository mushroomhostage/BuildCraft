package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.EntityPassiveItem;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerProvider;
import buildcraft.core.Utils;
import buildcraft.transport.TilePipe;
import buildcraft.transport.TransportProxy;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityMinecart;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;

public class TileObsidianPipe extends TilePipe implements IPowerReceptor {

   private PowerProvider powerProvider;
   private int[] entitiesDropped = new int[32];
   private int entitiesDroppedIndex = 0;


   public TileObsidianPipe() {
      for(int var1 = 0; var1 < this.entitiesDropped.length; ++var1) {
         this.entitiesDropped[var1] = -1;
      }

      this.powerProvider = BuildCraftCore.powerFramework.createPowerProvider();
      this.powerProvider.configure(25, 1, 64, 1, 256);
      this.powerProvider.configurePowerPerdition(1, 1);
   }

   public Orientations getSuckingOrientation() {
      Position var1 = new Position((double)this.x, (double)this.y, (double)this.z);
      int var2 = 0;
      Position var3 = new Position(var1);

      for(int var4 = 0; var4 <= 5; ++var4) {
         Position var5 = new Position(var1);
         var5.orientation = Orientations.values()[var4];
         var5.moveForwards(1.0D);
         if(Utils.checkPipesConnections(this.world, (int)var5.x, (int)var5.y, (int)var5.z, this.x, this.y, this.z)) {
            ++var2;
            if(var2 == 1) {
               var3 = new Position(var5);
            }
         }
      }

      if(var2 <= 1 && var2 != 0) {
         return var3.orientation.reverse();
      } else {
         return Orientations.Unknown;
      }
   }

   private AxisAlignedBB getSuckingBox(Orientations var1, int var2) {
      if(var1 == Orientations.Unknown) {
         return null;
      } else {
         Position var3 = new Position((double)this.x, (double)this.y, (double)this.z, var1);
         Position var4 = new Position((double)this.x, (double)this.y, (double)this.z, var1);
         switch(TileObsidianPipe.NamelessClass995489607.$SwitchMap$net$minecraft$src$buildcraft$api$Orientations[var1.ordinal()]) {
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

         switch(TileObsidianPipe.NamelessClass995489607.$SwitchMap$net$minecraft$src$buildcraft$api$Orientations[var1.ordinal()]) {
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

   public void doWork() {
      for(int var1 = 1; var1 < 5; ++var1) {
         if(this.trySucc(var1)) {
            return;
         }
      }

      this.powerProvider.useEnergy(1, 1, true);
   }

   private boolean trySucc(int var1) {
      AxisAlignedBB var2 = this.getSuckingBox(this.getSuckingOrientation(), var1);
      if(var2 == null) {
         return false;
      } else {
         List var3 = this.world.a(Entity.class, var2);

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            if(var3.get(var4) instanceof Entity) {
               Entity var5 = (Entity)var3.get(var4);
               if(this.canSuck(var5, var1)) {
                  this.pullItemIntoPipe(var5, var1);
                  return true;
               }

               if(var1 == 1 && var3.get(var4) instanceof EntityMinecart) {
                  EntityMinecart var6 = (EntityMinecart)var3.get(var4);
                  if(!var6.dead && var6.type == 1) {
                     ItemStack var7 = this.checkExtractGeneric(var6, true, this.getSuckingOrientation().reverse());
                     if(var7 != null && this.powerProvider.useEnergy(1, 1, true) == 1) {
                        EntityItem var8 = new EntityItem(this.world, var6.locX, var6.locY + 0.30000001192092896D, var6.locZ, var7);
                        var8.pickupDelay = 10;
                        this.world.addEntity(var8);
                        this.pullItemIntoPipe(var8, 1);
                        return true;
                     }
                  }
               }
            }
         }

         return false;
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

   public void pullItemIntoPipe(Entity var1, int var2) {
      if(!APIProxy.isClient(this.world)) {
         Orientations var3 = this.getSuckingOrientation();
         if(var3 != Orientations.Unknown) {
            this.world.makeSound(var1, "random.pop", 0.2F, ((this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            ItemStack var4 = null;
            if(var1 instanceof EntityItem) {
               EntityItem var5 = (EntityItem)var1;
               TransportProxy.obsidianPipePickup(this.world, var5, this);
               int var6 = this.powerProvider.useEnergy(var2, var5.itemStack.count * var2, true);
               if(var2 != 0 && var6 / var2 != var5.itemStack.count) {
                  var4 = var5.itemStack.a(var6 / var2);
               } else {
                  var4 = var5.itemStack;
                  APIProxy.removeEntity(var1);
               }
            } else if(var1 instanceof EntityArrow) {
               this.powerProvider.useEnergy(var2, var2, true);
               var4 = new ItemStack(Item.ARROW, 1);
               APIProxy.removeEntity(var1);
            }

            EntityPassiveItem var7 = new EntityPassiveItem(this.world, (double)this.x + 0.5D, (double)((float)this.y + Utils.getPipeFloorOf(var4)), (double)this.z + 0.5D, var4);
            this.entityEntering(var7, var3.reverse());
         }

      }
   }

   public void onDropped(EntityItem var1) {
      if(this.entitiesDroppedIndex + 1 >= this.entitiesDropped.length) {
         this.entitiesDroppedIndex = 0;
      } else {
         ++this.entitiesDroppedIndex;
      }

      this.entitiesDropped[this.entitiesDroppedIndex] = var1.id;
   }

   public boolean canSuck(Entity var1, int var2) {
      if(var1.dead) {
         return false;
      } else if(var1 instanceof EntityItem) {
         EntityItem var3 = (EntityItem)var1;

         for(int var4 = 0; var4 < this.entitiesDropped.length; ++var4) {
            if(var3.id == this.entitiesDropped[var4]) {
               return false;
            }
         }

         return this.powerProvider.useEnergy(1, var2, false) >= var2;
      } else {
         return var1 instanceof EntityArrow?this.powerProvider.useEnergy(1, var2, false) >= var2:false;
      }
   }

   public void setPowerProvider(PowerProvider var1) {
      this.powerProvider = var1;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   // $FF: synthetic class
   static class NamelessClass995489607 {

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
