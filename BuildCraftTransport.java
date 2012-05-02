package net.minecraft.server;

import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.core.ItemBuildCraftTexture;
import buildcraft.transport.BlockDockingStation;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.GuiHandler;
import buildcraft.transport.LegacyBlock;
import buildcraft.transport.LegacyTile;
import buildcraft.transport.PipeLogicWood;
import buildcraft.transport.TileDummyGenericPipe;
import buildcraft.transport.TileDummyGenericPipe2;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.network.ConnectionHandler;
import buildcraft.transport.pipes.PipeItemsCobblestone;
import buildcraft.transport.pipes.PipeItemsDiamond;
import buildcraft.transport.pipes.PipeItemsGold;
import buildcraft.transport.pipes.PipeItemsIron;
import buildcraft.transport.pipes.PipeItemsObsidian;
import buildcraft.transport.pipes.PipeItemsStone;
import buildcraft.transport.pipes.PipeItemsWood;
import buildcraft.transport.pipes.PipeLiquidsCobblestone;
import buildcraft.transport.pipes.PipeLiquidsGold;
import buildcraft.transport.pipes.PipeLiquidsIron;
import buildcraft.transport.pipes.PipeLiquidsStone;
import buildcraft.transport.pipes.PipeLiquidsWood;
import buildcraft.transport.pipes.PipePowerGold;
import buildcraft.transport.pipes.PipePowerStone;
import buildcraft.transport.pipes.PipePowerWood;
import forge.MinecraftForge;
import forge.Property;
import java.util.Iterator;
import java.util.LinkedList;

public class BuildCraftTransport
{
    private static boolean initialized = false;
    public static BlockGenericPipe genericPipeBlock;
    public static BlockDockingStation dockingStationBlock;
    public static int[] diamondTextures = new int[6];
    public static boolean alwaysConnectPipes;
    public static Item pipeWaterproof;
    public static Item pipeItemsWood;
    public static Item pipeItemsStone;
    public static Item pipeItemsCobblestone;
    public static Item pipeItemsIron;
    public static Item pipeItemsGold;
    public static Item pipeItemsDiamond;
    public static Item pipeItemsObsidian;
    public static Item pipeLiquidsWood;
    public static Item pipeLiquidsCobblestone;
    public static Item pipeLiquidsStone;
    public static Item pipeLiquidsIron;
    public static Item pipeLiquidsGold;
    public static Item pipePowerWood;
    public static Item pipePowerStone;
    public static Item pipePowerGold;
    private static LinkedList pipeRecipes = new LinkedList();

    public static void load()
    {
        MinecraftForge.registerConnectionHandler(new ConnectionHandler());
        MinecraftForge.setGuiHandler(mod_BuildCraftTransport.instance, new GuiHandler());
    }

