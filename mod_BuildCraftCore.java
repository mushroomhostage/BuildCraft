package net.minecraft.server;

import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftCore;

public class mod_BuildCraftCore extends BaseModMp {

   public static mod_BuildCraftCore instance;
   BuildCraftCore proxy = new BuildCraftCore();


   public mod_BuildCraftCore() {
      instance = this;
   }

   public static void initialize() {
      BuildCraftCore.initialize();
   }

   public void ModsLoaded() {
      initialize();
      BuildCraftCore.initializeModel(this);
   }

   public String Version() {
      return version();
   }

   public static String version() {
      return "2.2.1";
   }
}
