package buildcraft.api;

import net.minecraft.server.World;

public class SafeTimeTracker
{

    private long lastMark = 0L;


    public boolean markTimeIfDelay(World var1, long var2)
    {
        if (var1 == null)
        {
            return false;
        }
        else
        {
            long var4 = var1.getTime();
            if (var4 < this.lastMark)
            {
                this.lastMark = var4;
                return false;
            }
            else if (this.lastMark + var2 <= var4)
            {
                this.lastMark = var1.getTime();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public void markTime(World var1)
    {
        this.lastMark = var1.getTime();
    }
}
