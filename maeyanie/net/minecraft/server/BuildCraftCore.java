package net.minecraft.server;

import buildcraft.api.API;
import buildcraft.api.LiquidData;
import buildcraft.api.PowerFramework;
import buildcraft.core.BuildCraftConfiguration;
import buildcraft.core.CoreProxy;
import buildcraft.core.DefaultProps;
import buildcraft.core.FakePlayer;
import buildcraft.core.ItemBuildCraftTexture;
import buildcraft.core.RedstonePowerFramework;
import buildcraft.core.network.ConnectionHandler;
import forge.MinecraftForge;
import forge.Property;
import java.io.File;
import java.util.TreeMap;
import net.minecraft.server.BaseMod;
import net.minecraft.server.Block;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;
import net.minecraft.server.mod_BuildCraftCore;

public class BuildCraftCore {

   private static EntityHuman buildCraftPlayer;
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
   public static int refineryInput = 0;
   public static boolean loadDefaultRecipes = true;
   public static boolean forcePneumaticPower = false;
   public static boolean consumeWaterSources = true;


   public static void load() {
      MinecraftForge.registerConnectionHandler(new ConnectionHandler());
   }

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
         Property var0 = mainConfiguration.getOrCreateBooleanProperty("current.continuous", "general", DefaultProps.CURRENT_CONTINUOUS);
         var0.comment = "set to true for allowing machines to be driven by continuous current";
         continuousCurrentModel = Boolean.parseBoolean(var0.value);
         Property var1 = mainConfiguration.getOrCreateBooleanProperty("trackNetworkUsage", "general", false);
         trackNetworkUsage = Boolean.parseBoolean(var1.value);
         Property var2 = mainConfiguration.getOrCreateProperty("power.framework", "general", "buildcraft.energy.PneumaticPowerFramework");
         Property var3 = mainConfiguration.getOrCreateIntProperty("network.updateFactor", "general", 10);
         var3.comment = "increasing this number will decrease network update frequency, useful for overloaded servers";
         updateFactor = Integer.parseInt(var3.value);
         String var4 = "";
         if(BuildCraftCore.class.getName().startsWith("net.minecraft.server.")) {
            var4 = "net.minecraft.server.";
         }

         if(forcePneumaticPower) {
            try {
               PowerFramework.currentFramework = (PowerFramework)Class.forName("buildcraft.energy.PneumaticPowerFramework").getConstructor((Class[])null).newInstance((Object[])null);
            } catch (Throwable var10) {
               var10.printStackTrace();
            }
         } else {
            try {
               String var5 = var2.value;
               if(var5.startsWith("net.minecraft.server.")) {
                  var5 = var5.replace("net.minecraft.server.", "");
               } else if(var5.startsWith("net.minecraft.src.")) {
                  var5 = var5.replace("net.minecraft.src.", "");
               }

               PowerFramework.currentFramework = (PowerFramework)Class.forName(var5).getConstructor((Class[])null).newInstance((Object[])null);
            } catch (Throwable var9) {
               var9.printStackTrace();
               PowerFramework.currentFramework = new RedstonePowerFramework();
            }
         }

