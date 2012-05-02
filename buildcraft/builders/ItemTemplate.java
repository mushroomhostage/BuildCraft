package buildcraft.builders;

import forge.ITextureProvider;
import java.util.List;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Entity;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemTemplate extends Item implements ITextureProvider
{
    public ItemTemplate(int var1)
    {
        super(var1);
        this.maxStackSize = 1;
        this.textureId = 32;
    }

    public void addInformation(ItemStack var1, List var2)
        {
        var2.add("#" + var1.getData());
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void a(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {}

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftSprites;
    }
}
