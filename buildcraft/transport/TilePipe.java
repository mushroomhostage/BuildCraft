package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.EntityPassiveItem;
import buildcraft.api.IPipeEntry;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.SafeTimeTracker;
import buildcraft.core.CoreProxy;
import buildcraft.core.ILiquidContainer;
import buildcraft.core.PacketIds;
import buildcraft.core.StackUtil;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.TileNetworkData;
import buildcraft.core.Utils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import net.minecraft.server.EntityItem;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftTransport;

public abstract class TilePipe extends TileBuildCraft implements IPipeEntry, ILiquidContainer {

   public static int flowRate = 20;
   @TileNetworkData(
      staticSize = 6
   )
   public int[] sideToCenter = new int[6];
   @TileNetworkData(
      staticSize = 6
   )
   public int[] centerToSide = new int[6];
   @TileNetworkData
   public int centerIn = 0;
   @TileNetworkData
   public int centerOut = 0;
   @TileNetworkData(
      staticSize = 6
   )
   public boolean[] isInput = new boolean[6];
   @TileNetworkData
   public Orientations lastFromOrientation;
   @TileNetworkData
   public Orientations lastToOrientation;
   private SafeTimeTracker timeTracker;
   private boolean blockNeighborChange;
   public TreeMap travelingEntities;
   LinkedList entitiesToLoad;


   public TilePipe() {
      this.lastFromOrientation = Orientations.XPos;
      this.lastToOrientation = Orientations.XPos;
      this.timeTracker = new SafeTimeTracker();
      this.blockNeighborChange = false;
      this.travelingEntities = new TreeMap();
      this.entitiesToLoad = new LinkedList();

      for(int var1 = 0; var1 < 6; ++var1) {
         this.sideToCenter[var1] = 0;
         this.centerToSide[var1] = 0;
         this.isInput[var1] = false;
      }

   }

   public void readjustSpeed(EntityPassiveItem var1) {
      if(var1.speed > Utils.pipeNormalSpeed) {
         var1.speed -= Utils.pipeNormalSpeed;
      }

      if(var1.speed < Utils.pipeNormalSpeed) {
         var1.speed = Utils.pipeNormalSpeed;
      }

   }

   public void entityEntering(EntityPassiveItem var1, Orientations var2) {
      this.readjustSpeed(var1);
      if(!this.travelingEntities.containsKey(new Integer(var1.entityId))) {
         this.travelingEntities.put(new Integer(var1.entityId), new TilePipe.EntityData(var1, var2));
         var1.container = this;
      }

      if(var2 != Orientations.YPos && var2 != Orientations.YNeg) {
         var1.setPosition(var1.posX, (double)((float)this.y + Utils.getPipeFloorOf(var1.item)), var1.posZ);
      }

      if(APIProxy.isServerSide() && var1.synchroTracker.markTimeIfDelay(this.world, 20L)) {
         CoreProxy.sendToPlayers(this.createItemPacket(var1, var2), this.x, this.y, this.z, 50, mod_BuildCraftTransport.instance);
      }

   }

   public LinkedList getPossibleMovements(Position var1, EntityPassiveItem var2) {
      LinkedList var3 = new LinkedList();

      for(int var4 = 0; var4 <= 5; ++var4) {
         if(Orientations.values()[var4] != var1.orientation.reverse()) {
            Position var5 = new Position(var1);
            var5.orientation = Orientations.values()[var4];
            var5.moveForwards(1.0D);
            if(this.canReceivePipeObjects(var5, var2)) {
               var3.add(var5.orientation);
            }
         }
      }

      return var3;
   }

   public boolean canReceivePipeObjects(Position var1, EntityPassiveItem var2) {
      TileEntity var3 = this.world.getTileEntity((int)var1.x, (int)var1.y, (int)var1.z);
      return !Utils.checkPipesConnections(this.world, (int)var1.x, (int)var1.y, (int)var1.z, this.x, this.y, this.z)?false:(var3 instanceof IPipeEntry?true:var3 instanceof IInventory && (new StackUtil(var2.item)).checkAvailableSlot((IInventory)var3, false, var1.orientation.reverse()));
   }

   public boolean canReceiveLiquid(Position var1) {
      TileEntity var2 = this.world.getTileEntity((int)var1.x, (int)var1.y, (int)var1.z);
      return this.isInput[var1.orientation.ordinal()]?false:(!Utils.checkPipesConnections(this.world, (int)var1.x, (int)var1.y, (int)var1.z, this.x, this.y, this.z)?false:var2 instanceof IPipeEntry || var2 instanceof ILiquidContainer);
   }

