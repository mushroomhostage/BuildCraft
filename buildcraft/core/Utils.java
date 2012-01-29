package buildcraft.core;

import buildcraft.api.APIProxy;
import buildcraft.api.EntityPassiveItem;
import buildcraft.api.IAreaProvider;
import buildcraft.api.IPipeConnection;
import buildcraft.api.IPipeEntry;
import buildcraft.api.LaserKind;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.BlockIndex;
import buildcraft.core.EntityBlock;
import buildcraft.core.ILiquid;
import buildcraft.core.ISynchronizedTile;
import buildcraft.core.PacketIds;
import buildcraft.core.TileBuildCraft;
import java.util.LinkedList;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityItem;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.World;

public class Utils
{

    public static final float pipeMinPos = 0.25F;
    public static final float pipeMaxPos = 0.75F;
    public static float pipeNormalSpeed = 0.01F;


    public static float getPipeFloorOf(ItemStack var0)
    {
        return 0.25F;
    }

    public static Orientations get2dOrientation(Position var0, Position var1)
    {
        double var2 = var0.x - var1.x;
        double var4 = var0.z - var1.z;
        double var6 = Math.atan2(var4, var2) / 3.141592653589793D * 180.0D + 180.0D;
        return var6 >= 45.0D && var6 <= 315.0D ? (var6 < 135.0D ? Orientations.ZPos : (var6 < 225.0D ? Orientations.XNeg : Orientations.ZNeg)) : Orientations.XPos;
    }

    public static Orientations get3dOrientation(Position var0, Position var1)
    {
        double var2 = var0.x - var1.x;
        double var4 = var0.y - var1.y;
        double var6 = Math.atan2(var4, var2) / 3.141592653589793D * 180.0D + 180.0D;
        return var6 > 45.0D && var6 < 135.0D ? Orientations.YPos : (var6 > 225.0D && var6 < 315.0D ? Orientations.YNeg : get2dOrientation(var0, var1));
    }

