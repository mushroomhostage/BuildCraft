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

    public void l_()
    {
        if (!this.init)
        {
            this.init = true;
            this.timeTracker.markTime(this.world);
            BlockIndex var1 = new BlockIndex(this.x, this.y, this.z);
            if (BuildCraftEnergy.saturationStored.containsKey(var1))
            {
                this.saturation = ((Integer)BuildCraftEnergy.saturationStored.remove(var1)).intValue();
            }
            else
            {
                this.saturation = 1;
            }
        }
        else if (this.timeTracker.markTimeIfDelay(this.world, 20L))
        {
            ;
        }
    }
}
