package buildcraft.core;


public class BlockContents
{

    public int blockId;
    public int x;
    public int y;
    public int z;


    public BlockContents clone()
    {
        BlockContents var1 = new BlockContents();
        var1.x = this.x;
        var1.y = this.y;
        var1.z = this.z;
        var1.blockId = this.blockId;
        return var1;
    }
}
