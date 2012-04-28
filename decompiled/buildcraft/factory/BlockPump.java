package buildcraft.factory;

import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockPump extends BlockContainer implements ITextureProvider
{
    public BlockPump(int var1)
    {
        super(var1, Material.iron);
        this.setHardness(5.0F);
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TilePump();
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int getBlockTextureFromSide(int var1)
    {
        switch (var1)
        {
            case 0:
                return 100;

            case 1:
                return 101;

            default:
                return 99;
        }
    }

    /**
     * Called whenever the block is removed.
     */
    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        Utils.preDestroyBlock(var1, var2, var3, var4);
        super.onBlockRemoval(var1, var2, var3, var4);
    }
}
