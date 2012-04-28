package buildcraft.factory;

import buildcraft.api.IPowerReceptor;
import buildcraft.core.IMachine;
import buildcraft.core.TileBuildCraft;

public abstract class TileMachine extends TileBuildCraft implements IMachine, IPowerReceptor
{

    public int powerRequest()
    {
        return this.isActive() ? this.getPowerProvider().maxEnergyReceived : 0;
    }
}
