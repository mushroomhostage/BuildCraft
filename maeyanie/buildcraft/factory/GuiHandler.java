package buildcraft.factory;

import buildcraft.factory.ContainerAutoWorkbench;
import buildcraft.factory.TileAutoWorkbench;
import forge.IGuiHandler;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class GuiHandler implements IGuiHandler
{

    public Object getGuiElement(int var1, EntityHuman var2, World var3, int var4, int var5, int var6)
    {
        if (!var3.isLoaded(var4, var5, var6))
        {
            return null;
        }
        else
        {
            TileEntity var7 = var3.getTileEntity(var4, var5, var6);
            switch (var1)
            {
                case 30:
                    if (!(var7 instanceof TileAutoWorkbench))
                    {
                        return null;
                    }

                    return new ContainerAutoWorkbench(var2.inventory, (TileAutoWorkbench)var7);
                default:
                    return null;
            }
        }
    }
}
