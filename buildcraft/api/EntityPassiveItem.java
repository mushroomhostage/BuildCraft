package buildcraft.api;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.SafeTimeTracker;
import java.util.TreeMap;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class EntityPassiveItem {

   public static TreeMap allEntities = new TreeMap();
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
      this(var1, maxId != Integer.MAX_VALUE?++maxId:(maxId = 0));
   }

   public EntityPassiveItem(World var1, int var2) {
      this.speed = 0.01F;
      this.synchroTracker = new SafeTimeTracker();
      this.deterministicRandomization = 0;
      this.entityId = var2;
      allEntities.put(Integer.valueOf(this.entityId), this);
      this.worldObj = var1;
   }

   public static EntityPassiveItem getOrCreate(World var0, int var1) {
      return allEntities.containsKey(Integer.valueOf(var1))?(EntityPassiveItem)allEntities.get(Integer.valueOf(var1)):new EntityPassiveItem(var0, var1);
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
      this.posX = var1.getDouble("x");
      this.posY = var1.getDouble("y");
      this.posZ = var1.getDouble("z");
      this.speed = var1.getFloat("speed");
      this.item = ItemStack.a(var1.getCompound("Item"));
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.setDouble("x", this.posX);
      var1.setDouble("y", this.posY);
      var1.setDouble("z", this.posZ);
      var1.setFloat("speed", this.speed);
      NBTTagCompound var2 = new NBTTagCompound();
      this.item.b(var2);
      var1.setCompound("Item", var2);
   }

   public EntityItem toEntityItem(Orientations var1) {
      if(!APIProxy.isClient(this.worldObj)) {
         Position var2 = new Position(0.0D, 0.0D, 0.0D, var1);
         var2.moveForwards(0.1D + (double)(this.speed * 2.0F));
         EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.item);
         float var4 = 0.0F + this.worldObj.random.nextFloat() * 0.04F - 0.02F;
         var3.motX = (double)((float)this.worldObj.random.nextGaussian() * var4) + var2.x;
         var3.motY = (double)((float)this.worldObj.random.nextGaussian() * var4) + var2.y;
         var3.motZ = (double)((float)this.worldObj.random.nextGaussian() * var4) + var2.z;
         this.worldObj.addEntity(var3);
         this.remove();
         var3.pickupDelay = 20;
         return var3;
      } else {
         return null;
      }
   }

   public void remove() {
      if(allEntities.containsKey(Integer.valueOf(this.entityId))) {
         allEntities.remove(Integer.valueOf(this.entityId));
      }

   }

   public float getEntityBrightness(float var1) {
      int var2 = MathHelper.floor(this.posX);
      int var3 = MathHelper.floor(this.posZ);
      this.worldObj.getClass();
      if(this.worldObj.isLoaded(var2, 64, var3)) {
         double var4 = 0.66D;
         int var6 = MathHelper.floor(this.posY + var4);
         return this.worldObj.m(var2, var6, var3);
      } else {
         return 0.0F;
      }
   }

   public boolean isCorrupted() {
      return this.item == null || this.item.count <= 0 || Item.byId[this.item.id] == null;
   }

}
