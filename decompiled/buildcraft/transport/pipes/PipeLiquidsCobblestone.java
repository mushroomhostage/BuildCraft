package buildcraft.transport.pipes;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicCobblestone;
import buildcraft.transport.PipeTransportLiquids;

public class PipeLiquidsCobblestone extends Pipe
{
    public PipeLiquidsCobblestone(int var1)
    {
        super(new PipeTransportLiquids(), new PipeLogicCobblestone(), var1);
    }

    public int getBlockTexture()
    {
        return 113;
    }
}
