package buildcraft.transport.pipes;

import buildcraft.api.Orientations;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicIron;
import buildcraft.transport.PipeTransportLiquids;

public class PipeLiquidsIron extends Pipe
{
    private int baseTexture = 115;
    private int plainTexture = 19;
    private int nextTexture;

    public PipeLiquidsIron(int var1)
    {
        super(new PipeTransportLiquids(), new PipeLogicIron(), var1);
        this.nextTexture = this.baseTexture;
    }

    public void prepareTextureFor(Orientations var1)
    {
        if (var1 == Orientations.Unknown)
        {
            this.nextTexture = this.baseTexture;
        }
        else
        {
            int var2 = this.worldObj.getData(this.xCoord, this.yCoord, this.zCoord);

            if (var2 == var1.ordinal())
            {
                this.nextTexture = this.baseTexture;
            }
            else
            {
                this.nextTexture = this.plainTexture;
            }
        }
    }

    public int getBlockTexture()
    {
        return this.nextTexture;
    }
}
