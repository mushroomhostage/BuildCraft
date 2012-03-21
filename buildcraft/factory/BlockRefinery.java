package buildcraft.factory;

import buildcraft.api.API;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockRefinery extends BlockContainer
{
    public BlockRefinery(int var1)
    {
        super(var1, Material.ORE);
        this.c(0.5F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean a()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return false (examples: signs, buttons, stairs, etc)
     */
    public boolean b()
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
    public int c()
    {
        return BuildCraftCore.blockByEntityModel;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileRefinery();
    }

    /**
     * Called when a block is using an item and passed in who placed it. Args: x, y, z, entityLiving
     */
    public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5)
    {
        super.postPlace(var1, var2, var3, var4, var5);
        Orientations var6 = Utils.get2dOrientation(new Position(var5.locX, var5.locY, var5.locZ), new Position((double)var2, (double)var3, (double)var4));
        var1.setData(var2, var3, var4, var6.reverse().ordinal());
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5)
    {
        if (var5.T() != null)
        {
            int var6;

            if (var5.T().getItem() == BuildCraftCore.wrenchItem)
            {
                var6 = var1.getData(var2, var3, var4);
                switch (Orientations.values()[var6])
                {
                    case XNeg:
                        var1.setRawData(var2, var3, var4, Orientations.ZPos.ordinal());
                        break;
                    case XPos:
                        var1.setRawData(var2, var3, var4, Orientations.ZNeg.ordinal());
                        break;
                    case ZNeg:
                        var1.setRawData(var2, var3, var4, Orientations.XNeg.ordinal());
                        break;
                    case ZPos:
                        var1.setRawData(var2, var3, var4, Orientations.XPos.ordinal());
                }

                var1.notify(var2, var3, var4);
            }
            else
            {
                var6 = API.getLiquidForBucket(var5.T().id);

                if (var6 != 0)
                {
                    int var7 = ((TileRefinery)var1.getTileEntity(var2, var3, var4)).fill(Orientations.Unknown, 1000, var6, true);

                    if (var7 != 0 && !BuildCraftCore.debugMode)
                    {
                        var5.inventory.setItem(var5.inventory.itemInHandIndex, new ItemStack(Item.BUCKET, 1));
                    }

                    return true;
                }
            }
        }

        return false;
    }

}
