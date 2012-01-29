package buildcraft.transport.pipes;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeLogicDiamond;
import buildcraft.transport.PipeTransportItems;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;

public class PipeItemsDiamond extends Pipe implements IPipeTransportItemsHook
{

    int nextTexture = 21;


    public PipeItemsDiamond(int var1)
    {
        super(new PipeTransportItems(), new PipeLogicDiamond(), var1);
    }

    public int getBlockTexture()
    {
        return this.nextTexture;
    }

    public void prepareTextureFor(Orientations var1)
    {
        if (var1 == Orientations.Unknown)
        {
            this.nextTexture = 21;
        }
        else
        {
            this.nextTexture = BuildCraftTransport.diamondTextures[var1.ordinal()];
        }

    }

    public LinkedList filterPossibleMovements(LinkedList var1, Position var2, EntityPassiveItem var3)
    {
        LinkedList var4 = new LinkedList();
        LinkedList var5 = new LinkedList();
        Iterator var6 = var1.iterator();

        while (var6.hasNext())
        {
            Orientations var7 = (Orientations)var6.next();
            boolean var8 = false;

            for (int var9 = 0; var9 < 9; ++var9)
            {
                ItemStack var10 = this.logic.getStackInSlot(var7.ordinal() * 9 + var9);
                if (var10 != null)
                {
                    var8 = true;
                }

                if (var10 != null && var10.id == var3.item.id)
                {
                    if (Item.byId[var3.item.id].g())
                    {
                        var4.add(var7);
                    }
                    else if (var10.getData() == var3.item.getData())
                    {
                        var4.add(var7);
                    }
                }
            }

            if (!var8)
            {
                var5.add(var7);
            }
        }

        if (var4.size() != 0)
        {
            return var4;
        }
        else
        {
            return var5;
        }
    }

    public void entityEntered(EntityPassiveItem var1, Orientations var2) {}

    public void readjustSpeed(EntityPassiveItem var1)
    {
        ((PipeTransportItems)this.transport).defaultReajustSpeed(var1);
    }
}
