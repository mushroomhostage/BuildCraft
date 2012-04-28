package buildcraft.factory;


public class RefineryRecipe {

   public final int sourceId1;
   public final int sourceId2;
   public final int sourceQty1;
   public final int sourceQty2;
   public final int energy;
   public final int resultId;
   public final int resultQty;
   public final int delay;


   public RefineryRecipe(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.sourceId1 = var1;
      this.sourceId2 = var3;
      this.sourceQty1 = var2;
      this.sourceQty2 = var4;
      this.energy = var5;
      this.resultId = var6;
      this.resultQty = var7;
      this.delay = var8;
   }
}
