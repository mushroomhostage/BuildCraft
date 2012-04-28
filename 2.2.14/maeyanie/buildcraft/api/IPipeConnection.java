package buildcraft.api;

import net.minecraft.server.IBlockAccess;

public interface IPipeConnection
{

    boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7);
}
