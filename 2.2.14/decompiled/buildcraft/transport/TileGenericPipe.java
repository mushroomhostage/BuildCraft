package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.EntityPassiveItem;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPipeEntry;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.PowerProvider;
import buildcraft.api.SafeTimeTracker;
import buildcraft.api.TileNetworkData;
import buildcraft.core.BlockIndex;
import buildcraft.core.CoreProxy;
import buildcraft.core.PersistentTile;
import buildcraft.core.PersistentWorld;
import buildcraft.core.network.ISynchronizedTile;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketPipeDescription;
import buildcraft.core.network.PacketTileUpdate;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.TileEntity;
import net.minecraft.server.mod_BuildCraftCore;

public class TileGenericPipe extends TileEntity implements IPowerReceptor, ILiquidContainer, ISpecialInventory, IPipeEntry, ISynchronizedTile
{
    public SafeTimeTracker networkSyncTracker = new SafeTimeTracker();
    public Pipe pipe;
    private boolean blockNeighborChange = false;
    private boolean pipeBound = false;
    @TileNetworkData
    public int pipeId = -1;

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound var1)
    {
        super.b(var1);

        if (this.pipe != null)
        {
            var1.setInt("pipeId", this.pipe.itemID);
            this.pipe.writeToNBT(var1);
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound var1)
    {
        super.a(var1);
        this.pipe = BlockGenericPipe.createPipe(var1.getInt("pipeId"));

        if (this.pipe != null)
        {
            this.pipe.setTile(this);
            this.pipe.readFromNBT(var1);
        }
    }

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

    public ItemStack[] getContents() {
        return null;
    }
    // CraftBukkit end

    public void synchronizeIfDelay(int var1)
    {
        if (APIProxy.isServerSide() && this.networkSyncTracker.markTimeIfDelay(this.world, (long)var1))
        {
            CoreProxy.sendToPlayers(this.getUpdatePacket(), this.x, this.y, this.z, 40, mod_BuildCraftCore.instance);
        }
    }

    /**
     * invalidates a tile entity
     */
    public void j()
    {
        super.j();

        if (BlockGenericPipe.isValid(this.pipe))
        {
            BlockGenericPipe.removePipe(this.pipe);
        }

        PersistentWorld.getWorld(this.world).removeTile(new BlockIndex(this.x, this.y, this.z));
    }

    /**
     * validates a tile entity
     */
    public void m()
    {
        this.bindPipe();
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void q_()
    {
        this.bindPipe();

        if (this.pipe != null)
        {
            this.pipe.initialize();
        }

        if (BlockGenericPipe.isValid(this.pipe))
        {
            if (this.blockNeighborChange)
            {
                this.pipe.onNeighborBlockChange();
                this.blockNeighborChange = false;
            }

            PowerProvider var1 = this.getPowerProvider();

            if (var1 != null)
            {
                var1.update(this);
            }

            if (this.pipe != null)
            {
                this.pipe.updateEntity();
            }
        }
    }

    private void bindPipe()
    {
        if (!this.pipeBound)
        {
            if (this.pipe == null)
            {
                PersistentTile var1 = PersistentWorld.getWorld(this.world).getTile(new BlockIndex(this.x, this.y, this.z));

                if (var1 != null && var1 instanceof Pipe)
                {
                    this.pipe = (Pipe)var1;
                }
            }

            if (this.pipe != null)
            {
                this.pipe.setTile(this);
                this.pipe.setWorld(this.world);
                PersistentWorld.getWorld(this.world).storeTile(this.pipe, new BlockIndex(this.x, this.y, this.z));
                this.pipeId = this.pipe.itemID;
                this.pipeBound = true;
            }
        }
    }

    public void setPowerProvider(PowerProvider var1)
    {
        if (BlockGenericPipe.isValid(this.pipe) && this.pipe instanceof IPowerReceptor)
        {
            ((IPowerReceptor)this.pipe).setPowerProvider(var1);
        }
    }

    public PowerProvider getPowerProvider()
    {
        return BlockGenericPipe.isValid(this.pipe) && this.pipe instanceof IPowerReceptor ? ((IPowerReceptor)this.pipe).getPowerProvider() : null;
    }

    public void doWork()
    {
        if (BlockGenericPipe.isValid(this.pipe) && this.pipe instanceof IPowerReceptor)
        {
            ((IPowerReceptor)this.pipe).doWork();
        }
    }

    public int fill(Orientations var1, int var2, int var3, boolean var4)
    {
        return BlockGenericPipe.isValid(this.pipe) && this.pipe.transport instanceof ILiquidContainer ? ((ILiquidContainer)this.pipe.transport).fill(var1, var2, var3, var4) : 0;
    }

    public int empty(int var1, boolean var2)
    {
        return BlockGenericPipe.isValid(this.pipe) && this.pipe.transport instanceof ILiquidContainer ? ((ILiquidContainer)this.pipe.transport).empty(var1, var2) : 0;
    }

    public int getLiquidQuantity()
    {
        return BlockGenericPipe.isValid(this.pipe) && this.pipe.transport instanceof ILiquidContainer ? ((ILiquidContainer)this.pipe.transport).getLiquidQuantity() : 0;
    }

    public int getCapacity()
    {
        return BlockGenericPipe.isValid(this.pipe) && this.pipe.transport instanceof ILiquidContainer ? ((ILiquidContainer)this.pipe.transport).getCapacity() : 0;
    }

    public int getLiquidId()
    {
        return BlockGenericPipe.isValid(this.pipe) && this.pipe.transport instanceof ILiquidContainer ? ((ILiquidContainer)this.pipe.transport).getLiquidId() : 0;
    }

    public void scheduleNeighborChange()
    {
        this.blockNeighborChange = true;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSize()
    {
        return BlockGenericPipe.isFullyDefined(this.pipe) ? this.pipe.logic.getSizeInventory() : 0;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getItem(int var1)
    {
        return BlockGenericPipe.isFullyDefined(this.pipe) ? this.pipe.logic.getStackInSlot(var1) : null;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack splitStack(int var1, int var2)
    {
        return BlockGenericPipe.isFullyDefined(this.pipe) ? this.pipe.logic.decrStackSize(var1, var2) : null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int var1, ItemStack var2)
    {
        if (BlockGenericPipe.isFullyDefined(this.pipe))
        {
            this.pipe.logic.setInventorySlotContents(var1, var2);
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getName()
    {
        return BlockGenericPipe.isFullyDefined(this.pipe) ? this.pipe.logic.getInvName() : "";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getMaxStackSize()
    {
        return BlockGenericPipe.isFullyDefined(this.pipe) ? this.pipe.logic.getInventoryStackLimit() : 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean a(EntityHuman var1)
    {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : (BlockGenericPipe.isFullyDefined(this.pipe) ? this.pipe.logic.canInteractWith(var1) : false);
    }

    public boolean addItem(ItemStack var1, boolean var2, Orientations var3)
    {
        return BlockGenericPipe.isValid(this.pipe) ? this.pipe.logic.addItem(var1, var2, var3) : false;
    }

    public ItemStack extractItem(boolean var1, Orientations var2)
    {
        return BlockGenericPipe.isValid(this.pipe) ? this.pipe.logic.extractItem(var1, var2) : null;
    }

    public void entityEntering(EntityPassiveItem var1, Orientations var2)
    {
        if (BlockGenericPipe.isValid(this.pipe))
        {
            this.pipe.transport.entityEntering(var1, var2);
        }
    }

    public boolean acceptItems()
    {
        return BlockGenericPipe.isValid(this.pipe) ? this.pipe.transport.acceptItems() : false;
    }

    public void handleDescriptionPacket(PacketUpdate var1)
    {
        if (this.pipe == null && var1.payload.intPayload[0] != 0)
        {
            this.pipe = BlockGenericPipe.createPipe(var1.payload.intPayload[0]);
            this.pipeBound = false;
            this.bindPipe();

            if (this.pipe != null)
            {
                this.pipe.initialize();
            }
        }
    }

    public void handleUpdatePacket(PacketUpdate var1)
    {
        if (BlockGenericPipe.isValid(this.pipe))
        {
            this.pipe.handlePacket(var1);
        }
    }

    public void postPacketHandling(PacketUpdate var1) {}

    public Packet getUpdatePacket()
    {
        return (new PacketTileUpdate(this)).getPacket();
    }

    /**
     * Overriden in a sign to provide the text
     */
    public Packet d()
    {
        this.bindPipe();
        PacketPipeDescription var1;

        if (this.pipe != null)
        {
            var1 = new PacketPipeDescription(this.x, this.y, this.z, this.pipe.itemID);
        }
        else
        {
            var1 = new PacketPipeDescription(this.x, this.y, this.z, 0);
        }

        return var1.getPacket();
    }

    public PacketPayload getPacketPayload()
    {
        return this.pipe.getNetworkPacket();
    }

    public void f() {}

    public void g() {}

    public int powerRequest()
    {
        return this.getPowerProvider().maxEnergyReceived;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack splitWithoutUpdate(int var1)
    {
        return null;
    }
}
