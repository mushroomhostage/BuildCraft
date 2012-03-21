package net.minecraft.server;

public class mod_BuildCraftFactory extends BaseModMp
{
    public static mod_BuildCraftFactory instance;

    public void modsLoaded()
    {
        super.modsLoaded();
        BuildCraftFactory.initialize();
        instance = this;
    }

    public String getVersion()
    {
        return "2.2.13";
    }

    public void load() {}
}
