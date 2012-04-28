package buildcraft.energy;

import buildcraft.api.API;
import buildcraft.api.IronEngineFuel;
import buildcraft.api.Orientations;
import net.minecraft.server.Block;
import net.minecraft.server.ICrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class EngineIron extends Engine
{
    public static int MAX_LIQUID = 10000;
    public static int MAX_HEAT = 100000;
    public static int COOLANT_THRESHOLD = 49000;
    int burnTime = 0;
    int liquidQty = 0;
    int liquidId = 0;
    int coolantQty = 0;
    int coolantId = 0;
    int heat = 0;

    public EngineIron(TileEngine var1)
    {
        super(var1);
        this.maxEnergy = 100000;
        this.maxEnergyExtracted = 500;
    }

    public String getTextureFile()
    {
        return "/net.minecraft.server/buildcraft/energy/gui/base_iron.png";
    }

    public int explosionRange()
    {
        return 8;
    }

    public int maxEnergyReceived()
    {
        return 2000;
    }

    public float getPistonSpeed()
    {
        switch (EngineIron.NamelessClass1963762410.$SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[this.getEnergyStage().ordinal()])
        {
            case 1:
                return 0.04F;

            case 2:
                return 0.05F;

            case 3:
                return 0.06F;

            case 4:
                return 0.07F;

            default:
                return 0.0F;
        }
    }

    public boolean isBurning()
    {
        return this.liquidQty > 0 && this.tile.worldObj.isBlockIndirectlyGettingPowered(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord);
    }

    public void burn()
    {
        IronEngineFuel var1 = (IronEngineFuel)API.ironEngineFuel.get(Integer.valueOf(this.liquidId));

        if (var1 != null)
        {
            if (this.tile.worldObj.isBlockIndirectlyGettingPowered(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord) && (this.burnTime > 0 || this.liquidQty > 0))
            {
                if (this.burnTime > 0)
                {
                    --this.burnTime;
                }
                else
                {
                    --this.liquidQty;
                    this.burnTime = var1.totalBurningTime / 1000;
                }

                this.addEnergy(var1.powerPerCycle);
                this.heat += var1.powerPerCycle;
            }
        }
    }

    public void update()
    {
        super.update();
        ItemStack var1 = this.tile.getStackInSlot(0);
        int var2;

        if (var1 != null)
        {
            var2 = API.getLiquidForBucket(var1.itemID);

            if (var2 != 0 && this.fill(Orientations.Unknown, 1000, var2, false) == 1000)
            {
                this.fill(Orientations.Unknown, 1000, var2, true);
                this.tile.setInventorySlotContents(0, new ItemStack(Item.bucketEmpty, 1));
            }
        }

        if (this.heat > COOLANT_THRESHOLD)
        {
            var2 = this.heat - COOLANT_THRESHOLD;

            if (this.coolantQty > var2)
            {
                this.coolantQty -= var2;
                this.heat = COOLANT_THRESHOLD;
            }
            else
            {
                this.heat -= this.coolantQty;
                this.coolantQty = 0;
            }
        }

        if (this.heat > 0 && !this.tile.worldObj.isBlockIndirectlyGettingPowered(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord))
        {
            if (this.heat >= 10)
            {
                this.heat -= 10;
            }
            else
            {
                --this.heat;
            }
        }
    }

    public void computeEnergyStage()
    {
        if (this.heat <= MAX_HEAT / 4)
        {
            this.energyStage = Engine.EnergyStage.Blue;
        }
        else if (this.heat <= MAX_HEAT / 2)
        {
            this.energyStage = Engine.EnergyStage.Green;
        }
        else if ((float)this.heat <= (float)MAX_HEAT * 3.0F / 4.0F)
        {
            this.energyStage = Engine.EnergyStage.Yellow;
        }
        else if (this.heat <= MAX_HEAT)
        {
            this.energyStage = Engine.EnergyStage.Red;
        }
        else
        {
            this.energyStage = Engine.EnergyStage.Explosion;
        }
    }

    public int getScaledBurnTime(int var1)
    {
        return (int)((float)this.liquidQty / (float)MAX_LIQUID * (float)var1);
    }

    public int fill(Orientations var1, int var2, int var3, boolean var4)
    {
        if (var3 == Block.waterStill.blockID)
        {
            return this.fillCoolant(var1, var2, var3, var4);
        }
        else
        {
            boolean var5 = false;

            if (this.liquidQty > 0 && this.liquidId != var3)
            {
                return 0;
            }
            else if (!API.ironEngineFuel.containsKey(Integer.valueOf(var3)))
            {
                return 0;
            }
            else
            {
                int var6;

                if (this.liquidQty + var2 <= MAX_LIQUID)
                {
                    if (var4)
                    {
                        this.liquidQty += var2;
                    }

                    var6 = var2;
                }
                else
                {
                    var6 = MAX_LIQUID - this.liquidQty;

                    if (var4)
                    {
                        this.liquidQty = MAX_LIQUID;
                    }
                }

                this.liquidId = var3;
                return var6;
            }
        }
    }

    private int fillCoolant(Orientations var1, int var2, int var3, boolean var4)
    {
        boolean var5 = false;

        if (this.coolantQty > 0 && this.coolantId != var3)
        {
            return 0;
        }
        else
        {
            int var6;

            if (this.coolantQty + var2 <= MAX_LIQUID)
            {
                if (var4)
                {
                    this.coolantQty += var2;
                }

                var6 = var2;
            }
            else
            {
                var6 = MAX_LIQUID - this.coolantQty;

                if (var4)
                {
                    this.coolantQty = MAX_LIQUID;
                }
            }

            this.coolantId = var3;
            return var6;
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        this.liquidId = var1.getInteger("liquidId");
        this.liquidQty = var1.getInteger("liquidQty");
        this.burnTime = var1.getInteger("burnTime");
        this.coolantId = var1.getInteger("coolantId");
        this.coolantQty = var1.getInteger("coolantQty");
        this.heat = var1.getInteger("heat");
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        var1.setInteger("liquidId", this.liquidId);
        var1.setInteger("liquidQty", this.liquidQty);
        var1.setInteger("burnTime", this.burnTime);
        var1.setInteger("coolantId", this.coolantId);
        var1.setInteger("coolantQty", this.coolantQty);
        var1.setInteger("heat", this.heat);
    }

    public int getScaledCoolant(int var1)
    {
        return (int)((float)this.coolantQty / (float)MAX_LIQUID * (float)var1);
    }

    public void delete() {}

    public void getGUINetworkData(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.liquidQty = var2;
                break;

            case 1:
                this.liquidId = var2;
                break;

            case 2:
                this.coolantQty = var2;
                break;

            case 3:
                this.coolantId = var2;
        }
    }

    public void sendGUINetworkData(ContainerEngine var1, ICrafting var2)
    {
        var2.updateCraftingInventoryInfo(var1, 0, this.liquidQty);
        var2.updateCraftingInventoryInfo(var1, 1, this.liquidId);
        var2.updateCraftingInventoryInfo(var1, 2, this.coolantQty);
        var2.updateCraftingInventoryInfo(var1, 3, this.coolantId);
    }

    static class NamelessClass1963762410
    {
        static final int[] $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage = new int[Engine.EnergyStage.values().length];

        static
        {
            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Blue.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Green.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Yellow.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Red.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
