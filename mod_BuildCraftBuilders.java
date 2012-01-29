package net.minecraft.server;

import net.minecraft.server.BaseModMp;
import net.minecraft.server.BuildCraftBuilders;

public class mod_BuildCraftBuilders extends BaseModMp
{

    public static mod_BuildCraftBuilders instance;

    public mod_BuildCraftBuilders()
    {
        instance = this;
    }

    public void ModsLoaded()
    {
        super.ModsLoaded();
        BuildCraftBuilders.initialize();
    }

    public String getVersion()
    {
        return "2.2.12";
    }

    public void load() {}
}
