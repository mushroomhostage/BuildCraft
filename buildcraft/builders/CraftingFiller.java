package buildcraft.builders;

import buildcraft.core.BuildCraftContainer;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.Slot;

class CraftingFiller extends BuildCraftContainer
{
    IInventory playerIInventory;
    IInventory fillerInventory;

    public CraftingFiller(IInventory var1, IInventory var2)
    {
        super(var2.getSize());
        this.playerIInventory = var1;
        this.fillerInventory = var2;
        int var3;
        int var4;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 3; ++var4)
            {
                this.a(new Slot(var2, var4 + var3 * 3, 31 + var4 * 18, 16 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.a(new Slot(var2, 9 + var4 + var3 * 9, 8 + var4 * 18, 85 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 153 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.a(new Slot(var1, var3, 8 + var3 * 18, 211));
        }
    }

    // Mae start
    @Override
    public EntityHuman getPlayer() {
        return ((net.minecraft.server.PlayerInventory)playerIInventory).player;
    }

    @Override
    public IInventory getInventory() {
        return this.fillerInventory;
    }
    // Mae end

    public boolean b(EntityHuman var1)
    {
        return this.fillerInventory.a(var1);
    }
}
