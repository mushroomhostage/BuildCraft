package net.minecraft.server;

import buildcraft.api.API;
import buildcraft.api.IronEngineFuel;
import buildcraft.api.LiquidData;
import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.core.ItemBuildCraftTexture;
import buildcraft.energy.BlockEngine;
import buildcraft.energy.BlockOilFlowing;
import buildcraft.energy.BlockOilStill;
import buildcraft.energy.GuiHandler;
import buildcraft.energy.ItemBucketOil;
import buildcraft.energy.ItemEngine;
import buildcraft.energy.ItemFuel;
import buildcraft.energy.OilBucketHandler;
import buildcraft.energy.OilPopulate;
import buildcraft.factory.RefineryRecipe;
import buildcraft.factory.TileRefinery;
import forge.MinecraftForge;
import forge.Property;
import java.util.Random;
import java.util.TreeMap;

public class BuildCraftEnergy
{
    public static final int ENERGY_REMOVE_BLOCK = 25;
    public static final int ENERGY_EXTRACT_ITEM = 2;
    public static BlockEngine engineBlock;
    public static Block oilMoving;
    public static Block oilStill;
    public static Item bucketOil;
    public static Item bucketFuel;
    public static Item fuel;
    public static TreeMap saturationStored = new TreeMap();
    private static boolean initialized = false;

    public static void load()
    {
        MinecraftForge.setGuiHandler(mod_BuildCraftEnergy.instance, new GuiHandler());
    }

    public static void initialize()
    {
        if (!initialized)
        {
            initialized = true;
            BuildCraftCore.initialize();
            Property var0 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("engine.id", DefaultProps.ENGINE_ID);
            Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilStill.id", DefaultProps.OIL_STILL_ID);
            Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilMoving.id", DefaultProps.OIL_MOVING_ID);
            Property var3 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("bucketOil.id", "item", DefaultProps.BUCKET_OIL_ID);
            Property var4 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("bucketFuel.id", "item", DefaultProps.BUCKET_FUEL_ID);
            Property var5 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("fuel.id", "item", DefaultProps.FUEL_ID);
            BuildCraftCore.mainConfiguration.save();
            engineBlock = new BlockEngine(Integer.parseInt(var0.value));
            ModLoader.registerBlock(engineBlock);
            Item.byId[engineBlock.id] = null;
            Item.byId[engineBlock.id] = new ItemEngine(engineBlock.id - 256);
            CoreProxy.addName(new ItemStack(engineBlock, 1, 0), "Redstone Engine");
            CoreProxy.addName(new ItemStack(engineBlock, 1, 1), "Steam Engine");
            CoreProxy.addName(new ItemStack(engineBlock, 1, 2), "Combustion Engine");
            oilMoving = (new BlockOilFlowing(Integer.parseInt(var2.value), Material.WATER)).c(100.0F).f(3).a("oil");
            CoreProxy.addName(oilMoving.a("oilMoving"), "Oil");
            ModLoader.registerBlock(oilMoving);
            oilStill = (new BlockOilStill(Integer.parseInt(var1.value), Material.WATER)).c(100.0F).f(3).a("oil");
            CoreProxy.addName(oilStill.a("oilStill"), "Oil");
            ModLoader.registerBlock(oilStill);

            if (oilMoving.id + 1 != oilStill.id)
            {
                throw new RuntimeException("Oil Still id must be Oil Moving id + 1");
            }
            else
            {
                MinecraftForge.registerCustomBucketHandler(new OilBucketHandler());
                bucketOil = (new ItemBucketOil(Integer.parseInt(var3.value))).a("bucketOil").a(Item.BUCKET);
                CoreProxy.addName(bucketOil, "Oil Bucket");
                fuel = new ItemFuel(Integer.parseInt(var5.value));
                bucketFuel = (new ItemBuildCraftTexture(Integer.parseInt(var4.value))).d(3).a("bucketFuel").e(1).a(Item.BUCKET);
                CoreProxy.addName(bucketFuel, "Fuel Bucket");
                TileRefinery.addRecipe(new RefineryRecipe(oilStill.id, 1, 0, 0, 10, fuel.id, 1, 1));
                API.ironEngineFuel.put(Integer.valueOf(Block.STATIONARY_LAVA.id), new IronEngineFuel(oilStill.id, 1, 20000));
                API.ironEngineFuel.put(Integer.valueOf(oilStill.id), new IronEngineFuel(oilStill.id, 2, 10000));
                API.ironEngineFuel.put(Integer.valueOf(fuel.id), new IronEngineFuel(fuel.id, 5, 50000));
                API.liquids.add(new LiquidData(oilStill.id, bucketOil.id));
                API.liquids.add(new LiquidData(fuel.id, bucketFuel.id));
                API.softBlocks[oilMoving.id] = true;
                API.softBlocks[oilStill.id] = true;
                BuildCraftCore.refineryInput = oilStill.id;

                if (BuildCraftCore.loadDefaultRecipes)
                {
                    loadRecipes();
                }
            }
        }
    }

    public static void loadRecipes()
    {
        CraftingManager var0 = CraftingManager.getInstance();
        var0.registerShapedRecipe(new ItemStack(engineBlock, 1, 0), new Object[] {"www", " g ", "GpG", 'w', Block.WOOD, 'g', Block.GLASS, 'G', BuildCraftCore.woodenGearItem, 'p', Block.PISTON});
        var0.registerShapedRecipe(new ItemStack(engineBlock, 1, 1), new Object[] {"www", " g ", "GpG", 'w', Block.COBBLESTONE, 'g', Block.GLASS, 'G', BuildCraftCore.stoneGearItem, 'p', Block.PISTON});
        var0.registerShapedRecipe(new ItemStack(engineBlock, 1, 2), new Object[] {"www", " g ", "GpG", 'w', Item.IRON_INGOT, 'g', Block.GLASS, 'G', BuildCraftCore.ironGearItem, 'p', Block.PISTON});
    }

    public static void generateSurface(World var0, Random var1, int var2, int var3)
    {
        OilPopulate.doPopulate(var0, var2, var3);
    }
}
