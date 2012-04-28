package buildcraft.energy;

import buildcraft.api.APIProxy;
import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import java.util.Random;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_BuildCraftEnergy;

public class BlockEngine extends BlockContainer implements IPipeConnection
{
    public BlockEngine(int var1)
    {
        super(var1, Material.iron);
        this.setHardness(0.5F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
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
    public int getRenderType()
    {
        return BuildCraftCore.blockByEntityModel;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileEngine();
    }

    /**
     * Called whenever the block is removed.
     */
    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        TileEngine var5 = (TileEngine)var1.getBlockTileEntity(var2, var3, var4);

        if (var5 != null)
        {
            var5.delete();
        }

        super.onBlockRemoval(var1, var2, var3, var4);
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        TileEngine var6 = (TileEngine)var1.getBlockTileEntity(var2, var3, var4);

        if (var5.getCurrentEquippedItem() != null && var5.getCurrentEquippedItem().getItem() == BuildCraftCore.wrenchItem)
        {
            var6.switchOrientation();
            return true;
        }
        else if (var6.engine instanceof EngineStone)
        {
            if (!APIProxy.isClient(var6.worldObj))
            {
                var5.openGui(mod_BuildCraftEnergy.instance, 21, var1, var2, var3, var4);
            }

            return true;
        }
        else if (var6.engine instanceof EngineIron)
        {
            if (!APIProxy.isClient(var6.worldObj))
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
    public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5)
    {
        TileEngine var6 = (TileEngine)var1.getBlockTileEntity(var2, var3, var4);
        var6.orientation = Orientations.YPos.ordinal();
        var6.switchOrientation();
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    protected int damageDropped(int var1)
    {
        return var1;
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        TileEngine var6 = (TileEngine)var1.getBlockTileEntity(var2, var3, var4);

        if (var6.isBurning())
        {
            float var7 = (float)var2 + 0.5F;
            float var8 = (float)var3 + 0.0F + var5.nextFloat() * 6.0F / 16.0F;
            float var9 = (float)var4 + 0.5F;
            float var10 = 0.52F;
            float var11 = var5.nextFloat() * 0.6F - 0.3F;
            var1.spawnParticle("reddust", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("reddust", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("reddust", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("reddust", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
        }
    }

    public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        TileEngine var8 = (TileEngine)var1.getBlockTileEntity(var2, var3, var4);

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
            switch (BlockEngine.NamelessClass1357595130.$SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.values()[var8.orientation].ordinal()])
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

    static class NamelessClass1357595130
    {
        static final int[] $SwitchMap$net.minecraft.server$buildcraft$api$Orientations = new int[Orientations.values().length];

        static
        {
            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.YPos.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.YNeg.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.ZPos.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.ZNeg.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.XPos.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.XNeg.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
