package buildcraft.energy;

import buildcraft.api.APIProxy;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerProvider;
import buildcraft.core.ILiquidContainer;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.TileNetworkData;
import buildcraft.energy.Engine;
import buildcraft.energy.EngineIron;
import buildcraft.energy.EngineStone;
import buildcraft.energy.EngineWood;
import buildcraft.energy.PneumaticPowerProvider;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.ModLoader;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;

public class TileEngine extends TileBuildCraft implements IPowerReceptor, IInventory, ILiquidContainer {

   @TileNetworkData
   public Engine engine;
   @TileNetworkData
   public int progressPart = 0;
   @TileNetworkData
   public int burnTime = 0;
   @TileNetworkData
   public float serverPistonSpeed = 0.0F;
   boolean lastPower = false;
   public int orientation;
   private ItemStack itemInInventory;
   public int totalBurnTime = 0;
   public short scaledBurnTime = 0;
   PowerProvider provider;
   public static int OIL_BUCKET_TIME = 10000;


   public TileEngine() {
      this.provider = BuildCraftCore.powerFramework.createPowerProvider();
   }
   
   public ItemStack[] getContents() {
      ItemStack[] ret = new ItemStack[1];
      ret[0] = itemInInventory;
      return ret;
   }

   public void initialize() {
      if(!APIProxy.isClient(this.world)) {
         if(this.engine == null) {
            this.createEngineIfNeeded();
         }

         this.engine.orientation = Orientations.values()[this.orientation];
         this.provider.configure(0, 1, this.engine.maxEnergyReceived(), 1, this.engine.maxEnergy);
      }

   }

   public void g_() {
      super.g_();
      if(this.engine != null) {
         if(APIProxy.isClient(this.world)) {
            if(this.progressPart != 0) {
               this.engine.progress += this.serverPistonSpeed;
               if(this.engine.progress > 1.0F) {
                  this.progressPart = 0;
               }
            }

         } else {
            this.engine.update();
            boolean var1 = this.world.isBlockIndirectlyPowered(this.x, this.y, this.z);
            Position var2;
            TileEntity var3;
            IPowerReceptor var4;
            if(this.progressPart != 0) {
               this.engine.progress += this.engine.getPistonSpeed();
               if((double)this.engine.progress > 0.5D && this.progressPart == 1) {
                  this.progressPart = 2;
                  var2 = new Position((double)this.x, (double)this.y, (double)this.z, this.engine.orientation);
                  var2.moveForwards(1.0D);
                  var3 = this.world.getTileEntity((int)var2.x, (int)var2.y, (int)var2.z);
                  if(this.isPoweredTile(var3)) {
                     var4 = (IPowerReceptor)var3;
                     int var5 = this.engine.extractEnergy(var4.getPowerProvider().minEnergyReceived, var4.getPowerProvider().maxEnergyReceived, true);
                     if(var5 > 0) {
                        var4.getPowerProvider().receiveEnergy(var5);
                     }
                  }
               } else if(this.engine.progress >= 1.0F) {
                  this.engine.progress = 0.0F;
                  this.progressPart = 0;
               }
            } else if(var1) {
               var2 = new Position((double)this.x, (double)this.y, (double)this.z, this.engine.orientation);
               var2.moveForwards(1.0D);
               var3 = this.world.getTileEntity((int)var2.x, (int)var2.y, (int)var2.z);
               if(this.isPoweredTile(var3)) {
                  var4 = (IPowerReceptor)var3;
                  if(this.engine.extractEnergy(var4.getPowerProvider().minEnergyReceived, var4.getPowerProvider().maxEnergyReceived, false) > 0) {
                     this.progressPart = 1;
                     this.sendNetworkUpdate();
                  }
               }
            } else if(this.world.getTime() % 20L * 10L == 0L) {
               this.sendNetworkUpdate();
            }

            if(this.engine instanceof EngineStone) {
               if(this.burnTime > 0) {
                  --this.burnTime;
                  this.engine.addEnergy(1);
               }

               if(this.burnTime == 0 && var1) {
                  this.burnTime = this.totalBurnTime = this.getItemBurnTime(this.itemInInventory);
                  if(this.burnTime > 0) {
                     this.splitStack(1, 1);
                  }
               }
            } else if(this.engine instanceof EngineIron) {
               if(var1 && this.burnTime > 0) {
                  --this.burnTime;
                  this.engine.addEnergy(2);
               }

               if(this.itemInInventory != null && this.itemInInventory.id == BuildCraftEnergy.bucketOil.id) {
                  this.totalBurnTime = OIL_BUCKET_TIME * 10;
                  int var6 = OIL_BUCKET_TIME;
                  if(this.burnTime + var6 <= this.totalBurnTime) {
                     this.itemInInventory = new ItemStack(Item.BUCKET, 1);
                     this.burnTime += var6;
                  }
               }
            }

            if(this.totalBurnTime != 0) {
               this.scaledBurnTime = (short)(this.burnTime * 1000 / this.totalBurnTime);
            } else {
               this.scaledBurnTime = 0;
            }

         }
      }
   }

   private void createEngineIfNeeded() {
      if(this.engine == null) {
         int var1 = this.world.getData(this.x, this.y, this.z);
         if(var1 == 0) {
            this.engine = new EngineWood(this);
         } else if(var1 == 1) {
            this.engine = new EngineStone(this);
         } else if(var1 == 2) {
            this.engine = new EngineIron(this);
         }

         this.engine.orientation = Orientations.values()[this.orientation];
      }

   }

