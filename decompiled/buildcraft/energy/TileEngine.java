package buildcraft.energy;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.TileNetworkData;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.TileEntity;

public class TileEngine extends TileBuildCraft implements IPowerReceptor, IInventory, ILiquidContainer, IEngineProvider
{
    @TileNetworkData
    public Engine engine;
    @TileNetworkData
    public int progressPart = 0;
    @TileNetworkData
    public float serverPistonSpeed = 0.0F;
    boolean lastPower = false;
    public int orientation;
    private ItemStack itemInInventory;
    PowerProvider provider;

    public TileEngine()
    {
        this.provider = PowerFramework.currentFramework.createPowerProvider();
    }

    public void initialize()
    {
        if (!APIProxy.isClient(this.worldObj))
        {
            if (this.engine == null)
            {
                this.createEngineIfNeeded();
            }

            this.engine.orientation = Orientations.values()[this.orientation];
            this.provider.configure(0, 1, this.engine.maxEnergyReceived(), 1, this.engine.maxEnergy);
        }
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.engine != null)
        {
            if (APIProxy.isClient(this.worldObj))
            {
                if (this.progressPart != 0)
                {
                    this.engine.progress += this.serverPistonSpeed;

                    if (this.engine.progress > 1.0F)
                    {
                        this.progressPart = 0;
                    }
                }
            }
            else
            {
                this.engine.update();
                boolean var1 = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
                Position var2;
                TileEntity var3;
                IPowerReceptor var4;

                if (this.progressPart != 0)
                {
                    this.engine.progress += this.engine.getPistonSpeed();

                    if ((double)this.engine.progress > 0.5D && this.progressPart == 1)
                    {
                        this.progressPart = 2;
                        var2 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, this.engine.orientation);
                        var2.moveForwards(1.0D);
                        var3 = this.worldObj.getBlockTileEntity((int)var2.x, (int)var2.y, (int)var2.z);

                        if (this.isPoweredTile(var3))
                        {
                            var4 = (IPowerReceptor)var3;
                            int var5 = this.engine.extractEnergy(var4.getPowerProvider().minEnergyReceived, var4.getPowerProvider().maxEnergyReceived, true);

                            if (var5 > 0)
                            {
                                var4.getPowerProvider().receiveEnergy(var5);
                            }
                        }
                    }
                    else if (this.engine.progress >= 1.0F)
                    {
                        this.engine.progress = 0.0F;
                        this.progressPart = 0;
                    }
                }
                else if (var1)
                {
                    var2 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, this.engine.orientation);
                    var2.moveForwards(1.0D);
                    var3 = this.worldObj.getBlockTileEntity((int)var2.x, (int)var2.y, (int)var2.z);

                    if (this.isPoweredTile(var3))
                    {
                        var4 = (IPowerReceptor)var3;

                        if (this.engine.extractEnergy(var4.getPowerProvider().minEnergyReceived, var4.getPowerProvider().maxEnergyReceived, false) > 0)
                        {
                            this.progressPart = 1;
                            this.sendNetworkUpdate();
                        }
                    }
                }
                else if (this.worldObj.getWorldTime() % 20L * 10L == 0L)
                {
                    this.sendNetworkUpdate();
                }

