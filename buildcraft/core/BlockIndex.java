package buildcraft.core;


public class BlockIndex implements Comparable
{

    public int i;
    public int j;
    public int k;


    public BlockIndex(int var1, int var2, int var3)
    {
        this.i = var1;
        this.j = var2;
        this.k = var3;
    }

    public int compareTo(BlockIndex var1)
    {
        return var1.i < this.i ? 1 : (var1.i > this.i ? -1 : (var1.k < this.k ? 1 : (var1.k > this.k ? -1 : (var1.j < this.j ? 1 : (var1.j > this.j ? -1 : 0)))));
    }

    // $FF: synthetic method
    // $FF: bridge method
    public int compareTo(Object var1)
    {
        return this.compareTo((BlockIndex)var1);
    }
}
