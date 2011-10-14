package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.api.IAreaProvider;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.LaserKind;
import buildcraft.api.Orientations;
import buildcraft.api.PowerProvider;
import buildcraft.core.BlockContents;
import buildcraft.core.BluePrint;
import buildcraft.core.BluePrintBuilder;
import buildcraft.core.Box;
import buildcraft.core.DefaultAreaProvider;
import buildcraft.core.IMachine;
import buildcraft.core.StackUtil;
import buildcraft.core.TileNetworkData;
import buildcraft.core.Utils;
import buildcraft.factory.EntityMechanicalArm;
import buildcraft.factory.IArmListener;
import buildcraft.factory.TileMachine;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftBlockUtil;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftFactory;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet230ModLoader;

public class TileQuarry extends TileMachine implements IArmListener, IMachine, IPowerReceptor {

   BlockContents nextBlockForBluePrint = null;
   boolean isDigging = false;
   @TileNetworkData
   public Box box = new Box();
   @TileNetworkData
   public boolean inProcess = false;
   public EntityMechanicalArm arm;
   @TileNetworkData
   public int targetX;
   @TileNetworkData
   public int targetY;
   @TileNetworkData
   public int targetZ;
   @TileNetworkData
   public double headPosX;
   @TileNetworkData
   public double headPosY;
   @TileNetworkData
   public double headPosZ;
   @TileNetworkData
   public double speed = 0.03D;
   boolean loadArm = false;
   BluePrintBuilder bluePrintBuilder;
   @TileNetworkData
   public PowerProvider powerProvider;
   public static int MAX_ENERGY = 7000;
   private boolean loadDefaultBoundaries = false;


   public TileQuarry() {
      this.powerProvider = BuildCraftCore.powerFramework.createPowerProvider();
      this.powerProvider.configure(20, 25, 25, 25, MAX_ENERGY);
   }

   public void createUtilsIfNeeded() {
      if(this.world == null) this.world = APIProxy.getWorld();
      if(this.box.isInitialized() || !APIProxy.isClient(this.world)) {
         if(this.bluePrintBuilder == null) {
            if(!this.box.isInitialized()) {
               this.setBoundaries(this.loadDefaultBoundaries);
            }

            this.initializeBluePrintBuilder();
         }

         this.nextBlockForBluePrint = this.bluePrintBuilder.findNextBlock(this.world);
         if(this.bluePrintBuilder.done) {
            this.box.deleteLasers();
            if(this.arm == null) {
               this.createArm();
            }

            if(this.loadArm) {
               this.arm.joinToWorld(this.world);
               this.loadArm = false;
               if(this.findTarget(false)) {
                  this.isDigging = true;
               }
            }
         } else {
            this.box.createLasers(this.world, LaserKind.Stripes);
            this.isDigging = true;
         }

      }
   }

   private void createArm() {
      if(this.world == null) this.world = APIProxy.getWorld();
      this.arm = new EntityMechanicalArm(this.world, (double)((float)this.box.xMin + 0.75F), (double)((float)(this.y + this.bluePrintBuilder.bluePrint.sizeY - 1) + 0.25F), (double)((float)this.box.zMin + 0.75F), (double)((float)(this.bluePrintBuilder.bluePrint.sizeX - 2) + 0.5F), (double)((float)(this.bluePrintBuilder.bluePrint.sizeZ - 2) + 0.5F));
      this.arm.listener = this;
      this.loadArm = true;
   }

   public void h_() {
      super.h_();
      if(this.inProcess && this.arm != null) {
         this.arm.speed = 0.0D;
         int var1 = 2 + this.powerProvider.energyStored / 1000;
         int var2 = this.powerProvider.useEnergy(var1, var1, true);
         if(var2 > 0) {
            this.arm.doMove(0.015D + (double)((float)var2 / 200.0F));
         }
      }

      if(this.arm != null) {
         this.headPosX = this.arm.headPosX;
         this.headPosY = this.arm.headPosY;
         this.headPosZ = this.arm.headPosZ;
         this.speed = this.arm.speed;
      }

   }

