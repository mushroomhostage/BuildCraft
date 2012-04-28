package buildcraft.builders;

import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;

class CraftingTemplate extends CraftingTemplateRoot
{
    public CraftingTemplate(IInventory var1, TileTemplate var2)
    {
        super(var1, var2);
    }

    public void onCraftGuiOpened(ICrafting var1)
    {
        super.onCraftGuiOpened(var1);
        var1.updateCraftingInventoryInfo(this, 0, this.template.computingTime);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);

            if (this.computingTime != this.template.computingTime)
            {
                var2.updateCraftingInventoryInfo(this, 0, this.template.computingTime);
            }
        }

        this.computingTime = this.template.computingTime;
    }
}
