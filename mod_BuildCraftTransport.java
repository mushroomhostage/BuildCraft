package net.minecraft.server;

import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.ModLoader;

public class mod_BuildCraftTransport extends BaseModMp
{
    public static mod_BuildCraftTransport instance;

    public void ModsLoaded()
    {
        super.ModsLoaded();
        BuildCraftTransport.initialize();
        instance = this;
    }

    public String getVersion()
    {
        return "2.2.12";
    }

    public static void registerTilePipe(Class var0, String var1)
    {
        ModLoader.RegisterTileEntity(var0, var1);
    }

    public void load() {}
}
