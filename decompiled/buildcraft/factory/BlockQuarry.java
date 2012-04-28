package buildcraft.factory;

import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockQuarry extends BlockMachineRoot implements ITextureProvider, IPipeConnection
{
    int textureTop;
    int textureFront;
    int textureSide;

    public BlockQuarry(int var1)
    {
        super(var1, Material.iron);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setStepSound(soundStoneFootstep);
        this.textureSide = 41;
        this.textureFront = 39;
        this.textureTop = 40;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5)
    {
        super.onBlockPlacedBy(var1, var2, var3, var4, var5);
        Orientations var6 = Utils.get2dOrientation(new Position(var5.posX, var5.posY, var5.posZ), new Position((double)var2, (double)var3, (double)var4));
        var1.setBlockMetadataWithNotify(var2, var3, var4, var6.reverse().ordinal());
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        if (var2 == 0 && var1 == 3)
        {
            return this.textureFront;
        }
        else if (var1 == var2)
        {
            return this.textureFront;
        }
        else
        {
            switch (var1)
            {
                case 1:
                    return this.textureTop;

                default:
                    return this.textureSide;
            }
        }
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileQuarry();
    }

    /**
     * Called whenever the block is removed.
     */
    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        Utils.preDestroyBlock(var1, var2, var3, var4);
        super.onBlockRemoval(var1, var2, var3, var4);
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        return true;
    }
}
