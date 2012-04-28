package buildcraft.energy;

import forge.ITextureProvider;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Item;

public class ItemFuel extends Item implements ITextureProvider
{
    public ItemFuel(int var1)
    {
        super(var1);
        this.iconIndex = 48;
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftSprites;
    }
}
