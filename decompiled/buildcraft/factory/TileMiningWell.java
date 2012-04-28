package buildcraft.factory;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.core.IMachine;
import buildcraft.core.StackUtil;
import buildcraft.core.Utils;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftBlockUtil;
import net.minecraft.server.BuildCraftFactory;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class TileMiningWell extends TileMachine implements IMachine, IPowerReceptor
{
    boolean isDigging = true;
    PowerProvider powerProvider;

    public TileMiningWell()
    {
        this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
        this.powerProvider.configure(50, 25, 25, 25, 1000);
    }

    public void doWork()
    {
        if (this.powerProvider.useEnergy(25, 25, true) >= 25)
        {
            World var1 = this.worldObj;
            int var2;

            for (var2 = this.yCoord - 1; var1.getBlockId(this.xCoord, var2, this.zCoord) == BuildCraftFactory.plainPipeBlock.blockID; --var2)
            {
                ;
            }

            if (var2 >= 0 && var1.getBlockId(this.xCoord, var2, this.zCoord) != Block.bedrock.blockID && var1.getBlockId(this.xCoord, var2, this.zCoord) != Block.lavaMoving.blockID && var1.getBlockId(this.xCoord, var2, this.zCoord) != Block.lavaStill.blockID)
            {
                int var3 = var1.getBlockId(this.xCoord, var2, this.zCoord);
                ItemStack var4 = BuildCraftBlockUtil.getItemStackFromBlock(var1, this.xCoord, var2, this.zCoord);
                var1.setBlockWithNotify(this.xCoord, var2, this.zCoord, BuildCraftFactory.plainPipeBlock.blockID);

                if (var3 != 0)
                {
                    if (var4 != null)
                    {
                        StackUtil var5 = new StackUtil(var4);

                        if (!var5.addToRandomInventory(this, Orientations.Unknown) || var5.items.stackSize != 0)
                        {
                            if (!Utils.addToRandomPipeEntry(this, Orientations.Unknown, var4) || var5.items.stackSize != 0)
                            {
                                float var6 = var1.rand.nextFloat() * 0.8F + 0.1F;
                                float var7 = var1.rand.nextFloat() * 0.8F + 0.1F;
                                float var8 = var1.rand.nextFloat() * 0.8F + 0.1F;
                                EntityItem var9 = new EntityItem(var1, (double)((float)this.xCoord + var6), (double)((float)this.yCoord + var7 + 0.5F), (double)((float)this.zCoord + var8), var5.items);
                                float var10 = 0.05F;
                                var9.motionX = (double)((float)var1.rand.nextGaussian() * var10);
                                var9.motionY = (double)((float)var1.rand.nextGaussian() * var10 + 1.0F);
                                var9.motionZ = (double)((float)var1.rand.nextGaussian() * var10);
                                var1.spawnEntityInWorld(var9);
                            }
                        }
                    }
                }
            }
            else
            {
                this.isDigging = false;
            }
        }
    }

    public boolean isActive()
    {
        return this.isDigging;
    }

    public void setPowerProvider(PowerProvider var1)
    {
        this.powerProvider = var1;
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