   public void g_() {
      super.g_();
      this.moveSolids();
      this.moveLiquids();
      if(this.blockNeighborChange) {
         this.blockNeighborChange = false;
         this.neighborChange();
      }

      if(APIProxy.isServerSide() && this.timeTracker.markTimeIfDelay(this.world, 50L)) {
         this.sendNetworkUpdate();
      }

   }

   private void moveSolids() {
      Iterator var1 = this.entitiesToLoad.iterator();

      while(var1.hasNext()) {
         TilePipe.EntityData var2 = (TilePipe.EntityData)var1.next();
         this.travelingEntities.put(new Integer(var2.item.entityId), var2);
      }

      this.entitiesToLoad.clear();
      LinkedList var9 = new LinkedList();
      Iterator var10 = this.travelingEntities.values().iterator();

      while(var10.hasNext()) {
         TilePipe.EntityData var3 = (TilePipe.EntityData)var10.next();
         Position var4 = new Position(0.0D, 0.0D, 0.0D, var3.orientation);
         var4.moveForwards((double)var3.item.speed);
         var3.item.setPosition(var3.item.posX + var4.x, var3.item.posY + var4.y, var3.item.posZ + var4.z);
         if((!var3.toCenter || !this.middleReached(var3)) && !this.outOfBounds(var3)) {
            if(!var3.toCenter && this.endReached(var3)) {
               var9.add(var3);
               Position var11 = new Position((double)this.x, (double)this.y, (double)this.z, var3.orientation);
               var11.moveForwards(1.0D);
               TileEntity var12 = this.world.getTileEntity((int)var11.x, (int)var11.y, (int)var11.z);
               if(var12 instanceof IPipeEntry) {
                  ((IPipeEntry)var12).entityEntering(var3.item, var3.orientation);
               } else if(var12 instanceof IInventory) {
                  StackUtil var13 = new StackUtil(var3.item.item);
                  if(!APIProxy.isClient(this.world) && (!var13.checkAvailableSlot((IInventory)var12, true, var11.orientation.reverse()) || var13.items.count != 0)) {
                     var3.item.item = var13.items;
                     EntityItem var8 = var3.item.toEntityItem(this.world, var3.orientation);
                     if(var8 != null) {
                        this.onDropped(var8);
                     }
                  }
               } else {
                  EntityItem var7 = var3.item.toEntityItem(this.world, var3.orientation);
                  if(var7 != null) {
                     this.onDropped(var7);
                  }
               }
            }
         } else {
            var3.toCenter = false;
            var3.item.setPosition((double)this.x + 0.5D, (double)((float)this.y + Utils.getPipeFloorOf(var3.item.item)), (double)this.z + 0.5D);
            Orientations var5 = this.resolveDestination(var3);
            if(var5 == Orientations.Unknown) {
               var9.add(var3);
               EntityItem var6 = var3.item.toEntityItem(this.world, var3.orientation);
               if(var6 != null) {
                  this.onDropped(var6);
               }
            } else {
               var3.orientation = var5;
            }
         }
      }

      this.travelingEntities.values().removeAll(var9);
   }

   public boolean middleReached(TilePipe.EntityData var1) {
      float var2 = var1.item.speed * 1.01F;
      return Math.abs((double)this.x + 0.5D - var1.item.posX) < (double)var2 && Math.abs((double)((float)this.y + Utils.getPipeFloorOf(var1.item.item)) - var1.item.posY) < (double)var2 && Math.abs((double)this.z + 0.5D - var1.item.posZ) < (double)var2;
   }

   public boolean endReached(TilePipe.EntityData var1) {
      return var1.item.posX > (double)this.x + 1.0D || var1.item.posX < (double)this.x || var1.item.posY > (double)this.y + 1.0D || var1.item.posY < (double)this.y || var1.item.posZ > (double)this.z + 1.0D || var1.item.posZ < (double)this.z;
   }

   public boolean outOfBounds(TilePipe.EntityData var1) {
      return var1.item.posX > (double)this.x + 2.0D || var1.item.posX < (double)this.x - 1.0D || var1.item.posY > (double)this.y + 2.0D || var1.item.posY < (double)this.y - 1.0D || var1.item.posZ > (double)this.z + 2.0D || var1.item.posZ < (double)this.z - 1.0D;
   }

