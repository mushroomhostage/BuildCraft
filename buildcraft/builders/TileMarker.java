package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.api.IAreaProvider;
import buildcraft.api.LaserKind;
import buildcraft.api.Position;
import buildcraft.api.TileNetworkData;
import buildcraft.core.EntityBlock;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.Utils;
import net.minecraft.server.BuildCraftBuilders;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.World;

public class TileMarker extends TileBuildCraft implements IAreaProvider {

   private static int maxSize = 64;
   @TileNetworkData
   public TileMarker.Origin origin = new TileMarker.Origin();
   private EntityBlock[] lasers;
   private EntityBlock[] signals;
   private Position initVectO;
   private Position[] initVect;


   public void switchSignals() {
      if(this.signals != null) {
         EntityBlock[] var1 = this.signals;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EntityBlock var4 = var1[var3];
            if(var4 != null) {
               APIProxy.removeEntity(var4);
            }
         }

         this.signals = null;
      }

      if(this.world.isBlockIndirectlyPowered(this.x, this.y, this.z)) {
         this.signals = new EntityBlock[6];
         if(!this.origin.isSet() || !this.origin.vect[0].isSet()) {
            this.signals[0] = Utils.createLaser(this.world, new Position((double)this.x, (double)this.y, (double)this.z), new Position((double)(this.x + maxSize - 1), (double)this.y, (double)this.z), LaserKind.Blue);
            this.signals[1] = Utils.createLaser(this.world, new Position((double)(this.x - maxSize + 1), (double)this.y, (double)this.z), new Position((double)this.x, (double)this.y, (double)this.z), LaserKind.Blue);
         }

         if(!this.origin.isSet() || !this.origin.vect[1].isSet()) {
            this.signals[2] = Utils.createLaser(this.world, new Position((double)this.x, (double)this.y, (double)this.z), new Position((double)this.x, (double)(this.y + maxSize - 1), (double)this.z), LaserKind.Blue);
            this.signals[3] = Utils.createLaser(this.world, new Position((double)this.x, (double)(this.y - maxSize + 1), (double)this.z), new Position((double)this.x, (double)this.y, (double)this.z), LaserKind.Blue);
         }

         if(!this.origin.isSet() || !this.origin.vect[2].isSet()) {
            this.signals[4] = Utils.createLaser(this.world, new Position((double)this.x, (double)this.y, (double)this.z), new Position((double)this.x, (double)this.y, (double)(this.z + maxSize - 1)), LaserKind.Blue);
            this.signals[5] = Utils.createLaser(this.world, new Position((double)this.x, (double)this.y, (double)(this.z - maxSize + 1)), new Position((double)this.x, (double)this.y, (double)this.z), LaserKind.Blue);
         }
      }

