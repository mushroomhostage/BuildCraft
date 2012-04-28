package buildcraft.energy;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.TileNetworkData;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketUpdate;
import buildcraft.energy.Engine;
import buildcraft.energy.EngineIron;
import buildcraft.energy.EngineStone;
import buildcraft.energy.EngineWood;
import buildcraft.energy.IEngineProvider;
import buildcraft.energy.PneumaticPowerProvider;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.TileEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;

public class TileEngine extends TileBuildCraft implements IPowerReceptor, IInventory, ILiquidContainer, IEngineProvider {

   @TileNetworkData
   public Engine engine;
   @TileNetworkData
   public int progressPart = 0;
   @TileNetworkData
   public float serverPistonSpeed = 0.0F;
   boolean lastPower = false;
   public int orientation;
   private ItemStack itemInInventory;
   PowerProvider provider;
   public List transaction = new ArrayList();


   public TileEngine() {
      this.provider = PowerFramework.currentFramework.createPowerProvider();
   }

   public void onOpen(CraftHumanEntity var1) {
      this.transaction.add(var1);
   }

   public void onClose(CraftHumanEntity var1) {
      this.transaction.remove(var1);
   }

   public List getViewers() {
      return this.transaction;
   }

   public void setMaxStackSize(int var1) {}

   public ItemStack[] getContents() {
      ItemStack[] var1 = new ItemStack[]{this.itemInInventory};
      return var1;
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

   public void q_() {
      super.q_();
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

            this.engine.burn();
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
            this.world.k(this.x, this.y, this.z);
            break;
         }
      }

   }

   public void delete() {
      this.engine.delete();
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      int var2 = var1.getInt("kind");
      if(var2 == 0) {
         this.engine = new EngineWood(this);
      } else if(var2 == 1) {
         this.engine = new EngineStone(this);
      } else if(var2 == 2) {
         this.engine = new EngineIron(this);
      }

      this.orientation = var1.getInt("orientation");
      this.engine.progress = var1.getFloat("progress");
      this.engine.energy = var1.getInt("energy");
      this.engine.orientation = Orientations.values()[this.orientation];
      if(var1.hasKey("itemInInventory")) {
         NBTTagCompound var3 = var1.getCompound("itemInInventory");
         this.itemInInventory = ItemStack.a(var3);
      }

      this.engine.readFromNBT(var1);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("kind", this.world.getData(this.x, this.y, this.z));
      var1.setInt("orientation", this.orientation);
      var1.setFloat("progress", this.engine.progress);
      var1.setInt("energy", this.engine.energy);
      if(this.itemInInventory != null) {
         NBTTagCompound var2 = new NBTTagCompound();
         this.itemInInventory.save(var2);
         var1.set("itemInInventory", var2);
      }

      this.engine.writeToNBT(var1);
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

   public boolean a(EntityHuman var1) {
      return this.world.getTileEntity(this.x, this.y, this.z) == this;
   }

   public boolean isBurning() {
      return this.engine != null && this.engine.isBurning();
   }

   public int getScaledBurnTime(int var1) {
      return this.engine.getScaledBurnTime(var1);
   }

   public Packet d() {
      this.createEngineIfNeeded();
      return super.d();
   }

   public Packet getUpdatePacket() {
      this.serverPistonSpeed = this.engine.getPistonSpeed();
      return super.getUpdatePacket();
   }

   public void handleDescriptionPacket(PacketUpdate var1) {
      this.createEngineIfNeeded();
      super.handleDescriptionPacket(var1);
   }

   public void handleUpdatePacket(PacketUpdate var1) {
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

   public int fill(Orientations var1, int var2, int var3, boolean var4) {
      return this.engine instanceof EngineIron?((EngineIron)this.engine).fill(var1, var2, var3, true):0;
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

   public int getLiquidId() {
      return 0;
   }

   public void f() {}

   public void g() {}

   public int powerRequest() {
      return 0;
   }

   public Engine getEngine() {
      return this.engine;
   }

   public ItemStack splitWithoutUpdate(int var1) {
      return null;
   }
}
