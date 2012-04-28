package buildcraft.transport;

import forge.ITextureProvider;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemPipe extends Item implements ITextureProvider
{
    Pipe dummyPipe;

    protected ItemPipe(int var1)
    {
        super(var1);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7)
    {
        int var8 = BuildCraftTransport.genericPipeBlock.blockID;

        if (var3.getBlockId(var4, var5, var6) == Block.snow.blockID)
        {
            var7 = 0;
        }
        else
        {
            if (var7 == 0)
            {
                --var5;
            }

            if (var7 == 1)
            {
                ++var5;
            }

            if (var7 == 2)
            {
                --var6;
            }

            if (var7 == 3)
            {
                ++var6;
            }

            if (var7 == 4)
            {
                --var4;
            }

            if (var7 == 5)
            {
                ++var4;
            }
        }

        if (var1.stackSize == 0)
        {
            return false;
        }
        else if (var5 == 127 && Block.blocksList[var8].blockMaterial.isSolid())
        {
            return false;
        }
        else if (var3.canBlockBePlacedAt(var8, var4, var5, var6, false, var7))
        {
            BlockGenericPipe.createPipe(var3, var4, var5, var6, this.shiftedIndex);

            if (var3.setBlockAndMetadataWithNotify(var4, var5, var6, var8, 0))
            {
                Block.blocksList[var8].onBlockPlaced(var3, var4, var5, var6, var7);
                Block.blocksList[var8].onBlockPlacedBy(var3, var4, var5, var6, var2);
                --var1.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    public int getTextureIndex()
    {
        if (this.dummyPipe == null)
        {
            this.dummyPipe = BlockGenericPipe.createPipe(this.shiftedIndex);
        }

        return this.dummyPipe.getBlockTexture();
    }
}
