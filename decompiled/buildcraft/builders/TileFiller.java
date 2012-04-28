package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.api.IAreaProvider;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.LaserKind;
import buildcraft.api.Orientations;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.TileNetworkData;
import buildcraft.core.Box;
import buildcraft.core.FillerPattern;
import buildcraft.core.FillerRegistry;
import buildcraft.core.IMachine;
import buildcraft.core.StackUtil;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.Utils;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

public class TileFiller extends TileBuildCraft implements ISpecialInventory, IPowerReceptor, IMachine
{
    @TileNetworkData
    public Box box = new Box();
    @TileNetworkData
    public int currentPatternId = 0;
    @TileNetworkData
    public boolean done = true;
    FillerPattern currentPattern;
    boolean forceDone = false;
    private ItemStack[] contents = new ItemStack[this.getSizeInventory()];
    PowerProvider powerProvider;

    public TileFiller()
    {
        this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
        this.powerProvider.configure(10, 25, 100, 25, 100);
        this.powerProvider.configurePowerPerdition(25, 40);
    }

    public void initialize()
    {
        super.initialize();

        if (!APIProxy.isClient(this.worldObj))
        {
            IAreaProvider var1 = Utils.getNearbyAreaProvider(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

            if (var1 != null)
            {
                this.box.initialize(var1);

                if (var1 instanceof TileMarker)
                {
                    ((TileMarker)var1).removeFromWorld();
                }

                this.sendNetworkUpdate();
            }
        }

        this.computeRecipe();
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.box.isInitialized())
        {
            this.box.createLasers(this.worldObj, LaserKind.Stripes);
        }
        else
        {
            this.done = true;
        }

        if (this.powerProvider.energyStored > 25)
        {
            this.doWork();
        }
    }

