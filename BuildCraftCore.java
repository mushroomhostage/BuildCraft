package net.minecraft.server;

import buildcraft.api.API;
import buildcraft.api.LiquidData;
import buildcraft.api.PowerFramework;
import buildcraft.core.BuildCraftConfiguration;
import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.core.ItemBuildCraftTexture;
import buildcraft.core.RedstonePowerFramework;
import forge.Property;
import java.io.File;
import java.util.TreeMap;
import net.minecraft.server.BaseMod;
import net.minecraft.server.Block;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.mod_BuildCraftCore;

public class BuildCraftCore {

   public static boolean debugMode = false;
   public static boolean modifyWorld = false;
   public static boolean trackNetworkUsage = false;
   public static int updateFactor = 10;
   public static BuildCraftConfiguration mainConfiguration;
   public static TreeMap bufferedDescriptions = new TreeMap();
   public static final int trackedPassiveEntityId = 156;
   public static boolean continuousCurrentModel;
   private static boolean initialized = false;
   private static boolean gearsInitialized = false;
   public static Item woodenGearItem;
   public static Item stoneGearItem;
   public static Item ironGearItem;
   public static Item goldGearItem;
   public static Item diamondGearItem;
   public static Item wrenchItem;
   public static int redLaserTexture;
   public static int blueLaserTexture;
   public static int stripesLaserTexture;
   public static int transparentTexture;
   public static int blockByEntityModel;
   public static int pipeModel;
   public static int markerModel;
   public static int oilModel;
   public static String customBuildCraftTexture = "/net/minecraft/src/buildcraft/core/gui/block_textures.png";
   public static String customBuildCraftSprites = "/net/minecraft/src/buildcraft/core/gui/item_textures.png";


   public static void initialize() {
      if(!initialized) {
         ModLoader.getLogger().fine("Starting BuildCraft " + mod_BuildCraftCore.version());
         ModLoader.getLogger().fine("Copyright (c) SpaceToad, 2011");
         ModLoader.getLogger().fine("http://www.mod-buildcraft.com");
         System.out.println("Starting BuildCraft " + mod_BuildCraftCore.version());
         System.out.println("Copyright (c) SpaceToad, 2011");
         System.out.println("http://www.mod-buildcraft.com");
         initialized = true;
         mainConfiguration = new BuildCraftConfiguration(new File(CoreProxy.getBuildCraftBase(), "config/buildcraft.cfg"), true);
         mainConfiguration.load();
         redLaserTexture = 2;
         blueLaserTexture = 1;
         stripesLaserTexture = 3;
         transparentTexture = 0;
         Property var0 = mainConfiguration.getOrCreateBooleanProperty("current.continuous", 0, DefaultProps.CURRENT_CONTINUOUS);
         var0.comment = "set to true for allowing machines to be driven by continuous current";
         continuousCurrentModel = Boolean.parseBoolean(var0.value);
         Property var1 = mainConfiguration.getOrCreateBooleanProperty("trackNetworkUsage", 0, false);
         trackNetworkUsage = Boolean.parseBoolean(var1.value);
         Property var2 = mainConfiguration.getOrCreateProperty("power.framework", 0, "buildcraft.energy.PneumaticPowerFramework");
         Property var3 = mainConfiguration.getOrCreateIntProperty("network.updateFactor", 0, 10);
         var0.comment = "increasing this number will decrease network update frequency, useful for overloaded servers";
         updateFactor = Integer.parseInt(var3.value);

         try {
            PowerFramework.currentFramework = (PowerFramework)Class.forName(var2.value).getConstructor((Class[])null).newInstance((Object[])null);
         } catch (Throwable var6) {
            var6.printStackTrace();
            PowerFramework.currentFramework = new RedstonePowerFramework();
         }

         Property var4 = mainConfiguration.getOrCreateIntProperty("wrench.id", 2, DefaultProps.WRENCH_ID);
         mainConfiguration.save();
         initializeGears();
         CraftingManager var5 = CraftingManager.getInstance();
         wrenchItem = (new ItemBuildCraftTexture(Integer.parseInt(var4.value))).b(2).a("wrenchItem");
         var5.registerShapedRecipe(new ItemStack(wrenchItem), new Object[]{"I I", " G ", " I ", Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('G'), stoneGearItem});
         CoreProxy.addName(wrenchItem, "Wrench");
         API.liquids.add(new LiquidData(Block.STATIONARY_WATER.id, Item.WATER_BUCKET.id));
         API.liquids.add(new LiquidData(Block.STATIONARY_LAVA.id, Item.LAVA_BUCKET.id));
         API.liquids.add(new LiquidData(Block.STATIONARY_WATER.id, Block.STATIONARY_WATER.id));
         API.liquids.add(new LiquidData(Block.STATIONARY_LAVA.id, Block.STATIONARY_LAVA.id));
         mainConfiguration.save();
      }
   }

