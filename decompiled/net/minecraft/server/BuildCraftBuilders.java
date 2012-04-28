package net.minecraft.server;

import buildcraft.builders.BlockBuilder;
import buildcraft.builders.BlockFiller;
import buildcraft.builders.BlockMarker;
import buildcraft.builders.BlockTemplate;
import buildcraft.builders.FillerFillAll;
import buildcraft.builders.FillerFillPyramid;
import buildcraft.builders.FillerFillStairs;
import buildcraft.builders.FillerFillWalls;
import buildcraft.builders.FillerFlattener;
import buildcraft.builders.FillerRemover;
import buildcraft.builders.GuiHandler;
import buildcraft.builders.ItemTemplate;
import buildcraft.builders.TileBuilder;
import buildcraft.builders.TileFiller;
import buildcraft.builders.TileMarker;
import buildcraft.builders.TileTemplate;
import buildcraft.core.BluePrint;
import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.core.FillerRegistry;
import forge.MinecraftForge;
import forge.Property;
import java.io.File;

public class BuildCraftBuilders
{
    public static BlockMarker markerBlock;
    public static BlockFiller fillerBlock;
    public static BlockBuilder builderBlock;
    public static BlockTemplate templateBlock;
    public static ItemTemplate templateItem;
    private static boolean initialized = false;
    public static BluePrint[] bluePrints = new BluePrint[65025];

    public static void load()
    {
        MinecraftForge.setGuiHandler(mod_BuildCraftBuilders.instance, new GuiHandler());
    }

    public static void initialize()
    {
        if (!initialized)
        {
            initialized = true;
            mod_BuildCraftCore.initialize();
            BuildCraftCore.initializeGears();
            Property var0 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("templateItem.id", "item", DefaultProps.TEMPLATE_ITEM_ID);
            Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("marker.id", DefaultProps.MARKER_ID);
            Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("filler.id", DefaultProps.FILLER_ID);
            Property var3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("builder.id", DefaultProps.BUILDER_ID);
            Property var4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("template.id", DefaultProps.TEMPLATE_ID);
            BuildCraftCore.mainConfiguration.save();
            templateItem = new ItemTemplate(Integer.parseInt(var0.value));
            templateItem.a("templateItem");
            CoreProxy.addName(templateItem, "Blank Template");
            markerBlock = new BlockMarker(Integer.parseInt(var1.value));
            ModLoader.registerBlock(markerBlock);
            CoreProxy.addName(markerBlock.a("markerBlock"), "Land Mark");
            fillerBlock = new BlockFiller(Integer.parseInt(var2.value));
            ModLoader.registerBlock(fillerBlock);
            CoreProxy.addName(fillerBlock.a("fillerBlock"), "Filler");
            builderBlock = new BlockBuilder(Integer.parseInt(var3.value));
            ModLoader.registerBlock(builderBlock);
            CoreProxy.addName(builderBlock.a("builderBlock"), "Builder");
            templateBlock = new BlockTemplate(Integer.parseInt(var4.value));
            ModLoader.registerBlock(templateBlock);
            CoreProxy.addName(templateBlock.a("templateBlock"), "Template Drawing Table");
            ModLoader.registerTileEntity(TileMarker.class, "Marker");
            ModLoader.registerTileEntity(TileFiller.class, "Filler");
            ModLoader.registerTileEntity(TileBuilder.class, "net.minecraft.server.builders.TileBuilder");
            ModLoader.registerTileEntity(TileTemplate.class, "net.minecraft.server.builders.TileTemplate");
            FillerRegistry.addRecipe(new FillerFillAll(), new Object[] {"bbb", "bbb", "bbb", 'b', new ItemStack(Block.BRICK, 1)});
            FillerRegistry.addRecipe(new FillerFlattener(), new Object[] {"   ", "ggg", "bbb", 'g', Block.GLASS, 'b', Block.BRICK});
            FillerRegistry.addRecipe(new FillerRemover(), new Object[] {"ggg", "ggg", "ggg", 'g', Block.GLASS});
            FillerRegistry.addRecipe(new FillerFillWalls(), new Object[] {"bbb", "b b", "bbb", 'b', Block.BRICK});
            FillerRegistry.addRecipe(new FillerFillPyramid(), new Object[] {"   ", " b ", "bbb", 'b', Block.BRICK});
            FillerRegistry.addRecipe(new FillerFillStairs(), new Object[] {"  b", " bb", "bbb", 'b', Block.BRICK});
            BuildCraftCore.mainConfiguration.save();
            loadBluePrints();

            if (BuildCraftCore.loadDefaultRecipes)
            {
                loadRecipes();
            }
        }
    }

    public static void loadRecipes()
    {
        CraftingManager var0 = CraftingManager.getInstance();
        var0.registerShapedRecipe(new ItemStack(templateItem, 1), new Object[] {"ppp", "pip", "ppp", 'i', new ItemStack(Item.INK_SACK, 1, 0), 'p', Item.PAPER});
        var0.registerShapedRecipe(new ItemStack(markerBlock, 1), new Object[] {"l ", "r ", 'l', new ItemStack(Item.INK_SACK, 1, 4), 'r', Block.REDSTONE_TORCH_ON});
        var0.registerShapedRecipe(new ItemStack(fillerBlock, 1), new Object[] {"btb", "ycy", "gCg", 'b', new ItemStack(Item.INK_SACK, 1, 0), 't', markerBlock, 'y', new ItemStack(Item.INK_SACK, 1, 11), 'c', Block.WORKBENCH, 'g', BuildCraftCore.goldGearItem, 'C', Block.CHEST});
        var0.registerShapedRecipe(new ItemStack(builderBlock, 1), new Object[] {"btb", "ycy", "gCg", 'b', new ItemStack(Item.INK_SACK, 1, 0), 't', markerBlock, 'y', new ItemStack(Item.INK_SACK, 1, 11), 'c', Block.WORKBENCH, 'g', BuildCraftCore.diamondGearItem, 'C', Block.CHEST});
        var0.registerShapedRecipe(new ItemStack(templateBlock, 1), new Object[] {"btb", "ycy", "gCg", 'b', new ItemStack(Item.INK_SACK, 1, 0), 't', markerBlock, 'y', new ItemStack(Item.INK_SACK, 1, 11), 'c', Block.WORKBENCH, 'g', BuildCraftCore.diamondGearItem, 'C', new ItemStack(templateItem, 1)});
    }

    public static int storeBluePrint(BluePrint var0)
    {
        for (int var1 = 1; var1 < bluePrints.length; ++var1)
        {
            if (bluePrints[var1] == null)
            {
                bluePrints[var1] = var0;
                var0.save(var1);
                return var1;
            }
        }

        throw new RuntimeException("No more blueprint slot available.");
    }

    public static void loadBluePrints()
    {
        File var0 = new File(CoreProxy.getBuildCraftBase(), "blueprints/");
        var0.mkdir();
        String[] var1 = var0.list();
        String[] var2 = var1;
        int var3 = var1.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            String var5 = var2[var4];
            String[] var6 = var5.split("[.]");

            if (var6.length == 2 && var6[1].equals("bpt"))
            {
                int var7 = Integer.parseInt(var6[0]);

                if (var7 != 0)
                {
                    bluePrints[var7] = new BluePrint(new File(var0, var5));
                }
            }
        }
    }
}
