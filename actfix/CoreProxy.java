// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CoreProxy.java

package buildcraft.core;

import forge.DimensionManager;
import forge.NetworkMod;
import java.io.File;
import java.util.List;
import net.minecraft.server.*;

public class CoreProxy
{

    public CoreProxy()
    {
    }

    public static void addName(Object obj, String s)
    {
    }

    public static void setField804(EntityItem entityitem, float f)
    {
        entityitem.d = f;
    }

    public static void onCraftingPickup(World world, EntityHuman entityhuman, ItemStack itemstack)
    {
        itemstack.a(world, entityhuman, itemstack.count);
    }

    public static File getPropertyFile()
    {
        return new File("BuildCraft.cfg");
    }

    public static void sendToPlayers(Packet packet, int i, int j, int k, int l, NetworkMod networkmod)
    {
        if(packet != null)
        {
            for(int i1 = 0; i1 < DimensionManager.getWorlds().length; i1++)
            {
                for(int j1 = 0; j1 < DimensionManager.getWorlds()[i1].players.size(); j1++)
                {
                    EntityPlayer entityplayer = (EntityPlayer)DimensionManager.getWorlds()[i1].players.get(j1);
                    if(Math.abs(entityplayer.locX - (double)i) <= (double)l && Math.abs(entityplayer.locY - (double)j) <= (double)l && Math.abs(entityplayer.locZ - (double)k) <= (double)l)
                        entityplayer.netServerHandler.sendPacket(packet);
                }

            }

        }
    }

    public static boolean isPlainBlock(Block block)
    {
        return block.b();
    }

    public static File getBuildCraftBase()
    {
        return new File("buildcraft/");
    }

    public static void addLocalization(String s, String s1)
    {
    }

    public static int addFuel(int i, int j)
    {
        return ModLoader.addAllFuel(i, j);
    }

    public static int addCustomTexture(String s)
    {
        return 0;
    }

    public static long getHash(IBlockAccess iblockaccess)
    {
        return 0L;
    }

    public static void TakenFromCrafting(EntityHuman entityhuman, ItemStack itemstack, IInventory iinventory)
    {
        ModLoader.takenFromCrafting(entityhuman, itemstack, iinventory);
    }
}
