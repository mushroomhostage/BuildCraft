package buildcraft.api;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.PowerProvider;
import net.minecraft.server.NBTTagCompound;

public abstract class PowerFramework {

   private static String baseNBTName = "net.minecraft.src.buildcarft.Power";


   public abstract PowerProvider createPowerProvider();

   public void loadPowerProvider(IPowerReceptor var1, NBTTagCompound var2) {
      PowerProvider var3 = this.createPowerProvider();
      if(var2.hasKey(baseNBTName)) {
         NBTTagCompound var4 = var2.k(baseNBTName);
         if(var4.getString("class").equals(this.getClass().getName())) {
            var3.readFromNBT(var4.k("contents"));
         }
      }

      var1.setPowerProvider(var3);
   }

   public void savePowerProvider(IPowerReceptor var1, NBTTagCompound var2) {
      PowerProvider var3 = var1.getPowerProvider();
      if(var3 != null) {
         NBTTagCompound var4 = new NBTTagCompound();
         var4.setString("class", this.getClass().getName());
         NBTTagCompound var5 = new NBTTagCompound();
         var3.writeToNBT(var5);
         var4.a("contents", var5);
         var2.a(baseNBTName, var4);
      }
   }

}
