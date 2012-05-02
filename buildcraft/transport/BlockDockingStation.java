package buildcraft.transport;

import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockDockingStation extends BlockContainer implements ITextureProvider, IPipeConnection
{
    int textureSide = 37;
    int textureTopN;
    int textureTopS;
    int textureTopW;
    int textureTopE;
    int textureBottom = 38;

    public BlockDockingStation(int var1)
    {
        super(var1, Material.ORE);
        this.textureTopN = this.textureId = 128;
        this.textureTopS = 129;
        this.textureTopE = 130;
        this.textureTopW = 131;
        this.c(0.5F);
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5)
    {
        super.postPlace(var1, var2, var3, var4, var5);
        Orientations var6 = Utils.get2dOrientation(new Position(var5.locX, var5.locY, var5.locZ), new Position((double)var2, (double)var3, (double)var4));
        var1.setData(var2, var3, var4, var6.reverse().ordinal());
        System.out.println("ORIENTATION");
        System.out.println(var6.reverse().ordinal());
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int a(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                return this.textureBottom;
            case 1:

                switch (var2)
                {
                    case 0:
                        return this.textureTopE;
                    case 1:
                    default:
                        return this.textureTopN;
                    case 2:
                        return this.textureTopS;
                    case 3:
                        return this.textureTopN;
                    case 4:
                        return this.textureTopE;
                    case 5:
                        return this.textureTopW;
                }

            default:
                return this.textureSide;
        }
    }

    public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        return true;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileDockingStation();
    }
}
