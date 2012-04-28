package buildcraft.energy;

import net.minecraft.server.ICrafting;
import net.minecraft.server.InventoryPlayer;

public class ContainerEngine extends ContainerEngineRoot
{
    public ContainerEngine(InventoryPlayer var1, TileEngine var2)
    {
        super(var1, var2);
    }

    public void onCraftGuiOpened(ICrafting var1)
    {
        super.onCraftGuiOpened(var1);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            this.engine.engine.sendGUINetworkData(this, (ICrafting)this.crafters.get(var1));
        }
    }
}
