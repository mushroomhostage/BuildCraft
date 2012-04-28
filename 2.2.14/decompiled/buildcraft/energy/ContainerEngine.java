package buildcraft.energy;

import net.minecraft.server.ICrafting;
import net.minecraft.server.PlayerInventory;

public class ContainerEngine extends ContainerEngineRoot
{
    public ContainerEngine(PlayerInventory var1, TileEngine var2)
    {
        super(var1, var2);
    }

    public void addSlotListener(ICrafting var1)
    {
        super.addSlotListener(var1);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void a()
    {
        super.a();

        for (int var1 = 0; var1 < this.listeners.size(); ++var1)
        {
            this.engine.engine.sendGUINetworkData(this, (ICrafting)this.listeners.get(var1));
        }
    }
}
