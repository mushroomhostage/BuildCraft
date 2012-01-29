package buildcraft.builders;

import buildcraft.builders.CraftingTemplateRoot;
import buildcraft.builders.TileTemplate;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;

class CraftingTemplate extends CraftingTemplateRoot
{
    public CraftingTemplate(IInventory var1, TileTemplate var2)
    {
        super(var1, var2);
    }

    public void a(ICrafting var1)
    {
        super.a(var1);
        var1.a(this, 0, this.template.computingTime);
    }

    public void a()
    {
        super.a();

        for (int var1 = 0; var1 < this.listeners.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.listeners.get(var1);
            if (this.computingTime != this.template.computingTime)
            {
                var2.a(this, 0, this.template.computingTime);
            }
        }

        this.computingTime = this.template.computingTime;
    }
}
