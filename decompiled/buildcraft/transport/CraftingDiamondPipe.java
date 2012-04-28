package buildcraft.transport;

import buildcraft.core.BuildCraftContainer;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.Slot;

class CraftingDiamondPipe extends BuildCraftContainer
{
    IInventory playerIInventory;
    IInventory filterIInventory;

    public CraftingDiamondPipe(IInventory var1, IInventory var2)
    {
        super(var2.getSizeInventory());
        this.playerIInventory = var1;
        this.filterIInventory = var2;
        int var3;
        int var4;

        for (var3 = 0; var3 < 6; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(var2, var4 + var3 * 9, 8 + var4 * 18, 18 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 140 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 198));
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.filterIInventory.isUseableByPlayer(var1);
    }
}
