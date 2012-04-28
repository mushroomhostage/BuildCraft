package buildcraft.energy;

import buildcraft.core.BuildCraftContainer;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryPlayer;
import net.minecraft.server.Slot;

public class ContainerEngineRoot extends BuildCraftContainer
{
    protected TileEngine engine;

    public ContainerEngineRoot(InventoryPlayer var1, TileEngine var2)
    {
        super(var2.getSizeInventory());
        this.engine = var2;

        if (var2.engine instanceof EngineStone)
        {
            this.addSlot(new Slot(var2, 0, 80, 41));
        }
        else
        {
            this.addSlot(new Slot(var2, 0, 52, 41));
        }

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 142));
        }
    }

    public boolean isUsableByPlayer(EntityPlayer var1)
    {
        return this.engine.isUseableByPlayer(var1);
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.engine.isUseableByPlayer(var1);
    }
}