                this.engine.burn();
            }
        }
    }

    private void createEngineIfNeeded()
    {
        if (this.engine == null)
        {
            int var1 = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);

            if (var1 == 0)
            {
                this.engine = new EngineWood(this);
            }
            else if (var1 == 1)
            {
                this.engine = new EngineStone(this);
            }
            else if (var1 == 2)
            {
                this.engine = new EngineIron(this);
            }

            this.engine.orientation = Orientations.values()[this.orientation];
        }
    }

    public void switchOrientation()
    {
        for (int var1 = this.orientation + 1; var1 <= this.orientation + 6; ++var1)
        {
            Orientations var2 = Orientations.values()[var1 % 6];
            Position var3 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, var2);
            var3.moveForwards(1.0D);
            TileEntity var4 = this.worldObj.getBlockTileEntity((int)var3.x, (int)var3.y, (int)var3.z);

            if (this.isPoweredTile(var4))
            {
                if (this.engine != null)
                {
                    this.engine.orientation = var2;
                }

                this.orientation = var2.ordinal();
                this.worldObj.markBlockAsNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);
                break;
            }
        }
    }

    public void delete()
    {
        this.engine.delete();
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        int var2 = var1.getInteger("kind");

        if (var2 == 0)
        {
            this.engine = new EngineWood(this);
        }
        else if (var2 == 1)
        {
            this.engine = new EngineStone(this);
        }
        else if (var2 == 2)
        {
            this.engine = new EngineIron(this);
        }

        this.orientation = var1.getInteger("orientation");
        this.engine.progress = var1.getFloat("progress");
        this.engine.energy = var1.getInteger("energy");
        this.engine.orientation = Orientations.values()[this.orientation];

        if (var1.hasKey("itemInInventory"))
        {
            NBTTagCompound var3 = var1.getCompoundTag("itemInInventory");
            this.itemInInventory = ItemStack.loadItemStackFromNBT(var3);
        }

        this.engine.readFromNBT(var1);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setInteger("kind", this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
        var1.setInteger("orientation", this.orientation);
        var1.setFloat("progress", this.engine.progress);
        var1.setInteger("energy", this.engine.energy);

        if (this.itemInInventory != null)
        {
            NBTTagCompound var2 = new NBTTagCompound();
            this.itemInInventory.writeToNBT(var2);
            var1.setTag("itemInInventory", var2);
        }

        this.engine.writeToNBT(var1);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 1;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.itemInInventory;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        ItemStack var3 = this.itemInInventory.splitStack(var2);

        if (this.itemInInventory.stackSize == 0)
        {
            this.itemInInventory = null;
        }

        return var3;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.itemInInventory = var2;
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Engine";
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

    public boolean isBurning()
    {
        return this.engine != null && this.engine.isBurning();
    }

    public int getScaledBurnTime(int var1)
    {
        return this.engine.getScaledBurnTime(var1);
    }

    /**
     * Overriden in a sign to provide the text
     */
    public Packet getDescriptionPacket()
    {
        this.createEngineIfNeeded();
        return super.getDescriptionPacket();
    }

    public Packet getUpdatePacket()
    {
        this.serverPistonSpeed = this.engine.getPistonSpeed();
        return super.getUpdatePacket();
    }

    public void handleDescriptionPacket(PacketUpdate var1)
    {
        this.createEngineIfNeeded();
        super.handleDescriptionPacket(var1);
    }

    public void handleUpdatePacket(PacketUpdate var1)
    {
        this.createEngineIfNeeded();
        super.handleUpdatePacket(var1);
    }

    public void setPowerProvider(PowerProvider var1)
    {
        this.provider = var1;
    }

    public PowerProvider getPowerProvider()
    {
        return this.provider;
    }

    public void doWork()
    {
        if (!APIProxy.isClient(this.worldObj))
        {
            this.engine.addEnergy((int)((float)this.provider.useEnergy(1, this.engine.maxEnergyReceived(), true) * 0.95F));
        }
    }

    public boolean isPoweredTile(TileEntity var1)
    {
        if (!(var1 instanceof IPowerReceptor))
        {
            return false;
        }
        else
        {
            IPowerReceptor var2 = (IPowerReceptor)var1;
            PowerProvider var3 = var2.getPowerProvider();
            return var3 != null && var3.getClass().equals(PneumaticPowerProvider.class);
        }
    }

    public int fill(Orientations var1, int var2, int var3, boolean var4)
    {
        return this.engine instanceof EngineIron ? ((EngineIron)this.engine).fill(var1, var2, var3, true) : 0;
    }

    public int empty(int var1, boolean var2)
    {
        return 0;
    }

    public int getLiquidQuantity()
    {
        return 0;
    }

    public int getCapacity()
    {
        return 10000;
    }

    public int getLiquidId()
    {
        return 0;
    }

    public void openChest() {}

    public void closeChest() {}

    public int powerRequest()
    {
        return 0;
    }

    public Engine getEngine()
    {
        return this.engine;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return null;
    }
}