    public static void initialize()
    {
        if (!initialized)
        {
            initialized = true;
            mod_BuildCraftCore.initialize();
            Property var0 = BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("loadLegacyPipes", "general", true);
            var0.comment = "set to true to load pre 2.2.5 worlds pipes";
            Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("pipes.alwaysConnect", "general", DefaultProps.PIPES_ALWAYS_CONNECT);
            var1.comment = "set to false to deactivate pipe connection rules, true by default";
            Property var2 = BuildCraftCore.mainConfiguration.getOrCreateProperty("woodenPipe.exclusion", "block", "");
            PipeLogicWood.excludedBlocks = var2.value.split(",");
            Property var3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("pipe.id", DefaultProps.GENERIC_PIPE_ID);
            Property var4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("dockingStation.id", DefaultProps.DOCKING_STATION_ID);
            int var5;

            for (var5 = 0; var5 < PipeLogicWood.excludedBlocks.length; ++var5)
            {
                PipeLogicWood.excludedBlocks[var5] = PipeLogicWood.excludedBlocks[var5].trim();
            }

            BuildCraftCore.mainConfiguration.save();
            pipeWaterproof = (new ItemBuildCraftTexture(DefaultProps.PIPE_WATERPROOF_ID)).d(33);
            pipeWaterproof.a("pipeWaterproof");
            CoreProxy.addName(pipeWaterproof, "Pipe Waterproof");
            genericPipeBlock = new BlockGenericPipe(Integer.parseInt(var3.value));
            ModLoader.registerBlock(genericPipeBlock);
            mod_BuildCraftTransport.registerTilePipe(TileDummyGenericPipe.class, "net.minecraft.server.buildcraft.GenericPipe");
            mod_BuildCraftTransport.registerTilePipe(TileDummyGenericPipe.class, "net.minecraft.src.buildcraft.GenericPipe");
            mod_BuildCraftTransport.registerTilePipe(TileDummyGenericPipe2.class, "net.minecraft.server.buildcraft.transport.TileGenericPipe");
            mod_BuildCraftTransport.registerTilePipe(TileDummyGenericPipe2.class, "net.minecraft.src.buildcraft.transport.TileGenericPipe");
            mod_BuildCraftTransport.registerTilePipe(TileGenericPipe.class, "net.minecraft.server.buildcraft.transport.GenericPipe");
            mod_BuildCraftTransport.registerTilePipe(TileGenericPipe.class, "net.minecraft.src.buildcraft.transport.GenericPipe");
            pipeItemsWood = createPipe(DefaultProps.PIPE_ITEMS_WOOD_ID, PipeItemsWood.class, "Wooden Transport Pipe", Block.WOOD, Block.GLASS, Block.WOOD);
            pipeItemsCobblestone = createPipe(DefaultProps.PIPE_ITEMS_COBBLESTONE_ID, PipeItemsCobblestone.class, "Cobblestone Transport Pipe", Block.COBBLESTONE, Block.GLASS, Block.COBBLESTONE);
            pipeItemsStone = createPipe(DefaultProps.PIPE_ITEMS_STONE_ID, PipeItemsStone.class, "Stone Transport Pipe", Block.STONE, Block.GLASS, Block.STONE);
            pipeItemsIron = createPipe(DefaultProps.PIPE_ITEMS_IRON_ID, PipeItemsIron.class, "Iron Transport Pipe", Item.IRON_INGOT, Block.GLASS, Item.IRON_INGOT);
            pipeItemsGold = createPipe(DefaultProps.PIPE_ITEMS_GOLD_ID, PipeItemsGold.class, "Golden Transport Pipe", Item.GOLD_INGOT, Block.GLASS, Item.GOLD_INGOT);
            pipeItemsDiamond = createPipe(DefaultProps.PIPE_ITEMS_DIAMOND_ID, PipeItemsDiamond.class, "Diamond Transport Pipe", Item.DIAMOND, Block.GLASS, Item.DIAMOND);
            pipeItemsObsidian = createPipe(DefaultProps.PIPE_ITEMS_OBSIDIAN_ID, PipeItemsObsidian.class, "Obsidian Transport Pipe", Block.OBSIDIAN, Block.GLASS, Block.OBSIDIAN);
            pipeLiquidsWood = createPipe(DefaultProps.PIPE_LIQUIDS_WOOD_ID, PipeLiquidsWood.class, "Wooden Waterproof Pipe", pipeWaterproof, pipeItemsWood, (Object)null);
            pipeLiquidsCobblestone = createPipe(DefaultProps.PIPE_LIQUIDS_COBBLESTONE_ID, PipeLiquidsCobblestone.class, "Cobblestone Waterproof Pipe", pipeWaterproof, pipeItemsCobblestone, (Object)null);
            pipeLiquidsStone = createPipe(DefaultProps.PIPE_LIQUIDS_STONE_ID, PipeLiquidsStone.class, "Stone Waterproof Pipe", pipeWaterproof, pipeItemsStone, (Object)null);
            pipeLiquidsIron = createPipe(DefaultProps.PIPE_LIQUIDS_IRON_ID, PipeLiquidsIron.class, "Iron Waterproof Pipe", pipeWaterproof, pipeItemsIron, (Object)null);
            pipeLiquidsGold = createPipe(DefaultProps.PIPE_LIQUIDS_GOLD_ID, PipeLiquidsGold.class, "Golden Waterproof Pipe", pipeWaterproof, pipeItemsGold, (Object)null);
            pipePowerWood = createPipe(DefaultProps.PIPE_POWER_WOOD_ID, PipePowerWood.class, "Wooden Conductive Pipe", Item.REDSTONE, pipeItemsWood, (Object)null);
            pipePowerStone = createPipe(DefaultProps.PIPE_POWER_STONE_ID, PipePowerStone.class, "Stone Conductive Pipe", Item.REDSTONE, pipeItemsStone, (Object)null);
            pipePowerGold = createPipe(DefaultProps.PIPE_POWER_GOLD_ID, PipePowerGold.class, "Golden Conductive Pipe", Item.REDSTONE, pipeItemsGold, (Object)null);

            for (var5 = 0; var5 < 6; ++var5)
            {
                diamondTextures[var5] = 22 + var5;
            }

            alwaysConnectPipes = Boolean.parseBoolean(var1.value);

            if (var0.value.equals("true"))
            {
                Property var12 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("woodenPipe.id", DefaultProps.WOODEN_PIPE_ID);
                Property var6 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("stonePipe.id", DefaultProps.STONE_PIPE_ID);
                Property var7 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("ironPipe.id", DefaultProps.IRON_PIPE_ID);
                Property var8 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("goldenPipe.id", DefaultProps.GOLDEN_PIPE_ID);
                Property var9 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("diamondPipe.id", DefaultProps.DIAMOND_PIPE_ID);
                Property var10 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("obsidianPipe.id", DefaultProps.OBSIDIAN_PIPE_ID);
                Property var11 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("cobblestonePipe.id", DefaultProps.COBBLESTONE_PIPE_ID);
                ModLoader.registerBlock(new LegacyBlock(Integer.parseInt(var12.value), pipeItemsWood.id));
                ModLoader.registerBlock(new LegacyBlock(Integer.parseInt(var6.value), pipeItemsStone.id));
                ModLoader.registerBlock(new LegacyBlock(Integer.parseInt(var7.value), pipeItemsIron.id));
                ModLoader.registerBlock(new LegacyBlock(Integer.parseInt(var8.value), pipeItemsGold.id));
                ModLoader.registerBlock(new LegacyBlock(Integer.parseInt(var9.value), pipeItemsDiamond.id));
                ModLoader.registerBlock(new LegacyBlock(Integer.parseInt(var10.value), pipeItemsObsidian.id));
                ModLoader.registerBlock(new LegacyBlock(Integer.parseInt(var11.value), pipeItemsCobblestone.id));
                ModLoader.registerTileEntity(LegacyTile.class, "net.buildcraft.server.buildcraft.transport.legacy.LegacyTile");
                ModLoader.registerTileEntity(LegacyTile.class, "net.buildcraft.src.buildcraft.transport.legacy.LegacyTile");
            }

            BuildCraftCore.mainConfiguration.save();

            if (BuildCraftCore.loadDefaultRecipes)
            {
                loadRecipes();
            }
        }
    }

