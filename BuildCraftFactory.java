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

public class BuildCraftFactory
{
    public static BlockQuarry quarryBlock;
    public static BlockMiningWell miningWellBlock;
    public static BlockAutoWorkbench autoWorkbenchBlock;
    public static BlockFrame frameBlock;
    public static BlockPlainPipe plainPipeBlock;
    public static BlockPump pumpBlock;
    public static BlockTank tankBlock;
    public static BlockRefinery refineryBlock;
    public static int drillTexture;
    private static boolean initialized = false;
    public static boolean allowMining = true;


    public static void initialize()
    {
        if (!initialized)
        {
            initialized = true;
            mod_BuildCraftCore.initialize();
            BuildCraftCore.initializeGears();
            allowMining = Boolean.parseBoolean(BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("mining.enabled", 0, true).value);
            Property var0 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("miningWell.id", DefaultProps.MINING_WELL_ID);
            Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("drill.id", DefaultProps.DRILL_ID);
            Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("autoWorkbench.id", DefaultProps.AUTO_WORKBENCH_ID);
            Property var3 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("frame.id", DefaultProps.FRAME_ID);
            Property var4 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("quarry.id", DefaultProps.QUARRY_ID);
            Property var5 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("pump.id", DefaultProps.PUMP_ID);
            Property var6 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("tank.id", DefaultProps.TANK_ID);
            Property var7 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("refinery.id", DefaultProps.REFINERY_ID);
            BuildCraftCore.mainConfiguration.save();
            MinecraftForge.registerCustomBucketHandler(new TankBucketHandler());
            miningWellBlock = new BlockMiningWell(Integer.parseInt(var0.value));
            ModLoader.registerBlock(miningWellBlock);
            CoreProxy.addName(miningWellBlock.a("miningWellBlock"), "Mining Well");
            plainPipeBlock = new BlockPlainPipe(Integer.parseInt(var1.value));
            ModLoader.registerBlock(plainPipeBlock);
            CoreProxy.addName(plainPipeBlock.a("plainPipeBlock"), "Mining Pipe");
            autoWorkbenchBlock = new BlockAutoWorkbench(Integer.parseInt(var2.value));
            ModLoader.registerBlock(autoWorkbenchBlock);
            CoreProxy.addName(autoWorkbenchBlock.a("autoWorkbenchBlock"), "Automatic Crafting Table");
            frameBlock = new BlockFrame(Integer.parseInt(var3.value));
            ModLoader.registerBlock(frameBlock);
            CoreProxy.addName(frameBlock.a("frameBlock"), "Frame");
            quarryBlock = new BlockQuarry(Integer.parseInt(var4.value));
            ModLoader.registerBlock(quarryBlock);
            CoreProxy.addName(quarryBlock.a("machineBlock"), "Quarry");
            tankBlock = new BlockTank(Integer.parseInt(var6.value));
            ModLoader.registerBlock(tankBlock);
            CoreProxy.addName(tankBlock.a("tankBlock"), "Tank");
            pumpBlock = new BlockPump(Integer.parseInt(var5.value));
            ModLoader.registerBlock(pumpBlock);
            CoreProxy.addName(pumpBlock.a("pumpBlock"), "Pump");
            refineryBlock = new BlockRefinery(Integer.parseInt(var7.value));
            ModLoader.registerBlock(refineryBlock);
            CoreProxy.addName(refineryBlock.a("refineryBlock"), "Refinery");
            ModLoader.registerTileEntity(TileQuarry.class, "Machine");
            ModLoader.registerTileEntity(TileMiningWell.class, "MiningWell");
            ModLoader.registerTileEntity(TileAutoWorkbench.class, "AutoWorkbench");
            ModLoader.registerTileEntity(TilePump.class, "net.minecraft.server.buildcraft.factory.TilePump");
            ModLoader.registerTileEntity(TilePump.class, "net.minecraft.src.buildcraft.factory.TilePump");
            ModLoader.registerTileEntity(TileTank.class, "net.minecraft.server.buildcraft.factory.TileTank");
            ModLoader.registerTileEntity(TileTank.class, "net.minecraft.src.buildcraft.factory.TileTank");
            ModLoader.registerTileEntity(TileRefinery.class, "net.minecraft.server.buildcraft.factory.Refinery");
            ModLoader.registerTileEntity(TileRefinery.class, "net.minecraft.src.buildcraft.factory.Refinery");
            drillTexture = 33;
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

        if (allowMining)
        {
            var0.registerShapedRecipe(new ItemStack(miningWellBlock, 1), new Object[] {"ipi", "igi", "iPi", 'p', Item.REDSTONE, 'i', Item.IRON_INGOT, 'g', BuildCraftCore.ironGearItem, 'P', Item.IRON_PICKAXE});
            var0.registerShapedRecipe(new ItemStack(quarryBlock), new Object[] {"ipi", "gig", "dDd", 'i', BuildCraftCore.ironGearItem, 'p', Item.REDSTONE, 'g', BuildCraftCore.goldGearItem, 'd', BuildCraftCore.diamondGearItem, 'D', Item.DIAMOND_PICKAXE});
        }

        var0.registerShapedRecipe(new ItemStack(autoWorkbenchBlock), new Object[] {" g ", "gwg", " g ", 'w', Block.WORKBENCH, 'g', BuildCraftCore.woodenGearItem});
        var0.registerShapedRecipe(new ItemStack(pumpBlock), new Object[] {"T ", "W ", 'T', tankBlock, 'W', miningWellBlock});
        var0.registerShapedRecipe(new ItemStack(tankBlock), new Object[] {"ggg", "g g", "ggg", 'g', Block.GLASS});
        var0.registerShapedRecipe(new ItemStack(refineryBlock), new Object[] {"   ", "RTR", "TGT", 'T', tankBlock, 'G', BuildCraftCore.diamondGearItem, 'R', Block.REDSTONE_TORCH_ON});
    }
}