   public Position getPosition() {
      return new Position((double)this.x, (double)this.y, (double)this.z);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.l("travelingEntities");

      int var3;
      for(var3 = 0; var3 < var2.c(); ++var3) {
         try {
            NBTTagCompound var4 = (NBTTagCompound)var2.a(var3);
            EntityPassiveItem var5 = new EntityPassiveItem(APIProxy.getWorld());
            var5.readFromNBT(var4);
            var5.container = this;
            TilePipe.EntityData var6 = new TilePipe.EntityData(var5, Orientations.values()[var4.e("orientation")]);
            var6.toCenter = var4.m("toCenter");
            this.entitiesToLoad.add(var6);
         } catch (Throwable var7) {
            ;
         }
      }

      for(var3 = 0; var3 < 6; ++var3) {
         this.sideToCenter[var3] = var1.e("sideToCenter[" + var3 + "]");
         this.centerToSide[var3] = var1.e("centerToSide[" + var3 + "]");
         this.isInput[var3] = var1.m("isInput[" + var3 + "]");
      }

      this.centerIn = var1.e("centerIn");
      this.centerOut = var1.e("centerOut");
      this.lastFromOrientation = Orientations.values()[var1.e("lastFromOrientation")];
      this.lastToOrientation = Orientations.values()[var1.e("lastToOrientation")];
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      NBTTagList var2 = new NBTTagList();
      Iterator var3 = this.travelingEntities.values().iterator();

      while(var3.hasNext()) {
         TilePipe.EntityData var4 = (TilePipe.EntityData)var3.next();
         NBTTagCompound var5 = new NBTTagCompound();
         var2.a(var5);
         var4.item.writeToNBT(var5);
         var5.a("toCenter", var4.toCenter);
         var5.a("orientation", var4.orientation.ordinal());
      }

      var1.a("travelingEntities", var2);

      for(int var6 = 0; var6 < 6; ++var6) {
         var1.a("sideToCenter[" + var6 + "]", this.sideToCenter[var6]);
         var1.a("centerToSide[" + var6 + "]", this.centerToSide[var6]);
         var1.a("isInput[" + var6 + "]", this.isInput[var6]);
      }

      var1.a("centerIn", this.centerIn);
      var1.a("centerOut", this.centerOut);
      var1.a("lastFromOrientation", this.lastFromOrientation.ordinal());
      var1.a("lastToOrientation", this.lastToOrientation.ordinal());
   }

   public Orientations resolveDestination(TilePipe.EntityData var1) {
      LinkedList var2 = this.getPossibleMovements(new Position((double)this.x, (double)this.y, (double)this.z, var1.orientation), var1.item);
      if(var2.size() == 0) {
         return Orientations.Unknown;
      } else {
         int var3;
         if(!APIProxy.isClient(this.world) && !APIProxy.isServerSide()) {
            var3 = this.world.random.nextInt(var2.size());
         } else {
            var3 = Math.abs(var1.item.entityId + this.x + this.y + this.z + var1.item.deterministicRandomization) % var2.size();
         }

         return (Orientations)var2.get(var3);
      }
   }

   public void destroy() {
      Iterator var1 = this.travelingEntities.values().iterator();

      while(var1.hasNext()) {
         TilePipe.EntityData var2 = (TilePipe.EntityData)var1.next();
         var2.item.toEntityItem(this.world, var2.orientation);
      }

      this.travelingEntities.clear();
   }

   protected void doWork() {}

   public void handleItemPacket(Packet230ModLoader var1) {
      if(var1.packetType == PacketIds.PipeItem.ordinal()) {
         EntityPassiveItem var2 = new EntityPassiveItem(this.world);
         var2.entityId = var1.dataInt[3];
         int var3 = var1.dataInt[5];
         int var4 = var1.dataInt[6];
         int var5 = var1.dataInt[7];
         var2.item = new ItemStack(var3, var4, var5);
         Orientations var6 = Orientations.values()[var1.dataInt[4]];
         var2.setPosition((double)var1.dataFloat[0], (double)var1.dataFloat[1], (double)var1.dataFloat[2]);
         var2.speed = var1.dataFloat[3];
         var2.deterministicRandomization = var1.dataInt[8];
         if(var2.container == null) {
            this.travelingEntities.put(new Integer(var2.entityId), new TilePipe.EntityData(var2, var6));
            var2.container = this;
         } else {
            ((TilePipe.EntityData)this.travelingEntities.get(new Integer(var2.entityId))).orientation = var6;
         }

      }
   }

   public Packet230ModLoader createItemPacket(EntityPassiveItem var1, Orientations var2) {
      Packet230ModLoader var3 = new Packet230ModLoader();
      var1.deterministicRandomization += this.world.random.nextInt(6);
      var3.modId = mod_BuildCraftTransport.instance.getId();
      var3.packetType = PacketIds.PipeItem.ordinal();
      var3.k = true;
      var3.dataInt = new int[9];
      var3.dataInt[0] = this.x;
      var3.dataInt[1] = this.y;
      var3.dataInt[2] = this.z;
      var3.dataInt[3] = var1.entityId;
      var3.dataInt[4] = var2.ordinal();
      var3.dataInt[5] = var1.item.id;
      var3.dataInt[6] = var1.item.count;
      var3.dataInt[7] = var1.item.getData();
      var3.dataInt[8] = var1.deterministicRandomization;
      var3.dataFloat = new float[4];
      var3.dataFloat[0] = (float)var1.posX;
      var3.dataFloat[1] = (float)var1.posY;
      var3.dataFloat[2] = (float)var1.posZ;
      var3.dataFloat[3] = var1.speed;
      return var3;
   }

