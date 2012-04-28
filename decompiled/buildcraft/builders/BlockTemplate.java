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
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.mod_BuildCraftBuilders;

public class BlockTemplate extends BlockContainer implements ITextureProvider
{
    int blockTextureSides;
    int blockTextureFront;
    int blockTextureTopPos;
    int blockTextureTopNeg;

    public BlockTemplate(int var1)
    {
        super(var1, Material.iron);
        this.setHardness(0.5F);
        this.blockTextureSides = 48;
        this.blockTextureTopNeg = 49;
        this.blockTextureTopPos = 50;
        this.blockTextureFront = 52;
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileTemplate();
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        if (var5.getCurrentEquippedItem() != null && var5.getCurrentEquippedItem().getItem() == BuildCraftCore.wrenchItem)
        {
            int var7 = var1.getBlockMetadata(var2, var3, var4);

            switch (BlockTemplate.NamelessClass1521936395.$SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.values()[var7].ordinal()])
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
            TileTemplate var6 = (TileTemplate)var1.getBlockTileEntity(var2, var3, var4);

            if (!APIProxy.isClient(var1))
            {
                var5.openGui(mod_BuildCraftBuilders.instance, 14, var1, var2, var3, var4);
            }

            return true;
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

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5)
    {
        super.onBlockPlacedBy(var1, var2, var3, var4, var5);
        Orientations var6 = Utils.get2dOrientation(new Position(var5.posX, var5.posY, var5.posZ), new Position((double)var2, (double)var3, (double)var4));
        var1.setBlockMetadataWithNotify(var2, var3, var4, var6.reverse().ordinal());
    }

    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        int var6 = var1.getBlockMetadata(var2, var3, var4);

        if (var5 == 1)
        {
            boolean var7 = false;

            if (APIProxy.getWorld() == null)
            {
                return this.getBlockTextureFromSideAndMetadata(var5, var6);
            }
            else
            {
                var7 = APIProxy.getWorld().isBlockIndirectlyGettingPowered(var2, var3, var4);
                return !var7 ? this.blockTextureTopPos : this.blockTextureTopNeg;
            }
        }
        else
        {
            return this.getBlockTextureFromSideAndMetadata(var5, var6);
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        return var2 == 0 && var1 == 3 ? this.blockTextureFront : (var1 == 1 ? this.blockTextureTopPos : (var1 == var2 ? this.blockTextureFront : this.blockTextureSides));
    }

}
