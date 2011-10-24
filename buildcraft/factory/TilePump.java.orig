package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.TileNetworkData;
import buildcraft.core.BlockIndex;
import buildcraft.core.EntityBlock;
import buildcraft.core.IMachine;
import buildcraft.core.Utils;
import buildcraft.factory.TileMachine;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;

public class TilePump extends TileMachine implements IMachine, IPowerReceptor {

   EntityBlock tube;
   private TreeMap blocksToPump = new TreeMap();
   @TileNetworkData
   public int internalLiquid;
   @TileNetworkData
   public double tubeY = Double.NaN;
   @TileNetworkData
   public int aimY = 0;
   @TileNetworkData
   public int liquidId = 0;
   private PowerProvider powerProvider;


   public TilePump() {
      this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
      this.powerProvider.configure(20, 10, 10, 10, 100);
   }

   public void h_() {
      super.h_();
      if(!APIProxy.isClient(this.world)) {
         if(this.tube.locY - (double)this.aimY > 0.01D) {
            this.tubeY = this.tube.locY - 0.01D;
            this.setTubePosition();
            if(APIProxy.isServerSide()) {
               this.sendNetworkUpdate();
            }

            return;
         }

         if(this.internalLiquid <= 0) {
            BlockIndex var1 = this.getNextIndexToPump(false);
            int var2;
            if(this.isPumpableLiquid(var1)) {
               var2 = Utils.liquidId(this.world.getTypeId(var1.i, var1.j, var1.k));
               if(this.internalLiquid == 0 || this.liquidId == var2) {
                  this.liquidId = var2;
                  if(this.powerProvider.useEnergy(10, 10, true) == 10) {
                     var1 = this.getNextIndexToPump(true);
                     this.world.setTypeId(var1.i, var1.j, var1.k, 0);
                     this.internalLiquid = this.internalLiquid += 1000;
                     if(APIProxy.isServerSide()) {
                        this.sendNetworkUpdate();
                     }
                  }
               }
            } else if(this.world.getTime() % 100L == 0L) {
               this.initializePumpFromPosition(this.x, this.aimY, this.z);
               if(this.getNextIndexToPump(false) == null) {
                  for(var2 = this.y - 1; var2 > 0; --var2) {
                     if(this.isLiquid(new BlockIndex(this.x, var2, this.z))) {
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

      if(this.internalLiquid >= 0) {
         for(int var4 = 0; var4 < 6; ++var4) {
            Position var5 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var4]);
            var5.moveForwards(1.0D);
            TileEntity var3 = this.world.getTileEntity((int)var5.x, (int)var5.y, (int)var5.z);
            if(var3 instanceof ILiquidContainer) {
               this.internalLiquid -= ((ILiquidContainer)var3).fill(var5.orientation.reverse(), this.internalLiquid, this.liquidId, true);
               if(this.internalLiquid <= 0) {
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
      boolean var4 = false;
      TreeSet var5 = new TreeSet();
      TreeSet var6 = new TreeSet();
      if(!this.blocksToPump.containsKey(Integer.valueOf(var2))) {
         this.blocksToPump.put(Integer.valueOf(var2), new LinkedList());
      }

      LinkedList var7 = (LinkedList)this.blocksToPump.get(Integer.valueOf(var2));
      int var11 = this.world.getTypeId(var1, var2, var3);
      if(this.isLiquid(new BlockIndex(var1, var2, var3))) {
         this.addToPumpIfLiquid(new BlockIndex(var1, var2, var3), var5, var6, var7, var11);

         while(var6.size() > 0) {
            TreeSet var8 = new TreeSet(var6);
            var6.clear();
            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
               BlockIndex var10 = (BlockIndex)var9.next();
               this.addToPumpIfLiquid(new BlockIndex(var10.i + 1, var10.j, var10.k), var5, var6, var7, var11);
               this.addToPumpIfLiquid(new BlockIndex(var10.i - 1, var10.j, var10.k), var5, var6, var7, var11);
               this.addToPumpIfLiquid(new BlockIndex(var10.i, var10.j, var10.k + 1), var5, var6, var7, var11);
               this.addToPumpIfLiquid(new BlockIndex(var10.i, var10.j, var10.k - 1), var5, var6, var7, var11);
               if(!this.blocksToPump.containsKey(Integer.valueOf(var10.j + 1))) {
                  this.blocksToPump.put(Integer.valueOf(var10.j + 1), new LinkedList());
               }

               var7 = (LinkedList)this.blocksToPump.get(Integer.valueOf(var10.j + 1));
               this.addToPumpIfLiquid(new BlockIndex(var10.i, var10.j + 1, var10.k), var5, var6, var7, var11);
            }
         }

      }
   }

   public void addToPumpIfLiquid(BlockIndex var1, TreeSet var2, TreeSet var3, LinkedList var4, int var5) {
      if(var5 == this.world.getTypeId(var1.i, var1.j, var1.k)) {
         if(!var2.contains(var1)) {
            var2.add(var1);
            if((var1.i - this.x) * (var1.i - this.x) + (var1.k - this.z) * (var1.k - this.z) > 4096) {
               return;
            }

            if(this.isPumpableLiquid(var1)) {
               var4.push(var1);
            }

            if(this.isLiquid(var1)) {
               var3.add(var1);
            }
         }

      }
   }

   private boolean isPumpableLiquid(BlockIndex var1) {
      return this.isLiquid(var1) && this.world.getData(var1.i, var1.j, var1.k) == 0;
   }

   private boolean isLiquid(BlockIndex var1) {
      return var1 != null && Utils.liquidId(this.world.getTypeId(var1.i, var1.j, var1.k)) != 0;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.internalLiquid = var1.e("internalLiquid");
      this.aimY = var1.e("aimY");
      this.tubeY = (double)var1.g("tubeY");
      this.liquidId = var1.e("liquidId");
      PowerFramework.currentFramework.loadPowerProvider(this, var1);
      this.powerProvider.configure(20, 10, 10, 10, 100);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      PowerFramework.currentFramework.savePowerProvider(this, var1);
      var1.a("internalLiquid", this.internalLiquid);
      var1.a("aimY", this.aimY);
      if(this.tube != null) {
         var1.a("tubeY", (float)this.tube.locY);
      } else {
         var1.a("tubeY", (float)this.y);
      }

      var1.a("liquidId", this.liquidId);
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

   public boolean manageLiquids() {
      return true;
   }

   public boolean manageSolids() {
      return false;
   }
}
