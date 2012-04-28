package buildcraft.core;

import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityBlock extends Entity {

   public int texture;
   public float shadowSize;
   public float rotationX;
   public float rotationY;
   public float rotationZ;
   public double iSize;
   public double jSize;
   public double kSize;


   public EntityBlock(World var1) {
      super(var1);
      this.texture = -1;
      this.shadowSize = 0.0F;
      this.rotationX = 0.0F;
      this.rotationY = 0.0F;
      this.rotationZ = 0.0F;
      this.bf = false;
      this.bQ = true;
      this.fireProof = true;
   }

   public EntityBlock(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      this(var1);
      this.motX = 0.0D;
      this.motY = 0.0D;
      this.motZ = 0.0D;
      this.lastX = var2;
      this.lastY = var4;
      this.lastZ = var6;
      this.iSize = var8;
      this.jSize = var10;
      this.kSize = var12;
      this.setPosition(var2, var4, var6);
   }

   public EntityBlock(World var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      this(var1, var2, var4, var6, var8, var10, var12);
      this.texture = var14;
   }

   public void setPosition(double var1, double var3, double var5) {
      this.locX = var1;
      this.locY = var3;
      this.locZ = var5;
      this.boundingBox.a = this.locX;
      this.boundingBox.b = this.locY;
      this.boundingBox.c = this.locZ;
      this.boundingBox.d = this.locX + this.iSize;
      this.boundingBox.e = this.locY + this.jSize;
      this.boundingBox.f = this.locZ + this.kSize;
   }

   public void move(double var1, double var3, double var5) {
      this.setPosition(this.locX + var1, this.locY + var3, this.locZ + var5);
   }

   protected void b() {}

   protected void a(NBTTagCompound var1) {
      this.iSize = var1.getDouble("iSize");
      this.jSize = var1.getDouble("jSize");
      this.kSize = var1.getDouble("kSize");
   }

   protected void b(NBTTagCompound var1) {
      var1.setDouble("iSize", this.iSize);
      var1.setDouble("jSize", this.jSize);
      var1.setDouble("kSize", this.kSize);
   }

   public boolean o_() {
      return !this.dead;
   }
}
