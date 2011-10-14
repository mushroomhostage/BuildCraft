package net.minecraft.server;

import buildcraft.energy.TileEngine;
import java.util.Random;
import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class mod_BuildCraftEnergy extends BaseModMp {

   public static mod_BuildCraftEnergy instance;


   public void ModsLoaded() {
      super.ModsLoaded();
      BuildCraftEnergy.ModsLoaded();
      ModLoader.RegisterTileEntity(TileEngine.class, "net.minecraft.server.buildcraft.energy.Engine");
      instance = this;
   }

   public String Version() {
      return "2.2.1";
   }

   public void GenerateSurface(World var1, Random var2, int var3, int var4) {
      BuildCraftEnergy.generateSurface(var1, var2, var3, var4);
   }
}
