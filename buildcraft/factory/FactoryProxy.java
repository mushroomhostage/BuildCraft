package buildcraft.factory;

import buildcraft.core.PacketIds;
import buildcraft.core.Utils;
import buildcraft.factory.ContainerAutoWorkbench;
import buildcraft.factory.TileAutoWorkbench;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class FactoryProxy
{

    public static void displayGUIAutoCrafting(World var0, EntityHuman var1, int var2, int var3, int var4)
    {
        TileAutoWorkbench var5 = (TileAutoWorkbench)var0.getTileEntity(var2, var3, var4);
        ModLoader.OpenGUI(var1, Utils.packetIdToInt(PacketIds.AutoCraftingGUI), var5, new ContainerAutoWorkbench(var1.inventory, var5));
    }
}
