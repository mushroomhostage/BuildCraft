package buildcraft.core;

import net.minecraft.server.Container;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Slot;

public abstract class BuildCraftContainer extends Container
{
    private int inventorySize;

    public BuildCraftContainer(int var1)
    {
        this.inventorySize = var1;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(int var1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(var1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (var1 < this.inventorySize)
            {
                if (!this.mergeItemStack(var4, this.inventorySize, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 0, this.inventorySize, false))
            {
                return null;
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }
        }

        return var2;
    }
}
