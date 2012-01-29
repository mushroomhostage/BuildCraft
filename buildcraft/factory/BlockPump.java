package buildcraft.factory;

import buildcraft.core.Utils;
import buildcraft.factory.TilePump;
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
        super(var1, Material.ORE);
        this.c(5.0F);
    }

    public TileEntity a_()
    {
        return new TilePump();
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    public int a(int var1)
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

    public void remove(World var1, int var2, int var3, int var4)
    {
        Utils.preDestroyBlock(var1, var2, var3, var4);
        super.remove(var1, var2, var3, var4);
    }
}
