package net.minecraft.server;

import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.core.ItemBuildCraftTexture;
import buildcraft.transport.BlockDockingStation;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.LegacyBlock;
import buildcraft.transport.LegacyTile;
import buildcraft.transport.PipeLogicWood;
import buildcraft.transport.TileGenericPipe;
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
import forge.Property;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.mod_BuildCraftCore;

public class BuildCraftTransport {

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


   public static void initialize() {
      if(!initialized) {
         initialized = true;
         mod_BuildCraftCore.initialize();
         Property var0 = BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("loadLegacyPipes", 0, true);
         var0.comment = "set to true to load pre 2.2.1 worlds pipes";
         Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("pipes.alwaysConnect", 0, DefaultProps.PIPES_ALWAYS_CONNECT);
         var1.comment = "set to false to deactivate pipe connection rules, true by default";
         Property var2 = BuildCraftCore.mainConfiguration.getOrCreateProperty("woodenPipe.exclusion", 1, "");
         PipeLogicWood.excludedBlocks = var2.value.split(",");
         Property var3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("pipe.id", DefaultProps.GENERIC_PIPE_ID);
         Property var4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("dockingStation.id", DefaultProps.DOCKING_STATION_ID);

         for(int var5 = 0; var5 < PipeLogicWood.excludedBlocks.length; ++var5) {
            PipeLogicWood.excludedBlocks[var5] = PipeLogicWood.excludedBlocks[var5].trim();
         }

         BuildCraftCore.mainConfiguration.save();
         CraftingManager var13 = CraftingManager.getInstance();
         pipeWaterproof = (new ItemBuildCraftTexture(DefaultProps.PIPE_WATERPROOF_ID)).b(33);
         pipeWaterproof.a("pipeWaterproof");
         CoreProxy.addName(pipeWaterproof, "Pipe Waterproof");
         var13.registerShapedRecipe(new ItemStack(pipeWaterproof, 1), new Object[]{"W ", "  ", Character.valueOf('W'), new ItemStack(Item.INK_SACK, 1, 2)});
         genericPipeBlock = new BlockGenericPipe(Integer.parseInt(var3.value));
         ModLoader.RegisterTileEntity(TileGenericPipe.class, "net.minecraft.server.buildcraft.transport.TileGenericPipe");
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

         for(int var6 = 0; var6 < 6; ++var6) {
            diamondTextures[var6] = 22 + var6;
         }

         alwaysConnectPipes = Boolean.parseBoolean(var1.value);
         if(var0.value.equals("true")) {
            Property var14 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("woodenPipe.id", DefaultProps.WOODEN_PIPE_ID);
            Property var7 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("stonePipe.id", DefaultProps.STONE_PIPE_ID);
            Property var8 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("ironPipe.id", DefaultProps.IRON_PIPE_ID);
            Property var9 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("goldenPipe.id", DefaultProps.GOLDEN_PIPE_ID);
            Property var10 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("diamondPipe.id", DefaultProps.DIAMOND_PIPE_ID);
            Property var11 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("obsidianPipe.id", DefaultProps.OBSIDIAN_PIPE_ID);
            Property var12 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("cobblestonePipe.id", DefaultProps.COBBLESTONE_PIPE_ID);
            ModLoader.RegisterBlock(new LegacyBlock(Integer.parseInt(var14.value), pipeItemsWood.id));
            ModLoader.RegisterBlock(new LegacyBlock(Integer.parseInt(var7.value), pipeItemsStone.id));
            ModLoader.RegisterBlock(new LegacyBlock(Integer.parseInt(var8.value), pipeItemsIron.id));
            ModLoader.RegisterBlock(new LegacyBlock(Integer.parseInt(var9.value), pipeItemsGold.id));
            ModLoader.RegisterBlock(new LegacyBlock(Integer.parseInt(var10.value), pipeItemsDiamond.id));
            ModLoader.RegisterBlock(new LegacyBlock(Integer.parseInt(var11.value), pipeItemsObsidian.id));
            ModLoader.RegisterBlock(new LegacyBlock(Integer.parseInt(var12.value), pipeItemsCobblestone.id));
            ModLoader.RegisterTileEntity(LegacyTile.class, "net.buildcraft.server.buildcraft.transport.legacy.LegacyTile");
         }

         BuildCraftCore.mainConfiguration.save();
      }
   }

   private static Item createPipe(int var0, Class var1, String var2, Object var3, Object var4, Object var5) {
      String var6 = Character.toLowerCase(var1.getSimpleName().charAt(0)) + var1.getSimpleName().substring(1);
      Property var7 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty(var6 + ".id", 2, var0);
      int var8 = Integer.parseInt(var7.value);
      Item var9 = BlockGenericPipe.registerPipe(var8, var1);
      var9.a(var1.getSimpleName());
      CoreProxy.addName(var9, var2);
      CraftingManager var10 = CraftingManager.getInstance();
      if(var3 != null && var4 != null && var5 != null) {
         var10.registerShapedRecipe(new ItemStack(var9, 8), new Object[]{"   ", "ABC", "   ", Character.valueOf('A'), var3, Character.valueOf('B'), var4, Character.valueOf('C'), var5});
      } else if(var3 != null && var4 != null) {
         var10.registerShapedRecipe(new ItemStack(var9, 1), new Object[]{"A ", "B ", Character.valueOf('A'), var3, Character.valueOf('B'), var4});
      }

      return var9;
   }

   public static void ModsLoaded() {
      mod_BuildCraftCore.initialize();
      initialize();
   }

}
