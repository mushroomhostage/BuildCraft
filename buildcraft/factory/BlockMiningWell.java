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

public class BlockMiningWell extends BlockMachineRoot implements ITextureProvider, IPipeConnection
{
    int textureFront;
    int textureSides;
    int textureBack;
    int textureTop;

    public BlockMiningWell(int var1)
    {
        super(var1, Material.EARTH);
        this.c(1.5F);
        this.b(10.0F);
        this.a(h);
        this.textureFront = 35;
        this.textureSides = 37;
        this.textureBack = 38;
        this.textureTop = 36;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int a(int var1, int var2)
    {
        return var2 == 0 && var1 == 3 ? this.textureFront : (var1 == 1 ? this.textureTop : (var1 == 0 ? this.textureBack : (var1 == var2 ? this.textureFront : (var2 >= 0 && var2 < 6 && Orientations.values()[var2].reverse().ordinal() == var1 ? this.textureBack : this.textureSides))));
    }

    /**
     * Called when the block is placed in the world.
     */
    public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5)
    {
        Orientations var6 = Utils.get2dOrientation(new Position(var5.locX, var5.locY, var5.locZ), new Position((double)var2, (double)var3, (double)var4));
        var1.setData(var2, var3, var4, var6.reverse().ordinal());
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileMiningWell();
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
