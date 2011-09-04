package buildcraft.api;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.SafeTimeTracker;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class EntityPassiveItem {

   public float speed;
   public ItemStack item;
   public TileEntity container;
   public SafeTimeTracker synchroTracker;
   public int deterministicRandomization;
   World worldObj;
   public double posX;
   public double posY;
   public double posZ;
   public int entityId;
   private static int maxId = 0;


   public EntityPassiveItem(World var1) {
      this.speed = 0.01F;
      this.synchroTracker = new SafeTimeTracker();
      this.deterministicRandomization = 0;
      this.entityId = maxId++;
      if(maxId > Integer.MAX_VALUE) {
         maxId = 0;
      }

   }

   public EntityPassiveItem(World var1, double var2, double var4, double var6) {
      this(var1);
      this.posX = var2;
      this.posY = var4;
      this.posZ = var6;
      this.worldObj = var1;
   }

   public void setPosition(double var1, double var3, double var5) {
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
   }

   public EntityPassiveItem(World var1, double var2, double var4, double var6, ItemStack var8) {
      this(var1, var2, var4, var6);
      this.item = var8.cloneItemStack();
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.posX = var1.h("x");
      this.posY = var1.h("y");
      this.posZ = var1.h("z");
      this.speed = var1.g("speed");
      this.item = new ItemStack(var1.k("Item"));
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.a("x", this.posX);
      var1.a("y", this.posY);
      var1.a("z", this.posZ);
      var1.a("speed", this.speed);
      NBTTagCompound var2 = new NBTTagCompound();
      this.item.a(var2);
      var1.a("Item", var2);
   }

   public EntityItem toEntityItem(World var1, Orientations var2) {
      if(!APIProxy.isClient(this.worldObj)) {
         Position var3 = new Position(0.0D, 0.0D, 0.0D, var2);
         var3.moveForwards(0.1D + (double)(this.speed * 2.0F));
         EntityItem var4 = new EntityItem(var1, this.posX, this.posY, this.posZ, this.item);
         float var5 = 0.0F + var1.random.nextFloat() * 0.04F - 0.02F;
         var4.motX = (double)((float)var1.random.nextGaussian() * var5) + var3.x;
         var4.motY = (double)((float)var1.random.nextGaussian() * var5) + var3.y;
         var4.motZ = (double)((float)var1.random.nextGaussian() * var5) + var3.z;
         var1.addEntity(var4);
         var4.pickupDelay = 20;
         return var4;
      } else {
         return null;
      }
   }

}
