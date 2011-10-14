package buildcraft.api;

import buildcraft.api.LiquidData;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.Block;
import net.minecraft.server.World;

public class API {

   public static LinkedList liquids = new LinkedList();
   public static final int BUCKET_VOLUME = 1000;


   public static int getLiquidForBucket(int var0) {
      Iterator var1 = liquids.iterator();

      LiquidData var2;
      do {
         if(!var1.hasNext()) {
            return 0;
         }

         var2 = (LiquidData)var1.next();
      } while(var2.filledBucketId != var0);

      return var2.liquidId;
   }

   public static int getBucketForLiquid(int var0) {
      Iterator var1 = liquids.iterator();

      LiquidData var2;
      do {
         if(!var1.hasNext()) {
            return 0;
         }

         var2 = (LiquidData)var1.next();
      } while(var2.liquidId != var0);

      return var2.filledBucketId;
   }

   public static boolean softBlock(int var0) {
      return var0 == 0 || var0 == Block.STATIONARY_WATER.id || var0 == Block.WATER.id || Block.byId[var0] == null;
   }

   public static boolean unbreakableBlock(int var0) {
      return var0 == Block.BEDROCK.id || var0 == Block.STATIONARY_LAVA.id || var0 == Block.LAVA.id;
   }

   public static void breakBlock(World var0, int var1, int var2, int var3) {
      int var4 = var0.getTypeId(var1, var2, var3);
      if(var4 != 0) {
         Block.byId[var4].g(var0, var1, var2, var3, var0.getData(var1, var2, var3));
      }

      var0.setTypeId(var1, var2, var3, 0);
   }

}
