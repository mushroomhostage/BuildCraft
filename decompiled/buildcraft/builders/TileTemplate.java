package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.api.IAreaProvider;
import buildcraft.api.LaserKind;
import buildcraft.api.Orientations;
import buildcraft.api.TileNetworkData;
import buildcraft.core.BluePrint;
import buildcraft.core.Box;
import buildcraft.core.CoreProxy;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.Utils;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.server.BuildCraftBuilders;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

public class TileTemplate extends TileBuildCraft implements IInventory
{
    @TileNetworkData
    public Box box = new Box();
    private ItemStack[] items = new ItemStack[2];
    private boolean isComputing = false;
    public int computingTime = 0;
    private int lastTemplateId = 0;

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.isComputing)
        {
            if (this.computingTime < 200)
            {
                ++this.computingTime;
            }
            else
            {
                this.createBluePrint();
            }
        }
    }

    public void initialize()
    {
        super.initialize();

        if (!this.box.isInitialized())
        {
            IAreaProvider var1 = Utils.getNearbyAreaProvider(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

            if (var1 != null)
            {
                this.box.initialize(var1);
                var1.removeFromWorld();
            }
        }

        if (!APIProxy.isClient(this.worldObj) && this.box.isInitialized())
        {
            this.box.createLasers(this.worldObj, LaserKind.Stripes);
        }

        this.sendNetworkUpdate();
    }

    public void createBluePrint()
    {
        if (this.box.isInitialized() && this.items[1] == null)
        {
            byte var1 = 1;
            byte var2 = 0;

            if (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
            {
                var1 = 0;
                var2 = 1;
            }

            BluePrint var3 = new BluePrint(this.box.sizeX(), this.box.sizeY(), this.box.sizeZ());
            int var6;

            for (int var4 = this.box.xMin; var4 <= this.box.xMax; ++var4)
            {
                for (int var5 = this.box.yMin; var5 <= this.box.yMax; ++var5)
                {
                    for (var6 = this.box.zMin; var6 <= this.box.zMax; ++var6)
                    {
                        if (this.worldObj.getBlockId(var4, var5, var6) != 0)
                        {
                            var3.setBlockId(var4 - this.box.xMin, var5 - this.box.yMin, var6 - this.box.zMin, var1);
                        }
                        else
                        {
                            var3.setBlockId(var4 - this.box.xMin, var5 - this.box.yMin, var6 - this.box.zMin, var2);
                        }
                    }
                }
            }

            var3.anchorX = this.xCoord - this.box.xMin;
            var3.anchorY = this.yCoord - this.box.yMin;
            var3.anchorZ = this.zCoord - this.box.zMin;
            Orientations var7 = Orientations.values()[this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)].reverse();

            if (var7 != Orientations.XPos)
            {
                if (var7 == Orientations.ZPos)
                {
                    var3.rotateLeft();
                    var3.rotateLeft();
                    var3.rotateLeft();
                }
                else if (var7 == Orientations.XNeg)
                {
                    var3.rotateLeft();
                    var3.rotateLeft();
                }
                else if (var7 == Orientations.ZNeg)
                {
                    var3.rotateLeft();
                }
            }

            ItemStack var8 = new ItemStack(BuildCraftBuilders.templateItem);

            if (var3.equals(BuildCraftBuilders.bluePrints[this.lastTemplateId]))
            {
                BluePrint var10000 = BuildCraftBuilders.bluePrints[this.lastTemplateId];
                var8.setItemDamage(this.lastTemplateId);
            }
            else
            {
                var6 = BuildCraftBuilders.storeBluePrint(var3);
                var8.setItemDamage(var6);
                CoreProxy.addName(var8, "Template #" + var6);
                this.lastTemplateId = var6;
            }

            this.setInventorySlotContents(0, (ItemStack)null);
            this.setInventorySlotContents(1, var8);
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 2;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.items[var1];
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        ItemStack var3;

        if (this.items[var1] == null)
        {
            var3 = null;
        }
        else if (this.items[var1].stackSize > var2)
        {
            var3 = this.items[var1].splitStack(var2);
        }
        else
        {
            ItemStack var4 = this.items[var1];
            this.items[var1] = null;
            var3 = var4;
        }

        this.initializeComputing();
        return var3;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.items[var1] = var2;
        this.initializeComputing();
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Template";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 1;
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
        this.lastTemplateId = var1.getInteger("lastTemplateId");
        this.computingTime = var1.getInteger("computingTime");
        this.isComputing = var1.getBoolean("isComputing");

        if (var1.hasKey("box"))
        {
            this.box.initialize(var1.getCompoundTag("box"));
        }

        NBTTagList var2 = var1.getTagList("Items");
        this.items = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.items.length)
            {
                this.items[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("lastTemplateId", this.lastTemplateId);
        var1.setInteger("computingTime", this.computingTime);
        var1.setBoolean("isComputing", this.isComputing);

        if (this.box.isInitialized())
        {
            NBTTagCompound var2 = new NBTTagCompound();
            this.box.writeToNBT(var2);
            var1.setTag("box", var2);
        }

        NBTTagList var5 = new NBTTagList();

        for (int var3 = 0; var3 < this.items.length; ++var3)
        {
            if (this.items[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.items[var3].writeToNBT(var4);
                var5.appendTag(var4);
            }
        }

        var1.setTag("Items", var5);
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
        if (this.box.isInitialized())
        {
            this.box.deleteLasers();
        }
    }

    private void initializeComputing()
    {
        if (this.box.isInitialized())
        {
            if (!this.isComputing)
            {
                if (this.items[0] != null && this.items[0].getItem() instanceof ItemTemplate && this.items[1] == null)
                {
                    this.isComputing = true;
                    this.computingTime = 0;
                }
                else
                {
                    this.isComputing = false;
                    this.computingTime = 0;
                }
            }
            else if (this.items[0] == null || !(this.items[0].getItem() instanceof ItemTemplate))
            {
                this.isComputing = false;
                this.computingTime = 0;
            }
        }
    }

    public int getComputingProgressScaled(int var1)
    {
        return this.computingTime * var1 / 200;
    }

    public void handleDescriptionPacket(PacketUpdate var1)
    {
        boolean var2 = this.box.isInitialized();
        super.handleDescriptionPacket(var1);

        if (!var2 && this.box.isInitialized())
        {
            this.box.createLasers(this.worldObj, LaserKind.Stripes);
        }
    }

    public void handleUpdatePacket(PacketUpdate var1)
    {
        boolean var2 = this.box.isInitialized();
        super.handleUpdatePacket(var1);

        if (!var2 && this.box.isInitialized())
        {
            this.box.createLasers(this.worldObj, LaserKind.Stripes);
        }
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.items[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var2 = this.items[var1];
            this.items[var1] = null;
            return var2;
        }
    }
}
