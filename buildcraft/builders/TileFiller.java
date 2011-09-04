package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.api.FillerPattern;
import buildcraft.api.FillerRegistry;
import buildcraft.api.IAreaProvider;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.LaserKind;
import buildcraft.api.Orientations;
import buildcraft.api.PowerProvider;
import buildcraft.builders.TileMarker;
import buildcraft.core.Box;
import buildcraft.core.StackUtil;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.TileNetworkData;
import buildcraft.core.Utils;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet230ModLoader;

public class TileFiller extends TileBuildCraft implements ISpecialInventory, IPowerReceptor {

   @TileNetworkData
   public Box box = new Box();
   @TileNetworkData
   public int currentPatternId = 0;
   @TileNetworkData
   public boolean done = true;
   FillerPattern currentPattern;
   boolean forceDone = false;
   private ItemStack[] contents = new ItemStack[this.getSize()];
   PowerProvider powerProvider;


   public TileFiller() {
      this.powerProvider = BuildCraftCore.powerFramework.createPowerProvider();
      this.powerProvider.configure(10, 25, 25, 25, 25);
      this.powerProvider.configurePowerPerdition(25, 1);
   }
   
   public ItemStack[] getContents() {
      return contents;
   }

   public void initialize() {
      super.initialize();
      if(!APIProxy.isClient(this.world)) {
         IAreaProvider var1 = Utils.getNearbyAreaProvider(this.world, this.x, this.y, this.z);
         if(var1 != null) {
            this.box.initialize(var1);
            if(var1 instanceof TileMarker) {
               ((TileMarker)var1).removeFromWorld();
            }

            this.sendNetworkUpdate();
         }
      }

      this.computeRecipe();
   }

   public void g_() {
      super.g_();
      if(this.box.isInitialized()) {
         this.box.createLasers(this.world, LaserKind.Stripes);
      } else {
         this.done = true;
      }

   }

   public void doWork() {
      if(!APIProxy.isClient(this.world)) {
         if(this.powerProvider.useEnergy(25, 25, true) >= 25) {
            if(this.box.isInitialized() && this.currentPattern != null && !this.done) {
               ItemStack var1 = null;
               int var2 = 0;

               for(int var3 = 9; var3 < this.getSize(); ++var3) {
                  if(this.getItem(var3) != null && this.getItem(var3).count > 0 && this.getItem(var3).getItem() instanceof ItemBlock) {
                     var1 = this.contents[var3];
                     var2 = var3;
                     break;
                  }
               }

               this.done = this.currentPattern.iteratePattern(this, this.box, var1);
               if(var1 != null && var1.count == 0) {
                  this.contents[var2] = null;
               }

               if(this.done) {
                  this.world.i(this.x, this.y, this.z);
                  this.sendNetworkUpdate();
               }
            }

         }
      }
   }

   public int getSize() {
      return 36;
   }

   public ItemStack getItem(int var1) {
      return this.contents[var1];
   }

   public void computeRecipe() {
      if(!APIProxy.isClient(this.world)) {
         FillerPattern var1 = FillerRegistry.findMatchingRecipe(this);
         if(var1 != this.currentPattern) {
            this.currentPattern = var1;
            if(this.currentPattern != null && !this.forceDone) {
               this.done = false;
            } else {
               this.done = true;
               this.forceDone = false;
            }

            if(this.world != null) {
               this.world.i(this.x, this.y, this.z);
            }

            if(this.currentPattern == null) {
               this.currentPatternId = 0;
            } else {
               this.currentPatternId = this.currentPattern.id;
            }

            if(APIProxy.isServerSide()) {
               this.sendNetworkUpdate();
            }

         }
      }
   }

   public ItemStack splitStack(int var1, int var2) {
      if(this.contents[var1] != null) {
         ItemStack var3;
         if(this.contents[var1].count <= var2) {
            var3 = this.contents[var1];
            this.contents[var1] = null;
            this.computeRecipe();
            return var3;
         } else {
            var3 = this.contents[var1].a(var2);
            if(this.contents[var1].count == 0) {
               this.contents[var1] = null;
            }

            this.computeRecipe();
            return var3;
         }
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      this.contents[var1] = var2;
      if(var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

      this.computeRecipe();
   }

   public String getName() {
      return "Filler";
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.l("Items");
      this.contents = new ItemStack[this.getSize()];

      for(int var3 = 0; var3 < var2.c(); ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.a(var3);
         int var5 = var4.c("Slot") & 255;
         if(var5 >= 0 && var5 < this.contents.length) {
            this.contents[var5] = new ItemStack(var4);
         }
      }

      if(var1.hasKey("box")) {
         this.box.initialize(var1.k("box"));
      }

      this.done = var1.m("done");
      this.forceDone = this.done;
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.contents.length; ++var3) {
         if(this.contents[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.a("Slot", (byte)var3);
            this.contents[var3].a(var4);
            var2.a(var4);
         }
      }

      var1.a("Items", var2);
      if(this.box != null) {
         NBTTagCompound var5 = new NBTTagCompound();
         this.box.writeToNBT(var5);
         var1.a("box", var5);
      }

      var1.a("done", this.done);
   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean a_(EntityHuman var1) {
      return this.world.getTileEntity(this.x, this.y, this.z) != this?false:var1.e((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D) <= 64.0D;
   }

   public void destroy() {
      if(this.box != null) {
         this.box.deleteLasers();
      }

   }

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      StackUtil var4 = new StackUtil(var1);
      boolean var5 = false;

      int var6;
      for(var6 = 9; var6 < this.contents.length; ++var6) {
         if(var4.tryAdding(this, var6, var2, false)) {
            var5 = true;
            break;
         }
      }

      if(var5) {
         if(!var2) {
            return true;
         } else if(var1.count == 0) {
            return true;
         } else {
            this.addItem(var1, var5, var3);
            return true;
         }
      } else {
         if(!var5) {
            for(var6 = 9; var6 < this.contents.length; ++var6) {
               if(var4.tryAdding(this, var6, var2, true)) {
                  var5 = true;
                  break;
               }
            }
         }

         if(var5) {
            if(!var2) {
               return true;
            } else if(var1.count == 0) {
               return true;
            } else {
               this.addItem(var1, var5, var3);
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      for(int var3 = 9; var3 < this.contents.length; ++var3) {
         if(this.contents[var3] != null) {
            if(var1) {
               return this.splitStack(var3, 1);
            }

            return this.contents[var3];
         }
      }

      return null;
   }

   public void handleDescriptionPacket(Packet230ModLoader var1) {
      boolean var2 = this.box.isInitialized();
      super.handleDescriptionPacket(var1);
      this.currentPattern = FillerRegistry.getPattern(this.currentPatternId);
      this.world.i(this.x, this.y, this.z);
      if(!var2 && this.box.isInitialized()) {
         this.box.createLasers(this.world, LaserKind.Stripes);
      }

   }

   public void handleUpdatePacket(Packet230ModLoader var1) {
      boolean var2 = this.box.isInitialized();
      super.handleUpdatePacket(var1);
      this.currentPattern = FillerRegistry.getPattern(this.currentPatternId);
      this.world.i(this.x, this.y, this.z);
      if(!var2 && this.box.isInitialized()) {
         this.box.createLasers(this.world, LaserKind.Stripes);
      }

   }

   public void setPowerProvider(PowerProvider var1) {
      this.powerProvider = var1;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }
}
