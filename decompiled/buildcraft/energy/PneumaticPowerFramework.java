package buildcraft.energy;

import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;

public class PneumaticPowerFramework extends PowerFramework
{
    public PowerProvider createPowerProvider()
    {
        return new PneumaticPowerProvider();
    }
}
