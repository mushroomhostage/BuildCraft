package net.minecraft.server;

import buildcraft.api.*;
import buildcraft.core.*;
import buildcraft.core.network.ConnectionHandler;
import forge.MinecraftForge;
import forge.Property;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.logging.Logger;

public class BuildCraftCore
{
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

    public BuildCraftCore()
    {
    }

    public static void load()
    {
        MinecraftForge.registerConnectionHandler(new ConnectionHandler());
    }

    public static void initialize()
    {
        if (initialized)
        {
            return;
        }

        ModLoader.getLogger().fine((new StringBuilder()).append("Starting BuildCraft ").append(mod_BuildCraftCore.version()).toString());
        ModLoader.getLogger().fine("Copyright (c) SpaceToad, 2011");
        ModLoader.getLogger().fine("http://www.mod-buildcraft.com");
        System.out.println((new StringBuilder()).append("Starting BuildCraft ").append(mod_BuildCraftCore.version()).toString());
        System.out.println("Copyright (c) SpaceToad, 2011");
        System.out.println("http://www.mod-buildcraft.com");
        initialized = true;
        mainConfiguration = new BuildCraftConfiguration(new File(CoreProxy.getBuildCraftBase(), "config/buildcraft.cfg"), true);
        mainConfiguration.load();
        redLaserTexture = 2;
        blueLaserTexture = 1;
        stripesLaserTexture = 3;
        transparentTexture = 0;
        Property property = mainConfiguration.getOrCreateBooleanProperty("current.continuous", "general", DefaultProps.CURRENT_CONTINUOUS);
        property.comment = "set to true for allowing machines to be driven by continuous current";
        continuousCurrentModel = Boolean.parseBoolean(property.value);
        Property property1 = mainConfiguration.getOrCreateBooleanProperty("trackNetworkUsage", "general", false);
        trackNetworkUsage = Boolean.parseBoolean(property1.value);
        Property property2 = mainConfiguration.getOrCreateProperty("power.framework", "general", "buildcraft.energy.PneumaticPowerFramework");
        Property property3 = mainConfiguration.getOrCreateIntProperty("network.updateFactor", "general", 10);
        property3.comment = "increasing this number will decrease network update frequency, useful for overloaded servers";
        updateFactor = Integer.parseInt(property3.value);
        String s = "";

        if ((net.minecraft.server.BuildCraftCore.class).getName().startsWith("net.minecraft.server."))
        {
            s = "net.minecraft.server.";
        }

        if (forcePneumaticPower)
        {
            try
            {
                PowerFramework.currentFramework = (PowerFramework)Class.forName("buildcraft.energy.PneumaticPowerFramework").getConstructor((Class[])null).newInstance((Object[])null);
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
        else
        {
            try
            {
                String s1 = property2.value;
                if (s1.startsWith("net.minecraft.server."))
                {
                    s1 = s1.replace("net.minecraft.server.", "");
                }
                else if (s1.startsWith("net.minecraft.src."))
                {
                    s1 = s1.replace("net.minecraft.src.", "");
                }
                PowerFramework.currentFramework = (PowerFramework)Class.forName(s1).getConstructor((Class[])null).newInstance((Object[])null);
            }
            catch (Throwable throwable1)
            {
                throwable1.printStackTrace();
                PowerFramework.currentFramework = new RedstonePowerFramework();
            }
        }

        // MaeEdit start
        Property fakeplayer = mainConfiguration.getOrCreateProperty("blocks.placedby", "general", "fakeplayer");
        fakeplayer.comment = "Configures BLOCK_PLACE and BLOCK_BREAK events. Options are 'null' and 'fakeplayer'";
        buildcraft.core.FakePlayer.setMethod(fakeplayer.value);
        Property fakename = mainConfiguration.getOrCreateProperty("blocks.fakeplayername", "general", "[BuildCraft]");
        fakename.comment = "Configures the name of the fake player used when blocks.placedby=fakeplayer";
        buildcraft.core.FakePlayer.name = fakename.value;
        Property fakelogin = mainConfiguration.getOrCreateBooleanProperty("blocks.fakeplayerlogin", "general", false);
        fakelogin.comment = "Causes login and join events to be sent for the fake player. This may help some plugins, but will cause errors with others. YMMV.";
        buildcraft.core.FakePlayer.doLogin = Boolean.parseBoolean(fakelogin.value);
        // MaeEdit end

        Property property4 = mainConfiguration.getOrCreateIntProperty("wrench.id", "item", DefaultProps.WRENCH_ID);
        mainConfiguration.save();
        initializeGears();
        wrenchItem = (new ItemBuildCraftTexture(Integer.parseInt(property4.value))).d(2).a("wrenchItem");
        CoreProxy.addName(wrenchItem, "Wrench");
        API.liquids.add(new LiquidData(Block.STATIONARY_WATER.id, Item.WATER_BUCKET.id));
        API.liquids.add(new LiquidData(Block.STATIONARY_LAVA.id, Item.LAVA_BUCKET.id));
        API.softBlocks[Block.WATER.id] = true;
        API.softBlocks[Block.STATIONARY_WATER.id] = true;
        mainConfiguration.save();

        if (loadDefaultRecipes)
        {
            loadRecipes();
        }
    }

    public static void loadRecipes()
    {
        CraftingManager craftingmanager = CraftingManager.getInstance();
        craftingmanager.registerShapedRecipe(new ItemStack(wrenchItem), new Object[]
                {
                    "I I", " G ", " I ", 'I', Item.IRON_INGOT, 'G', stoneGearItem
                });
        craftingmanager.registerShapedRecipe(new ItemStack(woodenGearItem), new Object[]
                {
                    " S ", "S S", " S ", 'S', Item.STICK
                });
        craftingmanager.registerShapedRecipe(new ItemStack(stoneGearItem), new Object[]
                {
                    " I ", "IGI", " I ", 'I', Block.COBBLESTONE, 'G', woodenGearItem
                });
        craftingmanager.registerShapedRecipe(new ItemStack(ironGearItem), new Object[]
                {
                    " I ", "IGI", " I ", 'I', Item.IRON_INGOT, 'G', stoneGearItem
                });
        craftingmanager.registerShapedRecipe(new ItemStack(goldGearItem), new Object[]
                {
                    " I ", "IGI", " I ", 'I', Item.GOLD_INGOT, 'G', ironGearItem
                });
        craftingmanager.registerShapedRecipe(new ItemStack(diamondGearItem), new Object[]
                {
                    " I ", "IGI", " I ", 'I', Item.DIAMOND, 'G', goldGearItem
                });
    }

    public static void initializeGears()
    {
        if (gearsInitialized)
        {
            return;
        }
        else
        {
            Property property = mainConfiguration.getOrCreateIntProperty("woodenGearItem.id", "item", DefaultProps.WOODEN_GEAR_ID);
            Property property1 = mainConfiguration.getOrCreateIntProperty("stoneGearItem.id", "item", DefaultProps.STONE_GEAR_ID);
            Property property2 = mainConfiguration.getOrCreateIntProperty("ironGearItem.id", "item", DefaultProps.IRON_GEAR_ID);
            Property property3 = mainConfiguration.getOrCreateIntProperty("goldenGearItem.id", "item", DefaultProps.GOLDEN_GEAR_ID);
            Property property4 = mainConfiguration.getOrCreateIntProperty("diamondGearItem.id", "item", DefaultProps.DIAMOND_GEAR_ID);
            Property property5 = mainConfiguration.getOrCreateBooleanProperty("modifyWorld", "general", true);
            property5.comment = "set to false if BuildCraft should not generate custom blocks (e.g. oil)";
            mainConfiguration.save();
            modifyWorld = property5.value.equals("true");
            gearsInitialized = true;
            woodenGearItem = (new ItemBuildCraftTexture(Integer.parseInt(property.value))).d(16).a("woodenGearItem");
            CoreProxy.addName(woodenGearItem, "Wooden Gear");
            stoneGearItem = (new ItemBuildCraftTexture(Integer.parseInt(property1.value))).d(17).a("stoneGearItem");
            CoreProxy.addName(stoneGearItem, "Stone Gear");
            ironGearItem = (new ItemBuildCraftTexture(Integer.parseInt(property2.value))).d(18).a("ironGearItem");
            CoreProxy.addName(ironGearItem, "Iron Gear");
            goldGearItem = (new ItemBuildCraftTexture(Integer.parseInt(property3.value))).d(19).a("goldGearItem");
            CoreProxy.addName(goldGearItem, "Gold Gear");
            diamondGearItem = (new ItemBuildCraftTexture(Integer.parseInt(property4.value))).d(20).a("diamondGearItem");
            CoreProxy.addName(diamondGearItem, "Diamond Gear");
            mainConfiguration.save();
            return;
        }
    }

    public static void initializeModel(BaseMod basemod)
    {
        blockByEntityModel = ModLoader.getUniqueBlockModelID(basemod, true);
        pipeModel = ModLoader.getUniqueBlockModelID(basemod, true);
        markerModel = ModLoader.getUniqueBlockModelID(basemod, false);
        oilModel = ModLoader.getUniqueBlockModelID(basemod, false);
    }

    public static EntityHuman getBuildCraftPlayer(World world)
    {
        return buildcraft.core.FakePlayer.get(world);
    }

}
