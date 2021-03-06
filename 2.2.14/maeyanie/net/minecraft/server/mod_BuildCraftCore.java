package net.minecraft.server;

import buildcraft.core.ClassMapping;
import forge.NetworkMod;
import java.util.Date;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ModLoader;

public class mod_BuildCraftCore extends NetworkMod
{

    public static mod_BuildCraftCore instance;
    BuildCraftCore proxy = new BuildCraftCore();
    long lastReport = 0L;


    public mod_BuildCraftCore()
    {
        instance = this;
    }

    public static void initialize()
    {
        BuildCraftCore.initialize();
    }

    public void modsLoaded()
    {
        super.modsLoaded();
        initialize();
        BuildCraftCore.initializeModel(this);
        ModLoader.setInGameHook(this, true, true);
    }

    public String getVersion()
    {
        return version();
    }

    public static String version()
    {
        return "2.2.14";
    }

    public boolean onTickInGame(MinecraftServer var1)
    {
        if (BuildCraftCore.trackNetworkUsage)
        {
            Date var2 = new Date();
            if (var2.getTime() - this.lastReport > 10000L)
            {
                this.lastReport = var2.getTime();
                int var3 = ClassMapping.report();
                System.out.println("BuildCraft badwith = " + var3 / 10 + " bytes / second");
                System.out.println();
            }
        }

        return true;
    }

    public void load()
    {
        BuildCraftCore.load();
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
