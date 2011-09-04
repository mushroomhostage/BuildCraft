package buildcraft.api;

import net.minecraft.server.Entity;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class APIProxy {

   public static World getWorld() {
      return ModLoader.getMinecraftServerInstance().worlds.get(0);
   }

   public static boolean isClient(World var0) {
      return false;
   }

   public static boolean isServerSide() {
      return true;
   }

   public static Entity getEntity(World var0, int var1) {
      return null;
   }

   public static void storeEntity(World var0, Entity var1) {
      var0.addEntity(var1);
   }

   public static void removeEntity(Entity var0) {
      var0.die();
   }
}
