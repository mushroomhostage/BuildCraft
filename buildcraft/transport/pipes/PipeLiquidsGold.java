package buildcraft.transport.pipes;

import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicGold;
import buildcraft.transport.PipeTransportLiquids;

public class PipeLiquidsGold extends Pipe
{
    public PipeLiquidsGold(int var1)
    {
        super(new PipeTransportLiquids(), new PipeLogicGold(), var1);
        ((PipeTransportLiquids)this.transport).flowRate = 80;
        ((PipeTransportLiquids)this.transport).travelDelay = 2;
    }

    public int getBlockTexture()
    {
        return 116;
    }
}
