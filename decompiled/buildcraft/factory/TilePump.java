package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.TileNetworkData;
import buildcraft.core.BlockIndex;
import buildcraft.core.EntityBlock;
import buildcraft.core.IMachine;
import buildcraft.core.Utils;
import buildcraft.core.network.PacketUpdate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class TilePump extends TileMachine implements IMachine, IPowerReceptor
{
    EntityBlock tube;
    private TreeMap blocksToPump = new TreeMap();
    @TileNetworkData
    public int internalLiquid;
    @TileNetworkData
    public double tubeY = Double.NaN;
    @TileNetworkData
    public int aimY = 0;
    @TileNetworkData
    public int liquidId = 0;
    private PowerProvider powerProvider;

    public TilePump()
    {
        this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
        this.powerProvider.configure(20, 10, 10, 10, 100);
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.tube != null)
        {
            if (!APIProxy.isClient(this.worldObj))
            {
                if (this.tube.posY - (double)this.aimY > 0.01D)
                {
                    this.tubeY = this.tube.posY - 0.01D;
                    this.setTubePosition();

                    if (APIProxy.isServerSide())
                    {
                        this.sendNetworkUpdate();
                    }

                    return;
                }

                if (this.internalLiquid <= 0)
                {
                    BlockIndex var1 = this.getNextIndexToPump(false);
                    int var2;

                    if (this.isPumpableLiquid(var1))
                    {
                        var2 = Utils.liquidId(this.worldObj.getBlockId(var1.i, var1.j, var1.k));

                        if (this.internalLiquid == 0 || this.liquidId == var2)
                        {
                            this.liquidId = var2;

                            if (this.powerProvider.useEnergy(10, 10, true) == 10)
                            {
                                var1 = this.getNextIndexToPump(true);

                                if (this.liquidId != Block.waterStill.blockID || BuildCraftCore.consumeWaterSources)
                                {
                                    this.worldObj.setBlockWithNotify(var1.i, var1.j, var1.k, 0);
                                }

                                this.internalLiquid = this.internalLiquid += 1000;

                                if (APIProxy.isServerSide())
                                {
                                    this.sendNetworkUpdate();
                                }
                            }
                        }
                    }
                    else if (this.worldObj.getWorldTime() % 100L == 0L)
                    {
                        this.initializePumpFromPosition(this.xCoord, this.aimY, this.zCoord);

                        if (this.getNextIndexToPump(false) == null)
                        {
                            for (var2 = this.yCoord - 1; var2 > 0; --var2)
                            {
                                if (this.isLiquid(new BlockIndex(this.xCoord, var2, this.zCoord)))
                                {
                                    this.aimY = var2;
                                    return;
                                }

                                if (this.worldObj.getBlockId(this.xCoord, var2, this.zCoord) != 0)
                                {
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            if (this.internalLiquid >= 0)
            {
                for (int var4 = 0; var4 < 6; ++var4)
                {
                    Position var5 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var4]);
                    var5.moveForwards(1.0D);
                    TileEntity var3 = this.worldObj.getBlockTileEntity((int)var5.x, (int)var5.y, (int)var5.z);

                    if (var3 instanceof ILiquidContainer)
                    {
                        this.internalLiquid -= ((ILiquidContainer)var3).fill(var5.orientation.reverse(), this.internalLiquid, this.liquidId, true);

                        if (this.internalLiquid <= 0)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    public void initialize()
    {
        this.tube = new EntityBlock(this.worldObj);
        this.tube.texture = 102;

        if (!Double.isNaN(this.tubeY))
        {
            this.tube.posY = this.tubeY;
        }
        else
        {
            this.tube.posY = (double)this.yCoord;
        }

        this.tubeY = this.tube.posY;

        if (this.aimY == 0)
        {
            this.aimY = this.yCoord;
        }

        this.setTubePosition();
        this.worldObj.spawnEntityInWorld(this.tube);

        if (APIProxy.isServerSide())
        {
            this.sendNetworkUpdate();
        }
    }

    private BlockIndex getNextIndexToPump(boolean var1)
    {
        LinkedList var2 = null;
        int var3 = 0;
        Iterator var4 = this.blocksToPump.keySet().iterator();

        while (var4.hasNext())
        {
            Integer var5 = (Integer)var4.next();

            if (var5.intValue() > var3 && ((LinkedList)this.blocksToPump.get(var5)).size() != 0)
            {
                var3 = var5.intValue();
                var2 = (LinkedList)this.blocksToPump.get(var5);
            }
        }

        if (var2 != null)
        {
            if (var1)
            {
                BlockIndex var6 = (BlockIndex)var2.pop();

                if (var2.size() == 0)
                {
                    this.blocksToPump.remove(Integer.valueOf(var3));
                }

                return var6;
            }
            else
            {
                return (BlockIndex)var2.getLast();
            }
        }
        else
        {
            return null;
        }
    }

    private void initializePumpFromPosition(int var1, int var2, int var3)
    {
        boolean var4 = false;
        TreeSet var5 = new TreeSet();
        TreeSet var6 = new TreeSet();

        if (!this.blocksToPump.containsKey(Integer.valueOf(var2)))
        {
            this.blocksToPump.put(Integer.valueOf(var2), new LinkedList());
        }

        LinkedList var7 = (LinkedList)this.blocksToPump.get(Integer.valueOf(var2));
        int var11 = this.worldObj.getBlockId(var1, var2, var3);

        if (this.isLiquid(new BlockIndex(var1, var2, var3)))
        {
            this.addToPumpIfLiquid(new BlockIndex(var1, var2, var3), var5, var6, var7, var11);

            while (var6.size() > 0)
            {
                TreeSet var8 = new TreeSet(var6);
                var6.clear();
                Iterator var9 = var8.iterator();

                while (var9.hasNext())
                {
                    BlockIndex var10 = (BlockIndex)var9.next();
                    this.addToPumpIfLiquid(new BlockIndex(var10.i + 1, var10.j, var10.k), var5, var6, var7, var11);
                    this.addToPumpIfLiquid(new BlockIndex(var10.i - 1, var10.j, var10.k), var5, var6, var7, var11);
                    this.addToPumpIfLiquid(new BlockIndex(var10.i, var10.j, var10.k + 1), var5, var6, var7, var11);
                    this.addToPumpIfLiquid(new BlockIndex(var10.i, var10.j, var10.k - 1), var5, var6, var7, var11);

                    if (!this.blocksToPump.containsKey(Integer.valueOf(var10.j + 1)))
                    {
                        this.blocksToPump.put(Integer.valueOf(var10.j + 1), new LinkedList());
                    }

                    var7 = (LinkedList)this.blocksToPump.get(Integer.valueOf(var10.j + 1));
                    this.addToPumpIfLiquid(new BlockIndex(var10.i, var10.j + 1, var10.k), var5, var6, var7, var11);
                }
            }
        }
    }

    public void addToPumpIfLiquid(BlockIndex var1, TreeSet var2, TreeSet var3, LinkedList var4, int var5)
    {
        if (var5 == this.worldObj.getBlockId(var1.i, var1.j, var1.k))
        {
            if (!var2.contains(var1))
            {
                var2.add(var1);

                if ((var1.i - this.xCoord) * (var1.i - this.xCoord) + (var1.k - this.zCoord) * (var1.k - this.zCoord) > 4096)
                {
                    return;
                }

                if (this.isPumpableLiquid(var1))
                {
                    var4.push(var1);
                }

                if (this.isLiquid(var1))
                {
                    var3.add(var1);
                }
            }
        }
    }

    private boolean isPumpableLiquid(BlockIndex var1)
    {
        return this.isLiquid(var1) && this.worldObj.getBlockMetadata(var1.i, var1.j, var1.k) == 0;
    }

    private boolean isLiquid(BlockIndex var1)
    {
        return var1 != null && Utils.liquidId(this.worldObj.getBlockId(var1.i, var1.j, var1.k)) != 0;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        this.internalLiquid = var1.getInteger("internalLiquid");
        this.aimY = var1.getInteger("aimY");
        this.tubeY = (double)var1.getFloat("tubeY");
        this.liquidId = var1.getInteger("liquidId");
        PowerFramework.currentFramework.loadPowerProvider(this, var1);
        this.powerProvider.configure(20, 10, 10, 10, 100);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        PowerFramework.currentFramework.savePowerProvider(this, var1);
        var1.setInteger("internalLiquid", this.internalLiquid);
        var1.setInteger("aimY", this.aimY);

        if (this.tube != null)
        {
            var1.setFloat("tubeY", (float)this.tube.posY);
        }
        else
        {
            var1.setFloat("tubeY", (float)this.yCoord);
        }

        var1.setInteger("liquidId", this.liquidId);
    }

    public boolean isActive()
    {
        return true;
    }

    public void setPowerProvider(PowerProvider var1)
    {
        this.powerProvider = var1;
    }

    public PowerProvider getPowerProvider()
    {
        return this.powerProvider;
    }

    public void doWork() {}

    public void handleDescriptionPacket(PacketUpdate var1)
    {
        super.handleDescriptionPacket(var1);
        this.setTubePosition();
    }

    public void handleUpdatePacket(PacketUpdate var1)
    {
        super.handleDescriptionPacket(var1);
        this.setTubePosition();
    }

    private void setTubePosition()
    {
        if (this.tube != null)
        {
            this.tube.iSize = 0.5D;
            this.tube.kSize = 0.5D;
            this.tube.jSize = (double)this.yCoord - this.tube.posY;
            this.tube.setPosition((double)((float)this.xCoord + 0.25F), this.tubeY, (double)((float)this.zCoord + 0.25F));
        }
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
        if (this.tube != null)
        {
            APIProxy.removeEntity(this.tube);
            this.tube = null;
        }
    }

    public boolean manageLiquids()
    {
        return true;
    }

    public boolean manageSolids()
    {
        return false;
    }
}
