package buildcraft.factory;

import buildcraft.core.BuildCraftContainer;
import buildcraft.core.CoreProxy;
import buildcraft.factory.TileAutoWorkbench;
import forge.ForgeHooks;
import net.minecraft.server.AchievementList;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCraftResult;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;

public class ContainerAutoWorkbench extends BuildCraftContainer
{
    TileAutoWorkbench tile;
    public IInventory craftResult = new InventoryCraftResult();

    public ContainerAutoWorkbench(PlayerInventory var1, TileAutoWorkbench var2)
    {
        super(3);
        this.tile = var2;
        this.a(new ContainerAutoWorkbench.SlotAutoCrafting(var1.d, var2, this.craftResult, 0, 124, 35));

        int var3;
        int var4;
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 3; ++var4)
            {
                this.a(new Slot(var2, var4 + var3 * 3, 30 + var4 * 18, 17 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.a(new Slot(var1, var3, 8 + var3 * 18, 142));
        }

        this.a(var2);
    }

    public void a()
    {
        super.a();
        this.craftResult.setItem(0, this.tile.findRecipe());
    }

    public ItemStack a(int var1, int var2, boolean var3, EntityHuman var4)
    {
        this.craftResult.setItem(0, this.tile.findRecipe());
        ItemStack var5 = super.a(var1, var2, var3, var4);
        this.a(this.tile);
        return var5;
    }

    public boolean b(EntityHuman var1)
    {
        return this.tile.a(var1);
    }

    public ItemStack a(int var1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.e.get(var1);
        if (var3 != null && var3.c())
        {
            ItemStack var4 = var3.getItem();
            var2 = var4.cloneItemStack();
            if (var1 == 0)
            {
                if (!this.a(var4, 10, 46, true))
                {
                    return null;
                }
            }
            else if (var1 >= 10 && var1 < 37)
            {
                if (!this.a(var4, 37, 46, false))
                {
                    return null;
                }
            }
            else if (var1 >= 37 && var1 < 46)
            {
                if (!this.a(var4, 10, 37, false))
                {
                    return null;
                }
            }
            else if (!this.a(var4, 10, 46, false))
            {
                return null;
            }

            if (var4.count == 0)
            {
                var3.c((ItemStack)null);
            }
            else
            {
                var3.d();
            }

            if (var4.count == var2.count)
            {
                return null;
            }

            var3.b(var4);
        }

        return var2;
    }

    public class SlotAutoCrafting extends Slot
    {
        private final IInventory craftMatrix;
        private EntityHuman thePlayer;

        public SlotAutoCrafting(EntityHuman var2, IInventory var3, IInventory var4, int var5, int var6, int var7)
        {
            super(var4, var5, var6, var7);
            this.thePlayer = var2;
            this.craftMatrix = var3;
        }

        public boolean isAllowed(ItemStack var1)
        {
            return false;
        }

        public void b(ItemStack var1)
        {
            var1.c(this.thePlayer.world, this.thePlayer);
            if (var1.id == Block.WORKBENCH.id)
            {
                this.thePlayer.a(AchievementList.h, 1);
            }
            else if (var1.id == Item.WOOD_PICKAXE.id)
            {
                this.thePlayer.a(AchievementList.i, 1);
            }
            else if (var1.id == Block.FURNACE.id)
            {
                this.thePlayer.a(AchievementList.j, 1);
            }
            else if (var1.id == Item.WOOD_HOE.id)
            {
                this.thePlayer.a(AchievementList.l, 1);
            }
            else if (var1.id == Item.BREAD.id)
            {
                this.thePlayer.a(AchievementList.m, 1);
            }
            else if (var1.id == Item.CAKE.id)
            {
                this.thePlayer.a(AchievementList.n, 1);
            }
            else if (var1.id == Item.STONE_PICKAXE.id)
            {
                this.thePlayer.a(AchievementList.o, 1);
            }
            else if (var1.id == Item.WOOD_SWORD.id)
            {
                this.thePlayer.a(AchievementList.r, 1);
            }
            else if (var1.id == Block.ENCHANTMENT_TABLE.id)
            {
                this.thePlayer.a(AchievementList.D, 1);
            }
            else if (var1.id == Block.BOOKSHELF.id)
            {
                this.thePlayer.a(AchievementList.F, 1);
            }

            CoreProxy.TakenFromCrafting(this.thePlayer, var1, this.craftMatrix);
            ForgeHooks.onTakenFromCrafting(this.thePlayer, var1, this.craftMatrix);
            ContainerAutoWorkbench.this.tile.extractItem(true, true);
        }
    }
}