    public static boolean addToRandomPipeEntry(TileEntity var0, Orientations var1, ItemStack var2)
    {
        World var3 = var0.world; //APIProxy.getWorld();
        LinkedList var4 = new LinkedList();

        int var5;
        Position var6;
        for (var5 = 0; var5 < 6; ++var5)
        {
            if (var1.reverse().ordinal() != var5)
            {
                var6 = new Position((double)var0.x, (double)var0.y, (double)var0.z, Orientations.values()[var5]);
                var6.moveForwards(1.0D);
                TileEntity var7 = var3.getTileEntity((int)var6.x, (int)var6.y, (int)var6.z);
                if (var7 instanceof IPipeEntry && ((IPipeEntry)var7).acceptItems())
                {
                    var4.add(Orientations.values()[var5]);
                }
            }
        }

        if (var4.size() > 0)
        {
            var5 = var3.random.nextInt(var4.size());
            var6 = new Position((double)var0.x, (double)var0.y, (double)var0.z, (Orientations)var4.get(var5));
            Position var10 = new Position((double)var0.x, (double)var0.y, (double)var0.z, (Orientations)var4.get(var5));
            var6.x += 0.5D;
            var6.y += (double)getPipeFloorOf(var2);
            var6.z += 0.5D;
            var6.moveForwards(0.5D);
            var10.moveForwards(1.0D);
            IPipeEntry var8 = (IPipeEntry)var3.getTileEntity((int)var10.x, (int)var10.y, (int)var10.z);
            EntityPassiveItem var9 = new EntityPassiveItem(var3, var6.x, var6.y, var6.z, var2);
            var8.entityEntering(var9, var6.orientation);
            var2.count = 0;
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void dropItems(World var0, ItemStack var1, int var2, int var3, int var4)
    {
        float var5 = 0.7F;
        double var6 = (double)(var0.random.nextFloat() * var5) + (double)(1.0F - var5) * 0.5D;
        double var8 = (double)(var0.random.nextFloat() * var5) + (double)(1.0F - var5) * 0.5D;
        double var10 = (double)(var0.random.nextFloat() * var5) + (double)(1.0F - var5) * 0.5D;
        EntityItem var12 = new EntityItem(var0, (double)var2 + var6, (double)var3 + var8, (double)var4 + var10, var1);
        var12.pickupDelay = 10;
        var0.addEntity(var12);
    }

    public static void dropItems(World var0, IInventory var1, int var2, int var3, int var4)
    {
        for (int var5 = 0; var5 < var1.getSize(); ++var5)
        {
            ItemStack var6 = var1.getItem(var5);
            if (var6 != null && var6.count > 0)
            {
                dropItems(var0, var1.getItem(var5).cloneItemStack(), var2, var3, var4);
                var1.setItem(var5, null);
            }
        }

    }

    public static TileEntity getTile(World var0, Position var1, Orientations var2)
    {
        Position var3 = new Position(var1);
        var3.orientation = var2;
        var3.moveForwards(1.0D);
        return var0.getTileEntity((int)var3.x, (int)var3.y, (int)var3.z);
    }

    public static IInventory getInventory(IInventory var0)
    {
        if (var0 instanceof TileEntityChest)
        {
            TileEntityChest var1 = (TileEntityChest)var0;
            Position var2 = new Position((double)var1.x, (double)var1.y, (double)var1.z);
            IInventory var4 = null;
            TileEntity var3 = getTile(var1.world, var2, Orientations.XNeg);
            if (var3 instanceof TileEntityChest)
            {
                var4 = (IInventory)var3;
            }

            var3 = getTile(var1.world, var2, Orientations.XPos);
            if (var3 instanceof TileEntityChest)
            {
                var4 = (IInventory)var3;
            }

            var3 = getTile(var1.world, var2, Orientations.ZNeg);
            if (var3 instanceof TileEntityChest)
            {
                var4 = (IInventory)var3;
            }

            var3 = getTile(var1.world, var2, Orientations.ZPos);
            if (var3 instanceof TileEntityChest)
            {
                var4 = (IInventory)var3;
            }

            if (var4 != null)
            {
                return new InventoryLargeChest("", var0, var4);
            }
        }

        return var0;
    }

    public static IAreaProvider getNearbyAreaProvider(World var0, int var1, int var2, int var3)
    {
        TileEntity var4 = var0.getTileEntity(var1 + 1, var2, var3);
        TileEntity var5 = var0.getTileEntity(var1 - 1, var2, var3);
        TileEntity var6 = var0.getTileEntity(var1, var2, var3 + 1);
        TileEntity var7 = var0.getTileEntity(var1, var2, var3 - 1);
        TileEntity var8 = var0.getTileEntity(var1, var2 + 1, var3);
        TileEntity var9 = var0.getTileEntity(var1, var2 - 1, var3);
        return var4 instanceof IAreaProvider ? (IAreaProvider)var4 : (var5 instanceof IAreaProvider ? (IAreaProvider)var5 : (var6 instanceof IAreaProvider ? (IAreaProvider)var6 : (var7 instanceof IAreaProvider ? (IAreaProvider)var7 : (var8 instanceof IAreaProvider ? (IAreaProvider)var8 : (var9 instanceof IAreaProvider ? (IAreaProvider)var9 : null)))));
    }

    public static EntityBlock createLaser(World var0, Position var1, Position var2, LaserKind var3)
    {
        if (var1.equals(var2))
        {
            return null;
        }
        else
        {
            double var4 = var2.x - var1.x;
            double var6 = var2.y - var1.y;
            double var8 = var2.z - var1.z;
            double var10 = var1.x;
            double var12 = var1.y;
            double var14 = var1.z;
            if (var4 != 0.0D)
            {
                var10 += 0.5D;
                var12 += 0.45D;
                var14 += 0.45D;
                var6 = 0.1D;
                var8 = 0.1D;
            }
            else if (var6 != 0.0D)
            {
                var10 += 0.45D;
                var12 += 0.5D;
                var14 += 0.45D;
                var4 = 0.1D;
                var8 = 0.1D;
            }
            else if (var8 != 0.0D)
            {
                var10 += 0.45D;
                var12 += 0.45D;
                var14 += 0.5D;
                var4 = 0.1D;
                var6 = 0.1D;
            }

            int var16 = BuildCraftCore.redLaserTexture;
            switch (var3)
            {
                case Blue:
                    var16 = BuildCraftCore.blueLaserTexture;
                    break;
                case Red:
                    var16 = BuildCraftCore.redLaserTexture;
                    break;
                case Stripes:
                    var16 = BuildCraftCore.stripesLaserTexture;
            }

            EntityBlock var17 = new EntityBlock(var0, var10, var12, var14, var4, var6, var8, var16);
            var0.addEntity(var17);
            return var17;
        }
    }

    public static EntityBlock[] createLaserBox(World var0, double var1, double var3, double var5, double var7, double var9, double var11, LaserKind var13)
    {
        EntityBlock[] var14 = new EntityBlock[12];
        Position[] var15 = new Position[] {new Position(var1, var3, var5), new Position(var7, var3, var5), new Position(var1, var9, var5), new Position(var7, var9, var5), new Position(var1, var3, var11), new Position(var7, var3, var11), new Position(var1, var9, var11), new Position(var7, var9, var11)};
        var14[0] = createLaser(var0, var15[0], var15[1], var13);
        var14[1] = createLaser(var0, var15[0], var15[2], var13);
        var14[2] = createLaser(var0, var15[2], var15[3], var13);
        var14[3] = createLaser(var0, var15[1], var15[3], var13);
        var14[4] = createLaser(var0, var15[4], var15[5], var13);
        var14[5] = createLaser(var0, var15[4], var15[6], var13);
        var14[6] = createLaser(var0, var15[5], var15[7], var13);
        var14[7] = createLaser(var0, var15[6], var15[7], var13);
        var14[8] = createLaser(var0, var15[0], var15[4], var13);
        var14[9] = createLaser(var0, var15[1], var15[5], var13);
        var14[10] = createLaser(var0, var15[2], var15[6], var13);
        var14[11] = createLaser(var0, var15[3], var15[7], var13);
        return var14;
    }

    public static void handleDescriptionPacket(Packet230ModLoader var0)
    {
        int var1 = var0.dataInt[0];
        int var2 = var0.dataInt[1];
        int var3 = var0.dataInt[2];
        if (APIProxy.getWorld().isLoaded(var1, var2, var3))
        {
            TileEntity var4 = APIProxy.getWorld().getTileEntity(var1, var2, var3);
            if (var4 instanceof ISynchronizedTile)
            {
                ((ISynchronizedTile)var4).handleDescriptionPacket(var0);
                ((ISynchronizedTile)var4).postPacketHandling(var0);
                return;
            }
        }

        BlockIndex var5 = new BlockIndex(var1, var2, var3);
        if (BuildCraftCore.bufferedDescriptions.containsKey(var5))
        {
            BuildCraftCore.bufferedDescriptions.remove(var5);
        }

        BuildCraftCore.bufferedDescriptions.put(var5, var0);
    }

    public static void handleUpdatePacket(Packet230ModLoader var0)
    {
        int var1 = var0.dataInt[0];
        int var2 = var0.dataInt[1];
        int var3 = var0.dataInt[2];
        if (APIProxy.getWorld().isLoaded(var1, var2, var3))
        {
            TileEntity var4 = APIProxy.getWorld().getTileEntity(var1, var2, var3);
            if (var4 instanceof ISynchronizedTile)
            {
                ((ISynchronizedTile)var4).handleUpdatePacket(var0);
                ((ISynchronizedTile)var4).postPacketHandling(var0);
                return;
            }
        }

    }

    public static void handleBufferedDescription(ISynchronizedTile var0)
    {
        TileEntity var1 = (TileEntity)var0;
        BlockIndex var2 = new BlockIndex(var1.x, var1.y, var1.z);
        if (BuildCraftCore.bufferedDescriptions.containsKey(var2))
        {
            Packet230ModLoader var3 = (Packet230ModLoader)BuildCraftCore.bufferedDescriptions.get(var2);
            BuildCraftCore.bufferedDescriptions.remove(var2);
            var0.handleDescriptionPacket(var3);
            var0.postPacketHandling(var3);
        }

    }

    public static int liquidId(int var0)
    {
        return var0 != Block.STATIONARY_WATER.id && var0 != Block.WATER.id ? (var0 != Block.STATIONARY_LAVA.id && var0 != Block.LAVA.id ? (Block.byId[var0] instanceof ILiquid ? ((ILiquid)Block.byId[var0]).stillLiquidId() : 0) : Block.STATIONARY_LAVA.id) : Block.STATIONARY_WATER.id;
    }

    public static int packetIdToInt(PacketIds var0)
    {
        switch (var0)
        {
            case DiamondPipeGUI:
                return 70;
            case AutoCraftingGUI:
                return 71;
            case FillerGUI:
                return 72;
            case TemplateGUI:
                return 73;
            case BuilderGUI:
                return 74;
            case EngineSteamGUI:
                return 75;
            case EngineCombustionGUI:
                return 76;
            default:
                throw new RuntimeException("Invalid GUI id: " + var0);
        }
    }

    public static PacketIds intToPacketId(int var0)
    {
        switch (var0)
        {
            case 70:
                return PacketIds.DiamondPipeGUI;
            case 71:
                return PacketIds.AutoCraftingGUI;
            case 72:
                return PacketIds.FillerGUI;
            case 73:
                return PacketIds.TemplateGUI;
            case 74:
                return PacketIds.BuilderGUI;
            case 75:
                return PacketIds.EngineSteamGUI;
            case 76:
                return PacketIds.EngineCombustionGUI;
            default:
                throw new RuntimeException("Invalid GUI number: " + var0);
        }
    }

    public static void preDestroyBlock(World var0, int var1, int var2, int var3)
    {
        TileEntity var4 = var0.getTileEntity(var1, var2, var3);
        if (var4 instanceof IInventory && !APIProxy.isClient(var0))
        {
            dropItems(var0, (IInventory)var4, var1, var2, var3);
        }

        if (var4 instanceof TileBuildCraft)
        {
            ((TileBuildCraft)var4).destroy();
        }

    }

    public static boolean checkPipesConnections(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5, int var6)
    {
        Block var7 = Block.byId[var0.getTypeId(var1, var2, var3)];
        Block var8 = Block.byId[var0.getTypeId(var4, var5, var6)];
        return !(var7 instanceof IPipeConnection) && !(var8 instanceof IPipeConnection) ? false : (var7 instanceof IPipeConnection && !((IPipeConnection)var7).isPipeConnected(var0, var1, var2, var3, var4, var5, var6) ? false : !(var8 instanceof IPipeConnection) || ((IPipeConnection)var8).isPipeConnected(var0, var4, var5, var6, var1, var2, var3));
    }
}
