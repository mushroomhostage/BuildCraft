package buildcraft.builders;

import buildcraft.api.APIProxy;
import buildcraft.api.IAreaProvider;
import buildcraft.api.LaserKind;
import buildcraft.api.Orientations;
import buildcraft.api.TileNetworkData;
import buildcraft.builders.ItemTemplate;
import buildcraft.core.BluePrint;
import buildcraft.core.Box;
import buildcraft.core.CoreProxy;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.Utils;
import buildcraft.core.network.PacketUpdate;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.BuildCraftBuilders;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;

public class TileTemplate extends TileBuildCraft implements IInventory
{

    @TileNetworkData
    public Box box = new Box();
    private ItemStack[] items = new ItemStack[2];
    private boolean isComputing = false;
    public int computingTime = 0;
    private int lastTemplateId = 0;
    public List transaction = new ArrayList();


    public void onOpen(CraftHumanEntity var1)
    {
        this.transaction.add(var1);
    }

    public void onClose(CraftHumanEntity var1)
    {
        this.transaction.remove(var1);
    }

    public List getViewers()
    {
        return this.transaction;
    }

    public void setMaxStackSize(int var1) {}

    public ItemStack[] getContents()
    {
        return this.items;
    }

    public void q_()
    {
        super.q_();
        if (this.isComputing)
        {
            if (this.computingTime < 200)
            {
                ++this.computingTime;
            }
            else
            {
                this.createBluePrint();
            }
        }

    }

    public void initialize()
    {
        super.initialize();
        if (!this.box.isInitialized())
        {
            IAreaProvider var1 = Utils.getNearbyAreaProvider(this.world, this.x, this.y, this.z);
            if (var1 != null)
            {
                this.box.initialize(var1);
                var1.removeFromWorld();
            }
        }

        if (!APIProxy.isClient(this.world) && this.box.isInitialized())
        {
            this.box.createLasers(this.world, LaserKind.Stripes);
        }

        this.sendNetworkUpdate();
    }

    public void createBluePrint()
    {
        if (this.box.isInitialized() && this.items[1] == null)
        {
            byte var1 = 1;
            byte var2 = 0;
            if (this.world.isBlockIndirectlyPowered(this.x, this.y, this.z))
            {
                var1 = 0;
                var2 = 1;
            }

            BluePrint var3 = new BluePrint(this.box.sizeX(), this.box.sizeY(), this.box.sizeZ());

            int var4;
            for (int var5 = this.box.xMin; var5 <= this.box.xMax; ++var5)
            {
                for (int var6 = this.box.yMin; var6 <= this.box.yMax; ++var6)
                {
                    for (var4 = this.box.zMin; var4 <= this.box.zMax; ++var4)
                    {
                        if (this.world.getTypeId(var5, var6, var4) != 0)
                        {
                            var3.setBlockId(var5 - this.box.xMin, var6 - this.box.yMin, var4 - this.box.zMin, var1);
                        }
                        else
                        {
                            var3.setBlockId(var5 - this.box.xMin, var6 - this.box.yMin, var4 - this.box.zMin, var2);
                        }
                    }
                }
            }

            var3.anchorX = this.x - this.box.xMin;
            var3.anchorY = this.y - this.box.yMin;
            var3.anchorZ = this.z - this.box.zMin;
            Orientations var8 = Orientations.values()[this.world.getData(this.x, this.y, this.z)].reverse();
            if (var8 != Orientations.XPos)
            {
                if (var8 == Orientations.ZPos)
                {
                    var3.rotateLeft();
                    var3.rotateLeft();
                    var3.rotateLeft();
                }
                else if (var8 == Orientations.XNeg)
                {
                    var3.rotateLeft();
                    var3.rotateLeft();
                }
                else if (var8 == Orientations.ZNeg)
                {
                    var3.rotateLeft();
                }
            }

            ItemStack var9 = new ItemStack(BuildCraftBuilders.templateItem);
            if (var3.equals(BuildCraftBuilders.bluePrints[this.lastTemplateId]))
            {
                BluePrint var10000 = BuildCraftBuilders.bluePrints[this.lastTemplateId];
                var9.setData(this.lastTemplateId);
            }
            else
            {
                var4 = BuildCraftBuilders.storeBluePrint(var3);
                var9.setData(var4);
                CoreProxy.addName(var9, "Template #" + var4);
                this.lastTemplateId = var4;
            }

            this.setItem(0, (ItemStack)null);
            this.setItem(1, var9);
        }

    }

