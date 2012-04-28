package buildcraft.energy;

import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;

public class BlockPollution extends BlockContainer implements ITextureProvider
{
    public BlockPollution(int var1)
    {
        super(var1, Material.air);
        this.blockIndexInTexture = 80;
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
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
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TilePollution();
    }

    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return 80 + var1.getBlockMetadata(var2, var3, var4);
    }
}
