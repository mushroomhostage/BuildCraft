package net.minecraft.server;

import forge.NetworkMod;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.ModLoader;

public class mod_BuildCraftTransport extends NetworkMod {

   public static mod_BuildCraftTransport instance;


   public mod_BuildCraftTransport() {
      instance = this;
   }

   public void modsLoaded() {
      super.modsLoaded();
      BuildCraftTransport.initialize();
      instance = this;
   }

   public String getVersion() {
      return "2.2.14";
   }

   public static void registerTilePipe(Class var0, String var1) {
      ModLoader.registerTileEntity(var0, var1);
   }

   public void load() {
      BuildCraftTransport.load();
   }

   public boolean clientSideRequired() {
      return true;
   }

   public boolean serverSideRequired() {
      return false;
   }
}
