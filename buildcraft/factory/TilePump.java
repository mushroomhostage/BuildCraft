package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerProvider;
import buildcraft.core.BlockIndex;
import buildcraft.core.EntityBlock;
import buildcraft.core.IMachine;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.TileNetworkData;
import buildcraft.transport.TilePipe;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;

public class TilePump extends TileBuildCraft implements IMachine, IPowerReceptor {

   EntityBlock tube;
   private TreeMap blocksToPump = new TreeMap();
   @TileNetworkData
   public int internalLiquid;
   @TileNetworkData
   public double tubeY = Double.NaN;
   @TileNetworkData
   public int aimY = 0;
   private PowerProvider powerProvider;


   public TilePump() {
      this.powerProvider = BuildCraftCore.powerFramework.createPowerProvider();
      this.powerProvider.configure(20, 10, 10, 10, 100);
   }

   public void g_() {
      super.g_();
      if(!APIProxy.isClient(this.world)) {
         if(this.tube.locY - (double)this.aimY > 0.01D) {
            this.tubeY = this.tube.locY - 0.01D;
            this.setTubePosition();
            if(APIProxy.isServerSide()) {
               this.sendNetworkUpdate();
            }

            return;
         }

         if(this.internalLiquid <= TilePipe.flowRate) {
            BlockIndex var1 = this.getNextIndexToPump(false);
            if(this.isPumpableOil(var1)) {
               if(this.powerProvider.useEnergy(10, 10, true) == 10) {
                  var1 = this.getNextIndexToPump(true);
                  this.world.setTypeId(var1.i, var1.j, var1.k, 0);
                  this.internalLiquid = this.internalLiquid += 1000;
                  if(APIProxy.isServerSide()) {
                     this.sendNetworkUpdate();
                  }
               }
            } else if(this.world.getTime() % 100L == 0L) {
               this.initializePumpFromPosition(this.x, this.aimY, this.z);
               if(this.getNextIndexToPump(false) == null) {
                  for(int var2 = this.y - 1; var2 > 0; --var2) {
                     if(this.isOil(new BlockIndex(this.x, var2, this.z))) {
                        this.aimY = var2;
                        return;
                     }

                     if(this.world.getTypeId(this.x, var2, this.z) != 0) {
                        return;
                     }
                  }
               }
            }
         }
      }

      if(this.internalLiquid >= TilePipe.flowRate) {
         for(int var4 = 0; var4 < 6; ++var4) {
            Position var5 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var4]);
            var5.moveForwards(1.0D);
            TileEntity var3 = this.world.getTileEntity((int)var5.x, (int)var5.y, (int)var5.z);
            if(var3 instanceof TilePipe) {
               this.internalLiquid -= ((TilePipe)var3).fill(var5.orientation.reverse(), TilePipe.flowRate);
               if(this.internalLiquid < TilePipe.flowRate) {
                  break;
               }
            }
         }
      }

   }

   public void initialize() {
      this.tube = new EntityBlock(this.world);
      this.tube.texture = 102;
      if(!Double.isNaN(this.tubeY)) {
         this.tube.locY = this.tubeY;
      } else {
         this.tube.locY = (double)this.y;
      }

      this.tubeY = this.tube.locY;
      if(this.aimY == 0) {
         this.aimY = this.y;
      }

      this.setTubePosition();
      this.world.addEntity(this.tube);
      if(APIProxy.isServerSide()) {
         this.sendNetworkUpdate();
      }

   }

   private BlockIndex getNextIndexToPump(boolean var1) {
      LinkedList var2 = null;
      int var3 = 0;
      Iterator var4 = this.blocksToPump.keySet().iterator();

      while(var4.hasNext()) {
         Integer var5 = (Integer)var4.next();
         if(var5.intValue() > var3 && ((LinkedList)this.blocksToPump.get(var5)).size() != 0) {
            var3 = var5.intValue();
            var2 = (LinkedList)this.blocksToPump.get(var5);
         }
      }

      if(var2 != null) {
         if(var1) {
            BlockIndex var6 = (BlockIndex)var2.pop();
            if(var2.size() == 0) {
               this.blocksToPump.remove(Integer.valueOf(var3));
            }

            return var6;
         } else {
            return (BlockIndex)var2.getLast();
         }
      } else {
         return null;
      }
   }

   private void initializePumpFromPosition(int var1, int var2, int var3) {
      TreeSet var4 = new TreeSet();
      TreeSet var5 = new TreeSet();
      if(!this.blocksToPump.containsKey(Integer.valueOf(var2))) {
         this.blocksToPump.put(Integer.valueOf(var2), new LinkedList());
      }

      LinkedList var6 = (LinkedList)this.blocksToPump.get(Integer.valueOf(var2));
      this.addToPumpIfOil(new BlockIndex(var1, var2, var3), var4, var5, var6);

      while(var5.size() > 0) {
         TreeSet var7 = new TreeSet(var5);
         var5.clear();
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            BlockIndex var9 = (BlockIndex)var8.next();
            this.addToPumpIfOil(new BlockIndex(var9.i + 1, var9.j, var9.k), var4, var5, var6);
            this.addToPumpIfOil(new BlockIndex(var9.i - 1, var9.j, var9.k), var4, var5, var6);
            this.addToPumpIfOil(new BlockIndex(var9.i, var9.j, var9.k + 1), var4, var5, var6);
            this.addToPumpIfOil(new BlockIndex(var9.i, var9.j, var9.k - 1), var4, var5, var6);
            if(!this.blocksToPump.containsKey(Integer.valueOf(var9.j + 1))) {
               this.blocksToPump.put(Integer.valueOf(var9.j + 1), new LinkedList());
            }

            var6 = (LinkedList)this.blocksToPump.get(Integer.valueOf(var9.j + 1));
            this.addToPumpIfOil(new BlockIndex(var9.i, var9.j + 1, var9.k), var4, var5, var6);
         }
      }

   }

   public void addToPumpIfOil(BlockIndex var1, TreeSet var2, TreeSet var3, LinkedList var4) {
      if(!var2.contains(var1)) {
         var2.add(var1);
         if(this.isPumpableOil(var1)) {
            var4.push(var1);
         }

         if(this.isOil(var1)) {
            var3.add(var1);
         }
      }

   }

   private boolean isPumpableOil(BlockIndex var1) {
      return this.isOil(var1) && this.world.getData(var1.i, var1.j, var1.k) == 0;
   }

   private boolean isOil(BlockIndex var1) {
      return var1 != null && (this.world.getTypeId(var1.i, var1.j, var1.k) == BuildCraftEnergy.oilStill.id || this.world.getTypeId(var1.i, var1.j, var1.k) == BuildCraftEnergy.oilMoving.id);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.internalLiquid = var1.e("internalLiquid");
      this.aimY = var1.e("aimY");
      this.tubeY = (double)var1.g("tubeY");
      BuildCraftCore.powerFramework.loadPowerProvider(this, var1);
      this.powerProvider.configure(20, 10, 10, 10, 100);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      BuildCraftCore.powerFramework.savePowerProvider(this, var1);
      var1.a("internalLiquid", this.internalLiquid);
      var1.a("aimY", this.aimY);
      if(this.tube != null) {
         var1.a("tubeY", (float)this.tube.locY);
      } else {
         var1.a("tubeY", (float)this.y);
      }

   }

   public boolean isActive() {
      return true;
   }

   public void setPowerProvider(PowerProvider var1) {
      this.powerProvider = var1;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   public void doWork() {}

   public void handleDescriptionPacket(Packet230ModLoader var1) {
      super.handleDescriptionPacket(var1);
      this.setTubePosition();
   }

   public void handleUpdatePacket(Packet230ModLoader var1) {
      super.handleDescriptionPacket(var1);
      this.setTubePosition();
   }

   private void setTubePosition() {
      if(this.tube != null) {
         this.tube.iSize = 0.5D;
         this.tube.kSize = 0.5D;
         this.tube.jSize = (double)this.y - this.tube.locY;
         this.tube.setPosition((double)((float)this.x + 0.25F), this.tubeY, (double)((float)this.z + 0.25F));
      }

   }

   public void destroy() {
      if(this.tube != null) {
         APIProxy.removeEntity(this.tube);
      }

   }
}