   public void switchOrientation() {
      for(int var1 = this.orientation + 1; var1 <= this.orientation + 6; ++var1) {
         Orientations var2 = Orientations.values()[var1 % 6];
         Position var3 = new Position((double)this.x, (double)this.y, (double)this.z, var2);
         var3.moveForwards(1.0D);
         TileEntity var4 = this.world.getTileEntity((int)var3.x, (int)var3.y, (int)var3.z);
         if(this.isPoweredTile(var4)) {
            if(this.engine != null) {
               this.engine.orientation = var2;
            }

            this.orientation = var2.ordinal();
            this.world.i(this.x, this.y, this.z);
            break;
         }
      }

   }

   public void delete() {}

   public void a(NBTTagCompound var1) {
      super.a(var1);
      int var2 = var1.e("kind");
      if(var2 == 0) {
         this.engine = new EngineWood(this);
      } else if(var2 == 1) {
         this.engine = new EngineStone(this);
      } else if(var2 == 2) {
         this.engine = new EngineIron(this);
      }

      this.orientation = var1.e("orientation");
      this.engine.progress = var1.g("progress");
      this.engine.energy = var1.e("energy");
      this.engine.orientation = Orientations.values()[this.orientation];
      this.totalBurnTime = var1.e("totalBurnTime");
      this.burnTime = var1.e("burnTime");
      if(var1.hasKey("itemInInventory")) {
         NBTTagCompound var3 = var1.k("itemInInventory");
         this.itemInInventory = new ItemStack(var3);
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.a("kind", this.world.getData(this.x, this.y, this.z));
      var1.a("orientation", this.orientation);
      var1.a("progress", this.engine.progress);
      var1.a("energy", this.engine.energy);
      var1.a("totalBurnTime", this.totalBurnTime);
      var1.a("burnTime", this.burnTime);
      if(this.itemInInventory != null) {
         NBTTagCompound var2 = new NBTTagCompound();
         this.itemInInventory.a(var2);
         var1.a("itemInInventory", var2);
      }

   }

   public int getSize() {
      return 1;
   }

   public ItemStack getItem(int var1) {
      return this.itemInInventory;
   }

   public ItemStack splitStack(int var1, int var2) {
      ItemStack var3 = this.itemInInventory.a(var2);
      if(this.itemInInventory.count == 0) {
         this.itemInInventory = null;
      }

      return var3;
   }

   public void setItem(int var1, ItemStack var2) {
      this.itemInInventory = var2;
   }

   public String getName() {
      return "Engine";
   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean a_(EntityHuman var1) {
      return true;
   }

   private int getItemBurnTime(ItemStack var1) {
      if(var1 == null) {
         return 0;
      } else {
         int var2 = var1.getItem().id;
         return var2 < 256 && Block.byId[var2].material == Material.WOOD?300:(var2 == Item.STICK.id?100:(var2 == Item.COAL.id?1600:(var2 == Item.LAVA_BUCKET.id?20000:(var2 == Block.SAPLING.id?100:ModLoader.AddAllFuel(var2)))));
      }
   }

   public boolean isBurning() {
      return this.engine != null && this.engine.isBurning();
   }

   public int getBurnTimeRemainingScaled(int var1) {
      return this.scaledBurnTime * var1 / 1000;
   }

   public Packet getDescriptionPacket() {
      this.createEngineIfNeeded();
      return super.getDescriptionPacket();
   }

   public Packet230ModLoader getUpdatePacket() {
      this.serverPistonSpeed = this.engine.getPistonSpeed();
      return super.getUpdatePacket();
   }

   public void handleDescriptionPacket(Packet230ModLoader var1) {
      this.createEngineIfNeeded();
      super.handleDescriptionPacket(var1);
   }

   public void handleUpdatePacket(Packet230ModLoader var1) {
      this.createEngineIfNeeded();
      super.handleUpdatePacket(var1);
   }

   public void setPowerProvider(PowerProvider var1) {
      this.provider = var1;
   }

   public PowerProvider getPowerProvider() {
      return this.provider;
   }

   public void doWork() {
      if(!APIProxy.isClient(this.world)) {
         this.engine.addEnergy((int)((float)this.provider.useEnergy(1, this.engine.maxEnergyReceived(), true) * 0.95F));
      }
   }

   public boolean isPoweredTile(TileEntity var1) {
      if(!(var1 instanceof IPowerReceptor)) {
         return false;
      } else {
         IPowerReceptor var2 = (IPowerReceptor)var1;
         PowerProvider var3 = var2.getPowerProvider();
         return var3 != null && var3.getClass().equals(PneumaticPowerProvider.class);
      }
   }

   public int fill(Orientations var1, int var2) {
      if(this.engine instanceof EngineIron) {
         this.totalBurnTime = OIL_BUCKET_TIME * 10;
         int var3 = (int)((float)var2 * (float)OIL_BUCKET_TIME / 1000.0F);
         if(var3 + this.burnTime <= OIL_BUCKET_TIME * 10) {
            this.burnTime += var3;
            return var2;
         } else {
            var3 = OIL_BUCKET_TIME * 10 - this.burnTime;
            int var4 = (int)((float)var3 * 1000.0F / (float)OIL_BUCKET_TIME);
            this.burnTime += (int)((float)var4 * (float)OIL_BUCKET_TIME / 1000.0F);
            return var4;
         }
      } else {
         return 0;
      }
   }

   public int empty(int var1, boolean var2) {
      return 0;
   }

   public int getLiquidQuantity() {
      return 0;
   }

   public int getCapacity() {
      return 10000;
   }

}
