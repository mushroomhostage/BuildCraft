package buildcraft.energy;

import buildcraft.api.PowerProvider;

public class PneumaticPowerProvider extends PowerProvider
{

    public void configure(int var1, int var2, int var3, int var4, int var5)
    {
        super.configure(var1, var2, var3, var4, var5);
        this.latency = 0;
    }
}
