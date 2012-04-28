package buildcraft.factory;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.core.FakePlayer;
import buildcraft.core.IMachine;
import buildcraft.core.StackUtil;
import buildcraft.core.Utils;
import buildcraft.factory.TileMachine;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftBlockUtil;
import net.minecraft.server.BuildCraftFactory;
import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import org.bukkit.event.block.BlockBreakEvent;

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
            World var1 = this.world;

            int var2;
            for (var2 = this.y - 1; var1.getTypeId(this.x, var2, this.z) == BuildCraftFactory.plainPipeBlock.id; --var2)
            {
                ;
            }

            if (var2 >= 0 && var1.getTypeId(this.x, var2, this.z) != Block.BEDROCK.id && var1.getTypeId(this.x, var2, this.z) != Block.LAVA.id && var1.getTypeId(this.x, var2, this.z) != Block.STATIONARY_LAVA.id)
            {
                int var3 = var1.getTypeId(this.x, var2, this.z);
                if (var3 != 0)
                {
                    org.bukkit.block.Block var4 = this.world.getWorld().getBlockAt(this.x, var2, this.z);
                    BlockBreakEvent var5 = new BlockBreakEvent(var4, FakePlayer.getBukkitEntity(this.world));
                    this.world.getServer().getPluginManager().callEvent(var5);
                    if (var5.isCancelled())
                    {
                        return;
                    }
                }

                ItemStack var11 = BuildCraftBlockUtil.getItemStackFromBlock(var1, this.x, var2, this.z);
                var1.setTypeId(this.x, var2, this.z, BuildCraftFactory.plainPipeBlock.id);
                if (var3 != 0 && var11 != null)
                {
                    StackUtil var12 = new StackUtil(var11);
                    if ((!var12.addToRandomInventory(this, Orientations.Unknown) || var12.items.count != 0) && (!Utils.addToRandomPipeEntry(this, Orientations.Unknown, var11) || var12.items.count != 0))
                    {
                        float var6 = var1.random.nextFloat() * 0.8F + 0.1F;
                        float var7 = var1.random.nextFloat() * 0.8F + 0.1F;
                        float var8 = var1.random.nextFloat() * 0.8F + 0.1F;
                        EntityItem var9 = new EntityItem(var1, (double)((float)this.x + var6), (double)((float)this.y + var7 + 0.5F), (double)((float)this.z + var8), var12.items);
                        float var10 = 0.05F;
                        var9.motX = (double)((float)var1.random.nextGaussian() * var10);
                        var9.motY = (double)((float)var1.random.nextGaussian() * var10 + 1.0F);
                        var9.motZ = (double)((float)var1.random.nextGaussian() * var10);
                        var1.addEntity(var9);
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