   public void doWork() {
      if(!APIProxy.isClient(this.world)) {
         if(!this.inProcess) {
            if(this.isDigging) {
               this.createUtilsIfNeeded();
               if(this.bluePrintBuilder != null) {
                  if(this.bluePrintBuilder.done && this.nextBlockForBluePrint != null) {
                     this.bluePrintBuilder.done = false;
                     this.box.createLasers(this.world, LaserKind.Stripes);
                  }

                  if(!this.bluePrintBuilder.done) {
                     this.powerProvider.configure(20, 25, 25, 25, MAX_ENERGY);
                     if(this.powerProvider.useEnergy(25, 25, true) == 25) {
                        this.powerProvider.timeTracker.markTime(this.world);
                        BlockContents var1 = this.bluePrintBuilder.findNextBlock(this.world);
                        int var2 = this.world.getTypeId(var1.x, var1.y, var1.z);
                        if(var1 != null) {
                           if(!Utils.softBlock(var2)) {
                              this.world.setTypeId(var1.x, var1.y, var1.z, 0);
                           } else if(var1.blockId != 0) {
                              this.world.setTypeId(var1.x, var1.y, var1.z, var1.blockId);
                           }
                        }

                     }
                  } else {
                     this.powerProvider.configure(20, 30, 200, 50, MAX_ENERGY);
                     if(!this.findTarget(true)) {
                        this.arm.setTarget((double)this.box.xMin + this.arm.sizeX / 2.0D, (double)(this.y + 2), (double)this.box.zMin + this.arm.sizeX / 2.0D);
                        this.isDigging = false;
                     }

                     this.inProcess = true;
                     if(APIProxy.isServerSide()) {
                        this.sendNetworkUpdate();
                     }

                  }
               }
            }
         }
      }
   }

