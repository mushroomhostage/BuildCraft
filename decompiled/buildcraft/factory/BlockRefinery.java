package buildcraft.factory;

import buildcraft.api.API;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockRefinery extends BlockContainer
{
    public BlockRefinery(int var1)
    {
        super(var1, Material.iron);
        this.setHardness(0.5F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isACube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BuildCraftCore.blockByEntityModel;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileRefinery();
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
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        if (var5.getCurrentEquippedItem() != null)
        {
            int var6;

            if (var5.getCurrentEquippedItem().getItem() == BuildCraftCore.wrenchItem)
            {
                var6 = var1.getBlockMetadata(var2, var3, var4);

                switch (BlockRefinery.NamelessClass309084170.$SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.values()[var6].ordinal()])
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
            }
            else
            {
                var6 = API.getLiquidForBucket(var5.getCurrentEquippedItem().itemID);

                if (var6 != 0)
                {
                    int var7 = ((TileRefinery)var1.getBlockTileEntity(var2, var3, var4)).fill(Orientations.Unknown, 1000, var6, true);

                    if (var7 != 0 && !BuildCraftCore.debugMode)
                    {
                        var5.inventory.setInventorySlotContents(var5.inventory.currentItem, new ItemStack(Item.bucketEmpty, 1));
                    }

                    return true;
                }
            }
        }

        return false;
    }

}
