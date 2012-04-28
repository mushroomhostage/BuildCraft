package buildcraft.factory;

import buildcraft.api.APIProxy;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_BuildCraftFactory;

public class BlockAutoWorkbench extends BlockContainer implements ITextureProvider
{
    int topTexture = 43;
    int sideTexture = 44;

    public BlockAutoWorkbench(int var1)
    {
        super(var1, Material.wood);
        this.setHardness(1.0F);
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int getBlockTextureFromSide(int var1)
    {
        return var1 != 1 && var1 != 0 ? this.sideTexture : this.topTexture;
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        super.blockActivated(var1, var2, var3, var4, var5);

        if (!APIProxy.isClient(var1))
        {
            var5.openGui(mod_BuildCraftFactory.instance, 30, var1, var2, var3, var4);
        }

        return true;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileAutoWorkbench();
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
}
