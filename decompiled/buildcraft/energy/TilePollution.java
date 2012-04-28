package buildcraft.energy;

import buildcraft.api.SafeTimeTracker;
import buildcraft.core.BlockIndex;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.TileEntity;

public class TilePollution extends TileEntity
{
    public boolean init = false;
    public SafeTimeTracker timeTracker = new SafeTimeTracker();
    public int saturation = 0;

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (!this.init)
        {
            this.init = true;
            this.timeTracker.markTime(this.worldObj);
            BlockIndex var1 = new BlockIndex(this.xCoord, this.yCoord, this.zCoord);

            if (BuildCraftEnergy.saturationStored.containsKey(var1))
            {
                this.saturation = ((Integer)BuildCraftEnergy.saturationStored.remove(var1)).intValue();
            }
            else
            {
                this.saturation = 1;
            }
        }
        else if (this.timeTracker.markTimeIfDelay(this.worldObj, 20L))
        {
            ;
        }
    }
}
