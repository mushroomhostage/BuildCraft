package buildcraft.core;

import forge.ITextureProvider;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Item;

public class ItemBuildCraftTexture extends Item implements ITextureProvider
{
    public ItemBuildCraftTexture(int var1)
    {
        super(var1);
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftSprites;
    }
}
