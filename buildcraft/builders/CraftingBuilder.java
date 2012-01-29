package buildcraft.builders;

import buildcraft.core.BuildCraftContainer;
import net.minecraft.server.IInventory;
import net.minecraft.server.Slot;

class CraftingBuilder extends BuildCraftContainer
{

    IInventory playerIInventory;
    IInventory builderInventory;


    public CraftingBuilder(IInventory var1, IInventory var2)
    {
        super(var2.getSize());
        this.playerIInventory = var1;
        this.builderInventory = var2;
        this.a(new Slot(var2, 0, 80, 27));

        int var3;
        int var4;
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.a(new Slot(var2, 1 + var4 + var3 * 9, 8 + var4 * 18, 72 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 140 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.a(new Slot(var1, var3, 8 + var3 * 18, 198));
        }

    }
}