   public static void initializeGears() {
      if(!gearsInitialized) {
         Property var0 = mainConfiguration.getOrCreateIntProperty("woodenGearItem.id", 2, DefaultProps.WOODEN_GEAR_ID);
         Property var1 = mainConfiguration.getOrCreateIntProperty("stoneGearItem.id", 2, DefaultProps.STONE_GEAR_ID);
         Property var2 = mainConfiguration.getOrCreateIntProperty("ironGearItem.id", 2, DefaultProps.IRON_GEAR_ID);
         Property var3 = mainConfiguration.getOrCreateIntProperty("goldenGearItem.id", 2, DefaultProps.GOLDEN_GEAR_ID);
         Property var4 = mainConfiguration.getOrCreateIntProperty("diamondGearItem.id", 2, DefaultProps.DIAMOND_GEAR_ID);
         Property var5 = mainConfiguration.getOrCreateBooleanProperty("modifyWorld", 0, true);
         var5.comment = "set to false if BuildCraft should not generate custom blocks (e.g. oil)";
         mainConfiguration.save();
         modifyWorld = var5.value.equals("true");
         gearsInitialized = true;
         CraftingManager var6 = CraftingManager.getInstance();
         woodenGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var0.value))).b(16).a("woodenGearItem");
         var6.registerShapedRecipe(new ItemStack(woodenGearItem), new Object[]{" S ", "S S", " S ", Character.valueOf('S'), Item.STICK});
         CoreProxy.addName(woodenGearItem, "Wooden Gear");
         stoneGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var1.value))).b(17).a("stoneGearItem");
         var6.registerShapedRecipe(new ItemStack(stoneGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Block.COBBLESTONE, Character.valueOf('G'), woodenGearItem});
         CoreProxy.addName(stoneGearItem, "Stone Gear");
         ironGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var2.value))).b(18).a("ironGearItem");
         var6.registerShapedRecipe(new ItemStack(ironGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('G'), stoneGearItem});
         CoreProxy.addName(ironGearItem, "Iron Gear");
         goldGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var3.value))).b(19).a("goldGearItem");
         var6.registerShapedRecipe(new ItemStack(goldGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.GOLD_INGOT, Character.valueOf('G'), ironGearItem});
         CoreProxy.addName(goldGearItem, "Gold Gear");
         diamondGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var4.value))).b(20).a("diamondGearItem");
         var6.registerShapedRecipe(new ItemStack(diamondGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.DIAMOND, Character.valueOf('G'), goldGearItem});
         CoreProxy.addName(diamondGearItem, "Diamond Gear");
         mainConfiguration.save();
      }
   }

   public static void initializeModel(BaseMod var0) {
      blockByEntityModel = ModLoader.getUniqueBlockModelID(var0, true);
      pipeModel = ModLoader.getUniqueBlockModelID(var0, true);
      markerModel = ModLoader.getUniqueBlockModelID(var0, false);
      oilModel = ModLoader.getUniqueBlockModelID(var0, false);
   }

}
