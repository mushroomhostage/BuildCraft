package net.minecraft.server;

import buildcraft.api.FillerRegistry;
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
import buildcraft.builders.ItemTemplate;
import buildcraft.builders.TileBuilder;
import buildcraft.builders.TileFiller;
import buildcraft.builders.TileMarker;
import buildcraft.builders.TileTemplate;
import buildcraft.core.BluePrint;
import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import net.minecraft.server.forge.Property;
import java.io.File;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.mod_BuildCraftCore;

public class BuildCraftBuilders {

   public static BlockMarker markerBlock;
   public static BlockFiller fillerBlock;
   public static BlockBuilder builderBlock;
   public static BlockTemplate templateBlock;
   public static ItemTemplate templateItem;
   public static BluePrint[] bluePrints = new BluePrint['\ufe01'];


   public static void initialize() {
      mod_BuildCraftCore.initialize();
      BuildCraftCore.initializeGears();
      Property var0 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("templateItem.id", 2, DefaultProps.TEMPLATE_ITEM_ID);
      Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("marker.id", DefaultProps.MARKER_ID);
      Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("filler.id", DefaultProps.FILLER_ID);
      Property var3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("builder.id", DefaultProps.BUILDER_ID);
      Property var4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("template.id", DefaultProps.TEMPLATE_ID);
      BuildCraftCore.mainConfiguration.save();
      CraftingManager var5 = CraftingManager.getInstance();
      templateItem = new ItemTemplate(Integer.parseInt(var0.value));
      templateItem.a("templateItem");
      CoreProxy.addName(templateItem, "Blank Template");
      var5.registerShapedRecipe(new ItemStack(templateItem, 1), new Object[]{"ppp", "pip", "ppp", Character.valueOf('i'), new ItemStack(Item.INK_SACK, 1, 0), Character.valueOf('p'), Item.PAPER});
      markerBlock = new BlockMarker(Integer.parseInt(var1.value));
      ModLoader.RegisterBlock(markerBlock);
      CoreProxy.addName(markerBlock.a("markerBlock"), "Land Mark");
      var5.registerShapedRecipe(new ItemStack(markerBlock, 1), new Object[]{"l ", "r ", Character.valueOf('l'), new ItemStack(Item.INK_SACK, 1, 4), Character.valueOf('r'), Block.REDSTONE_TORCH_ON});
      fillerBlock = new BlockFiller(Integer.parseInt(var2.value));
      ModLoader.RegisterBlock(fillerBlock);
      CoreProxy.addName(fillerBlock.a("fillerBlock"), "Filler");
      var5.registerShapedRecipe(new ItemStack(fillerBlock, 1), new Object[]{"btb", "ycy", "gCg", Character.valueOf('b'), new ItemStack(Item.INK_SACK, 1, 0), Character.valueOf('t'), markerBlock, Character.valueOf('y'), new ItemStack(Item.INK_SACK, 1, 11), Character.valueOf('c'), Block.WORKBENCH, Character.valueOf('g'), BuildCraftCore.goldGearItem, Character.valueOf('C'), Block.CHEST});
      builderBlock = new BlockBuilder(Integer.parseInt(var3.value));
      ModLoader.RegisterBlock(builderBlock);
      CoreProxy.addName(builderBlock.a("builderBlock"), "Builder");
      var5.registerShapedRecipe(new ItemStack(builderBlock, 1), new Object[]{"btb", "ycy", "gCg", Character.valueOf('b'), new ItemStack(Item.INK_SACK, 1, 0), Character.valueOf('t'), markerBlock, Character.valueOf('y'), new ItemStack(Item.INK_SACK, 1, 11), Character.valueOf('c'), Block.WORKBENCH, Character.valueOf('g'), BuildCraftCore.diamondGearItem, Character.valueOf('C'), Block.CHEST});
      templateBlock = new BlockTemplate(Integer.parseInt(var4.value));
      ModLoader.RegisterBlock(templateBlock);
      CoreProxy.addName(templateBlock.a("templateBlock"), "Template Drawing Table");
      var5.registerShapedRecipe(new ItemStack(templateBlock, 1), new Object[]{"btb", "ycy", "gCg", Character.valueOf('b'), new ItemStack(Item.INK_SACK, 1, 0), Character.valueOf('t'), markerBlock, Character.valueOf('y'), new ItemStack(Item.INK_SACK, 1, 11), Character.valueOf('c'), Block.WORKBENCH, Character.valueOf('g'), BuildCraftCore.diamondGearItem, Character.valueOf('C'), new ItemStack(templateItem, 1)});
      ModLoader.RegisterTileEntity(TileMarker.class, "Marker");
      ModLoader.RegisterTileEntity(TileFiller.class, "Filler");
      ModLoader.RegisterTileEntity(TileBuilder.class, "net.minecraft.src.builders.TileBuilder");
      ModLoader.RegisterTileEntity(TileTemplate.class, "net.minecraft.src.builders.TileTemplate");
      FillerRegistry.addRecipe(new FillerFillAll(), new Object[]{"bbb", "bbb", "bbb", Character.valueOf('b'), new ItemStack(Block.BRICK, 1)});
      FillerRegistry.addRecipe(new FillerFlattener(), new Object[]{"   ", "ggg", "bbb", Character.valueOf('g'), Block.GLASS, Character.valueOf('b'), Block.BRICK});
      FillerRegistry.addRecipe(new FillerRemover(), new Object[]{"ggg", "ggg", "ggg", Character.valueOf('g'), Block.GLASS});
      FillerRegistry.addRecipe(new FillerFillWalls(), new Object[]{"bbb", "b b", "bbb", Character.valueOf('b'), Block.BRICK});
      FillerRegistry.addRecipe(new FillerFillPyramid(), new Object[]{"   ", " b ", "bbb", Character.valueOf('b'), Block.BRICK});
      FillerRegistry.addRecipe(new FillerFillStairs(), new Object[]{"  b", " bb", "bbb", Character.valueOf('b'), Block.BRICK});
      BuildCraftCore.mainConfiguration.save();
      loadBluePrints();
   }

   public static int storeBluePrint(BluePrint var0) {
      for(int var1 = 1; var1 < bluePrints.length; ++var1) {
         if(bluePrints[var1] == null) {
            bluePrints[var1] = var0;
            var0.save(var1);
            return var1;
         }
      }

      throw new RuntimeException("No more blueprint slot available.");
   }

   public static void loadBluePrints() {
      File var0 = new File(CoreProxy.getBuildCraftBase(), "blueprints/");
      var0.mkdir();
      String[] var1 = var0.list();
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         String[] var6 = var5.split("[.]");
         if(var6.length == 2 && var6[1].equals("bpt")) {
            int var7 = Integer.parseInt(var6[0]);
            if(var7 != 0) {
               bluePrints[var7] = new BluePrint(new File(var0, var5));
            }
         }
      }

   }

}
