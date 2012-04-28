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
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;

public class TileAutoWorkbench extends TileEntity implements ISpecialInventory
{
    private ItemStack[] stackList = new ItemStack[9];

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.stackList.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.stackList[var1];
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        ItemStack var3 = this.stackList[var1].copy();
        var3.stackSize = var2;
        this.stackList[var1].stackSize -= var2;

        if (this.stackList[var1].stackSize == 0)
        {
            this.stackList[var1] = null;
        }

        return var3;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.stackList[var1] = var2;
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagList var2 = var1.getTagList("stackList");
        this.stackList = new ItemStack[var2.tagCount()];

        for (int var3 = 0; var3 < this.stackList.length; ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);

            if (!var4.getBoolean("isNull"))
            {
                this.stackList[var3] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.stackList.length; ++var3)
        {
            NBTTagCompound var4 = new NBTTagCompound();
            var2.appendTag(var4);

            if (this.stackList[var3] == null)
            {
                var4.setBoolean("isNull", true);
            }
            else
            {
                var4.setBoolean("isNull", false);
                this.stackList[var3].writeToNBT(var4);
            }
        }

        var1.setTag("stackList", var2);
    }

    public boolean addItem(ItemStack var1, boolean var2, Orientations var3)
    {
        StackUtil var4 = new StackUtil(var1);
        int var5 = Integer.MAX_VALUE;
        int var6 = -1;

        for (int var7 = 0; var7 < this.getSizeInventory(); ++var7)
        {
            ItemStack var8 = this.getStackInSlot(var7);

            if (var8 != null && var8.stackSize > 0 && var8.itemID == var1.itemID && var8.getItemDamage() == var1.getItemDamage() && var8.stackSize < var5)
            {
                var5 = var8.stackSize;
                var6 = var7;
            }
        }

        if (var6 != -1)
        {
            if (var4.tryAdding(this, var6, var2, false))
            {
                if (var2 && var1.stackSize != 0)
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

        for (int var2 = 0; var2 < this.getSizeInventory(); ++var2)
        {
            ItemStack var3 = this.getStackInSlot(var2);
            var1.setInventorySlotContents(var2, var3);
        }

        ItemStack var4 = CraftingManager.getInstance().findMatchingRecipe(var1);
        return var4;
    }

    public ItemStack extractItem(boolean var1, boolean var2)
    {
        TileAutoWorkbench.LocalInventoryCrafting var3 = new TileAutoWorkbench.LocalInventoryCrafting();
        LinkedList var4 = new LinkedList();
        int var5 = var2 ? 0 : 1;
        TileAutoWorkbench.StackPointer var8;

        for (int var6 = 0; var6 < this.getSizeInventory(); ++var6)
        {
            ItemStack var7 = this.getStackInSlot(var6);

            if (var7 != null)
            {
                if (var7.stackSize <= var5)
                {
                    var8 = this.getNearbyItem(var7.itemID, var7.getItemDamage());

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
                    var8.item = this.decrStackSize(var6, 1);
                    var8.index = var6;
                    var7 = var8.item;
                    var4.add(var8);
                }
            }

            var3.setInventorySlotContents(var6, var7);
        }

        ItemStack var10 = CraftingManager.getInstance().findMatchingRecipe(var3);

        if (var10 != null && var1)
        {
            Iterator var11 = var4.iterator();

            while (var11.hasNext())
            {
                var8 = (TileAutoWorkbench.StackPointer)var11.next();

                if (var8.item.getItem().getContainerItem() != null)
                {
                    ItemStack var9 = new ItemStack(var8.item.getItem().getContainerItem(), 1);
                    var8.inventory.setInventorySlotContents(var8.index, var9);
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
            ItemStack var4 = var3.inventory.getStackInSlot(var3.index);

            if (var4 == null)
            {
                var3.inventory.setInventorySlotContents(var3.index, var3.item);
            }
            else
            {
                ++var3.inventory.getStackInSlot(var3.index).stackSize;
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
        Position var4 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, var3);
        var4.moveForwards(1.0D);
        TileEntity var5 = this.worldObj.getBlockTileEntity((int)var4.x, (int)var4.y, (int)var4.z);

        if (!(var5 instanceof ISpecialInventory) && var5 instanceof IInventory)
        {
            IInventory var6 = Utils.getInventory((IInventory)var5);

            for (int var7 = 0; var7 < var6.getSizeInventory(); ++var7)
            {
                ItemStack var8 = var6.getStackInSlot(var7);

                if (var8 != null && var8.stackSize > 0 && var8.itemID == var1 && var8.getItemDamage() == var2)
                {
                    var6.decrStackSize(var7, 1);
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

    public void openChest() {}

    public void closeChest() {}

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
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
                public boolean isUsableByPlayer(EntityPlayer var1)
                {
                    return false;
                }
                public boolean canInteractWith(EntityPlayer var1)
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
