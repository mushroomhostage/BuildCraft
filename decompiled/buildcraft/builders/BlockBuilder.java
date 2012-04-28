package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_BuildCraftBuilders;

public class BlockBuilder extends BlockContainer implements ITextureProvider
{
    int blockTextureTop = 54;
    int blockTextureSide = 53;
    int blockTextureFront = 55;

    public BlockBuilder(int var1)
    {
        super(var1, Material.iron);
        this.setHardness(0.7F);
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileBuilder();
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        if (var2 == 0 && var1 == 3)
        {
            return this.blockTextureFront;
        }
        else if (var1 == var2)
        {
            return this.blockTextureFront;
        }
        else
        {
            switch (var1)
            {
                case 1:
                    return this.blockTextureTop;

                default:
                    return this.blockTextureSide;
            }
        }
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        if (var5.getCurrentEquippedItem() != null && var5.getCurrentEquippedItem().getItem() == BuildCraftCore.wrenchItem)
        {
            int var6 = var1.getBlockMetadata(var2, var3, var4);

            switch (BlockBuilder.NamelessClass782920481.$SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.values()[var6].ordinal()])
            {
                case 1:
                    var1.setBlockMetadata(var2, var3, var4, Orientations.ZPos.ordinal());
                    break;

                case 2:
                    var1.setBlockMetadata(var2, var3, var4, Orientations.ZNeg.ordinal());
                    break;

                case 3:
                    var1.setBlockMetadata(var2, var3, var4, Orientations.XNeg.ordinal());
                    break;

                case 4:
                    var1.setBlockMetadata(var2, var3, var4, Orientations.XPos.ordinal());
            }

            var1.markBlockNeedsUpdate(var2, var3, var4);
            return true;
        }
        else
        {
            if (!APIProxy.isClient(var1))
            {
                var5.openGui(mod_BuildCraftBuilders.instance, 11, var1, var2, var3, var4);
            }

            return true;
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5)
    {
        super.onBlockPlacedBy(var1, var2, var3, var4, var5);
        Orientations var6 = Utils.get2dOrientation(new Position(var5.posX, var5.posY, var5.posZ), new Position((double)var2, (double)var3, (double)var4));
        var1.setBlockMetadataWithNotify(var2, var3, var4, var6.reverse().ordinal());
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
