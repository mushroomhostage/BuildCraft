package buildcraft.factory;

import buildcraft.api.API;
import buildcraft.api.Orientations;
import forge.ITextureProvider;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockTank extends BlockContainer implements ITextureProvider
{
    public BlockTank(int var1)
    {
        super(var1, Material.glass);
        this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
        this.setHardness(1.0F);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isACube()
    {
        return false;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileTank();
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
            case 1:
                return 98;

            default:
                return 96;
        }
    }

    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        switch (var5)
        {
            case 0:
            case 1:
                return 98;

            default:
                return var1.getBlockId(var2, var3 - 1, var4) == this.blockID ? 97 : 96;
        }
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        if (var5.getCurrentEquippedItem() != null)
        {
            int var6 = var5.getCurrentEquippedItem().itemID;
            int var7 = API.getLiquidForBucket(var6);
            TileTank var8 = (TileTank)var1.getBlockTileEntity(var2, var3, var4);

            if (var7 != 0)
            {
                int var9 = var8.fill(Orientations.Unknown, 1000, var7, true);

                if (var9 != 0 && !BuildCraftCore.debugMode)
                {
                    var5.inventory.setInventorySlotContents(var5.inventory.currentItem, new ItemStack(Item.bucketEmpty, 1));
                }

                return true;
            }
        }

        return false;
    }
}