    public void doWork()
    {
        if (!APIProxy.isClient(this.worldObj))
        {
            if (this.powerProvider.useEnergy(25, 25, true) >= 25)
            {
                if (this.box.isInitialized() && this.currentPattern != null && !this.done)
                {
                    ItemStack var1 = null;
                    int var2 = 0;

                    for (int var3 = 9; var3 < this.getSizeInventory(); ++var3)
                    {
                        if (this.getStackInSlot(var3) != null && this.getStackInSlot(var3).stackSize > 0 && this.getStackInSlot(var3).getItem() instanceof ItemBlock)
                        {
                            var1 = this.contents[var3];
                            var2 = var3;
                            break;
                        }
                    }

                    this.done = this.currentPattern.iteratePattern(this, this.box, var1);

                    if (var1 != null && var1.stackSize == 0)
                    {
                        this.contents[var2] = null;
                    }

                    if (this.done)
                    {
                        this.worldObj.markBlockAsNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);
                        this.sendNetworkUpdate();
                    }
                }

                if (this.powerProvider.energyStored > 25)
                {
                    this.doWork();
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 36;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.contents[var1];
    }

    public void computeRecipe()
    {
        if (!APIProxy.isClient(this.worldObj))
        {
            FillerPattern var1 = FillerRegistry.findMatchingRecipe(this);

            if (var1 != this.currentPattern)
            {
                this.currentPattern = var1;

                if (this.currentPattern != null && !this.forceDone)
                {
                    this.done = false;
                }
                else
                {
                    this.done = true;
                    this.forceDone = false;
                }

                if (this.worldObj != null)
                {
                    this.worldObj.markBlockAsNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);
                }

                if (this.currentPattern == null)
                {
                    this.currentPatternId = 0;
                }
                else
                {
                    this.currentPatternId = this.currentPattern.id;
                }

                if (APIProxy.isServerSide())
                {
                    this.sendNetworkUpdate();
                }
            }
        }
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.contents[var1] != null)
        {
            ItemStack var3;

            if (this.contents[var1].stackSize <= var2)
            {
                var3 = this.contents[var1];
                this.contents[var1] = null;
                this.computeRecipe();
                return var3;
            }
            else
            {
                var3 = this.contents[var1].splitStack(var2);

                if (this.contents[var1].stackSize == 0)
                {
                    this.contents[var1] = null;
                }

                this.computeRecipe();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.contents[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }

        this.computeRecipe();
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Filler";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagList var2 = var1.getTagList("Items");
        this.contents = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.contents.length)
            {
                this.contents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        if (var1.hasKey("box"))
        {
            this.box.initialize(var1.getCompoundTag("box"));
        }

        this.done = var1.getBoolean("done");
        this.forceDone = this.done;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.contents.length; ++var3)
        {
            if (this.contents[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.contents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        var1.setTag("Items", var2);

        if (this.box != null)
        {
            NBTTagCompound var5 = new NBTTagCompound();
            this.box.writeToNBT(var5);
            var1.setTag("box", var5);
        }

        var1.setBoolean("done", this.done);
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
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        this.destroy();
    }

    public void destroy()
    {
        if (this.box != null)
        {
            this.box.deleteLasers();
        }
    }

    public boolean addItem(ItemStack var1, boolean var2, Orientations var3)
    {
        StackUtil var4 = new StackUtil(var1);
        boolean var5 = false;
        int var6;

        for (var6 = 9; var6 < this.contents.length; ++var6)
        {
            if (var4.tryAdding(this, var6, var2, false))
            {
                var5 = true;
                break;
            }
        }

        if (var5)
        {
            if (!var2)
            {
                return true;
            }
            else if (var1.stackSize == 0)
            {
                return true;
            }
            else
            {
                this.addItem(var1, var5, var3);
                return true;
            }
        }
        else
        {
            if (!var5)
            {
                for (var6 = 9; var6 < this.contents.length; ++var6)
                {
                    if (var4.tryAdding(this, var6, var2, true))
                    {
                        var5 = true;
                        break;
                    }
                }
            }

            if (var5)
            {
                if (!var2)
                {
                    return true;
                }
                else if (var1.stackSize == 0)
                {
                    return true;
                }
                else
                {
                    this.addItem(var1, var5, var3);
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }

    public ItemStack extractItem(boolean var1, Orientations var2)
    {
        for (int var3 = 9; var3 < this.contents.length; ++var3)
        {
            if (this.contents[var3] != null)
            {
                if (var1)
                {
                    return this.decrStackSize(var3, 1);
                }

                return this.contents[var3];
            }
        }

        return null;
    }

    public void handleDescriptionPacket(PacketUpdate var1)
    {
        boolean var2 = this.box.isInitialized();
        super.handleDescriptionPacket(var1);
        this.currentPattern = FillerRegistry.getPattern(this.currentPatternId);
        this.worldObj.markBlockAsNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);

        if (!var2 && this.box.isInitialized())
        {
            this.box.createLasers(this.worldObj, LaserKind.Stripes);
        }
    }

    public void handleUpdatePacket(PacketUpdate var1)
    {
        boolean var2 = this.box.isInitialized();
        super.handleUpdatePacket(var1);
        this.currentPattern = FillerRegistry.getPattern(this.currentPatternId);
        this.worldObj.markBlockAsNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);

        if (!var2 && this.box.isInitialized())
        {
            this.box.createLasers(this.worldObj, LaserKind.Stripes);
        }
    }

    public void setPowerProvider(PowerProvider var1)
    {
        this.powerProvider = var1;
    }

    public PowerProvider getPowerProvider()
    {
        return this.powerProvider;
    }

    public boolean isActive()
    {
        return true;
    }

    public boolean manageLiquids()
    {
        return false;
    }

    public boolean manageSolids()
    {
        return true;
    }

    public void openChest() {}

    public void closeChest() {}

    public int powerRequest()
    {
        return this.powerProvider.maxEnergyReceived;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.contents[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var2 = this.contents[var1];
            this.contents[var1] = null;
            return var2;
        }
    }
}
