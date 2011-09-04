package buildcraft.factory;

import buildcraft.core.EntityBlock;
import buildcraft.factory.IArmListener;
import net.minecraft.server.BuildCraftFactory;
import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityMechanicalArm extends Entity {

   double sizeX;
   double sizeZ;
   EntityBlock xArm;
   EntityBlock yArm;
   EntityBlock zArm;
   EntityBlock head;
   double angle;
   public double targetX;
   public double targetY;
   public double targetZ;
   public double headPosX;
   public double headPosY;
   public double headPosZ;
   public double speed = 0.03D;
   double baseY;
   IArmListener listener;
   boolean inProgressionXZ = false;
   boolean inProgressionY = false;


   public EntityMechanicalArm(World var1) {
      super(var1);
   }

   public EntityMechanicalArm(World var1, double var2, double var4, double var6, double var8, double var10) {
      super(var1);
      this.setPosition(var2, var4, var6);
      this.motX = 0.0D;
      this.motY = 0.0D;
      this.motZ = 0.0D;
      this.lastX = var2;
      this.lastY = var4;
      this.lastZ = var6;
      this.sizeX = var10;
      this.sizeZ = var8;
      this.bt = true;
      this.baseY = var4;
      this.headPosX = var2;
      this.headPosY = var4 - 2.0D;
      this.headPosZ = var6;
      this.setTarget(this.headPosX, this.headPosY, this.headPosZ);
      this.inProgressionXZ = false;
      this.inProgressionY = false;
      this.xArm = new EntityBlock(var1, var2, var4, var6, var8, 0.5D, 0.5D);
      this.xArm.texture = BuildCraftFactory.drillTexture;
      var1.addEntity(this.xArm);
      this.yArm = new EntityBlock(var1, var2, var4, var6, 0.5D, 1.0D, 0.5D);
      this.yArm.texture = BuildCraftFactory.drillTexture;
      var1.addEntity(this.yArm);
      this.zArm = new EntityBlock(var1, var2, var4, var6, 0.5D, 0.5D, var10);
      this.zArm.texture = BuildCraftFactory.drillTexture;
      var1.addEntity(this.zArm);
      this.head = new EntityBlock(var1, var2, var4, var6, 0.2D, 1.0D, 0.2D);
      this.head.texture = 42;
      var1.addEntity(this.head);
      this.head.shadowSize = 1.0F;
      this.updatePosition();
   }

   protected void b() {}

   protected void a(NBTTagCompound var1) {
      this.sizeX = var1.h("sizeX");
      this.sizeZ = var1.h("sizeZ");
      this.targetX = var1.h("targetX");
      this.targetY = var1.h("targetY");
      this.targetZ = var1.h("targetZ");
      this.angle = var1.h("angle");
      this.headPosX = var1.h("headPosX");
      this.headPosY = var1.h("headPosY");
      this.headPosZ = var1.h("headPosZ");
      this.baseY = var1.h("baseY");
      this.speed = var1.h("speed");
      this.inProgressionXZ = var1.m("progressionXY");
      this.inProgressionY = var1.m("progressionY");
      NBTTagCompound var2 = var1.k("xArm");
      NBTTagCompound var3 = var1.k("yArm");
      NBTTagCompound var4 = var1.k("zArm");
      NBTTagCompound var5 = var1.k("head");
      this.xArm = new EntityBlock(this.world);
      this.yArm = new EntityBlock(this.world);
      this.zArm = new EntityBlock(this.world);
      this.head = new EntityBlock(this.world);
      this.xArm.texture = BuildCraftFactory.drillTexture;
      this.yArm.texture = BuildCraftFactory.drillTexture;
      this.zArm.texture = BuildCraftFactory.drillTexture;
      this.head.texture = 42;
      this.xArm.e(var2);
      this.yArm.e(var3);
      this.zArm.e(var4);
      this.head.e(var5);
   }

   protected void b(NBTTagCompound var1) {
      var1.a("sizeX", this.sizeX);
      var1.a("sizeZ", this.sizeZ);
      var1.a("targetX", this.targetX);
      var1.a("targetY", this.targetY);
      var1.a("targetZ", this.targetZ);
      var1.a("angle", this.angle);
      var1.a("headPosX", this.headPosX);
      var1.a("headPosY", this.headPosY);
      var1.a("headPosZ", this.headPosZ);
      var1.a("baseY", this.baseY);
      var1.a("speed", this.speed);
      var1.a("progressionXY", this.inProgressionXZ);
      var1.a("progressionY", this.inProgressionY);
      NBTTagCompound var2 = new NBTTagCompound();
      NBTTagCompound var3 = new NBTTagCompound();
      NBTTagCompound var4 = new NBTTagCompound();
      NBTTagCompound var5 = new NBTTagCompound();
      var1.a("xArm", var2);
      var1.a("yArm", var3);
      var1.a("zArm", var4);
      var1.a("head", var5);
      this.xArm.d(var2);
      this.yArm.d(var3);
      this.zArm.d(var4);
      this.head.d(var5);
   }

   public void setTarget(double var1, double var3, double var5) {
      this.targetX = var1;
      this.targetY = var3;
      this.targetZ = var5;
      double var7 = this.targetX - this.headPosX;
      double var9 = this.targetZ - this.headPosZ;
      this.angle = Math.atan2(var9, var7);
      this.inProgressionXZ = true;
      this.inProgressionY = true;
   }

   public double[] getTarget() {
      return new double[]{this.targetX, this.targetY, this.targetZ};
   }

   public void m_() {
      if(this.speed > 0.0D) {
         this.doMove(this.speed);
      }

   }

   public void doMove(double var1) {
      super.m_();
      if(this.inProgressionXZ) {
         if(Math.abs(this.targetX - this.headPosX) < var1 * 2.0D && Math.abs(this.targetZ - this.headPosZ) < var1 * 2.0D) {
            this.headPosX = this.targetX;
            this.headPosZ = this.targetZ;
            this.inProgressionXZ = false;
            if(this.listener != null && !this.inProgressionY) {
               this.listener.positionReached(this);
            }
         } else {
            this.headPosX += Math.cos(this.angle) * var1;
            this.headPosZ += Math.sin(this.angle) * var1;
         }
      }

      if(this.inProgressionY) {
         if(Math.abs(this.targetY - this.headPosY) < var1 * 2.0D) {
            this.headPosY = this.targetY;
            this.inProgressionY = false;
            if(this.listener != null && !this.inProgressionXZ) {
               this.listener.positionReached(this);
            }
         } else if(this.targetY > this.headPosY) {
            this.headPosY += var1 / 2.0D;
         } else {
            this.headPosY -= var1 / 2.0D;
         }
      }

      this.updatePosition();
   }

   public void updatePosition() {
      this.xArm.setPosition(this.xArm.locX, this.xArm.locY, this.headPosZ + 0.25D);
      this.yArm.jSize = this.baseY - this.headPosY - 1.0D;
      this.yArm.setPosition(this.headPosX + 0.25D, this.headPosY + 1.0D, this.headPosZ + 0.25D);
      this.zArm.setPosition(this.headPosX + 0.25D, this.zArm.locY, this.zArm.locZ);
      this.head.setPosition(this.headPosX + 0.4D, this.headPosY, this.headPosZ + 0.4D);
   }

   public void joinToWorld(World var1) {
      super.world = var1;
      this.xArm.world = var1;
      this.yArm.world = var1;
      this.zArm.world = var1;
      this.head.world = var1;
      var1.addEntity(this);
      var1.addEntity(this.xArm);
      var1.addEntity(this.yArm);
      var1.addEntity(this.zArm);
      var1.addEntity(this.head);
   }

   public void die() {
      this.xArm.die();
      this.yArm.die();
      this.zArm.die();
      this.head.die();
      super.die();
   }

   public double[] getHeadPosition() {
      return new double[]{this.headPosX, this.headPosY, this.headPosZ};
   }

   public void setHeadPosition(double var1, double var3, double var5) {
      this.headPosX = var1;
      this.headPosY = var3;
      this.headPosZ = var5;
   }
}
