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
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;

public class BuildCraftEnergy {

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


   public static void initialize() {
      if(!initialized) {
         initialized = true;
         BuildCraftCore.initialize();
         Property var0 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("engine.id", DefaultProps.ENGINE_ID);
         Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilStill.id", DefaultProps.OIL_STILL_ID);
         Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilMoving.id", DefaultProps.OIL_MOVING_ID);
         Property var3 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("bucketOil.id", 2, DefaultProps.BUCKET_OIL_ID);
         Property var4 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("bucketFuel.id", 2, DefaultProps.BUCKET_FUEL_ID);
         Property var5 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("fuel.id", 2, DefaultProps.FUEL_ID);
         BuildCraftCore.mainConfiguration.save();
         CraftingManager var6 = CraftingManager.getInstance();

         engineBlock = new BlockEngine(Integer.parseInt(var0.value));
         ModLoader.RegisterBlock(engineBlock);
         Item.byId[engineBlock.id] = null;
         Item.byId[engineBlock.id] = new ItemEngine(engineBlock.id - Block.byId.length);
         CoreProxy.addName(new ItemStack(engineBlock, 1, 0), "Redstone Engine");
         CoreProxy.addName(new ItemStack(engineBlock, 1, 1), "Steam Engine");
         CoreProxy.addName(new ItemStack(engineBlock, 1, 2), "Combustion Engine");
         var6.registerShapedRecipe(new ItemStack(engineBlock, 1, 0), new Object[]{"www", " g ", "GpG", Character.valueOf('w'), Block.WOOD, Character.valueOf('g'), Block.GLASS, Character.valueOf('G'), BuildCraftCore.woodenGearItem, Character.valueOf('p'), Block.PISTON});
         var6.registerShapedRecipe(new ItemStack(engineBlock, 1, 1), new Object[]{"www", " g ", "GpG", Character.valueOf('w'), Block.COBBLESTONE, Character.valueOf('g'), Block.GLASS, Character.valueOf('G'), BuildCraftCore.stoneGearItem, Character.valueOf('p'), Block.PISTON});
         var6.registerShapedRecipe(new ItemStack(engineBlock, 1, 2), new Object[]{"www", " g ", "GpG", Character.valueOf('w'), Item.IRON_INGOT, Character.valueOf('g'), Block.GLASS, Character.valueOf('G'), BuildCraftCore.ironGearItem, Character.valueOf('p'), Block.PISTON});

         oilMoving = (new BlockOilFlowing(Integer.parseInt(var2.value), Material.WATER)).c(100.0F).g(3).a("oil");
         CoreProxy.addName(oilMoving.a("oilMoving"), "Oil");
         ModLoader.RegisterBlock(oilMoving);
         oilStill = (new BlockOilStill(Integer.parseInt(var1.value), Material.WATER)).c(100.0F).g(3).a("oil");
         CoreProxy.addName(oilStill.a("oilStill"), "Oil");
         ModLoader.RegisterBlock(oilStill);
         if(oilMoving.id + 1 != oilStill.id) {
            throw new RuntimeException("Oil Still id must be Oil Moving id + 1");
         } else {
            MinecraftForge.registerCustomBucketHandler(new OilBucketHandler());
            bucketOil = (new ItemBucketOil(Integer.parseInt(var3.value))).a("bucketOil").a(Item.BUCKET);
            CoreProxy.addName(bucketOil, "Oil Bucket");
            fuel = new ItemFuel(Integer.parseInt(var5.value));
            bucketFuel = (new ItemBuildCraftTexture(Integer.parseInt(var4.value))).d(3).a("bucketFuel").e(1).a(Item.BUCKET);
            CoreProxy.addName(bucketFuel, "Fuel Bucket");
            TileRefinery.addRecipe(new RefineryRecipe(oilStill.id, 1, 0, 0, 10, fuel.id, 1, 1));
            API.ironEngineFuel.put(Integer.valueOf(Block.STATIONARY_LAVA.id), new IronEngineFuel(oilStill.id, 1, 20000));
            API.ironEngineFuel.put(Integer.valueOf(oilStill.id), new IronEngineFuel(oilStill.id, 2, 10000));
            API.ironEngineFuel.put(Integer.valueOf(fuel.id), new IronEngineFuel(fuel.id, 5, '\uc350'));
            API.liquids.add(new LiquidData(oilStill.id, bucketOil.id));
            API.liquids.add(new LiquidData(fuel.id, bucketFuel.id));
            API.softBlocks[oilMoving.id] = true;
            API.softBlocks[oilStill.id] = true;
         }
      }
   }

   public static void generateSurface(World var0, Random var1, int var2, int var3) {
      OilPopulate.doPopulate(var0, var2, var3);
   }

}