    public int getSize()
    {
        return 2;
    }

    public ItemStack getItem(int var1)
    {
        return this.items[var1];
    }

    public ItemStack splitStack(int var1, int var2)
    {
        ItemStack var3;
        if (this.items[var1] == null)
        {
            var3 = null;
        }
        else if (this.items[var1].count > var2)
        {
            var3 = this.items[var1].a(var2);
        }
        else
        {
            ItemStack var4 = this.items[var1];
            this.items[var1] = null;
            var3 = var4;
        }

        this.initializeComputing();
        return var3;
    }

    public void setItem(int var1, ItemStack var2)
    {
        this.items[var1] = var2;
        this.initializeComputing();
    }

    public String getName()
    {
        return "Template";
    }

    public int getMaxStackSize()
    {
        return 1;
    }

    public boolean a(EntityHuman var1)
    {
        return this.world.getTileEntity(this.x, this.y, this.z) == this;
    }

    public void a(NBTTagCompound var1)
    {
        super.a(var1);
        this.lastTemplateId = var1.getInt("lastTemplateId");
        this.computingTime = var1.getInt("computingTime");
        this.isComputing = var1.getBoolean("isComputing");
        if (var1.hasKey("box"))
        {
            this.box.initialize(var1.getCompound("box"));
        }

        NBTTagList var2 = var1.getList("Items");
        this.items = new ItemStack[this.getSize()];

        for (int var3 = 0; var3 < var2.size(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.get(var3);
            int var5 = var4.getByte("Slot") & 255;
            if (var5 >= 0 && var5 < this.items.length)
            {
                this.items[var5] = ItemStack.a(var4);
            }
        }

    }

    public void b(NBTTagCompound var1)
    {
        super.b(var1);
        var1.setInt("lastTemplateId", this.lastTemplateId);
        var1.setInt("computingTime", this.computingTime);
        var1.setBoolean("isComputing", this.isComputing);
        if (this.box.isInitialized())
        {
            NBTTagCompound var2 = new NBTTagCompound();
            this.box.writeToNBT(var2);
            var1.set("box", var2);
        }

        NBTTagList var5 = new NBTTagList();

        for (int var3 = 0; var3 < this.items.length; ++var3)
        {
            if (this.items[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.items[var3].save(var4);
                var5.add(var4);
            }
        }

        var1.set("Items", var5);
    }

    public void j()
    {
        this.destroy();
    }

    public void destroy()
    {
        if (this.box.isInitialized())
        {
            this.box.deleteLasers();
        }

    }

    private void initializeComputing()
    {
        if (this.box.isInitialized())
        {
            if (!this.isComputing)
            {
                if (this.items[0] != null && this.items[0].getItem() instanceof ItemTemplate && this.items[1] == null)
                {
                    this.isComputing = true;
                    this.computingTime = 0;
                }
                else
                {
                    this.isComputing = false;
                    this.computingTime = 0;
                }
            }
            else if (this.items[0] == null || !(this.items[0].getItem() instanceof ItemTemplate))
            {
                this.isComputing = false;
                this.computingTime = 0;
            }
        }

    }

    public int getComputingProgressScaled(int var1)
    {
        return this.computingTime * var1 / 200;
    }

    public void handleDescriptionPacket(PacketUpdate var1)
    {
        boolean var2 = this.box.isInitialized();
        super.handleDescriptionPacket(var1);
        if (!var2 && this.box.isInitialized())
        {
            this.box.createLasers(this.world, LaserKind.Stripes);
        }

    }

    public void handleUpdatePacket(PacketUpdate var1)
    {
        boolean var2 = this.box.isInitialized();
        super.handleUpdatePacket(var1);
        if (!var2 && this.box.isInitialized())
        {
            this.box.createLasers(this.world, LaserKind.Stripes);
        }

    }

    public void f() {}

    public void g() {}

    public ItemStack splitWithoutUpdate(int var1)
    {
        if (this.items[var1] == null)
        {
            return null;
        }
        else
        {
            ItemStack var2 = this.items[var1];
            this.items[var1] = null;
            return var2;
        }
    }
}
