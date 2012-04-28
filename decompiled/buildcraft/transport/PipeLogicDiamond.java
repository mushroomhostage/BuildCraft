package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.SafeTimeTracker;
import buildcraft.api.TileNetworkData;
import buildcraft.core.CoreProxy;
import buildcraft.core.network.PacketUpdate;
import buildcraft.core.network.TilePacketWrapper;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet;
import net.minecraft.server.mod_BuildCraftTransport;

public class PipeLogicDiamond extends PipeLogic
{
    ItemStack[] items = new ItemStack[54];
    private static TilePacketWrapper networkPacket;
    private SafeTimeTracker tracker = new SafeTimeTracker();

    public PipeLogicDiamond()
    {
        if (networkPacket == null)
        {
            networkPacket = new TilePacketWrapper(new Class[] {PipeLogicDiamond.PacketStack.class});
        }
    }

    public boolean blockActivated(EntityPlayer var1)
    {
        if (var1.getCurrentEquippedItem() != null && var1.getCurrentEquippedItem().itemID < Block.blocksList.length && Block.blocksList[var1.getCurrentEquippedItem().itemID] instanceof BlockGenericPipe)
        {
            return false;
        }
        else
        {
            if (!APIProxy.isClient(this.container.worldObj))
            {
                var1.openGui(mod_BuildCraftTransport.instance, 50, this.container.worldObj, this.container.xCoord, this.container.yCoord, this.container.zCoord);
            }

            return true;
        }
    }

    public int getSizeInventory()
    {
        return this.items.length;
    }

    public ItemStack getStackInSlot(int var1)
    {
        return this.items[var1];
    }

    public ItemStack decrStackSize(int var1, int var2)
    {
        ItemStack var3 = this.items[var1].copy();
        var3.stackSize = var2;
        this.items[var1].stackSize -= var2;

        if (this.items[var1].stackSize == 0)
        {
            this.items[var1] = null;
        }

        if (APIProxy.isServerSide())
        {
            for (int var4 = 0; var4 < 6; ++var4)
            {
                CoreProxy.sendToPlayers(this.getContentsPacket(var4), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
            }
        }

        return var3;
    }

    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        if (this.items[var1] != null || var2 != null)
        {
            if (this.items[var1] == null || var2 == null || !this.items[var1].isStackEqual(var2))
            {
                if (var2 != null)
                {
                    this.items[var1] = var2.copy();
                }
                else
                {
                    this.items[var1] = null;
                }

                if (APIProxy.isServerSide())
                {
                    for (int var3 = 0; var3 < 6; ++var3)
                    {
                        CoreProxy.sendToPlayers(this.getContentsPacket(var3), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
                    }
                }
            }
        }
    }

    public void updateEntity()
    {
        if (this.tracker.markTimeIfDelay(this.worldObj, (long)(20 * BuildCraftCore.updateFactor)) && APIProxy.isServerSide())
        {
            for (int var1 = 0; var1 < 6; ++var1)
            {
                CoreProxy.sendToPlayers(this.getContentsPacket(var1), this.xCoord, this.yCoord, this.zCoord, 50, mod_BuildCraftTransport.instance);
            }
        }
    }

    public String getInvName()
    {
        return "Filters";
    }

    public int getInventoryStackLimit()
    {
        return 1;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagList var2 = var1.getTagList("items");

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getInteger("index");
            this.items[var5] = ItemStack.loadItemStackFromNBT(var4);
        }
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.items.length; ++var3)
        {
            if (this.items[var3] != null && this.items[var3].stackSize > 0)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var2.appendTag(var4);
                var4.setInteger("index", var3);
                this.items[var3].writeToNBT(var4);
            }
        }

        var1.setTag("items", var2);
    }

    public boolean addItem(ItemStack var1, boolean var2, Orientations var3)
    {
        return false;
    }

    public ItemStack extractItem(boolean var1, Orientations var2)
    {
        return null;
    }

    public Packet getContentsPacket(int var1)
    {
        PipeLogicDiamond.PacketStack var2 = new PipeLogicDiamond.PacketStack();
        var2.num = var1;

        for (int var3 = 0; var3 < 9; ++var3)
        {
            if (this.items[var3 + var1 * 9] == null)
            {
                var2.ids[var3] = -1;
                var2.dmg[var3] = -1;
            }
            else
            {
                var2.ids[var3] = (short)this.items[var3 + var1 * 9].itemID;
                var2.dmg[var3] = this.items[var3 + var1 * 9].getItemDamage();
            }
        }

        return (new PacketUpdate(30, networkPacket.toPayload(this.xCoord, this.yCoord, this.zCoord, (Object)var2))).getPacket();
    }

    public void handleContentsPacket(PacketUpdate var1)
    {
        PipeLogicDiamond.PacketStack var2 = new PipeLogicDiamond.PacketStack();
        networkPacket.fromPayload((Object)var2, var1.payload);
        int var3 = var2.num;

        for (int var4 = 0; var4 < 9; ++var4)
        {
            if (var2.ids[var4] == -1)
            {
                this.items[var3 * 9 + var4] = null;
            }
            else
            {
                this.items[var3 * 9 + var4] = new ItemStack(var2.ids[var4], 1, var2.dmg[var4]);
            }
        }
    }

    public class PacketStack
    {
        @TileNetworkData(
                intKind = 1
        )
        public int num;
        @TileNetworkData(
                staticSize = 9
        )
        public short[] ids = new short[9];
        @TileNetworkData(
                staticSize = 9,
                intKind = 1
        )
        public int[] dmg = new int[9];
    }
}
