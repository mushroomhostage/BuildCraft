package buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import java.util.LinkedList;

public interface IPipeTransportItemsHook
{

    LinkedList filterPossibleMovements(LinkedList var1, Position var2, EntityPassiveItem var3);

    void entityEntered(EntityPassiveItem var1, Orientations var2);

    void readjustSpeed(EntityPassiveItem var1);
}
