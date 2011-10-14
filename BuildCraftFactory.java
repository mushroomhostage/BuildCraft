package net.minecraft.server;

import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.factory.BlockAutoWorkbench;
import buildcraft.factory.BlockFrame;
import buildcraft.factory.BlockMiningWell;
import buildcraft.factory.BlockPlainPipe;
import buildcraft.factory.BlockPump;
import buildcraft.factory.BlockQuarry;
import buildcraft.factory.BlockRefinery;
import buildcraft.factory.BlockTank;
import buildcraft.factory.TankBucketHandler;
import buildcraft.factory.TileAutoWorkbench;
import buildcraft.factory.TileMiningWell;
import buildcraft.factory.TilePump;
import buildcraft.factory.TileQuarry;
import buildcraft.factory.TileRefinery;
import buildcraft.factory.TileTank;
import forge.MinecraftForge;
import forge.Property;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.mod_BuildCraftCore;

public class BuildCraftFactory {

   public static BlockQuarry quarryBlock;
   public static BlockMiningWell miningWellBlock;
   public static BlockAutoWorkbench autoWorkbenchBlock;
   public static BlockFrame frameBlock;
   public static BlockPlainPipe plainPipeBlock;
   public static BlockPump pumpBlock;
   public static BlockTank tankBlock;
   public static BlockRefinery refineryBlock;
   public static int drillTexture;


   public static void initialize() {
      mod_BuildCraftCore.initialize();
      BuildCraftCore.initializeGears();
      CraftingManager var0 = CraftingManager.getInstance();
      boolean var1 = Boolean.parseBoolean(BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("mining.enabled", 0, true).value);
      Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("miningWell.id", DefaultProps.MINING_WELL_ID);
      Property var3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("drill.id", DefaultProps.DRILL_ID);
      Property var4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("autoWorkbench.id", DefaultProps.AUTO_WORKBENCH_ID);
      Property var5 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("frame.id", DefaultProps.FRAME_ID);
      Property var6 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("quarry.id", DefaultProps.QUARRY_ID);
      Property var7 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("pump.id", DefaultProps.PUMP_ID);
      Property var8 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("tank.id", DefaultProps.TANK_ID);
      Property var9 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("refinery.id", DefaultProps.REFINERY_ID);
      BuildCraftCore.mainConfiguration.save();
      MinecraftForge.registerCustomBucketHandler(new TankBucketHandler());
      miningWellBlock = new BlockMiningWell(Integer.parseInt(var2.value));
      ModLoader.RegisterBlock(miningWellBlock);
      CoreProxy.addName(miningWellBlock.a("miningWellBlock"), "Mining Well");
      if(var1) {
         var0.registerShapedRecipe(new ItemStack(miningWellBlock, 1), new Object[]{"ipi", "igi", "iPi", Character.valueOf('p'), Item.REDSTONE, Character.valueOf('i'), Item.IRON_INGOT, Character.valueOf('g'), BuildCraftCore.ironGearItem, Character.valueOf('P'), Item.IRON_PICKAXE});
      }

      plainPipeBlock = new BlockPlainPipe(Integer.parseInt(var3.value));
      ModLoader.RegisterBlock(plainPipeBlock);
      CoreProxy.addName(plainPipeBlock.a("plainPipeBlock"), "Mining Pipe");
      
      autoWorkbenchBlock = new BlockAutoWorkbench(Integer.parseInt(var4.value));
      ModLoader.RegisterBlock(autoWorkbenchBlock);
      var0.registerShapedRecipe(new ItemStack(autoWorkbenchBlock), new Object[]{" g ", "gwg", " g ", Character.valueOf('w'), Block.WORKBENCH, Character.valueOf('g'), BuildCraftCore.woodenGearItem});
      CoreProxy.addName(autoWorkbenchBlock.a("autoWorkbenchBlock"), "Automatic Crafting Table");
      
      frameBlock = new BlockFrame(Integer.parseInt(var5.value));
      ModLoader.RegisterBlock(frameBlock);
      CoreProxy.addName(frameBlock.a("frameBlock"), "Frame");
      
      quarryBlock = new BlockQuarry(Integer.parseInt(var6.value));
      ModLoader.RegisterBlock(quarryBlock);
      if(var1) {
         var0.registerShapedRecipe(new ItemStack(quarryBlock), new Object[]{"ipi", "gig", "dDd", Character.valueOf('i'), BuildCraftCore.ironGearItem, Character.valueOf('p'), Item.REDSTONE, Character.valueOf('g'), BuildCraftCore.goldGearItem, Character.valueOf('d'), BuildCraftCore.diamondGearItem, Character.valueOf('D'), Item.DIAMOND_PICKAXE});
      }

      CoreProxy.addName(quarryBlock.a("machineBlock"), "Quarry");

      tankBlock = new BlockTank(Integer.parseInt(var8.value));
      ModLoader.RegisterBlock(tankBlock);
      var0.registerShapedRecipe(new ItemStack(tankBlock), new Object[]{"ggg", "g g", "ggg", Character.valueOf('g'), Block.GLASS});
      CoreProxy.addName(tankBlock.a("tankBlock"), "Tank");

      pumpBlock = new BlockPump(Integer.parseInt(var7.value));
      ModLoader.RegisterBlock(pumpBlock);
      var0.registerShapedRecipe(new ItemStack(pumpBlock), new Object[]{"T ", "W ", Character.valueOf('T'), tankBlock, Character.valueOf('W'), miningWellBlock});
      CoreProxy.addName(pumpBlock.a("pumpBlock"), "Pump");

      refineryBlock = new BlockRefinery(Integer.parseInt(var9.value));
      ModLoader.RegisterBlock(refineryBlock);
      var0.registerShapedRecipe(new ItemStack(refineryBlock), new Object[]{"   ", "RTR", "TGT", Character.valueOf('T'), tankBlock, Character.valueOf('G'), BuildCraftCore.diamondGearItem, Character.valueOf('R'), Block.REDSTONE_TORCH_ON});
      CoreProxy.addName(refineryBlock.a("refineryBlock"), "Refinery");

      ModLoader.RegisterTileEntity(TileQuarry.class, "Machine");
      ModLoader.RegisterTileEntity(TileMiningWell.class, "MiningWell");
      ModLoader.RegisterTileEntity(TileAutoWorkbench.class, "AutoWorkbench");
      ModLoader.RegisterTileEntity(TilePump.class, "net.minecraft.server.buildcraft.factory.TilePump");
      ModLoader.RegisterTileEntity(TileTank.class, "net.minecraft.server.buildcraft.factory.TileTank");
      ModLoader.RegisterTileEntity(TileRefinery.class, "net.minecraft.server.buildcraft.factory.Refinery");
      drillTexture = 33;
      BuildCraftCore.mainConfiguration.save();
   }
}
