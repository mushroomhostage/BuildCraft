package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPipeEntry;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.api.TileNetworkData;
import buildcraft.core.IMachine;
import buildcraft.core.Utils;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityItem;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class PipeTransportLiquids extends PipeTransport implements ILiquidContainer
{
    public static int LIQUID_IN_PIPE = 250;
    public int travelDelay = 6;
    public int flowRate = 20;
    @TileNetworkData(
            staticSize = 6
    )
    public PipeTransportLiquids.LiquidBuffer[] side = new PipeTransportLiquids.LiquidBuffer[6];
    @TileNetworkData
    public PipeTransportLiquids.LiquidBuffer center;
    boolean[] isInput = new boolean[6];
    boolean[] isOutput = new boolean[] {false, false, false, false, false, false};
    int lockedTime = 0;
    private static long lastSplit = 0L;
    private static int[] splitVector;

    public PipeTransportLiquids()
    {
        for (int var1 = 0; var1 < 6; ++var1)
        {
            this.side[var1] = new PipeTransportLiquids.LiquidBuffer(var1);
            this.isInput[var1] = false;
        }

        this.center = new PipeTransportLiquids.LiquidBuffer(6);
    }

    public boolean canReceiveLiquid(Position var1)
    {
        TileEntity var2 = this.worldObj.getBlockTileEntity((int)var1.x, (int)var1.y, (int)var1.z);
        return this.isInput[var1.orientation.ordinal()] ? false : (!Utils.checkPipesConnections(this.worldObj, (int)var1.x, (int)var1.y, (int)var1.z, this.xCoord, this.yCoord, this.zCoord) ? false : var2 instanceof IPipeEntry || var2 instanceof ILiquidContainer);
    }

    public void updateEntity()
    {
        if (!APIProxy.isClient(this.worldObj))
        {
            this.moveLiquids();
            this.container.synchronizeIfDelay(1 * BuildCraftCore.updateFactor);
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);

        for (int var2 = 0; var2 < 6; ++var2)
        {
            if (var1.hasKey("side[" + var2 + "]"))
            {
                this.side[var2].readFromNBT(var1.getCompoundTag("side[" + var2 + "]"));
            }

            this.isInput[var2] = var1.getBoolean("isInput[" + var2 + "]");
        }

        if (var1.hasKey("center"))
        {
            this.center.readFromNBT(var1.getCompoundTag("center"));
        }

        NBTTagCompound var3 = new NBTTagCompound();
        this.center.writeToNBT(var3);
        var1.setTag("center", var3);
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);

        for (int var2 = 0; var2 < 6; ++var2)
        {
            NBTTagCompound var3 = new NBTTagCompound();
            this.side[var2].writeToNBT(var3);
            var1.setTag("side[" + var2 + "]", var3);
            var1.setBoolean("isInput[" + var2 + "]", this.isInput[var2]);
        }

        NBTTagCompound var4 = new NBTTagCompound();
        this.center.writeToNBT(var4);
        var1.setTag("center", var4);
    }

    protected void doWork() {}

    public void onDropped(EntityItem var1) {}

    public int fill(Orientations var1, int var2, int var3, boolean var4)
    {
        this.isInput[var1.ordinal()] = true;
        return this.container.pipe instanceof IPipeTransportLiquidsHook ? ((IPipeTransportLiquidsHook)this.container.pipe).fill(var1, var2, var3, var4) : this.side[var1.ordinal()].fill(var2, var4, (short)var3);
    }

    private void moveLiquids()
    {
        this.isOutput = new boolean[] {false, false, false, false, false, false};
        int var1 = this.computeOutputs();

        if (var1 == 0)
        {
            ++this.lockedTime;
        }
        else
        {
            this.lockedTime = 0;
        }

        if (this.lockedTime > 20)
        {
            for (int var2 = 0; var2 < 6; ++var2)
            {
                this.isInput[var2] = false;
            }

            var1 = this.computeOutputs();
        }

        int[] var5 = getSplitVector(this.worldObj);
        int var3;
        int var4;

        for (var3 = 0; var3 < 6; ++var3)
        {
            var4 = var5[var3];
            this.side[var4].empty(this.flowRate);
        }

        this.center.empty(this.flowRate);
        this.center.update();

        for (var3 = 0; var3 < 6; ++var3)
        {
            var4 = var5[var3];
            this.side[var4].update();

            if (this.side[var4].qty != 0)
            {
                this.side[var4].emptyTime = 0;
            }

            if (this.side[var4].bouncing)
            {
                this.isInput[var4] = true;
            }
            else if (this.side[var4].qty == 0)
            {
                ++this.side[var4].emptyTime;
            }

            if (this.side[var4].emptyTime > 20)
            {
                this.isInput[var4] = false;
            }
        }
    }

    private int computeOutputs()
    {
        int var1 = 0;

        for (int var2 = 0; var2 < 6; ++var2)
        {
            Position var3 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var2]);
            var3.moveForwards(1.0D);
            this.isOutput[var2] = this.container.pipe.outputOpen(var3.orientation) && this.canReceiveLiquid(var3) && !this.isInput[var2];

            if (this.isOutput[var2])
            {
                ++var1;
            }
        }

        return var1;
    }

    public int getSide(int var1)
    {
        return this.side[var1].average > LIQUID_IN_PIPE ? LIQUID_IN_PIPE : this.side[var1].average;
    }

    public int getCenter()
    {
        return this.center.average > LIQUID_IN_PIPE ? LIQUID_IN_PIPE : this.center.average;
    }

    public int getLiquidQuantity()
    {
        int var1 = this.center.qty;
        PipeTransportLiquids.LiquidBuffer[] var2 = this.side;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            PipeTransportLiquids.LiquidBuffer var5 = var2[var4];
            var1 += var5.qty;
        }

        return var1;
    }

    public int getCapacity()
    {
        return 0;
    }

    public int empty(int var1, boolean var2)
    {
        return 0;
    }

    public void onNeighborBlockChange()
    {
        super.onNeighborBlockChange();

        for (int var1 = 0; var1 < 6; ++var1)
        {
            Position var2 = new Position((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, Orientations.values()[var1]);
            var2.moveForwards(1.0D);

            if (!Utils.checkPipesConnections(this.worldObj, (int)var2.x, (int)var2.y, (int)var2.z, this.xCoord, this.yCoord, this.zCoord))
            {
                this.side[var1].reset();
            }
        }
    }

    public int getLiquidId()
    {
        return this.center.liquidId;
    }

    public boolean isPipeConnected(TileEntity var1)
    {
        return var1 instanceof TileGenericPipe || var1 instanceof ILiquidContainer || var1 instanceof IMachine && ((IMachine)var1).manageLiquids();
    }

    public static int[] getSplitVector(World var0)
    {
        if (lastSplit == var0.getWorldTime())
        {
            return splitVector;
        }
        else
        {
            lastSplit = var0.getWorldTime();
            splitVector = new int[6];
            int var1;

            for (var1 = 0; var1 < 6; splitVector[var1] = var1++)
            {
                ;
            }

            for (var1 = 0; var1 < 20; ++var1)
            {
                int var2 = var0.rand.nextInt(6);
                int var3 = var0.rand.nextInt(6);
                int var4 = splitVector[var2];
                splitVector[var2] = splitVector[var3];
                splitVector[var3] = var4;
            }

            return splitVector;
        }
    }

    public class LiquidBuffer
    {
        short[] in;
        short ready;
        short[] out;
        short qty;
        int orientation;
        short[] lastQty;
        int lastTotal;
        int emptyTime;
        @TileNetworkData(
                intKind = 1
        )
        public int average;
        @TileNetworkData
        public short liquidId;
        int totalBounced;
        boolean bouncing;
        private boolean[] filled;

        public LiquidBuffer(int var2)
        {
            this.in = new short[PipeTransportLiquids.this.travelDelay];
            this.out = new short[PipeTransportLiquids.this.travelDelay];
            this.lastQty = new short[100];
            this.lastTotal = 0;
            this.emptyTime = 0;
            this.liquidId = 0;
            this.totalBounced = 0;
            this.bouncing = false;
            this.orientation = var2;
            this.reset();
        }

        public void reset()
        {
            int var1;

            for (var1 = 0; var1 < PipeTransportLiquids.this.travelDelay; ++var1)
            {
                this.in[var1] = 0;
                this.out[var1] = 0;
            }

            for (var1 = 0; var1 < this.lastQty.length; ++var1)
            {
                this.lastQty[var1] = 0;
            }

            this.ready = 0;
            this.qty = 0;
            this.liquidId = 0;
            this.lastTotal = 0;
            this.totalBounced = 0;
            this.emptyTime = 0;
        }

        public int fill(int var1, boolean var2, short var3)
        {
            if (PipeTransportLiquids.this.worldObj == null)
            {
                return 0;
            }
            else if (this.qty > 0 && this.liquidId != var3 && this.liquidId != 0)
            {
                return 0;
            }
            else
            {
                if (this.liquidId != var3)
                {
                    this.reset();
                }

                this.liquidId = var3;
                int var4 = (int)(PipeTransportLiquids.this.worldObj.getWorldTime() % (long)PipeTransportLiquids.this.travelDelay);
                int var5 = var4 > 0 ? var4 - 1 : PipeTransportLiquids.this.travelDelay - 1;

                if (this.qty + var1 > PipeTransportLiquids.LIQUID_IN_PIPE + PipeTransportLiquids.this.flowRate)
                {
                    var1 = PipeTransportLiquids.LIQUID_IN_PIPE + PipeTransportLiquids.this.flowRate - this.qty;
                }

                if (var2)
                {
                    this.qty = (short)(this.qty + var1);
                    this.in[var5] = (short)(this.in[var5] + var1);
                }

                return var1;
            }
        }

        public int empty(int var1)
        {
            int var2 = (int)(PipeTransportLiquids.this.worldObj.getWorldTime() % (long)PipeTransportLiquids.this.travelDelay);
            int var3 = var2 > 0 ? var2 - 1 : PipeTransportLiquids.this.travelDelay - 1;

            if (this.ready - var1 < 0)
            {
                var1 = this.ready;
            }

            this.ready = (short)(this.ready - var1);
            this.out[var3] = (short)(this.out[var3] + var1);
            return var1;
        }

        public void update()
        {
            this.bouncing = false;
            int var1 = (int)(PipeTransportLiquids.this.worldObj.getWorldTime() % (long)PipeTransportLiquids.this.travelDelay);
            this.ready += this.in[var1];
            this.in[var1] = 0;
            int var2;

            if (this.out[var1] != 0)
            {
                var2 = 0;

                if (this.orientation < 6)
                {
                    if (PipeTransportLiquids.this.isInput[this.orientation])
                    {
                        var2 = PipeTransportLiquids.this.center.fill(this.out[var1], true, this.liquidId);
                    }

                    if (PipeTransportLiquids.this.isOutput[this.orientation])
                    {
                        Position var3 = new Position((double)PipeTransportLiquids.this.xCoord, (double)PipeTransportLiquids.this.yCoord, (double)PipeTransportLiquids.this.zCoord, Orientations.values()[this.orientation]);
                        var3.moveForwards(1.0D);
                        ILiquidContainer var4 = (ILiquidContainer)Utils.getTile(PipeTransportLiquids.this.worldObj, var3, Orientations.Unknown);
                        var2 = var4.fill(var3.orientation.reverse(), this.out[var1], this.liquidId, true);

                        if (var2 == 0)
                        {
                            ++this.totalBounced;

                            if (this.totalBounced > 20)
                            {
                                this.bouncing = true;
                            }

                            var2 += PipeTransportLiquids.this.center.fill(this.out[var1], true, this.liquidId);
                        }
                        else
                        {
                            this.totalBounced = 0;
                        }
                    }
                }
                else
                {
                    int var5 = 0;
                    int var6;

                    for (var6 = 0; var6 < 6; ++var6)
                    {
                        if (PipeTransportLiquids.this.isOutput[var6])
                        {
                            ++var5;
                        }
                    }

                    this.filled = new boolean[] {false, false, false, false, false, false};
                    var2 = this.splitLiquid(this.out[var1], var5);

                    if (var2 < this.out[var1])
                    {
                        var5 = 0;

                        for (var6 = 0; var6 < 6; ++var6)
                        {
                            if (PipeTransportLiquids.this.isOutput[var6] && !this.filled[var6])
                            {
                                ++var5;
                            }
                        }

                        var2 += this.splitLiquid(this.out[var1] - var2, var5);
                    }
                }

                this.qty = (short)(this.qty - var2);
                this.ready = (short)(this.ready + (this.out[var1] - var2));
                this.out[var1] = 0;
            }

            var2 = (int)(PipeTransportLiquids.this.worldObj.getWorldTime() % (long)this.lastQty.length);
            this.lastTotal += this.qty - this.lastQty[var2];
            this.lastQty[var2] = this.qty;
            this.average = this.lastTotal / this.lastQty.length;

            if (this.qty != 0 && this.average == 0)
            {
                this.average = 1;
            }
        }

        private int splitLiquid(int var1, int var2)
        {
            int var3 = 0;
            int var4 = (int)Math.ceil((double)var1 / (double)var2);
            int[] var5 = PipeTransportLiquids.getSplitVector(PipeTransportLiquids.this.worldObj);

            for (int var6 = 0; var6 < 6; ++var6)
            {
                int var7 = var4 <= var1 ? var4 : var1;
                int var8 = var5[var6];

                if (PipeTransportLiquids.this.isOutput[var8] && !this.filled[var8])
                {
                    var3 += PipeTransportLiquids.this.side[var8].fill(var7, true, this.liquidId);
                    var1 -= var7;

                    if (var3 != var7)
                    {
                        this.filled[var8] = true;
                    }
                }
            }

            return var3;
        }

        public void readFromNBT(NBTTagCompound var1)
        {
            for (int var2 = 0; var2 < PipeTransportLiquids.this.travelDelay; ++var2)
            {
                this.in[var2] = var1.getShort("in[" + var2 + "]");
                this.out[var2] = var1.getShort("out[" + var2 + "]");
            }

            this.ready = var1.getShort("ready");
            this.qty = var1.getShort("qty");
            this.liquidId = var1.getShort("liquidId");
        }

        public void writeToNBT(NBTTagCompound var1)
        {
            for (int var2 = 0; var2 < PipeTransportLiquids.this.travelDelay; ++var2)
            {
                var1.setShort("in[" + var2 + "]", this.in[var2]);
                var1.setShort("out[" + var2 + "]", this.out[var2]);
            }

            var1.setShort("ready", this.ready);
            var1.setShort("qty", this.qty);
            var1.setShort("liquidId", this.liquidId);
        }
    }
}
