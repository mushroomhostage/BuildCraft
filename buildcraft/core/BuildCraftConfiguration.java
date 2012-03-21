package buildcraft.core;

import forge.Configuration;
import forge.Property;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import net.minecraft.server.mod_BuildCraftCore;

public class BuildCraftConfiguration extends Configuration
{
    public BuildCraftConfiguration(File var1, boolean var2)
    {
        super(var1);

        if (var2)
        {
            this.loadLegacyProperties();
        }
    }

    public void loadLegacyProperties()
    {
        File var1 = CoreProxy.getPropertyFile();
        Properties var2 = new Properties();

        try
        {
            if (!var1.exists())
            {
                return;
            }

            if (var1.canRead())
            {
                FileInputStream var3 = new FileInputStream(var1);
                var2.load(var3);
                var3.close();
            }

            this.getOrCreateProperty("stonePipe.id", 1, var2.getProperty("stonePipe.blockId"));
            this.getOrCreateProperty("woodenPipe.id", 1, var2.getProperty("woodenPipe.blockId"));
            this.getOrCreateProperty("ironPipe.id", 1, var2.getProperty("ironPipe.blockId"));
            this.getOrCreateProperty("goldenPipe.id", 1, var2.getProperty("goldenPipe.blockId"));
            this.getOrCreateProperty("diamondPipe.id", 1, var2.getProperty("diamondPipe.blockId"));
            this.getOrCreateProperty("obsidianPipe.id", 1, var2.getProperty("obsidianPipeBlock.blockId"));
            this.getOrCreateProperty("autoWorkbench.id", 1, var2.getProperty("autoWorkbench.blockId"));
            this.getOrCreateProperty("miningWell.id", 1, var2.getProperty("miningWell.blockId"));
            this.getOrCreateProperty("quarry.id", 1, var2.getProperty("quarry.blockId"));
            this.getOrCreateProperty("drill.id", 1, var2.getProperty("drill.blockId"));
            this.getOrCreateProperty("frame.id", 1, var2.getProperty("frame.blockId"));
            this.getOrCreateProperty("marker.id", 1, var2.getProperty("marker.blockId"));
            this.getOrCreateProperty("filler.id", 1, var2.getProperty("filler.blockId"));
            this.getOrCreateProperty("woodenGearItem.id", 2, var2.getProperty("woodenGearItem.id"));
            this.getOrCreateProperty("stoneGearItem.id", 2, var2.getProperty("stoneGearItem.id"));
            this.getOrCreateProperty("ironGearItem.id", 2, var2.getProperty("ironGearItem.id"));
            this.getOrCreateProperty("goldenGearItem.id", 2, var2.getProperty("goldGearItem.id"));
            this.getOrCreateProperty("diamondGearItem.id", 2, var2.getProperty("diamondGearItem.id"));
            this.getOrCreateProperty("mining.enabled", 0, var2.getProperty("mining.enabled"));
            this.getOrCreateProperty("current.continuous", 0, var2.getProperty("current.continous"));
            var1.delete();
        }
        catch (IOException var4)
        {
            var4.printStackTrace();
        }
    }

    public void save()
    {
        Property var1 = null;

        if (!this.generalProperties.containsKey("version"))
        {
            var1 = new Property();
            var1.name = "version";
            this.generalProperties.put("version", var1);
        }
        else
        {
            var1 = (Property)this.generalProperties.get("version");
        }

        var1.value = mod_BuildCraftCore.version();
        super.save();
    }
}
