package buildcraft.api;

public interface IPipeEntry
{
    void entityEntering(EntityPassiveItem var1, Orientations var2);

    boolean acceptItems();
}
