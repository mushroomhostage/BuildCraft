package buildcraft.api;

import buildcraft.api.PowerProvider;

public interface IPowerReceptor {

   void setPowerProvider(PowerProvider var1);

   PowerProvider getPowerProvider();

   void doWork();

   int powerRequest();
}
