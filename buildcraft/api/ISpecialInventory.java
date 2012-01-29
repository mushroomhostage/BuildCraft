package buildcraft.api;

import buildcraft.api.Orientations;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public interface ISpecialInventory extends IInventory
{
    boolean addItem(ItemStack var1, boolean var2, Orientations var3);

    ItemStack extractItem(boolean var1, Orientations var2);
}
