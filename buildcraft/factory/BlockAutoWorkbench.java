package buildcraft.factory;

import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockAutoWorkbench extends BlockContainer implements ITextureProvider
{
    int topTexture = 43;
    int sideTexture = 44;

    public BlockAutoWorkbench(int var1)
    {
        super(var1, Material.WOOD);
        this.c(1.0F);
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int a(int var1)
    {
        return var1 != 1 && var1 != 0 ? this.sideTexture : this.topTexture;
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5)
    {
        super.interact(var1, var2, var3, var4, var5);
        FactoryProxy.displayGUIAutoCrafting(var1, var5, var2, var3, var4);
        return true;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileAutoWorkbench();
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
