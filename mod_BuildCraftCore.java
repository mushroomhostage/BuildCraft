package net.minecraft.server;

import buildcraft.core.ClassMapping;
import java.util.Date;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.ModLoader;

public class mod_BuildCraftCore extends BaseModMp {

   public static mod_BuildCraftCore instance;
   BuildCraftCore proxy = new BuildCraftCore();
   long lastReport = 0L;


   public mod_BuildCraftCore() {
      instance = this;
   }

   public static void initialize() {
      BuildCraftCore.initialize();
   }

   public void ModsLoaded() {
      initialize();
      BuildCraftCore.initializeModel(this);
      ModLoader.SetInGameHook(this, true, true);
   }

   public String Version() {
      return version();
   }

   public static String version() {
      return "2.2.10";
   }

   public void OnTickInGame(MinecraftServer var1) {
      if(BuildCraftCore.trackNetworkUsage) {
         Date var2 = new Date();
         if(var2.getTime() - this.lastReport > 10000L) {
            this.lastReport = var2.getTime();
            int var3 = ClassMapping.report();
            System.out.println("BuildCraft badwith = " + var3 / 10 + " bytes / second");
            System.out.println();
         }
      }

   }
}
