package net.minecraft.server;

import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftFactory;

public class mod_BuildCraftFactory extends BaseModMp {

   public static mod_BuildCraftFactory instance;


   public void ModsLoaded() {
      super.ModsLoaded();
      BuildCraftFactory.initialize();
      instance = this;
   }

   public String Version() {
      return "2.1.1";
   }
}
