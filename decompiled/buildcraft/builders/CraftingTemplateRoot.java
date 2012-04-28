package buildcraft.builders;

import buildcraft.core.BuildCraftContainer;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.Slot;

class CraftingTemplateRoot extends BuildCraftContainer
{
    IInventory playerIInventory;
    TileTemplate template;
    int computingTime = 0;

    public CraftingTemplateRoot(IInventory var1, TileTemplate var2)
    {
        super(var2.getSizeInventory());
        this.playerIInventory = var1;
        this.template = var2;
        this.addSlot(new Slot(var2, 0, 55, 35));
        this.addSlot(new Slot(var2, 1, 114, 35));
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

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.template.isUseableByPlayer(var1);
    }
}
