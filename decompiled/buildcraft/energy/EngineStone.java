package buildcraft.energy;

import buildcraft.core.CoreProxy;
import buildcraft.core.Utils;
import net.minecraft.server.Block;
import net.minecraft.server.ICrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;

public class EngineStone extends Engine
{
    int burnTime = 0;
    int totalBurnTime = 0;

    public EngineStone(TileEngine var1)
    {
        super(var1);
        this.maxEnergy = 10000;
        this.maxEnergyExtracted = 100;
    }

    public String getTextureFile()
    {
        return "/net.minecraft.server/buildcraft/energy/gui/base_stone.png";
    }

    public int explosionRange()
    {
        return 4;
    }

    public int maxEnergyReceived()
    {
        return 200;
    }

    public float getPistonSpeed()
    {
        switch (EngineStone.NamelessClass1890875550.$SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[this.getEnergyStage().ordinal()])
        {
            case 1:
                return 0.02F;

            case 2:
                return 0.04F;

            case 3:
                return 0.08F;

            case 4:
                return 0.16F;

            default:
                return 0.0F;
        }
    }

    public boolean isBurning()
    {
        return this.burnTime > 0;
    }

    public void burn()
    {
        if (this.burnTime > 0)
        {
            --this.burnTime;
            this.addEnergy(1);
        }

        if (this.burnTime == 0 && this.tile.worldObj.isBlockIndirectlyGettingPowered(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord))
        {
            this.burnTime = this.totalBurnTime = this.getItemBurnTime(this.tile.getStackInSlot(0));

            if (this.burnTime > 0)
            {
                ItemStack var1 = this.tile.decrStackSize(1, 1);

                if (var1.getItem().getContainerItem() != null)
                {
                    this.tile.setInventorySlotContents(1, new ItemStack(var1.getItem().getContainerItem(), 1));
                }
            }
        }
    }

    public int getScaledBurnTime(int var1)
    {
        return (int)((float)this.burnTime / (float)this.totalBurnTime * (float)var1);
    }

    private int getItemBurnTime(ItemStack var1)
    {
        if (var1 == null)
        {
            return 0;
        }
        else
        {
            int var2 = var1.getItem().shiftedIndex;
            return var2 < Block.blocksList.length && Block.blocksList[var2] != null && Block.blocksList[var2].blockMaterial == Material.wood ? 300 : (var2 == Item.stick.shiftedIndex ? 100 : (var2 == Item.coal.shiftedIndex ? 1600 : (var2 == Item.bucketLava.shiftedIndex ? 20000 : (var2 == Block.sapling.blockID ? 100 : CoreProxy.addFuel(var2, var1.getItemDamage())))));
        }
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        this.burnTime = var1.getInteger("burnTime");
        this.totalBurnTime = var1.getInteger("totalBurnTime");
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        var1.setInteger("burnTime", this.burnTime);
        var1.setInteger("totalBurnTime", this.totalBurnTime);
    }

    public void delete()
    {
        ItemStack var1 = this.tile.getStackInSlot(0);

        if (var1 != null)
        {
            Utils.dropItems(this.tile.worldObj, var1, this.tile.xCoord, this.tile.yCoord, this.tile.zCoord);
        }
    }

    public void getGUINetworkData(int var1, int var2)
    {
        if (var1 == 0)
        {
            this.burnTime = var2;
        }
        else if (var1 == 1)
        {
            this.totalBurnTime = var2;
        }
    }

    public void sendGUINetworkData(ContainerEngine var1, ICrafting var2)
    {
        var2.updateCraftingInventoryInfo(var1, 0, this.burnTime);
        var2.updateCraftingInventoryInfo(var1, 1, this.totalBurnTime);
    }

}
