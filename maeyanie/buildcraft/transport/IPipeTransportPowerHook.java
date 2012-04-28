package buildcraft.transport;

import buildcraft.api.Orientations;

public interface IPipeTransportPowerHook
{

    void receiveEnergy(Orientations var1, double var2);

    void requestEnergy(Orientations var1, int var2);
}
