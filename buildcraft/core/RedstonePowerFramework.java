package buildcraft.core;

import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;

public class RedstonePowerFramework extends PowerFramework
{
    public PowerProvider createPowerProvider()
    {
        return new RedstonePowerProvider();
    }
}
