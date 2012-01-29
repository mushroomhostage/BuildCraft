package net.minecraft.server;

import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftFactory;

public class mod_BuildCraftFactory extends BaseModMp
{
    public static mod_BuildCraftFactory instance;

    public void ModsLoaded()
    {
        super.ModsLoaded();
        BuildCraftFactory.initialize();
        instance = this;
    }

    public String getVersion()
    {
        return "2.2.12";
    }

    public void load() {}
}
