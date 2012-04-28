package buildcraft.builders;

import buildcraft.builders.CraftingBuilder;
import buildcraft.builders.CraftingFiller;
import buildcraft.builders.CraftingTemplateRoot;
import buildcraft.builders.TileBuilder;
import buildcraft.builders.TileFiller;
import buildcraft.builders.TileTemplate;
import forge.IGuiHandler;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class GuiHandler implements IGuiHandler
{

    public Object getGuiElement(int var1, EntityHuman var2, World var3, int var4, int var5, int var6)
    {
        if (!var3.isLoaded(var4, var5, var6))
        {
            return null;
        }
        else
        {
            TileEntity var7 = var3.getTileEntity(var4, var5, var6);
            switch (var1)
            {
                case 11:
                    if (!(var7 instanceof TileBuilder))
                    {
                        return null;
                    }

                    return new CraftingBuilder(var2.inventory, (TileBuilder)var7);
                case 12:
                    if (!(var7 instanceof TileFiller))
                    {
                        return null;
                    }

                    return new CraftingFiller(var2.inventory, (TileFiller)var7);
                case 13:
                default:
                    return null;
                case 14:
                    return !(var7 instanceof TileTemplate) ? null : new CraftingTemplateRoot(var2.inventory, (TileTemplate)var7);
            }
        }
    }
}
