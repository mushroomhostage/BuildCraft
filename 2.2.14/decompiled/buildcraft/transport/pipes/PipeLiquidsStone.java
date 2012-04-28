package buildcraft.transport.pipes;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicStone;
import buildcraft.transport.PipeTransportLiquids;

public class PipeLiquidsStone extends Pipe
{
    public PipeLiquidsStone(int var1)
    {
        super(new PipeTransportLiquids(), new PipeLogicStone(), var1);
    }

    public int getBlockTexture()
    {
        return 114;
    }
}
