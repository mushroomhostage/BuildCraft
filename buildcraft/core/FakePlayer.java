package buildcraft.core;

import net.minecraft.server.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.*;

public class FakePlayer
{
    enum Method { NULL, FAKEPLAYER };

    private static Method method = Method.FAKEPLAYER;
    private static EntityPlayer fakePlayer = null;
    public static String name = "[BuildCraft]";
    public static boolean doLogin = false;

    public static void setMethod(String value)
    {
        if (value.equalsIgnoreCase("null"))
        {
            method = Method.NULL;
        }
        else if (value.equalsIgnoreCase("fakeplayer"))
        {
            method = Method.FAKEPLAYER;
        }
        else
        {
            System.err.println("Unknown blocks.placedby type '" + value + "'");
        }
    }

    public static EntityPlayer get(World world)
    {
        switch (method)
        {
            case NULL:
                return null;
            case FAKEPLAYER:
                if (fakePlayer == null)
                {
                    fakePlayer = new EntityPlayer(ModLoader.getMinecraftServerInstance(), world,
                            name, new ItemInWorldManager(world));

                    if (doLogin)
                    {
                        PlayerLoginEvent ple = new PlayerLoginEvent((CraftPlayer)fakePlayer.getBukkitEntity());
                        world.getServer().getPluginManager().callEvent(ple);
                        if (ple.getResult() != PlayerLoginEvent.Result.ALLOWED)
                        {
                            System.err.println("[BuildCraft] Warning: FakePlayer login event was disallowed. Ignoring, but this may cause confused plugins.");
                        }

                        PlayerJoinEvent pje = new PlayerJoinEvent((CraftPlayer)fakePlayer.getBukkitEntity(), "");
                        world.getServer().getPluginManager().callEvent(pje);
                    }
                }
                return fakePlayer;
        }
        return null;
    }

    public static CraftPlayer getBukkitEntity(World world)
    {
        EntityPlayer player = get(world);
        if (player != null)
        {
            return (CraftPlayer)player.getBukkitEntity();
        }
        return null;
    }
}
