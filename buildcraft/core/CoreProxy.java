package buildcraft.core;

import java.io.File;
import net.minecraft.server.BaseModMp;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.ModLoaderMp;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.WorldServer;

public class CoreProxy {

   public static void addName(Object var0, String var1) {}

   public static void setField804(EntityItem var0, float var1) {
      var0.d = var1;
   }

   public static File getPropertyFile() {
      return new File("BuildCraft.cfg");
   }

    public static void sendToPlayers(Packet230ModLoader packet230modloader, int i, int j, int k, int l, BaseModMp basemodmp) {
        if(packet230modloader != null) {
            for(WorldServer world : ModLoader.getMinecraftServerInstance().worlds) {
                for(Object player : world.players) {
                    EntityPlayer entityplayer = (EntityPlayer)player;
                    if(Math.abs(entityplayer.locX - (double)i) <= (double)l && Math.abs(entityplayer.locY - (double)j) <= (double)l && Math.abs(entityplayer.locZ - (double)k) <= (double)l) {
                        ModLoaderMp.SendPacketTo(basemodmp, entityplayer, packet230modloader);
                    }
                }
            }
        }
    }
   public static boolean isPlainBlock(Block var0) {
      return var0.b();
   }

   public static File getBuildCraftBase() {
      return new File("buildcraft/");
   }

   public static void addLocalization(String var0, String var1) {}

   public static int addFuel(int var0, int var1) {
      return ModLoader.AddAllFuel(var0);
   }

   public static int addCustomTexture(String var0) {
      return 0;
   }

   public static long getHash(IBlockAccess var0) {
      return 0L;
   }

   public static void TakenFromCrafting(EntityHuman var0, ItemStack var1, IInventory var2) {
      ModLoader.TakenFromCrafting(var0, var1);
   }
}
