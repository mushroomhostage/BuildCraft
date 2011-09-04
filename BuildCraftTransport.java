package net.minecraft.server;

import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.transport.BlockCobblestonePipe;
import buildcraft.transport.BlockDiamondPipe;
import buildcraft.transport.BlockGoldenPipe;
import buildcraft.transport.BlockIronPipe;
import buildcraft.transport.BlockObsidianPipe;
import buildcraft.transport.BlockStonePipe;
import buildcraft.transport.BlockWoodenPipe;
import buildcraft.transport.TileCobblestonePipe;
import buildcraft.transport.TileDiamondPipe;
import buildcraft.transport.TileGoldenPipe;
import buildcraft.transport.TileIronPipe;
import buildcraft.transport.TileObsidianPipe;
import buildcraft.transport.TileStonePipe;
import buildcraft.transport.TileWoodenPipe;
import net.minecraft.server.forge.Property;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.mod_BuildCraftCore;

public class BuildCraftTransport {

   private static boolean initialized = false;
   public static BlockWoodenPipe woodenPipeBlock;
   public static BlockStonePipe stonePipeBlock;
   public static BlockIronPipe ironPipeBlock;
   public static BlockGoldenPipe goldenPipeBlock;
   public static BlockDiamondPipe diamondPipeBlock;
   public static BlockObsidianPipe obsidianPipeBlock;
   public static BlockCobblestonePipe cobblestonePipeBlock;
   public static int plainIronTexture;
   public static int[] diamondTextures = new int[6];
   public static boolean alwaysConnectPipes;


   public static void initialize() {
      if(!initialized) {
         initialized = true;
         mod_BuildCraftCore.initialize();
         Property var0 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("woodenPipe.id", DefaultProps.WOODEN_PIPE_ID);
         Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("stonePipe.id", DefaultProps.STONE_PIPE_ID);
         Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("ironPipe.id", DefaultProps.IRON_PIPE_ID);
         Property var3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("goldenPipe.id", DefaultProps.GOLDEN_PIPE_ID);
         Property var4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("diamondPipe.id", DefaultProps.DIAMOND_PIPE_ID);
         Property var5 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("obsidianPipe.id", DefaultProps.OBSIDIAN_PIPE_ID);
         Property var6 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("cobblestonePipe.id", DefaultProps.COBBLESTONE_PIPE_ID);
         Property var7 = BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("pipes.alwaysConnect", 0, DefaultProps.PIPES_ALWAYS_CONNECT);
         var7.comment = "set to false to deactivate pipe connection rules, true by default";
         Property var8 = BuildCraftCore.mainConfiguration.getOrCreateProperty("woodenPipe.exclusion", 1, "");
         BlockWoodenPipe.excludedBlocks = var8.value.split(",");

         for(int var9 = 0; var9 < BlockWoodenPipe.excludedBlocks.length; ++var9) {
            BlockWoodenPipe.excludedBlocks[var9] = BlockWoodenPipe.excludedBlocks[var9].trim();
         }

         BuildCraftCore.mainConfiguration.save();
         CraftingManager var11 = CraftingManager.getInstance();
         woodenPipeBlock = new BlockWoodenPipe(Integer.parseInt(var0.value));
         CoreProxy.addName(woodenPipeBlock.a("woodenPipe"), "Wooden Pipe");
         ModLoader.RegisterBlock(woodenPipeBlock);
         var11.registerShapedRecipe(new ItemStack(woodenPipeBlock, 8), new Object[]{"   ", "PGP", "   ", Character.valueOf('P'), Block.WOOD, Character.valueOf('G'), Block.GLASS});
         stonePipeBlock = new BlockStonePipe(Integer.parseInt(var1.value));
         CoreProxy.addName(stonePipeBlock.a("stonePipe"), "Stone Pipe");
         ModLoader.RegisterBlock(stonePipeBlock);
         var11.registerShapedRecipe(new ItemStack(stonePipeBlock, 8), new Object[]{"   ", "PGP", "   ", Character.valueOf('P'), Block.STONE, Character.valueOf('G'), Block.GLASS});
         ironPipeBlock = new BlockIronPipe(Integer.parseInt(var2.value));
         CoreProxy.addName(ironPipeBlock.a("ironPipe"), "Iron Pipe");
         ModLoader.RegisterBlock(ironPipeBlock);
         var11.registerShapedRecipe(new ItemStack(ironPipeBlock, 8), new Object[]{"   ", "PGP", "   ", Character.valueOf('P'), Item.IRON_INGOT, Character.valueOf('G'), Block.GLASS});
         goldenPipeBlock = new BlockGoldenPipe(Integer.parseInt(var3.value));
         CoreProxy.addName(goldenPipeBlock.a("goldenPipe"), "Golden Pipe");
         ModLoader.RegisterBlock(goldenPipeBlock);
         var11.registerShapedRecipe(new ItemStack(goldenPipeBlock, 8), new Object[]{"   ", "PGP", "   ", Character.valueOf('P'), Item.GOLD_INGOT, Character.valueOf('G'), Block.GLASS});
         diamondPipeBlock = new BlockDiamondPipe(Integer.parseInt(var4.value));
         CoreProxy.addName(diamondPipeBlock.a("diamondPipe"), "Diamond Pipe");
         ModLoader.RegisterBlock(diamondPipeBlock);
         var11.registerShapedRecipe(new ItemStack(diamondPipeBlock, 8), new Object[]{"   ", "PGP", "   ", Character.valueOf('P'), Item.DIAMOND, Character.valueOf('G'), Block.GLASS});
         obsidianPipeBlock = new BlockObsidianPipe(Integer.parseInt(var5.value));
         CoreProxy.addName(obsidianPipeBlock.a("obsidianPipe"), "Obsidian Pipe");
         ModLoader.RegisterBlock(obsidianPipeBlock);
         var11.registerShapedRecipe(new ItemStack(obsidianPipeBlock, 8), new Object[]{"   ", "PGP", "   ", Character.valueOf('P'), Block.OBSIDIAN, Character.valueOf('G'), Block.GLASS});
         cobblestonePipeBlock = new BlockCobblestonePipe(Integer.parseInt(var6.value));
         CoreProxy.addName(cobblestonePipeBlock.a("cobblestonePipe"), "Cobblestone Pipe");
         ModLoader.RegisterBlock(cobblestonePipeBlock);
         var11.registerShapedRecipe(new ItemStack(cobblestonePipeBlock, 8), new Object[]{"   ", "PGP", "   ", Character.valueOf('P'), Block.COBBLESTONE, Character.valueOf('G'), Block.GLASS});
         ModLoader.RegisterTileEntity(TileWoodenPipe.class, "WoodenPipe");
         ModLoader.RegisterTileEntity(TileStonePipe.class, "StonePipe");
         ModLoader.RegisterTileEntity(TileIronPipe.class, "IronPipe");
         ModLoader.RegisterTileEntity(TileGoldenPipe.class, "GoldenPipe");
         ModLoader.RegisterTileEntity(TileDiamondPipe.class, "DiamondPipe");
         ModLoader.RegisterTileEntity(TileObsidianPipe.class, "ObsidianPipe");
         ModLoader.RegisterTileEntity(TileCobblestonePipe.class, "CobblestonePipe");
         plainIronTexture = 19;

         for(int var10 = 0; var10 < 6; ++var10) {
            diamondTextures[var10] = 22 + var10;
         }

         alwaysConnectPipes = Boolean.parseBoolean(var7.value);
         BuildCraftCore.mainConfiguration.save();
      }
   }

   public static void ModsLoaded() {
      mod_BuildCraftCore.initialize();
      initialize();
   }

}
