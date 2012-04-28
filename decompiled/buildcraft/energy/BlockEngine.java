package buildcraft.energy;

import buildcraft.api.APIProxy;
import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import java.util.Random;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_BuildCraftEnergy;

public class BlockEngine extends BlockContainer implements IPipeConnection
{
    public BlockEngine(int var1)
    {
        super(var1, Material.ORE);
        this.c(0.5F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean a()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean b()
    {
        return false;
    }

    public boolean isACube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int c()
    {
        return BuildCraftCore.blockByEntityModel;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileEngine();
    }

    /**
     * Called whenever the block is removed.
     */
    public void remove(World var1, int var2, int var3, int var4)
    {
        TileEngine var5 = (TileEngine)var1.getTileEntity(var2, var3, var4);

        if (var5 != null)
        {
            var5.delete();
        }

        super.remove(var1, var2, var3, var4);
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5)
    {
        TileEngine var6 = (TileEngine)var1.getTileEntity(var2, var3, var4);

        if (var5.U() != null && var5.U().getItem() == BuildCraftCore.wrenchItem)
        {
            var6.switchOrientation();
            return true;
        }
        else if (var6.engine instanceof EngineStone)
        {
            if (!APIProxy.isClient(var6.world))
            {
                var5.openGui(mod_BuildCraftEnergy.instance, 21, var1, var2, var3, var4);
            }

            return true;
        }
        else if (var6.engine instanceof EngineIron)
        {
            if (!APIProxy.isClient(var6.world))
            {
                var5.openGui(mod_BuildCraftEnergy.instance, 20, var1, var2, var3, var4);
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called when a block is placed using an item. Used often for taking the facing and figuring out how to position
     * the item. Args: x, y, z, facing
     */
    public void postPlace(World var1, int var2, int var3, int var4, int var5)
    {
        TileEngine var6 = (TileEngine)var1.getTileEntity(var2, var3, var4);
        var6.orientation = Orientations.YPos.ordinal();
        var6.switchOrientation();
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    protected int getDropData(int var1)
    {
        return var1;
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        TileEngine var6 = (TileEngine)var1.getTileEntity(var2, var3, var4);

        if (var6.isBurning())
        {
            float var7 = (float)var2 + 0.5F;
            float var8 = (float)var3 + 0.0F + var5.nextFloat() * 6.0F / 16.0F;
            float var9 = (float)var4 + 0.5F;
            float var10 = 0.52F;
            float var11 = var5.nextFloat() * 0.6F - 0.3F;
            var1.a("reddust", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            var1.a("reddust", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            var1.a("reddust", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
            var1.a("reddust", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
        }
    }

    public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        TileEngine var8 = (TileEngine)var1.getTileEntity(var2, var3, var4);

        if (var8 == null)
        {
            return false;
        }
        else if (var8.engine instanceof EngineWood)
        {
            return false;
        }
        else
        {
            switch (Orientations.values()[var8.orientation].ordinal())
            {
                case 1:
                    return var3 - var6 != -1;

                case 2:
                    return var3 - var6 != 1;

                case 3:
                    return var4 - var7 != -1;

                case 4:
                    return var4 - var7 != 1;

                case 5:
                    return var2 - var5 != -1;

                case 6:
                    return var2 - var5 != 1;

                default:
                    return true;
            }
        }
    }

}
