package net.minecraft.server;

import forge.NetworkMod;
import net.minecraft.server.BuildCraftFactory;

public class mod_BuildCraftFactory extends NetworkMod {

   public static mod_BuildCraftFactory instance;


   public mod_BuildCraftFactory() {
      instance = this;
   }

   public void modsLoaded() {
      super.modsLoaded();
      BuildCraftFactory.initialize();
      instance = this;
   }

   public String getVersion() {
      return "2.2.14";
   }

   public void load() {
      BuildCraftFactory.load();
   }

   public boolean clientSideRequired() {
      return true;
   }

   public boolean serverSideRequired() {
      return false;
   }
}
