package buildcraft.transport.pipes;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicCobblestone;
import buildcraft.transport.PipeTransportItems;

public class PipeItemsCobblestone extends Pipe
{

    public PipeItemsCobblestone(int var1)
    {
        super(new PipeTransportItems(), new PipeLogicCobblestone(), var1);
    }

    public int getBlockTexture()
    {
        return 17;
    }
}
