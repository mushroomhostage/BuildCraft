package buildcraft.energy;

import buildcraft.core.BuildCraftContainer;
import buildcraft.energy.EngineStone;
import buildcraft.energy.TileEngine;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;

public class ContainerEngineRoot extends BuildCraftContainer
{
    protected TileEngine engine;

    public ContainerEngineRoot(PlayerInventory var1, TileEngine var2)
    {
        super(var2.getSize());
        this.engine = var2;
        if (var2.engine instanceof EngineStone)
        {
            this.a(new Slot(var2, 0, 80, 41));
        }
        else
        {
            this.a(new Slot(var2, 0, 52, 41));
        }

        int var3;
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.a(new Slot(var1, var3, 8 + var3 * 18, 142));
        }
    }

    public boolean isUsableByPlayer(EntityHuman var1)
    {
        return this.engine.a(var1);
    }

    public boolean b(EntityHuman var1)
    {
        return this.engine.a(var1);
    }
}
