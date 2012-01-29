package buildcraft.transport.pipes;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicStone;
import buildcraft.transport.PipeTransportItems;
import java.util.LinkedList;

public class PipeItemsStone extends Pipe implements IPipeTransportItemsHook
{

    public PipeItemsStone(int var1)
    {
        super(new PipeTransportItems(), new PipeLogicStone(), var1);
    }

    public int getBlockTexture()
    {
        return 29;
    }

    public void readjustSpeed(EntityPassiveItem var1)
    {
        if (var1.speed > Utils.pipeNormalSpeed)
        {
            var1.speed -= Utils.pipeNormalSpeed / 2.0F;
        }

        if (var1.speed < Utils.pipeNormalSpeed)
        {
            var1.speed = Utils.pipeNormalSpeed;
        }

    }

    public LinkedList filterPossibleMovements(LinkedList var1, Position var2, EntityPassiveItem var3)
    {
        return var1;
    }

    public void entityEntered(EntityPassiveItem var1, Orientations var2) {}
}