         Property var11 = mainConfiguration.getOrCreateProperty("blocks.placedby", "general", "fakeplayer");
         var11.comment = "Configures BLOCK_PLACE and BLOCK_BREAK events. Options are \'null\' and \'fakeplayer\'";
         FakePlayer.setMethod(var11.value);
         Property var6 = mainConfiguration.getOrCreateProperty("blocks.fakeplayername", "general", "[BuildCraft]");
         var6.comment = "Configures the name of the fake player used when blocks.placedby=fakeplayer";
         FakePlayer.name = var6.value;
         Property var7 = mainConfiguration.getOrCreateBooleanProperty("blocks.fakeplayerlogin", "general", false);
         var7.comment = "Causes login and join events to be sent for the fake player. This may help some plugins, but will cause errors with others. YMMV.";
         FakePlayer.doLogin = Boolean.parseBoolean(var7.value);
         Property var8 = mainConfiguration.getOrCreateIntProperty("wrench.id", "item", DefaultProps.WRENCH_ID);
         mainConfiguration.save();
         initializeGears();
         wrenchItem = (new ItemBuildCraftTexture(Integer.parseInt(var8.value))).d(2).a("wrenchItem");
         CoreProxy.addName(wrenchItem, "Wrench");
         API.liquids.add(new LiquidData(Block.STATIONARY_WATER.id, Item.WATER_BUCKET.id));
         API.liquids.add(new LiquidData(Block.STATIONARY_LAVA.id, Item.LAVA_BUCKET.id));
         API.softBlocks[Block.WATER.id] = true;
         API.softBlocks[Block.STATIONARY_WATER.id] = true;
         mainConfiguration.save();
         if(loadDefaultRecipes) {
            loadRecipes();
         }

      }
   }

   public static void loadRecipes() {
      CraftingManager var0 = CraftingManager.getInstance();
      var0.registerShapedRecipe(new ItemStack(wrenchItem), new Object[]{"I I", " G ", " I ", Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('G'), stoneGearItem});
      var0.registerShapedRecipe(new ItemStack(woodenGearItem), new Object[]{" S ", "S S", " S ", Character.valueOf('S'), Item.STICK});
      var0.registerShapedRecipe(new ItemStack(stoneGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Block.COBBLESTONE, Character.valueOf('G'), woodenGearItem});
      var0.registerShapedRecipe(new ItemStack(ironGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('G'), stoneGearItem});
      var0.registerShapedRecipe(new ItemStack(goldGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.GOLD_INGOT, Character.valueOf('G'), ironGearItem});
      var0.registerShapedRecipe(new ItemStack(diamondGearItem), new Object[]{" I ", "IGI", " I ", Character.valueOf('I'), Item.DIAMOND, Character.valueOf('G'), goldGearItem});
   }

   public static void initializeGears() {
      if(!gearsInitialized) {
         Property var0 = mainConfiguration.getOrCreateIntProperty("woodenGearItem.id", "item", DefaultProps.WOODEN_GEAR_ID);
         Property var1 = mainConfiguration.getOrCreateIntProperty("stoneGearItem.id", "item", DefaultProps.STONE_GEAR_ID);
         Property var2 = mainConfiguration.getOrCreateIntProperty("ironGearItem.id", "item", DefaultProps.IRON_GEAR_ID);
         Property var3 = mainConfiguration.getOrCreateIntProperty("goldenGearItem.id", "item", DefaultProps.GOLDEN_GEAR_ID);
         Property var4 = mainConfiguration.getOrCreateIntProperty("diamondGearItem.id", "item", DefaultProps.DIAMOND_GEAR_ID);
         Property var5 = mainConfiguration.getOrCreateBooleanProperty("modifyWorld", "general", true);
         var5.comment = "set to false if BuildCraft should not generate custom blocks (e.g. oil)";
         mainConfiguration.save();
         modifyWorld = var5.value.equals("true");
         gearsInitialized = true;
         woodenGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var0.value))).d(16).a("woodenGearItem");
         CoreProxy.addName(woodenGearItem, "Wooden Gear");
         stoneGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var1.value))).d(17).a("stoneGearItem");
         CoreProxy.addName(stoneGearItem, "Stone Gear");
         ironGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var2.value))).d(18).a("ironGearItem");
         CoreProxy.addName(ironGearItem, "Iron Gear");
         goldGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var3.value))).d(19).a("goldGearItem");
         CoreProxy.addName(goldGearItem, "Gold Gear");
         diamondGearItem = (new ItemBuildCraftTexture(Integer.parseInt(var4.value))).d(20).a("diamondGearItem");
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

   public static EntityHuman getBuildCraftPlayer(World var0) {
      return FakePlayer.get(var0);
   }

}
