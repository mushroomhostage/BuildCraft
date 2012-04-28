package buildcraft.factory;

import buildcraft.core.BuildCraftContainer;
import buildcraft.core.CoreProxy;
import forge.ForgeHooks;
import net.minecraft.server.AchievementList;
import net.minecraft.server.Block;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCraftResult;
import net.minecraft.server.InventoryPlayer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Slot;

public class ContainerAutoWorkbench extends BuildCraftContainer
{
    TileAutoWorkbench tile;
    public IInventory craftResult = new InventoryCraftResult();

    public ContainerAutoWorkbench(InventoryPlayer var1, TileAutoWorkbench var2)
    {
        super(3);
        this.tile = var2;
        this.addSlot(new ContainerAutoWorkbench.SlotAutoCrafting(var1.player, var2, this.craftResult, 0, 124, 35));
        int var3;
        int var4;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 3; ++var4)
            {
                this.addSlot(new Slot(var2, var4 + var3 * 3, 30 + var4 * 18, 17 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 142));
        }

        this.onCraftMatrixChanged(var2);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();
        this.craftResult.setInventorySlotContents(0, this.tile.findRecipe());
    }

    public ItemStack slotClick(int var1, int var2, boolean var3, EntityPlayer var4)
    {
        this.craftResult.setInventorySlotContents(0, this.tile.findRecipe());
        ItemStack var5 = super.slotClick(var1, var2, var3, var4);
        this.onCraftMatrixChanged(this.tile);
        return var5;
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tile.isUseableByPlayer(var1);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(int var1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(var1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (var1 == 0)
            {
                if (!this.mergeItemStack(var4, 10, 46, true))
                {
                    return null;
                }
            }
            else if (var1 >= 10 && var1 < 37)
            {
                if (!this.mergeItemStack(var4, 37, 46, false))
                {
                    return null;
                }
            }
            else if (var1 >= 37 && var1 < 46)
            {
                if (!this.mergeItemStack(var4, 10, 37, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 10, 46, false))
            {
                return null;
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.onPickupFromSlot(var4);
        }

        return var2;
    }

    public class SlotAutoCrafting extends Slot
    {
        private final IInventory craftMatrix;
        private EntityPlayer thePlayer;

        public SlotAutoCrafting(EntityPlayer var2, IInventory var3, IInventory var4, int var5, int var6, int var7)
        {
            super(var4, var5, var6, var7);
            this.thePlayer = var2;
            this.craftMatrix = var3;
        }

        public boolean isItemValid(ItemStack var1)
        {
            return false;
        }

        public void onPickupFromSlot(ItemStack var1)
        {
            CoreProxy.onCraftingPickup(this.thePlayer.worldObj, this.thePlayer, var1);

            if (var1.itemID == Block.workbench.blockID)
            {
                this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
            }
            else if (var1.itemID == Item.pickaxeWood.shiftedIndex)
            {
                this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
            }
            else if (var1.itemID == Block.stoneOvenIdle.blockID)
            {
                this.thePlayer.addStat(AchievementList.buildFurnace, 1);
            }
            else if (var1.itemID == Item.hoeWood.shiftedIndex)
            {
                this.thePlayer.addStat(AchievementList.buildHoe, 1);
            }
            else if (var1.itemID == Item.bread.shiftedIndex)
            {
                this.thePlayer.addStat(AchievementList.makeBread, 1);
            }
            else if (var1.itemID == Item.cake.shiftedIndex)
            {
                this.thePlayer.addStat(AchievementList.bakeCake, 1);
            }
            else if (var1.itemID == Item.pickaxeStone.shiftedIndex)
            {
                this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
            }
            else if (var1.itemID == Item.swordWood.shiftedIndex)
            {
                this.thePlayer.addStat(AchievementList.buildSword, 1);
            }
            else if (var1.itemID == Block.enchantmentTable.blockID)
            {
                this.thePlayer.addStat(AchievementList.enchantments, 1);
            }
            else if (var1.itemID == Block.bookShelf.blockID)
            {
                this.thePlayer.addStat(AchievementList.bookcase, 1);
            }

            CoreProxy.TakenFromCrafting(this.thePlayer, var1, this.craftMatrix);
            ForgeHooks.onTakenFromCrafting(this.thePlayer, var1, this.craftMatrix);
            ContainerAutoWorkbench.this.tile.extractItem(true, true);
        }
    }
}
