package buildcraft.factory;

import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.StackUtil;
import buildcraft.core.Utils;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.Container;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;

public class TileAutoWorkbench extends TileEntity implements ISpecialInventory
{
    private ItemStack[] stackList = new ItemStack[9];

    // CraftBukkit start
    public java.util.List<org.bukkit.entity.HumanEntity> transaction = 
            new java.util.ArrayList<org.bukkit.entity.HumanEntity>();
    
    public void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity who) {
        transaction.remove(who);
    }

    public java.util.List<org.bukkit.entity.HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {}

    public ItemStack[] getContents()
    {
        return stackList;
    }
    // CraftBukkit end

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSize()
    {
        return this.stackList.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getItem(int var1)
    {
        return this.stackList[var1];
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack splitStack(int var1, int var2)
    {
        ItemStack var3 = this.stackList[var1].cloneItemStack();
        var3.count = var2;
        this.stackList[var1].count -= var2;

        if (this.stackList[var1].count == 0)
        {
            this.stackList[var1] = null;
        }

        return var3;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int var1, ItemStack var2)
    {
        this.stackList[var1] = var2;
    }

    /**
     * Returns the name of the inventory.
     */
    public String getName()
    {
        return "";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getMaxStackSize()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean a(EntityHuman var1)
    {
        return this.world.getTileEntity(this.x, this.y, this.z) == this;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound var1)
    {
        super.a(var1);
        NBTTagList var2 = var1.getList("stackList");
        this.stackList = new ItemStack[var2.size()];

        for (int var3 = 0; var3 < this.stackList.length; ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.get(var3);

            if (!var4.getBoolean("isNull"))
            {
                this.stackList[var3] = ItemStack.a(var4);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound var1)
    {
        super.b(var1);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.stackList.length; ++var3)
        {
            NBTTagCompound var4 = new NBTTagCompound();
            var2.add(var4);

            if (this.stackList[var3] == null)
            {
                var4.setBoolean("isNull", true);
            }
            else
            {
                var4.setBoolean("isNull", false);
                this.stackList[var3].save(var4);
            }
        }

        var1.set("stackList", var2);
    }

    public boolean addItem(ItemStack var1, boolean var2, Orientations var3)
    {
        StackUtil var4 = new StackUtil(var1);
        int var5 = Integer.MAX_VALUE;
        int var6 = -1;

        for (int var7 = 0; var7 < this.getSize(); ++var7)
        {
            ItemStack var8 = this.getItem(var7);

            if (var8 != null && var8.count > 0 && var8.id == var1.id && var8.getData() == var1.getData() && var8.count < var5)
            {
                var5 = var8.count;
                var6 = var7;
            }
        }

        if (var6 != -1)
        {
            if (var4.tryAdding(this, var6, var2, false))
            {
                if (var2 && var1.count != 0)
                {
                    this.addItem(var1, var2, var3);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public ItemStack findRecipe()
    {
        TileAutoWorkbench.LocalInventoryCrafting var1 = new TileAutoWorkbench.LocalInventoryCrafting();

        for (int var2 = 0; var2 < this.getSize(); ++var2)
        {
            ItemStack var3 = this.getItem(var2);
            var1.setItem(var2, var3);
        }

        ItemStack var4 = CraftingManager.getInstance().craft(var1);
        return var4;
    }

    public ItemStack extractItem(boolean var1, boolean var2)
    {
        TileAutoWorkbench.LocalInventoryCrafting var3 = new TileAutoWorkbench.LocalInventoryCrafting();
        LinkedList var4 = new LinkedList();
        int var5 = var2 ? 0 : 1;
        TileAutoWorkbench.StackPointer var8;

        for (int var6 = 0; var6 < this.getSize(); ++var6)
        {
            ItemStack var7 = this.getItem(var6);

            if (var7 != null)
            {
                if (var7.count <= var5)
                {
                    var8 = this.getNearbyItem(var7.id, var7.getData());

                    if (var8 == null)
                    {
                        this.resetPointers(var4);
                        return null;
                    }

                    var4.add(var8);
                }
                else
                {
                    var8 = new TileAutoWorkbench.StackPointer();
                    var8.inventory = this;
                    var8.item = this.splitStack(var6, 1);
                    var8.index = var6;
                    var7 = var8.item;
                    var4.add(var8);
                }
            }

            var3.setItem(var6, var7);
        }

        ItemStack var10 = CraftingManager.getInstance().craft(var3);

        if (var10 != null && var1)
        {
            Iterator var11 = var4.iterator();

            while (var11.hasNext())
            {
                var8 = (TileAutoWorkbench.StackPointer)var11.next();

                if (var8.item.getItem().j() != null)
                {
                    ItemStack var9 = new ItemStack(var8.item.getItem().j(), 1);
                    var8.inventory.setItem(var8.index, var9);
                }
            }
        }
        else
        {
            this.resetPointers(var4);
        }

        return var10;
    }

    public ItemStack extractItem(boolean var1, Orientations var2)
    {
        return this.extractItem(var1, false);
    }

    public void resetPointers(LinkedList var1)
    {
        Iterator var2 = var1.iterator();

        while (var2.hasNext())
        {
            TileAutoWorkbench.StackPointer var3 = (TileAutoWorkbench.StackPointer)var2.next();
            ItemStack var4 = var3.inventory.getItem(var3.index);

            if (var4 == null)
            {
                var3.inventory.setItem(var3.index, var3.item);
            }
            else
            {
                ++var3.inventory.getItem(var3.index).count;
            }
        }
    }

    public TileAutoWorkbench.StackPointer getNearbyItem(int var1, int var2)
    {
        TileAutoWorkbench.StackPointer var3 = null;
        var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.XNeg);

        if (var3 == null)
        {
            var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.XPos);
        }

        if (var3 == null)
        {
            var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.YNeg);
        }

        if (var3 == null)
        {
            var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.YPos);
        }

        if (var3 == null)
        {
            var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.ZNeg);
        }

        if (var3 == null)
        {
            var3 = this.getNearbyItemFromOrientation(var1, var2, Orientations.ZPos);
        }

        return var3;
    }

    public TileAutoWorkbench.StackPointer getNearbyItemFromOrientation(int var1, int var2, Orientations var3)
    {
        Position var4 = new Position((double)this.x, (double)this.y, (double)this.z, var3);
        var4.moveForwards(1.0D);
        TileEntity var5 = this.world.getTileEntity((int)var4.x, (int)var4.y, (int)var4.z);

        if (!(var5 instanceof ISpecialInventory) && var5 instanceof IInventory)
        {
            IInventory var6 = Utils.getInventory((IInventory)var5);

            for (int var7 = 0; var7 < var6.getSize(); ++var7)
            {
                ItemStack var8 = var6.getItem(var7);

                if (var8 != null && var8.count > 0 && var8.id == var1 && var8.getData() == var2)
                {
                    var6.splitStack(var7, 1);
                    TileAutoWorkbench.StackPointer var9 = new TileAutoWorkbench.StackPointer();
                    var9.inventory = var6;
                    var9.index = var7;
                    var9.item = var8;
                    return var9;
                }
            }
        }

        return null;
    }

    public void f() {}

    public void g() {}

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack splitWithoutUpdate(int var1)
    {
        if (this.stackList[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var2 = this.stackList[var1];
            this.stackList[var1] = null;
            return var2;
        }
    }

    class LocalInventoryCrafting extends InventoryCrafting
    {
        public LocalInventoryCrafting()
        {
            super(new Container()
            {
                public boolean isUsableByPlayer(EntityHuman var1)
                {
                    return false;
                }
                public boolean b(EntityHuman var1)
                {
                    return false;
                }
            }, 3, 3);
        }
    }

    class StackPointer
    {
        IInventory inventory;
        int index;
        ItemStack item;
    }
}
