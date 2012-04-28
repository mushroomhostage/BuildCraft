package buildcraft.energy;

import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;

public class ItemEngine extends ItemBlock
{
    public ItemEngine(int var1)
    {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int var1)
    {
        return this.getPlacedBlockMetadata(var1);
    }

    public int getPlacedBlockMetadata(int var1)
    {
        return var1;
    }

    public String getItemNameIS(ItemStack var1)
    {
        return var1.getItemDamage() == 0 ? "tile.engineWood" : (var1.getItemDamage() == 1 ? "tile.engineStone" : "tile.engineIron");
    }
}