    public static void loadRecipes()
    {
        CraftingManager var0 = CraftingManager.getInstance();
        var0.registerShapedRecipe(new ItemStack(pipeWaterproof, 1), new Object[] {"W ", "  ", 'W', new ItemStack(Item.INK_SACK, 1, 2)});
        Iterator var1 = pipeRecipes.iterator();

        while (var1.hasNext())
        {
            BuildCraftTransport.PipeRecipe var2 = (BuildCraftTransport.PipeRecipe)var1.next();
            var0.registerShapedRecipe(var2.result, var2.input);
        }
    }

    private static Item createPipe(int var0, Class var1, String var2, Object var3, Object var4, Object var5)
    {
        String var6 = Character.toLowerCase(var1.getSimpleName().charAt(0)) + var1.getSimpleName().substring(1);
        Property var7 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty(var6 + ".id", "item", var0);
        int var8 = Integer.parseInt(var7.value);
        Item var9 = BlockGenericPipe.registerPipe(var8, var1);
        var9.a(var1.getSimpleName());
        CoreProxy.addName(var9, var2);
        BuildCraftTransport.PipeRecipe var10 = new BuildCraftTransport.PipeRecipe((BuildCraftTransport.NamelessClass1494450339)null);

        if (var3 != null && var4 != null && var5 != null)
        {
            var10.result = new ItemStack(var9, 8);
            var10.input = new Object[] {"   ", "ABC", "   ", 'A', var3, 'B', var4, 'C', var5};
            pipeRecipes.add(var10);
        }
        else if (var3 != null && var4 != null)
        {
            var10.result = new ItemStack(var9, 1);
            var10.input = new Object[] {"A ", "B ", 'A', var3, 'B', var4};
            pipeRecipes.add(var10);
        }

        return var9;
    }

    static class NamelessClass1494450339
    {
    }

    private static class PipeRecipe
    {
        ItemStack result;
        Object[] input;

        private PipeRecipe() {}

        PipeRecipe(BuildCraftTransport.NamelessClass1494450339 var1)
        {
            this();
        }
    }
}
