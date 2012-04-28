package net.minecraft.server;

import buildcraft.energy.TileEngine;
import forge.NetworkMod;
import java.util.Random;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class mod_BuildCraftEnergy extends NetworkMod
{

    public static mod_BuildCraftEnergy instance;


    public mod_BuildCraftEnergy()
    {
        instance = this;
    }

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
        return "2.2.14";
    }

    public void generateSurface(World var1, Random var2, int var3, int var4)
    {
        BuildCraftEnergy.generateSurface(var1, var2, var3, var4);
    }

    public void load()
    {
        BuildCraftEnergy.load();
    }

    public boolean clientSideRequired()
    {
        return true;
    }

    public boolean serverSideRequired()
    {
        return false;
    }
}
