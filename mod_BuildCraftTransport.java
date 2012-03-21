package net.minecraft.server;

public class mod_BuildCraftTransport extends BaseModMp
{
    public static mod_BuildCraftTransport instance;

    public void modsLoaded()
    {
        super.modsLoaded();
        BuildCraftTransport.initialize();
        instance = this;
    }

    public String getVersion()
    {
        return "2.2.13";
    }

    public static void registerTilePipe(Class var0, String var1)
    {
        ModLoader.registerTileEntity(var0, var1);
    }

    public void load() {}
}
