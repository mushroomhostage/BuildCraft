package buildcraft.api;

import java.util.Random;
import net.minecraft.server.Entity;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class APIProxy
{
    public static World getWorld()
    {
        return ModLoader.getMinecraftServerInstance().getWorldServer(0);
    }

    public static boolean isClient(World var0)
    {
        return false;
    }

    public static boolean isServerSide()
    {
        return true;
    }

    public static void removeEntity(Entity var0)
    {
        var0.die();
    }

    public static Random createNewRandom(World var0)
    {
        return new Random(var0.getSeed());
    }
}
