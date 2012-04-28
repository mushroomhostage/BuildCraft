package buildcraft.factory;

import buildcraft.api.IBlockPipe;
import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;

public class BlockPlainPipe extends Block implements IPipeConnection, IBlockPipe, ITextureProvider
{
    public BlockPlainPipe(int var1)
    {
        super(var1, Material.glass);
        this.blockIndexInTexture = 32;
        this.minX = 0.25D;
        this.minY = 0.0D;
        this.minZ = 0.25D;
        this.maxX = 0.75D;
        this.maxY = 1.0D;
        this.maxZ = 0.75D;
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

    public int idDropped(int var1, Random var2)
    {
        return 0;
    }

    public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        return false;
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    public float getHeightInPipe()
    {
        return 0.5F;
    }

    public void prepareTextureFor(IBlockAccess var1, int var2, int var3, int var4, Orientations var5) {}
}
