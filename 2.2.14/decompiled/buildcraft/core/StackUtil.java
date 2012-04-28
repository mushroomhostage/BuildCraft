package buildcraft.core;

import buildcraft.api.APIProxy;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import forge.ISidedInventory;
import java.util.LinkedList;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class StackUtil
{
    public ItemStack items;

    public StackUtil(ItemStack var1)
    {
        this.items = var1;
    }

    public boolean addToRandomInventory(TileEntity var1, Orientations var2)
    {
        World var3 = APIProxy.getWorld();
        LinkedList var4 = new LinkedList();
        int var5;
        Position var6;
        TileEntity var7;

        for (var5 = 0; var5 < 6; ++var5)
        {
            if (var2.reverse().ordinal() != var5)
            {
                var6 = new Position((double)var1.x, (double)var1.y, (double)var1.z, Orientations.values()[var5]);
                var6.moveForwards(1.0D);
                var7 = var3.getTileEntity((int)var6.x, (int)var6.y, (int)var6.z);

                if (var7 instanceof ISpecialInventory && ((ISpecialInventory)var7).addItem(this.items, false, var2))
                {
                    var4.add(var6.orientation);
                }

                if (var7 instanceof IInventory && Utils.checkPipesConnections(var1.world, var1.x, var1.y, var1.z, var7.x, var7.y, var7.z) && this.checkAvailableSlot((IInventory)var7, false, var6.orientation.reverse()))
                {
                    var4.add(var6.orientation);
                }
            }
        }

        if (var4.size() > 0)
        {
            var5 = var3.random.nextInt(var4.size());
            var6 = new Position((double)var1.x, (double)var1.y, (double)var1.z, (Orientations)var4.get(var5));
            var6.moveForwards(1.0D);
            var7 = var3.getTileEntity((int)var6.x, (int)var6.y, (int)var6.z);
            this.checkAvailableSlot((IInventory)var7, true, var6.orientation.reverse());

            if (this.items.count > 0)
            {
                return this.addToRandomInventory(var7, var2);
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean checkAvailableSlot(IInventory var1, boolean var2, Orientations var3)
    {
        if (var1 instanceof ISpecialInventory)
        {
            return ((ISpecialInventory)var1).addItem(this.items, var2, var3);
        }
        else
        {
            boolean var4 = false;
            IInventory var5;
            ISidedInventory var6;
            int var7;
            int var8;
            int var9;
            int var10;

            if (var1 instanceof ISidedInventory)
            {
                var5 = Utils.getInventory(var1);
                var6 = (ISidedInventory)var5;
                var7 = var6.getStartInventorySide(var3.ordinal());
                var8 = var7 + var6.getSizeInventorySide(var3.ordinal()) - 1;

                for (var9 = var7; var9 <= var8; ++var9)
                {
                    if (this.tryAdding(var5, var9, var2, false))
                    {
                        var4 = true;
                        break;
                    }
                }
            }
            else if (var1.getSize() == 2)
            {
                if (var3 != Orientations.YNeg && var3 != Orientations.YPos)
                {
                    if (this.tryAdding(var1, 1, var2, false))
                    {
                        var4 = true;
                    }
                }
                else if (this.tryAdding(var1, 0, var2, false))
                {
                    var4 = true;
                }
            }
            else if (var1.getSize() == 3)
            {
                if (var3 == Orientations.YPos)
                {
                    if (this.tryAdding(var1, 0, var2, false))
                    {
                        var4 = true;
                    }
                }
                else if (var3 == Orientations.YNeg && this.tryAdding(var1, 1, var2, false))
                {
                    var4 = true;
                }
            }
            else
            {
                var5 = Utils.getInventory(var1);

                for (var10 = 0; var10 < var5.getSize(); ++var10)
                {
                    if (this.tryAdding(var5, var10, var2, false))
                    {
                        var4 = true;
                        break;
                    }
                }
            }

            if (var4)
            {
                if (!var2)
                {
                    return true;
                }
                else if (this.items.count == 0)
                {
                    return true;
                }
                else
                {
                    this.checkAvailableSlot(var1, var4, var3);
                    return true;
                }
            }
            else
            {
                if (var1 instanceof ISidedInventory)
                {
                    var5 = Utils.getInventory(var1);
                    var6 = (ISidedInventory)var5;
                    var7 = var6.getStartInventorySide(var3.ordinal());
                    var8 = var7 + var6.getSizeInventorySide(var3.ordinal()) - 1;

                    for (var9 = var7; var9 <= var8; ++var9)
                    {
                        if (this.tryAdding(var5, var9, var2, true))
                        {
                            var4 = true;
                            break;
                        }
                    }
                }
                else if (var1.getSize() == 2)
                {
                    if (var3 != Orientations.YNeg && var3 != Orientations.YPos)
                    {
                        if (this.tryAdding(var1, 1, var2, true))
                        {
                            var4 = true;
                        }
                    }
                    else if (this.tryAdding(var1, 0, var2, true))
                    {
                        var4 = true;
                    }
                }
                else if (var1.getSize() == 3)
                {
                    if (var3 == Orientations.YPos)
                    {
                        if (this.tryAdding(var1, 0, var2, true))
                        {
                            var4 = true;
                        }
                    }
                    else if (var3 == Orientations.YNeg && this.tryAdding(var1, 1, var2, true))
                    {
                        var4 = true;
                    }
                }
                else
                {
                    var5 = Utils.getInventory(var1);

                    for (var10 = 0; var10 < var5.getSize(); ++var10)
                    {
                        if (this.tryAdding(var5, var10, var2, true))
                        {
                            var4 = true;
                            break;
                        }
                    }
                }

                if (var4)
                {
                    if (!var2)
                    {
                        return true;
                    }
                    else if (this.items.count == 0)
                    {
                        return true;
                    }
                    else
                    {
                        this.checkAvailableSlot(var1, var4, var3);
                        return true;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public boolean tryAdding(IInventory var1, int var2, boolean var3, boolean var4)
    {
        ItemStack var5 = var1.getItem(var2);

        if (!var4)
        {
            if (var5 != null && var5.getItem() == this.items.getItem() && var5.getData() == this.items.getData() && var5.count + 1 <= var5.getMaxStackSize())
            {
                if (var3)
                {
                    ++var5.count;
                    --this.items.count;
                }

                return true;
            }
        }
        else if (var5 == null)
        {
            if (var3)
            {
                var5 = this.items.cloneItemStack();
                var5.count = 1;
                --this.items.count;
                var1.setItem(var2, var5);
            }

            return true;
        }

        return false;
    }
}