   public boolean findTarget(boolean var1) {
      boolean[][] var2 = new boolean[this.bluePrintBuilder.bluePrint.sizeX - 2][this.bluePrintBuilder.bluePrint.sizeZ - 2];

      int var3;
      int var4;
      for(var3 = 0; var3 < this.bluePrintBuilder.bluePrint.sizeX - 2; ++var3) {
         for(var4 = 0; var4 < this.bluePrintBuilder.bluePrint.sizeZ - 2; ++var4) {
            var2[var3][var4] = false;
         }
      }

      for(var3 = this.y + 3; var3 >= 0; --var3) {
         int var5;
         byte var6;
         if(var3 % 2 == 0) {
            var4 = 0;
            var5 = this.bluePrintBuilder.bluePrint.sizeX - 2;
            var6 = 1;
         } else {
            var4 = this.bluePrintBuilder.bluePrint.sizeX - 3;
            var5 = -1;
            var6 = -1;
         }

         for(int var7 = var4; var7 != var5; var7 += var6) {
            int var8;
            int var9;
            byte var10;
            if(var7 % 2 == var3 % 2) {
               var8 = 0;
               var9 = this.bluePrintBuilder.bluePrint.sizeZ - 2;
               var10 = 1;
            } else {
               var8 = this.bluePrintBuilder.bluePrint.sizeZ - 3;
               var9 = -1;
               var10 = -1;
            }

            for(int var11 = var8; var11 != var9; var11 += var10) {
               if(!var2[var7][var11]) {
                  int var12 = this.box.xMin + var7 + 1;
                  int var14 = this.box.zMin + var11 + 1;
                  int var15 = this.world.getTypeId(var12, var3, var14);
                  if(this.blockDig(var15)) {
                     var2[var7][var11] = true;
                  } else if(this.canDig(var15)) {
                     if(var1) {
                        this.arm.setTarget((double)var12, (double)(var3 + 1), (double)var14);
                        this.targetX = (int)this.arm.targetX;
                        this.targetY = (int)this.arm.targetY;
                        this.targetZ = (int)this.arm.targetZ;
                     }

                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      BuildCraftCore.powerFramework.loadPowerProvider(this, var1);
      if(var1.hasKey("box")) {
         this.box.initialize(var1.k("box"));
         this.loadDefaultBoundaries = false;
      } else if(var1.hasKey("xSize")) {
         int var2 = var1.e("xMin");
         int var3 = var1.e("zMin");
         int var4 = var1.e("xSize");
         int var5 = var1.e("ySize");
         int var6 = var1.e("zSize");
         this.box.initialize(var2, this.y, var3, var2 + var4 - 1, this.y + var5 - 1, var3 + var6 - 1);
         this.loadDefaultBoundaries = false;
      } else {
         this.loadDefaultBoundaries = true;
      }

      this.targetX = var1.e("targetX");
      this.targetY = var1.e("targetY");
      this.targetZ = var1.e("targetZ");
      if(var1.m("hasArm")) {
         NBTTagCompound var7 = var1.k("arm");
         this.arm = new EntityMechanicalArm(this.world);
         this.arm.e(var7);
         this.arm.listener = this;
         this.loadArm = true;
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      BuildCraftCore.powerFramework.savePowerProvider(this, var1);
      var1.a("targetX", this.targetX);
      var1.a("targetY", this.targetY);
      var1.a("targetZ", this.targetZ);
      var1.a("hasArm", this.arm != null);
      NBTTagCompound var2;
      if(this.arm != null) {
         var2 = new NBTTagCompound();
         var1.a("arm", var2);
         this.arm.d(var2);
      }

      var2 = new NBTTagCompound();
      this.box.writeToNBT(var2);
      var1.a("box", var2);
   }

   public void positionReached(EntityMechanicalArm var1) {
      this.inProcess = false;
      if(!APIProxy.isClient(this.world)) {
         int var2 = this.targetX;
         int var3 = this.targetY - 1;
         int var4 = this.targetZ;
         int var5 = this.world.getTypeId(var2, var3, var4);
         if(this.canDig(var5)) {
            this.powerProvider.timeTracker.markTime(this.world);
            ItemStack var6 = BuildCraftBlockUtil.getItemStackFromBlock(this.world, var2, var3, var4);
            if(var6 != null) {
               boolean var7 = false;
               StackUtil var8 = new StackUtil(var6);
               var7 = var8.addToRandomInventory(this, Orientations.Unknown);
               if(!var7 || var8.items.count > 0) {
                  var7 = Utils.addToRandomPipeEntry(this, Orientations.Unknown, var8.items);
               }

               if(!var7) {
                  float var9 = this.world.random.nextFloat() * 0.8F + 0.1F;
                  float var10 = this.world.random.nextFloat() * 0.8F + 0.1F;
                  float var11 = this.world.random.nextFloat() * 0.8F + 0.1F;
                  EntityItem var12 = new EntityItem(this.world, (double)((float)this.x + var9), (double)((float)this.y + var10 + 0.5F), (double)((float)this.z + var11), var8.items);
                  float var13 = 0.05F;
                  var12.motX = (double)((float)this.world.random.nextGaussian() * var13);
                  var12.motY = (double)((float)this.world.random.nextGaussian() * var13 + 1.0F);
                  var12.motZ = (double)((float)this.world.random.nextGaussian() * var13);
                  this.world.addEntity(var12);
               }
            }

            this.world.setTypeId(var2, var3, var4, 0);
         }

      }
   }

   private boolean blockDig(int var1) {
      return var1 == Block.BEDROCK.id || var1 == Block.STATIONARY_LAVA.id || var1 == Block.LAVA.id;
   }

   private boolean canDig(int var1) {
      return !this.blockDig(var1) && !Utils.softBlock(var1) && var1 != Block.SNOW.id;
   }

   public void destroy() {
      if(this.arm != null) {
         this.arm.die();
      }

      this.box.deleteLasers();
   }

   public boolean isActive() {
      return this.isDigging;
   }

   private void setBoundaries(boolean var1) {
      Object var2 = null;
      if(!var1) {
         var2 = Utils.getNearbyAreaProvider(this.world, this.x, this.y, this.z);
      }

      if(var2 == null) {
         var2 = new DefaultAreaProvider(this.x, this.y, this.z, this.x + 10, this.y + 4, this.z + 10);
         var1 = true;
      }

      int var3 = ((IAreaProvider)var2).xMax() - ((IAreaProvider)var2).xMin() + 1;
      int var4 = ((IAreaProvider)var2).yMax() - ((IAreaProvider)var2).yMin() + 1;
      int var5 = ((IAreaProvider)var2).zMax() - ((IAreaProvider)var2).zMin() + 1;
      if(var3 < 3 || var5 < 3) {
         var2 = new DefaultAreaProvider(this.x, this.y, this.z, this.x + 10, this.y + 4, this.z + 10);
         var1 = true;
      }

      var3 = ((IAreaProvider)var2).xMax() - ((IAreaProvider)var2).xMin() + 1;
      var4 = ((IAreaProvider)var2).yMax() - ((IAreaProvider)var2).yMin() + 1;
      var5 = ((IAreaProvider)var2).zMax() - ((IAreaProvider)var2).zMin() + 1;
      this.box.initialize((IAreaProvider)var2);
      if(var4 < 5) {
         var4 = 5;
         this.box.yMax = this.box.yMin + var4 - 1;
      }

      if(var1) {
         int var6 = 0;
         int var7 = 0;
         Orientations var8 = Orientations.values()[this.world.getData(this.x, this.y, this.z)].reverse();
         switch(var8) {
         case XPos:
            var6 = this.x + 1;
            var7 = this.z - 4 - 1;
            break;
         case XNeg:
            var6 = this.x - 9 - 2;
            var7 = this.z - 4 - 1;
            break;
         case ZPos:
            var6 = this.x - 4 - 1;
            var7 = this.z + 1;
            break;
         case ZNeg:
            var6 = this.x - 4 - 1;
            var7 = this.z - 9 - 2;
         }

         this.box.initialize(var6, this.y, var7, var6 + var3 - 1, this.y + var4 - 1, var7 + var5 - 1);
      }

      ((IAreaProvider)var2).removeFromWorld();
   }

   private void initializeBluePrintBuilder() {
      BluePrint var1 = new BluePrint(this.box.sizeX(), this.box.sizeY(), this.box.sizeZ());

      int var2;
      int var3;
      for(var2 = 0; var2 < var1.sizeX; ++var2) {
         for(var3 = 0; var3 < var1.sizeY; ++var3) {
            for(int var4 = 0; var4 < var1.sizeZ; ++var4) {
               var1.setBlockId(var2, var3, var4, 0);
            }
         }
      }

      for(var2 = 0; var2 < 2; ++var2) {
         for(var3 = 0; var3 < var1.sizeX; ++var3) {
            var1.setBlockId(var3, var2 * (this.box.sizeY() - 1), 0, BuildCraftFactory.frameBlock.id);
            var1.setBlockId(var3, var2 * (this.box.sizeY() - 1), var1.sizeZ - 1, BuildCraftFactory.frameBlock.id);
         }

         for(var3 = 0; var3 < var1.sizeZ; ++var3) {
            var1.setBlockId(0, var2 * (this.box.sizeY() - 1), var3, BuildCraftFactory.frameBlock.id);
            var1.setBlockId(var1.sizeX - 1, var2 * (this.box.sizeY() - 1), var3, BuildCraftFactory.frameBlock.id);
         }
      }

      for(var2 = 1; var2 < this.box.sizeY(); ++var2) {
         var1.setBlockId(0, var2, 0, BuildCraftFactory.frameBlock.id);
         var1.setBlockId(0, var2, var1.sizeZ - 1, BuildCraftFactory.frameBlock.id);
         var1.setBlockId(var1.sizeX - 1, var2, 0, BuildCraftFactory.frameBlock.id);
         var1.setBlockId(var1.sizeX - 1, var2, var1.sizeZ - 1, BuildCraftFactory.frameBlock.id);
      }

      this.bluePrintBuilder = new BluePrintBuilder(var1, this.box.xMin, this.y, this.box.zMin);
   }

   public void postPacketHandling(Packet230ModLoader var1) {
      super.postPacketHandling(var1);
      this.createUtilsIfNeeded();
      if(this.arm != null) {
         this.arm.setHeadPosition(this.headPosX, this.headPosY, this.headPosZ);
         this.arm.setTarget((double)this.targetX, (double)this.targetY, (double)this.targetZ);
         this.arm.speed = this.speed;
      }

   }

   public void initialize() {
      super.initialize();
      if(!APIProxy.isClient(this.world)) {
         this.createUtilsIfNeeded();
      }

      this.sendNetworkUpdate();
   }

   public void setPowerProvider(PowerProvider var1) {
      var1 = this.powerProvider;
   }

   public PowerProvider getPowerProvider() {
      return this.powerProvider;
   }

   public boolean manageLiquids() {
      return false;
   }

   public boolean manageSolids() {
      return true;
   }
}
