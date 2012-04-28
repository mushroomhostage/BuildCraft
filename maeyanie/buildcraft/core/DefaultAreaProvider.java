package buildcraft.core;

import buildcraft.api.IAreaProvider;

public class DefaultAreaProvider implements IAreaProvider {

   int xMin;
   int yMin;
   int zMin;
   int xMax;
   int yMax;
   int zMax;


   public DefaultAreaProvider(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.xMin = var1;
      this.xMax = var4;
      this.yMin = var2;
      this.yMax = var5;
      this.zMin = var3;
      this.zMax = var6;
   }

   public int xMin() {
      return this.xMin;
   }

   public int yMin() {
      return this.yMin;
   }

   public int zMin() {
      return this.zMin;
   }

   public int xMax() {
      return this.xMax;
   }

   public int yMax() {
      return this.yMax;
   }

   public int zMax() {
      return this.zMax;
   }

   public void removeFromWorld() {}
}
