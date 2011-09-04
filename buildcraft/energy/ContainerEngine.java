package buildcraft.energy;

import buildcraft.energy.ContainerEngineRoot;
import buildcraft.energy.TileEngine;
import net.minecraft.server.ICrafting;
import net.minecraft.server.InventoryPlayer;

public class ContainerEngine extends ContainerEngineRoot {

   public ContainerEngine(InventoryPlayer var1, TileEngine var2) {
      super(var1, var2);
   }

   public void a(ICrafting var1) {
      super.a(var1);
      var1.a(this, 0, this.engine.scaledBurnTime);
   }

   public void a() {
      super.a();

      for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
         ICrafting var2 = (ICrafting)this.listeners.get(var1);
         if(this.scaledBurnTime != this.engine.scaledBurnTime) {
            var2.a(this, 0, this.engine.scaledBurnTime);
         }
      }

      this.scaledBurnTime = this.engine.scaledBurnTime;
   }
}
