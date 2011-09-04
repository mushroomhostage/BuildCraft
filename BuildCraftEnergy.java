package net.minecraft.server;

import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.energy.BlockEngine;
import buildcraft.energy.BlockOilFlowing;
import buildcraft.energy.BlockOilStill;
import buildcraft.energy.ItemBucketOil;
import buildcraft.energy.ItemEngine;
import buildcraft.energy.OilBucketHandler;
import buildcraft.energy.OilPopulate;
import net.minecraft.server.forge.MinecraftForge;
import net.minecraft.server.forge.Property;
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
   public static TreeMap saturationStored = new TreeMap();


   public static void ModsLoaded() {
      BuildCraftCore.initialize();
      Property var0 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("engine.id", DefaultProps.ENGINE_ID);
      Property var1 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilMoving.id", DefaultProps.OIL_MOVING_ID);
      Property var2 = BuildCraftCore.mainConfiguration.getOrCreateBlockIdProperty("oilStill.id", DefaultProps.OIL_STILL_ID);
      Property var3 = BuildCraftCore.mainConfiguration.getOrCreateIntProperty("bucketOil.id", 2, DefaultProps.BUCKET_OIL_ID);
      BuildCraftCore.mainConfiguration.save();
      CraftingManager var4 = CraftingManager.getInstance();
      engineBlock = new BlockEngine(Integer.parseInt(var0.value));
      var4.registerShapedRecipe(new ItemStack(engineBlock, 1, 0), new Object[]{"www", " g ", "GpG", Character.valueOf('w'), Block.WOOD, Character.valueOf('g'), Block.GLASS, Character.valueOf('G'), BuildCraftCore.woodenGearItem, Character.valueOf('p'), Block.PISTON});
      var4.registerShapedRecipe(new ItemStack(engineBlock, 1, 1), new Object[]{"www", " g ", "GpG", Character.valueOf('w'), Block.COBBLESTONE, Character.valueOf('g'), Block.GLASS, Character.valueOf('G'), BuildCraftCore.stoneGearItem, Character.valueOf('p'), Block.PISTON});
      var4.registerShapedRecipe(new ItemStack(engineBlock, 1, 2), new Object[]{"www", " g ", "GpG", Character.valueOf('w'), Item.IRON_INGOT, Character.valueOf('g'), Block.GLASS, Character.valueOf('G'), BuildCraftCore.ironGearItem, Character.valueOf('p'), Block.PISTON});
      ModLoader.RegisterBlock(engineBlock);
      Item.byId[engineBlock.id] = null;
      Item.byId[engineBlock.id] = new ItemEngine(engineBlock.id - 256);
      CoreProxy.addName(new ItemStack(engineBlock, 1, 0), "Redstone Engine");
      CoreProxy.addName(new ItemStack(engineBlock, 1, 1), "Steam Engine");
      CoreProxy.addName(new ItemStack(engineBlock, 1, 2), "Combustion Engine");
      oilMoving = (new BlockOilFlowing(Integer.parseInt(var1.value), Material.WATER)).c(100.0F).f(3).a("oil");
      CoreProxy.addName(oilMoving.a("oilMoving"), "Oil");
      ModLoader.RegisterBlock(oilMoving);
      oilStill = (new BlockOilStill(Integer.parseInt(var2.value), Material.WATER)).c(100.0F).f(3).a("oil");
      CoreProxy.addName(oilStill.a("oilStill"), "Oil");
      ModLoader.RegisterBlock(oilStill);
      if(oilMoving.id + 1 != oilStill.id) {
         throw new RuntimeException("Oil Still id must be Oil Moving id + 1");
      } else {
         MinecraftForge.registerCustomBucketHandler(new OilBucketHandler());
         bucketOil = (new ItemBucketOil(Integer.parseInt(var3.value))).a("bucketOil").a(Item.BUCKET);
         CoreProxy.addName(bucketOil, "Oil Bucket");
      }
   }

   public static void generateSurface(World var0, Random var1, int var2, int var3) {
      OilPopulate.doPopulate(var0, var2, var3);
   }

}