      if(APIProxy.isServerSide()) {
         this.sendNetworkUpdate();
      }

   }

   public void initialize() {
      super.initialize();
      this.switchSignals();
      if(this.initVectO != null) {
         this.origin = new TileMarker.Origin();
         this.origin.vectO = new TileMarker.TileWrapper((int)this.initVectO.x, (int)this.initVectO.y, (int)this.initVectO.z);

         for(int var1 = 0; var1 < 3; ++var1) {
            if(this.initVect[var1] != null) {
               this.linkTo((TileMarker)this.world.getTileEntity((int)this.initVect[var1].x, (int)this.initVect[var1].y, (int)this.initVect[var1].z), var1);
            }
         }
      }

   }

   public void tryConnection() {
      if(!APIProxy.isClient(this.world)) {
         for(int var1 = 0; var1 < 3; ++var1) {
            if(!this.origin.isSet() || !this.origin.vect[var1].isSet()) {
               this.setVect(var1);
            }
         }

         this.sendNetworkUpdate();
      }
   }

   void setVect(int var1) {
      int var2 = BuildCraftBuilders.markerBlock.id;
      int[] var3 = new int[]{this.x, this.y, this.z};
      if(!this.origin.isSet() || !this.origin.vect[var1].isSet()) {
         for(int var4 = 1; var4 < maxSize; ++var4) {
            var3[var1] += var4;
            int var5 = this.world.getTypeId(var3[0], var3[1], var3[2]);
            TileMarker var6;
            if(var5 == var2) {
               var6 = (TileMarker)this.world.getTileEntity(var3[0], var3[1], var3[2]);
               if(this.linkTo(var6, var1)) {
                  break;
               }
            }

            var3[var1] -= var4;
            var3[var1] -= var4;
            var5 = this.world.getTypeId(var3[0], var3[1], var3[2]);
            if(var5 == var2) {
               var6 = (TileMarker)this.world.getTileEntity(var3[0], var3[1], var3[2]);
               if(this.linkTo(var6, var1)) {
                  break;
               }
            }

            var3[var1] += var4;
         }
      }

   }

   private boolean linkTo(TileMarker var1, int var2) {
      if(var1 == null) {
         return false;
      } else if(this.origin.isSet() && var1.origin.isSet()) {
         return false;
      } else {
         if(!this.origin.isSet() && !var1.origin.isSet()) {
            this.origin = new TileMarker.Origin();
            var1.origin = this.origin;
            this.origin.vectO = new TileMarker.TileWrapper(this.x, this.y, this.z);
            this.origin.vect[var2] = new TileMarker.TileWrapper(var1.x, var1.y, var1.z);
         } else if(!this.origin.isSet()) {
            this.origin = var1.origin;
            this.origin.vect[var2] = new TileMarker.TileWrapper(this.x, this.y, this.z);
         } else {
            var1.origin = this.origin;
            this.origin.vect[var2] = new TileMarker.TileWrapper(var1.x, var1.y, var1.z);
         }

         this.origin.vectO.getMarker(this.world).createLasers();
         this.switchSignals();
         var1.switchSignals();
         return true;
      }
   }

   private void createLasers() {
      if(this.lasers != null) {
         EntityBlock[] var1 = this.lasers;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EntityBlock var4 = var1[var3];
            if(var4 != null) {
               APIProxy.removeEntity(var4);
            }
         }
      }

      this.lasers = new EntityBlock[12];
      TileMarker.Origin var5 = this.origin;
      if(!this.origin.vect[0].isSet()) {
         var5.xMin = this.origin.vectO.x;
         var5.xMax = this.origin.vectO.x;
      } else if(this.origin.vect[0].x < this.x) {
         var5.xMin = this.origin.vect[0].x;
         var5.xMax = this.x;
      } else {
         var5.xMin = this.x;
         var5.xMax = this.origin.vect[0].x;
      }

      if(!this.origin.vect[1].isSet()) {
         var5.yMin = this.origin.vectO.y;
         var5.yMax = this.origin.vectO.y;
      } else if(this.origin.vect[1].y < this.y) {
         var5.yMin = this.origin.vect[1].y;
         var5.yMax = this.y;
      } else {
         var5.yMin = this.y;
         var5.yMax = this.origin.vect[1].y;
      }

      if(!this.origin.vect[2].isSet()) {
         var5.zMin = this.origin.vectO.z;
         var5.zMax = this.origin.vectO.z;
      } else if(this.origin.vect[2].z < this.z) {
         var5.zMin = this.origin.vect[2].z;
         var5.zMax = this.z;
      } else {
         var5.zMin = this.z;
         var5.zMax = this.origin.vect[2].z;
      }

      this.lasers = Utils.createLaserBox(this.world, (double)var5.xMin, (double)var5.yMin, (double)var5.zMin, (double)var5.xMax, (double)var5.yMax, (double)var5.zMax, LaserKind.Red);
   }

   public int xMin() {
      return this.origin.isSet()?this.origin.xMin:this.x;
   }

   public int yMin() {
      return this.origin.isSet()?this.origin.yMin:this.y;
   }

   public int zMin() {
      return this.origin.isSet()?this.origin.zMin:this.z;
   }

   public int xMax() {
      return this.origin.isSet()?this.origin.xMax:this.x;
   }

   public int yMax() {
      return this.origin.isSet()?this.origin.yMax:this.y;
   }

   public int zMax() {
      return this.origin.isSet()?this.origin.zMax:this.z;
   }

   public void destroy() {
      TileMarker var1 = null;
      int var4;
      if(this.origin.isSet()) {
         var1 = this.origin.vectO.getMarker(this.world);
         TileMarker.Origin var2 = this.origin;
         int var5;
         if(var1 != null && var1.lasers != null) {
            EntityBlock[] var3 = var1.lasers;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
               EntityBlock var6 = var3[var5];
               if(var6 != null) {
                  var6.die();
               }
            }
         }

         TileMarker.TileWrapper[] var9 = var2.vect;
         var4 = var9.length;

         TileMarker var7;
         TileMarker.TileWrapper var11;
         for(var5 = 0; var5 < var4; ++var5) {
            var11 = var9[var5];
            var7 = var11.getMarker(this.world);
            if(var7 != null) {
               var7.lasers = null;
               if(var7 != this) {
                  var7.origin = new TileMarker.Origin();
               }
            }
         }

         var1.lasers = null;
         if(var1 != this) {
            var1.origin = new TileMarker.Origin();
         }

         var9 = var2.vect;
         var4 = var9.length;

         for(var5 = 0; var5 < var4; ++var5) {
            var11 = var9[var5];
            var7 = var11.getMarker(this.world);
            if(var7 != null) {
               var7.switchSignals();
            }
         }

         var1.switchSignals();
      }

      if(this.signals != null) {
         EntityBlock[] var8 = this.signals;
         int var10 = var8.length;

         for(var4 = 0; var4 < var10; ++var4) {
            EntityBlock var12 = var8[var4];
            if(var12 != null) {
               var12.die();
            }
         }
      }

      this.signals = null;
      if(APIProxy.isServerSide() && var1 != null && var1 != this) {
         var1.sendNetworkUpdate();
      }

   }

   public void removeFromWorld() {
      if(this.origin.isSet()) {
         TileMarker.Origin var1 = this.origin;
         TileMarker.TileWrapper[] var2 = (TileMarker.TileWrapper[])var1.vect.clone();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            TileMarker.TileWrapper var5 = var2[var4];
            if(var5.isSet()) {
               this.world.setTypeId(var5.x, var5.y, var5.z, 0);
               BuildCraftBuilders.markerBlock.g(this.world, var5.x, var5.y, var5.z, BuildCraftBuilders.markerBlock.id);
            }
         }

         this.world.setTypeId(var1.vectO.x, var1.vectO.y, var1.vectO.z, 0);
         BuildCraftBuilders.markerBlock.g(this.world, var1.vectO.x, var1.vectO.y, var1.vectO.z, BuildCraftBuilders.markerBlock.id);
      }
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKey("vectO")) {
         this.initVectO = new Position(var1.k("vectO"));
         this.initVect = new Position[3];

         for(int var2 = 0; var2 < 3; ++var2) {
            if(var1.hasKey("vect" + var2)) {
               this.initVect[var2] = new Position(var1.k("vect" + var2));
            }
         }
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      if(this.origin.isSet() && this.origin.vectO.getMarker(this.world) == this) {
         NBTTagCompound var2 = new NBTTagCompound();
         (new Position(this.origin.vectO.getMarker(this.world))).writeToNBT(var2);
         var1.a("vectO", var2);

         for(int var3 = 0; var3 < 3; ++var3) {
            if(this.origin.vect[var3].isSet()) {
               NBTTagCompound var4 = new NBTTagCompound();
               (new Position((double)this.origin.vect[var3].x, (double)this.origin.vect[var3].y, (double)this.origin.vect[var3].z)).writeToNBT(var4);
               var1.a("vect" + var3, var4);
            }
         }
      }

   }

   public Packet l() {
      return this.origin.vectO.getMarker(this.world) == this?super.l():null;
   }

   public Packet230ModLoader getUpdatePacket() {
      TileMarker var1 = this.origin.vectO.getMarker(this.world);
      if(var1 != this && var1 != null) {
         if(var1 != null) {
            var1.sendNetworkUpdate();
         }

         return null;
      } else {
         return super.getUpdatePacket();
      }
   }

   public void postPacketHandling(Packet230ModLoader var1) {
      super.postPacketHandling(var1);
      if(this.origin.vectO.isSet()) {
         this.origin.vectO.getMarker(this.world).switchSignals();
         TileMarker.TileWrapper[] var2 = this.origin.vect;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            TileMarker.TileWrapper var5 = var2[var4];
            TileMarker var6 = var5.getMarker(this.world);
            if(var6 != null) {
               var6.switchSignals();
            }
         }
      }

      this.createLasers();
   }


   public static class TileWrapper {

      @TileNetworkData
      public int x;
      @TileNetworkData
      public int y;
      @TileNetworkData
      public int z;
      private TileMarker marker;


      public TileWrapper() {
         this.x = Integer.MAX_VALUE;
         this.y = Integer.MAX_VALUE;
         this.z = Integer.MAX_VALUE;
      }

      public TileWrapper(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
      }

      public boolean isSet() {
         return this.x != Integer.MAX_VALUE;
      }

      public TileMarker getMarker(World var1) {
         if(!this.isSet()) {
            return null;
         } else {
            if(this.marker == null) {
               this.marker = (TileMarker)var1.getTileEntity(this.x, this.y, this.z);
            }

            return this.marker;
         }
      }

      public void reset() {
         this.x = Integer.MAX_VALUE;
         this.y = Integer.MAX_VALUE;
         this.z = Integer.MAX_VALUE;
      }
   }

   public static class Origin {

      @TileNetworkData
      public TileMarker.TileWrapper vectO = new TileMarker.TileWrapper();
      @TileNetworkData(
         staticSize = 3
      )
      public TileMarker.TileWrapper[] vect = new TileMarker.TileWrapper[]{new TileMarker.TileWrapper(), new TileMarker.TileWrapper(), new TileMarker.TileWrapper()};
      @TileNetworkData
      public int xMin;
      @TileNetworkData
      public int yMin;
      @TileNetworkData
      public int zMin;
      @TileNetworkData
      public int xMax;
      @TileNetworkData
      public int yMax;
      @TileNetworkData
      public int zMax;


      public boolean isSet() {
         return this.vectO.isSet();
      }
   }
}
