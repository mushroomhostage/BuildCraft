package buildcraft.energy;

import buildcraft.core.PacketIds;
import buildcraft.core.Utils;
import buildcraft.energy.ContainerEngine;
import buildcraft.energy.TileEngine;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ModLoader;

public class EnergyProxy {

   public static void displayGUISteamEngine(EntityHuman var0, TileEngine var1) {
      ModLoader.OpenGUI(var0, Utils.packetIdToInt(PacketIds.EngineSteamGUI), var1, new ContainerEngine(var0.inventory, var1));
   }

   public static void displayGUICombustionEngine(EntityHuman var0, TileEngine var1) {
      ModLoader.OpenGUI(var0, Utils.packetIdToInt(PacketIds.EngineCombustionGUI), var1, new ContainerEngine(var0.inventory, var1));
   }
}
