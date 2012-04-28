package buildcraft.builders;

import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftBuilders;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class BlockMarker extends BlockContainer implements ITextureProvider
{
    public BlockMarker(int var1)
    {
        super(var1, Material.circuits);
        this.blockIndexInTexture = 57;
        this.setLightValue(0.5F);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        double var6 = 0.15D;
        double var8 = 0.65D;

        switch (var5)
        {
            case 0:
                return AxisAlignedBB.getBoundingBoxFromPool((double)var2 + 0.5D - var6, (double)(var3 + 1) - var8, (double)var4 + 0.5D - var6, (double)var2 + 0.5D + var6, (double)(var3 + 1), (double)var4 + 0.5D + var6);

            case 1:
                return AxisAlignedBB.getBoundingBoxFromPool((double)var2, (double)var3 + 0.5D - var6, (double)var4 + 0.5D - var6, (double)var2 + var8, (double)var3 + 0.5D + var6, (double)var4 + 0.5D + var6);

            case 2:
            default:
                return AxisAlignedBB.getBoundingBoxFromPool((double)(var2 + 1) - var8, (double)var3 + 0.5D - var6, (double)var4 + 0.5D - var6, (double)(var2 + 1), (double)var3 + 0.5D + var6, (double)var4 + 0.5D + var6);

            case 3:
                return AxisAlignedBB.getBoundingBoxFromPool((double)var2 + 0.5D - var6, (double)var3 + 0.5D - var6, (double)var4, (double)var2 + 0.5D + var6, (double)var3 + 0.5D + var6, (double)var4 + var8);

            case 4:
                return AxisAlignedBB.getBoundingBoxFromPool((double)var2 + 0.5D - var6, (double)var3 + 0.5D - var6, (double)(var4 + 1) - var8, (double)var2 + 0.5D + var6, (double)var3 + 0.5D + var6, (double)(var4 + 1));

            case 5:
                return AxisAlignedBB.getBoundingBoxFromPool((double)var2 + 0.5D - var6, (double)var3, (double)var4 + 0.5D - var6, (double)var2 + 0.5D + var6, (double)var3 + var8, (double)var4 + 0.5D + var6);
        }
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BuildCraftCore.markerModel;
    }

    public boolean isACube()
    {
        return false;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileMarker();
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        ((TileMarker)var1.getBlockTileEntity(var2, var3, var4)).tryConnection();
        return true;
    }

    /**
     * Called whenever the block is removed.
     */
    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        Utils.preDestroyBlock(var1, var2, var3, var4);
        super.onBlockRemoval(var1, var2, var3, var4);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return Block.torchWood.isOpaqueCube();
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        ((TileMarker)var1.getBlockTileEntity(var2, var3, var4)).switchSignals();

        if (this.dropTorchIfCantStay(var1, var2, var3, var4))
        {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            boolean var7 = false;

            if (!BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4) && var6 == 1)
            {
                var7 = true;
            }

            if (!BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4) && var6 == 2)
            {
                var7 = true;
            }

            if (!BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1) && var6 == 3)
            {
                var7 = true;
            }

            if (!BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1) && var6 == 4)
            {
                var7 = true;
            }

            if (!BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4) && var6 == 5)
            {
                var7 = true;
            }

            if (!BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4) && var6 == 0)
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(var1, var2, var3, var4, BuildCraftBuilders.markerBlock.blockID, 0);
                var1.setBlockWithNotify(var2, var3, var4, 0);
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6)
    {
        return Block.torchWood.collisionRayTrace(var1, var2, var3, var4, var5, var6);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        return BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4) ? true : (BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4) ? true : (BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1) ? true : (BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1) ? true : (BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4) ? true : BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4)))));
    }

    /**
     * Called when a block is placed using an item. Used often for taking the facing and figuring out how to position
     * the item. Args: x, y, z, facing
     */
    public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5)
    {
        super.onBlockPlaced(var1, var2, var3, var4, var5);
        int var6 = var1.getBlockMetadata(var2, var3, var4);

        if (var5 == 1 && BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4))
        {
            var6 = 5;
        }

        if (var5 == 2 && BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1))
        {
            var6 = 4;
        }

        if (var5 == 3 && BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1))
        {
            var6 = 3;
        }

        if (var5 == 4 && BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4))
        {
            var6 = 2;
        }

        if (var5 == 5 && BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4))
        {
            var6 = 1;
        }

        if (var5 == 0 && BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4))
        {
            var6 = 0;
        }

        var1.setBlockMetadataWithNotify(var2, var3, var4, var6);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        super.onBlockAdded(var1, var2, var3, var4);

        if (BuildersProxy.canPlaceTorch(var1, var2 - 1, var3, var4))
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 1);
        }
        else if (BuildersProxy.canPlaceTorch(var1, var2 + 1, var3, var4))
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 2);
        }
        else if (BuildersProxy.canPlaceTorch(var1, var2, var3, var4 - 1))
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 3);
        }
        else if (BuildersProxy.canPlaceTorch(var1, var2, var3, var4 + 1))
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 4);
        }
        else if (BuildersProxy.canPlaceTorch(var1, var2, var3 - 1, var4))
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 5);
        }
        else if (BuildersProxy.canPlaceTorch(var1, var2, var3 + 1, var4))
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 0);
        }

        this.dropTorchIfCantStay(var1, var2, var3, var4);
    }

    private boolean dropTorchIfCantStay(World var1, int var2, int var3, int var4)
    {
        if (!this.canPlaceBlockAt(var1, var2, var3, var4))
        {
            this.dropBlockAsItem(var1, var2, var3, var4, BuildCraftBuilders.markerBlock.blockID, 0);
            var1.setBlockWithNotify(var2, var3, var4, 0);
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }
}
