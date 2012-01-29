package buildcraft.core;

import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.core.RedstonePowerProvider;

public class RedstonePowerFramework extends PowerFramework
{
    public PowerProvider createPowerProvider()
    {
        return new RedstonePowerProvider();
    }
}
