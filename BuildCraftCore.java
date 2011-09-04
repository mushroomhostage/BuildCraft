package net.minecraft.server;

import buildcraft.api.PowerFramework;
import buildcraft.core.BuildCraftConfiguration;
import buildcraft.core.BuildCraftItem;
import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.core.RedstonePowerFramework;
import net.minecraft.server.forge.Property;
import java.io.File;
import java.util.TreeMap;
import net.minecraft.server.BaseMod;
import net.minecraft.server.Block;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;

public class BuildCraftCore {

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
   public static PowerFramework powerFramework;
   public static final int OIL_BUCKET_QUANTITY = 1000;


   public static void initialize() {
      if(!initialized) {
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
         Property var1 = mainConfiguration.getOrCreateProperty("power.framework", 0, "buildcraft.energy.PneumaticPowerFramework");

         try {
            powerFramework = (PowerFramework)Class.forName(var1.value).getConstructor((Class[])null).newInstance((Object[])null);
         } catch (Throwable var4) {
            var4.printStackTrace();
            powerFramework = new RedstonePowerFramework();
         }

         Property var2 = mainConfiguration.getOrCreateIntProperty("wrench.id", 2, DefaultProps.WRENCH_ID);
         mainConfiguration.save();
         initializeGears();
         CraftingManager var3 = CraftingManager.getInstance();
         wrenchItem = (new BuildCraftItem(Integer.parseInt(var2.value))).b(2).a("wrenchItem");
         var3.registerShapedRecipe(new ItemStack(wrenchItem), new Object[]{"I I", " G ", " I ", Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('G'), stoneGearItem});
         CoreProxy.addName(wrenchItem, "Wrench");
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
         mainConfiguration.save();
         gearsInitialized = true;
         CraftingManager var5 = CraftingManager.getInstance();
         woodenGearItem = (new BuildCraftItem(Integer.parseInt(var0.value))).b(16).a("woodenGearItem");
         var5.registerShapedRecipe(new ItemStack(woodenGearItem), new Object[]{" S ", "S S", " S ", Character.valueOf('S'), Item.STICK});
         CoreProxy.addName(woodenGearItem, "Wooden Gear");
         stoneGearItem = (new BuildCraftItem(Integer.parseInt(var1.value))).b(17).a("stoneGearItem");
         var5.registerShapedRecipe(new ItemStack(stoneGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Block.COBBLESTONE, Character.valueOf('G'), woodenGearItem});
         CoreProxy.addName(stoneGearItem, "Stone Gear");
         ironGearItem = (new BuildCraftItem(Integer.parseInt(var2.value))).b(18).a("ironGearItem");
         var5.registerShapedRecipe(new ItemStack(ironGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('G'), stoneGearItem});
         CoreProxy.addName(ironGearItem, "Iron Gear");
         goldGearItem = (new BuildCraftItem(Integer.parseInt(var3.value))).b(19).a("goldGearItem");
         var5.registerShapedRecipe(new ItemStack(goldGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.GOLD_INGOT, Character.valueOf('G'), ironGearItem});
         CoreProxy.addName(goldGearItem, "Gold Gear");
         diamondGearItem = (new BuildCraftItem(Integer.parseInt(var4.value))).b(20).a("diamondGearItem");
         var5.registerShapedRecipe(new ItemStack(diamondGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.DIAMOND, Character.valueOf('G'), goldGearItem});
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
