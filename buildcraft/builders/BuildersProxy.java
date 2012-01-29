package buildcraft.builders;

import buildcraft.builders.CraftingBuilder;
import buildcraft.builders.CraftingFiller;
import buildcraft.builders.CraftingTemplate;
import buildcraft.builders.TileBuilder;
import buildcraft.builders.TileFiller;
import buildcraft.builders.TileTemplate;
import buildcraft.core.PacketIds;
import buildcraft.core.Utils;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class BuildersProxy
{
    public static void displayGUITemplate(EntityHuman var0, TileTemplate var1)
    {
        ModLoader.OpenGUI(var0, Utils.packetIdToInt(PacketIds.TemplateGUI), var1, new CraftingTemplate(var0.inventory, var1));
    }

    public static void displayGUIBuilder(EntityHuman var0, TileBuilder var1)
    {
        ModLoader.OpenGUI(var0, Utils.packetIdToInt(PacketIds.BuilderGUI), var1, new CraftingBuilder(var0.inventory, var1));
    }

    public static void displayGUIFiller(EntityHuman var0, TileFiller var1)
    {
        ModLoader.OpenGUI(var0, Utils.packetIdToInt(PacketIds.FillerGUI), var1, new CraftingFiller(var0.inventory, var1));
    }

    public static boolean canPlaceTorch(World var0, int var1, int var2, int var3)
    {
        Block var4 = Block.byId[var0.getTypeId(var1, var2, var3)];
        return var4 != null && var4.b();
    }
}
