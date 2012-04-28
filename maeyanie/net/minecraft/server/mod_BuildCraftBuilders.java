package net.minecraft.server;

import forge.NetworkMod;
import net.minecraft.server.BuildCraftBuilders;

public class mod_BuildCraftBuilders extends NetworkMod {

   public static mod_BuildCraftBuilders instance;


   public mod_BuildCraftBuilders() {
      instance = this;
   }

   public void modsLoaded() {
      super.modsLoaded();
      BuildCraftBuilders.initialize();
   }

   public String getVersion() {
      return "2.2.14";
   }

   public void load() {
      BuildCraftBuilders.load();
   }

   public boolean clientSideRequired() {
      return true;
   }

   public boolean serverSideRequired() {
      return false;
   }
}
