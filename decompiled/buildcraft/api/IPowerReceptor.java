package buildcraft.api;

public interface IPowerReceptor
{
    void setPowerProvider(PowerProvider var1);

    PowerProvider getPowerProvider();

    void doWork();

    int powerRequest();
}
