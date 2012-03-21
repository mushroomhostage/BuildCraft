package net.minecraft.server;

import buildcraft.energy.TileEngine;
import java.util.Random;

public class mod_BuildCraftEnergy extends BaseModMp
{
    public static mod_BuildCraftEnergy instance;

    public void modsLoaded()
    {
        super.modsLoaded();
        BuildCraftEnergy.initialize();
        ModLoader.registerTileEntity(TileEngine.class, "net.minecraft.server.buildcraft.energy.Engine");
        ModLoader.registerTileEntity(TileEngine.class, "net.minecraft.src.buildcraft.energy.Engine");
        instance = this;
    }

    public String getVersion()
    {
        return "2.2.13";
    }

    public void generateSurface(World var1, Random var2, int var3, int var4)
    {
        BuildCraftEnergy.generateSurface(var1, var2, var3, var4);
    }

    public void load() {}
}
