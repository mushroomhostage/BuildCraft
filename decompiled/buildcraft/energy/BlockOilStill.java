package buildcraft.energy;

import buildcraft.core.ILiquid;
import forge.ITextureProvider;
import net.minecraft.server.BlockStationary;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockOilStill extends BlockStationary implements ITextureProvider, ILiquid
{
    public BlockOilStill(int var1, Material var2)
    {
        super(var1, var2);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BuildCraftCore.oilModel;
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    public int stillLiquidId()
    {
        return BuildCraftEnergy.oilStill.blockID;
    }

    public boolean isBlockReplaceable(World var1, int var2, int var3, int var4)
    {
        return true;
    }
}
