package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.core.FillerPattern;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_BuildCraftBuilders;

public class BlockFiller extends BlockContainer implements ITextureProvider
{
    int textureSides;
    int textureTopOn;
    int textureTopOff;
    public FillerPattern currentPattern;

    public BlockFiller(int var1)
    {
        super(var1, Material.ORE);
        this.c(0.5F);
        this.textureSides = 66;
        this.textureTopOn = 64;
        this.textureTopOff = 65;
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5)
    {
        if (!APIProxy.isClient(var1))
        {
            var5.openGui(mod_BuildCraftBuilders.instance, 12, var1, var2, var3, var4);
        }

        return true;
    }

    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        int var6 = var1.getData(var2, var3, var4);

        if (APIProxy.getWorld() == null)
        {
            return this.a(var2, var6);
        }
        else
        {
            TileEntity var7 = APIProxy.getWorld().getTileEntity(var2, var3, var4);

            if (var7 != null && var7 instanceof TileFiller)
            {
                TileFiller var8 = (TileFiller)var7;
                return var5 != 1 && var5 != 0 ? (var8.currentPattern != null ? var8.currentPattern.getTextureIndex() : this.textureSides) : (var8.done ? this.textureTopOff : this.textureTopOn);
            }
            else
            {
                return this.a(var5, var6);
            }
        }
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int a(int var1)
    {
        return var1 != 0 && var1 != 1 ? this.textureSides : this.textureTopOn;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileFiller();
    }

    /**
     * Called whenever the block is removed.
     */
    public void remove(World var1, int var2, int var3, int var4)
    {
        Utils.preDestroyBlock(var1, var2, var3, var4);
        super.remove(var1, var2, var3, var4);
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }
}
