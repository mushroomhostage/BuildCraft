package buildcraft.factory;

import buildcraft.api.API;
import buildcraft.api.APIProxy;
import buildcraft.api.IAreaProvider;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.LaserKind;
import buildcraft.api.Orientations;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.api.TileNetworkData;
import buildcraft.core.BlockContents;
import buildcraft.core.BluePrint;
import buildcraft.core.BluePrintBuilder;
import buildcraft.core.Box;
import buildcraft.core.DefaultAreaProvider;
import buildcraft.core.IMachine;
import buildcraft.core.StackUtil;
import buildcraft.core.Utils;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftBlockUtil;
import net.minecraft.server.BuildCraftFactory;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class TileQuarry extends TileMachine implements IArmListener, IMachine, IPowerReceptor
{
    BlockContents nextBlockForBluePrint = null;
    boolean isDigging = false;
    @TileNetworkData
    public Box box = new Box();
    @TileNetworkData
    public boolean inProcess = false;
    public EntityMechanicalArm arm;
    @TileNetworkData
    public int targetX;
    @TileNetworkData
    public int targetY;
    @TileNetworkData
    public int targetZ;
    @TileNetworkData
    public double headPosX;
    @TileNetworkData
    public double headPosY;
    @TileNetworkData
    public double headPosZ;
    @TileNetworkData
    public double speed = 0.03D;
    boolean loadArm = false;
    BluePrintBuilder bluePrintBuilder;
    @TileNetworkData
    public PowerProvider powerProvider;
    public static int MAX_ENERGY = 7000;
    private boolean loadDefaultBoundaries = false;

    public TileQuarry()
    {
        this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
        this.powerProvider.configure(20, 25, 25, 25, MAX_ENERGY);
    }

    public void createUtilsIfNeeded()
    {
        if (this.box.isInitialized() || !APIProxy.isClient(this.worldObj))
        {
            if (this.bluePrintBuilder == null)
            {
                if (!this.box.isInitialized())
                {
                    this.setBoundaries(this.loadDefaultBoundaries);
                }

                this.initializeBluePrintBuilder();
            }

            this.nextBlockForBluePrint = this.bluePrintBuilder.findNextBlock(this.worldObj);

            if (this.bluePrintBuilder.done)
            {
                this.box.deleteLasers();

                if (this.arm == null)
                {
                    this.createArm();
                }

                if (this.loadArm)
                {
                    this.arm.joinToWorld(this.worldObj);
                    this.loadArm = false;

                    if (this.findTarget(false))
                    {
                        this.isDigging = true;
                    }
                }
            }
            else
            {
                this.box.createLasers(this.worldObj, LaserKind.Stripes);
                this.isDigging = true;
            }
        }
    }

    private void createArm()
    {
        this.arm = new EntityMechanicalArm(this.worldObj, (double)((float)this.box.xMin + 0.75F), (double)((float)(this.yCoord + this.bluePrintBuilder.bluePrint.sizeY - 1) + 0.25F), (double)((float)this.box.zMin + 0.75F), (double)((float)(this.bluePrintBuilder.bluePrint.sizeX - 2) + 0.5F), (double)((float)(this.bluePrintBuilder.bluePrint.sizeZ - 2) + 0.5F));
        this.arm.listener = this;
        this.loadArm = true;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (this.inProcess && this.arm != null)
        {
            this.arm.speed = 0.0D;
            int var1 = 2 + this.powerProvider.energyStored / 1000;
            int var2 = this.powerProvider.useEnergy(var1, var1, true);

            if (var2 > 0)
            {
                this.arm.doMove(0.015D + (double)((float)var2 / 200.0F));
            }
        }

        if (this.arm != null)
        {
            this.headPosX = this.arm.headPosX;
            this.headPosY = this.arm.headPosY;
            this.headPosZ = this.arm.headPosZ;
            this.speed = this.arm.speed;
        }
    }

    public void doWork()
    {
        if (!APIProxy.isClient(this.worldObj))
        {
            if (!this.inProcess)
            {
                if (this.isDigging)
                {
                    this.createUtilsIfNeeded();

                    if (this.bluePrintBuilder != null)
                    {
                        if (this.bluePrintBuilder.done && this.nextBlockForBluePrint != null)
                        {
                            this.bluePrintBuilder.done = false;
                            this.box.createLasers(this.worldObj, LaserKind.Stripes);
                        }

                        if (!this.bluePrintBuilder.done)
                        {
                            this.powerProvider.configure(20, 25, 25, 25, MAX_ENERGY);

                            if (this.powerProvider.useEnergy(25, 25, true) == 25)
                            {
                                this.powerProvider.timeTracker.markTime(this.worldObj);
                                BlockContents var1 = this.bluePrintBuilder.findNextBlock(this.worldObj);
                                int var2 = this.worldObj.getBlockId(var1.x, var1.y, var1.z);

                                if (var1 != null)
                                {
                                    if (!API.softBlock(var2))
                                    {
                                        this.worldObj.setBlockWithNotify(var1.x, var1.y, var1.z, 0);
                                    }
                                    else if (var1.blockId != 0)
                                    {
                                        this.worldObj.setBlockWithNotify(var1.x, var1.y, var1.z, var1.blockId);
                                    }
                                }
                            }
                        }
                        else
                        {
                            this.powerProvider.configure(20, 30, 200, 50, MAX_ENERGY);

                            if (!this.findTarget(true))
                            {
                                this.arm.setTarget((double)this.box.xMin + this.arm.sizeX / 2.0D, (double)(this.yCoord + 2), (double)this.box.zMin + this.arm.sizeX / 2.0D);
                                this.isDigging = false;
                            }

                            this.inProcess = true;

                            if (APIProxy.isServerSide())
                            {
                                this.sendNetworkUpdate();
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean findTarget(boolean var1)
    {
        boolean[][] var2 = new boolean[this.bluePrintBuilder.bluePrint.sizeX - 2][this.bluePrintBuilder.bluePrint.sizeZ - 2];
        int var3;
        int var4;

        for (var3 = 0; var3 < this.bluePrintBuilder.bluePrint.sizeX - 2; ++var3)
        {
            for (var4 = 0; var4 < this.bluePrintBuilder.bluePrint.sizeZ - 2; ++var4)
            {
                var2[var3][var4] = false;
            }
        }

        for (var3 = this.yCoord + 3; var3 >= 0; --var3)
        {
            int var5;
            byte var6;

            if (var3 % 2 == 0)
            {
                var4 = 0;
                var5 = this.bluePrintBuilder.bluePrint.sizeX - 2;
                var6 = 1;
            }
            else
            {
                var4 = this.bluePrintBuilder.bluePrint.sizeX - 3;
                var5 = -1;
                var6 = -1;
            }

            for (int var7 = var4; var7 != var5; var7 += var6)
            {
                int var8;
                int var9;
                byte var10;

                if (var7 % 2 == var3 % 2)
                {
                    var8 = 0;
                    var9 = this.bluePrintBuilder.bluePrint.sizeZ - 2;
                    var10 = 1;
                }
                else
                {
                    var8 = this.bluePrintBuilder.bluePrint.sizeZ - 3;
                    var9 = -1;
                    var10 = -1;
                }

                for (int var11 = var8; var11 != var9; var11 += var10)
                {
                    if (!var2[var7][var11])
                    {
                        int var12 = this.box.xMin + var7 + 1;
                        int var14 = this.box.zMin + var11 + 1;
                        int var15 = this.worldObj.getBlockId(var12, var3, var14);

                        if (this.blockDig(var15))
                        {
                            var2[var7][var11] = true;
                        }
                        else if (this.canDig(var15))
                        {
                            if (var1)
                            {
                                this.arm.setTarget((double)var12, (double)(var3 + 1), (double)var14);
                                this.targetX = (int)this.arm.targetX;
                                this.targetY = (int)this.arm.targetY;
                                this.targetZ = (int)this.arm.targetZ;
                            }

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        PowerFramework.currentFramework.loadPowerProvider(this, var1);

        if (var1.hasKey("box"))
        {
            this.box.initialize(var1.getCompoundTag("box"));
            this.loadDefaultBoundaries = false;
        }
        else if (var1.hasKey("xSize"))
        {
            int var2 = var1.getInteger("xMin");
            int var3 = var1.getInteger("zMin");
            int var4 = var1.getInteger("xSize");
            int var5 = var1.getInteger("ySize");
            int var6 = var1.getInteger("zSize");
            this.box.initialize(var2, this.yCoord, var3, var2 + var4 - 1, this.yCoord + var5 - 1, var3 + var6 - 1);
            this.loadDefaultBoundaries = false;
        }
        else
        {
            this.loadDefaultBoundaries = true;
        }

        this.targetX = var1.getInteger("targetX");
        this.targetY = var1.getInteger("targetY");
        this.targetZ = var1.getInteger("targetZ");

        if (var1.getBoolean("hasArm"))
        {
            NBTTagCompound var7 = var1.getCompoundTag("arm");
            this.arm = new EntityMechanicalArm(this.worldObj);
            this.arm.readFromNBT(var7);
            this.arm.listener = this;
            this.loadArm = true;
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        PowerFramework.currentFramework.savePowerProvider(this, var1);
        var1.setInteger("targetX", this.targetX);
        var1.setInteger("targetY", this.targetY);
        var1.setInteger("targetZ", this.targetZ);
        var1.setBoolean("hasArm", this.arm != null);
        NBTTagCompound var2;

        if (this.arm != null)
        {
            var2 = new NBTTagCompound();
            var1.setTag("arm", var2);
            this.arm.writeToNBT(var2);
        }

        var2 = new NBTTagCompound();
        this.box.writeToNBT(var2);
        var1.setTag("box", var2);
    }

    public void positionReached(EntityMechanicalArm var1)
    {
        this.inProcess = false;

        if (!APIProxy.isClient(this.worldObj))
        {
            int var2 = this.targetX;
            int var3 = this.targetY - 1;
            int var4 = this.targetZ;
            int var5 = this.worldObj.getBlockId(var2, var3, var4);

            if (this.canDig(var5))
            {
                this.powerProvider.timeTracker.markTime(this.worldObj);
                ItemStack var6 = BuildCraftBlockUtil.getItemStackFromBlock(this.worldObj, var2, var3, var4);

                if (var6 != null)
                {
                    boolean var7 = false;
                    StackUtil var8 = new StackUtil(var6);
                    var7 = var8.addToRandomInventory(this, Orientations.Unknown);

                    if (!var7 || var8.items.stackSize > 0)
                    {
                        var7 = Utils.addToRandomPipeEntry(this, Orientations.Unknown, var8.items);
                    }

                    if (!var7)
                    {
                        float var9 = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;
                        float var10 = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;
                        float var11 = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;
                        EntityItem var12 = new EntityItem(this.worldObj, (double)((float)this.xCoord + var9), (double)((float)this.yCoord + var10 + 0.5F), (double)((float)this.zCoord + var11), var8.items);
                        float var13 = 0.05F;
                        var12.motionX = (double)((float)this.worldObj.rand.nextGaussian() * var13);
                        var12.motionY = (double)((float)this.worldObj.rand.nextGaussian() * var13 + 1.0F);
                        var12.motionZ = (double)((float)this.worldObj.rand.nextGaussian() * var13);
                        this.worldObj.spawnEntityInWorld(var12);
                    }
                }

                this.worldObj.setBlockWithNotify(var2, var3, var4, 0);
            }
        }
    }

    private boolean blockDig(int var1)
    {
        return var1 == Block.bedrock.blockID || var1 == Block.lavaStill.blockID || var1 == Block.lavaMoving.blockID;
    }

    private boolean canDig(int var1)
    {
        return !this.blockDig(var1) && !API.softBlock(var1) && var1 != Block.snow.blockID;
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
        if (this.arm != null)
        {
            this.arm.setEntityDead();
        }

        this.box.deleteLasers();
        this.arm = null;
    }

    public boolean isActive()
    {
        return this.isDigging;
    }

    private void setBoundaries(boolean var1)
    {
        Object var2 = null;

        if (!var1)
        {
            var2 = Utils.getNearbyAreaProvider(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }

        if (var2 == null)
        {
            var2 = new DefaultAreaProvider(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 10, this.yCoord + 4, this.zCoord + 10);
            var1 = true;
        }

        int var3 = ((IAreaProvider)var2).xMax() - ((IAreaProvider)var2).xMin() + 1;
        int var4 = ((IAreaProvider)var2).yMax() - ((IAreaProvider)var2).yMin() + 1;
        int var5 = ((IAreaProvider)var2).zMax() - ((IAreaProvider)var2).zMin() + 1;

        if (var3 < 3 || var5 < 3)
        {
            var2 = new DefaultAreaProvider(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 10, this.yCoord + 4, this.zCoord + 10);
            var1 = true;
        }

        var3 = ((IAreaProvider)var2).xMax() - ((IAreaProvider)var2).xMin() + 1;
        var4 = ((IAreaProvider)var2).yMax() - ((IAreaProvider)var2).yMin() + 1;
        var5 = ((IAreaProvider)var2).zMax() - ((IAreaProvider)var2).zMin() + 1;
        this.box.initialize((IAreaProvider)var2);

        if (var4 < 5)
        {
            var4 = 5;
            this.box.yMax = this.box.yMin + var4 - 1;
        }

        if (var1)
        {
            int var6 = 0;
            int var7 = 0;
            Orientations var8 = Orientations.values()[this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)].reverse();

            switch (TileQuarry.NamelessClass30844472.$SwitchMap$net.minecraft.server$buildcraft$api$Orientations[var8.ordinal()])
            {
                case 1:
                    var6 = this.xCoord + 1;
                    var7 = this.zCoord - 4 - 1;
                    break;

                case 2:
                    var6 = this.xCoord - 9 - 2;
                    var7 = this.zCoord - 4 - 1;
                    break;

                case 3:
                    var6 = this.xCoord - 4 - 1;
                    var7 = this.zCoord + 1;
                    break;

                case 4:
                    var6 = this.xCoord - 4 - 1;
                    var7 = this.zCoord - 9 - 2;
            }

            this.box.initialize(var6, this.yCoord, var7, var6 + var3 - 1, this.yCoord + var4 - 1, var7 + var5 - 1);
        }

        ((IAreaProvider)var2).removeFromWorld();
    }

    private void initializeBluePrintBuilder()
    {
        BluePrint var1 = new BluePrint(this.box.sizeX(), this.box.sizeY(), this.box.sizeZ());
        int var2;
        int var3;

        for (var2 = 0; var2 < var1.sizeX; ++var2)
        {
            for (var3 = 0; var3 < var1.sizeY; ++var3)
            {
                for (int var4 = 0; var4 < var1.sizeZ; ++var4)
                {
                    var1.setBlockId(var2, var3, var4, 0);
                }
            }
        }

        for (var2 = 0; var2 < 2; ++var2)
        {
            for (var3 = 0; var3 < var1.sizeX; ++var3)
            {
                var1.setBlockId(var3, var2 * (this.box.sizeY() - 1), 0, BuildCraftFactory.frameBlock.blockID);
                var1.setBlockId(var3, var2 * (this.box.sizeY() - 1), var1.sizeZ - 1, BuildCraftFactory.frameBlock.blockID);
            }

            for (var3 = 0; var3 < var1.sizeZ; ++var3)
            {
                var1.setBlockId(0, var2 * (this.box.sizeY() - 1), var3, BuildCraftFactory.frameBlock.blockID);
                var1.setBlockId(var1.sizeX - 1, var2 * (this.box.sizeY() - 1), var3, BuildCraftFactory.frameBlock.blockID);
            }
        }

        for (var2 = 1; var2 < this.box.sizeY(); ++var2)
        {
            var1.setBlockId(0, var2, 0, BuildCraftFactory.frameBlock.blockID);
            var1.setBlockId(0, var2, var1.sizeZ - 1, BuildCraftFactory.frameBlock.blockID);
            var1.setBlockId(var1.sizeX - 1, var2, 0, BuildCraftFactory.frameBlock.blockID);
            var1.setBlockId(var1.sizeX - 1, var2, var1.sizeZ - 1, BuildCraftFactory.frameBlock.blockID);
        }

        this.bluePrintBuilder = new BluePrintBuilder(var1, this.box.xMin, this.yCoord, this.box.zMin);
    }

    public void postPacketHandling(PacketUpdate var1)
    {
        super.postPacketHandling(var1);
        this.createUtilsIfNeeded();

        if (this.arm != null)
        {
            this.arm.setHeadPosition(this.headPosX, this.headPosY, this.headPosZ);
            this.arm.setTarget((double)this.targetX, (double)this.targetY, (double)this.targetZ);
            this.arm.speed = this.speed;
        }
    }

    public void initialize()
    {
        super.initialize();

        if (!APIProxy.isClient(this.worldObj))
        {
            this.createUtilsIfNeeded();
        }

        this.sendNetworkUpdate();
    }

    public void setPowerProvider(PowerProvider var1)
    {
        var1 = this.powerProvider;
    }

    public PowerProvider getPowerProvider()
    {
        return this.powerProvider;
    }

    public boolean manageLiquids()
    {
        return false;
    }

    public boolean manageSolids()
    {
        return true;
    }

}