   public int getNumberOfItems() {
      return this.travelingEntities.size();
   }

   public void onDropped(EntityItem var1) {}

   public int fill(Orientations var1, int var2) {
      int var3 = 250 - this.sideToCenter[var1.ordinal()] - this.centerToSide[var1.ordinal()] + flowRate;
      this.isInput[var1.ordinal()] = true;
      if(var3 <= 0) {
         return 0;
      } else {
         int var10001;
         int[] var10000;
         if(var3 > var2) {
            var10000 = this.sideToCenter;
            var10001 = var1.ordinal();
            var10000[var10001] += var2;
            return var2;
         } else {
            var10000 = this.sideToCenter;
            var10001 = var1.ordinal();
            var10000[var10001] += var3;
            return var3;
         }
      }
   }

   private void moveLiquids() {
      float var1 = (float)(500 - this.centerIn - this.centerOut + flowRate);
      boolean var2 = false;

      int var3;
      for(var3 = 0; var3 < 6; ++var3) {
         if(this.isInput[var3]) {
            if(this.centerToSide[var3] > 0 && var1 >= (float)flowRate) {
               this.lastFromOrientation = Orientations.values()[var3];
               this.centerToSide[var3] -= flowRate;
               this.centerIn += flowRate;
               var2 = true;
            }

            if(this.sideToCenter[var3] + this.centerToSide[var3] >= 250) {
               this.centerToSide[var3] += this.sideToCenter[var3];
               this.sideToCenter[var3] = 0;
            }
         }
      }

      if(this.centerIn + this.centerOut >= 500) {
         this.centerOut += this.centerIn;
         this.centerIn = 0;
      }

      Position var4;
      for(var3 = 0; var3 < 6; ++var3) {
         var4 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var3]);
         var4.moveForwards(1.0D);
         if(this.canReceiveLiquid(var4)) {
            if(this.sideToCenter[var3] > 0) {
               ILiquidContainer var5 = (ILiquidContainer)Utils.getTile(this.world, var4, Orientations.Unknown);
               this.sideToCenter[var3] -= var5.fill(var4.orientation.reverse(), flowRate);
               var2 = true;
            }

            if(this.centerOut > 0 && this.sideToCenter[var3] + this.centerToSide[var3] <= 250) {
               this.lastToOrientation = var4.orientation;
               this.centerToSide[var3] += flowRate;
               this.centerOut -= flowRate;
               var2 = true;
            }

            if(this.centerToSide[var3] + this.sideToCenter[var3] >= 250) {
               this.sideToCenter[var3] += this.centerToSide[var3];
               this.centerToSide[var3] = 0;
            }
         }
      }

      if(!var2) {
         for(var3 = 0; var3 < 6; ++var3) {
            var4 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var3]);
            var4.moveForwards(1.0D);
            if(this.canReceiveLiquid(var4)) {
               return;
            }
         }

         for(var3 = 0; var3 < 6; ++var3) {
            this.isInput[var3] = false;
         }
      }

   }

   public int getSideToCenter(int var1) {
      return this.sideToCenter[var1] > 250?250:this.sideToCenter[var1];
   }

   public int getCenterToSide(int var1) {
      return this.centerToSide[var1] > 250?250:this.centerToSide[var1];
   }

   public int getCenterIn() {
      return this.centerIn > 500?500:this.centerIn;
   }

   public int getCenterOut() {
      return this.centerOut > 500?500:this.centerOut;
   }

   public int getLiquidQuantity() {
      return 0;
   }

   public int getCapacity() {
      return 0;
   }

   public int empty(int var1, boolean var2) {
      return 0;
   }

   public void scheduleNeighborChange() {
      this.blockNeighborChange = true;
   }

   protected void neighborChange() {
      for(int var1 = 0; var1 < 6; ++var1) {
         Position var2 = new Position((double)this.x, (double)this.y, (double)this.z, Orientations.values()[var1]);
         var2.moveForwards(1.0D);
         if(!this.canReceiveLiquid(var2)) {
            this.centerToSide[var1] = 0;
            this.sideToCenter[var1] = 0;
         }
      }

   }


   public class EntityData {

      boolean toCenter = true;
      EntityPassiveItem item;
      public Orientations orientation;


      public EntityData(EntityPassiveItem var2, Orientations var3) {
         this.item = var2;
         this.orientation = var3;
      }
   }
}
