package net.minecraft.server;

import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftTransport;

public class mod_BuildCraftTransport extends BaseModMp {

   public static mod_BuildCraftTransport instance;


   public void ModsLoaded() {
      super.ModsLoaded();
      BuildCraftTransport.initialize();
      instance = this;
   }

   public String Version() {
      return "2.2.10";
   }
}
