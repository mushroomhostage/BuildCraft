package buildcraft.core;

import forge.DimensionManager;
import forge.NetworkMod;
import java.io.File;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.Packet;
import net.minecraft.server.World;

public class CoreProxy
{
    public static void addName(Object var0, String var1) {}

    public static void setField804(EntityItem var0, float var1)
    {
        var0.d = var1;
    }

    public static void onCraftingPickup(World var0, EntityHuman var1, ItemStack var2)
    {
        var2.a(var0, var1, var2.count);
    }

    public static File getPropertyFile()
    {
        return new File("BuildCraft.cfg");
    }

    public static void sendToPlayers(Packet var0, int var1, int var2, int var3, int var4, NetworkMod var5)
    {
        if (var0 != null)
        {
            for (int var6 = 0; var6 < DimensionManager.getWorlds().length; ++var6)
            {
                for (int var7 = 0; var7 < DimensionManager.getWorlds()[var6].players.size(); ++var7)
                {
                    EntityPlayer var8 = (EntityPlayer)DimensionManager.getWorlds()[var6].players.get(var7);

                    if (Math.abs(var8.locX - (double)var1) <= (double)var4 && Math.abs(var8.locY - (double)var2) <= (double)var4 && Math.abs(var8.locZ - (double)var3) <= (double)var4)
                    {
                        var8.netServerHandler.sendPacket(var0);
                    }
                }
            }
        }
    }

    public static boolean isPlainBlock(Block var0)
    {
        return var0.b();
    }

    public static File getBuildCraftBase()
    {
        return new File("buildcraft/");
    }

    public static void addLocalization(String var0, String var1) {}

    public static int addFuel(int var0, int var1)
    {
        return ModLoader.addAllFuel(var0, var1);
    }

    public static int addCustomTexture(String var0)
    {
        return 0;
    }

    public static long getHash(IBlockAccess var0)
    {
        return 0L;
    }

    public static void TakenFromCrafting(EntityHuman var0, ItemStack var1, IInventory var2)
    {
        ModLoader.takenFromCrafting(var0, var1, var2);
    }
}
