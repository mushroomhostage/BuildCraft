package net.minecraft.server;

public class mod_BuildCraftBuilders extends BaseModMp
{

    public static mod_BuildCraftBuilders instance;

    public mod_BuildCraftBuilders()
    {
        instance = this;
    }

    public void modsLoaded()
    {
        super.modsLoaded();
        BuildCraftBuilders.initialize();
    }

    public String getVersion()
    {
        return "2.2.13";
    }

    public void load() {}
}
