package buildcraft.transport;

import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;
import forge.ITextureProvider;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;

public class ItemPipe extends Item implements ITextureProvider
{

    Pipe dummyPipe;


    protected ItemPipe(int var1)
    {
        super(var1);
    }

    public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, int var4, int var5, int var6, int var7)
    {
        int var8 = var4;
        int var9 = var5;
        int var10 = var6;
        int var11 = BuildCraftTransport.genericPipeBlock.id;
        if (var3.getTypeId(var4, var5, var6) == Block.SNOW.id)
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

        if (var1.count == 0)
        {
            return false;
        }
        else if (var5 == 127 && Block.byId[var11].material.isBuildable())
        {
            return false;
        }
        else if (!var3.mayPlace(var11, var4, var5, var6, false, var7))
        {
            return false;
        }
        else
        {
            CraftBlockState var12 = CraftBlockState.getBlockState(var3, var4, var5, var6);
            BlockGenericPipe.createPipe(var3, var4, var5, var6, this.id);
            if (var3.setRawTypeIdAndData(var4, var5, var6, var11, 0))
            {
                BlockPlaceEvent var13 = CraftEventFactory.callBlockPlaceEvent(var3, var2, var12, var8, var9, var10);
                if (var13.isCancelled() || !var13.canBuild())
                {
                    var3.setTypeIdAndData(var4, var5, var6, var12.getTypeId(), var12.getRawData());
                    return true;
                }

                var3.notify(var4, var5, var6);
                var3.applyPhysics(var4, var5, var6, var11);
                Block.byId[var11].postPlace(var3, var4, var5, var6, var7);
                Block.byId[var11].postPlace(var3, var4, var5, var6, var2);
                --var1.count;
            }

            return true;
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
            this.dummyPipe = BlockGenericPipe.createPipe(this.id);
        }

        return this.dummyPipe.getBlockTexture();
    }
}
