package buildcraft.api;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;

public interface IPipeEntry
{
    void entityEntering(EntityPassiveItem var1, Orientations var2);

    boolean acceptItems();
}
