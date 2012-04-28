package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.IBlockPipe;
import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import buildcraft.core.BlockIndex;
import buildcraft.core.PersistentTile;
import buildcraft.core.PersistentWorld;
import buildcraft.core.Utils;
import forge.ITextureProvider;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class BlockGenericPipe extends BlockContainer implements IPipeConnection, IBlockPipe, ITextureProvider
{
    public static TreeMap pipes = new TreeMap();
    static long lastRemovedDate = -1L;
    public static TreeMap pipeRemoved = new TreeMap();

    public BlockGenericPipe(int var1)
    {
        super(var1, Material.glass);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BuildCraftCore.pipeModel;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isACube()
    {
        return false;
    }

    /**
     * Adds to the supplied array any colliding bounding boxes with the passed in bounding box. Args: world, x, y, z,
     * axisAlignedBB, arrayList
     */
    public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, ArrayList var6)
    {
        this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
        super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2 - 1, var3, var4))
        {
            this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2 + 1, var3, var4))
        {
            this.setBlockBounds(0.25F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 - 1, var4))
        {
            this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.75F, 0.75F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 + 1, var4))
        {
            this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 1.0F, 0.75F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 - 1))
        {
            this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.75F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 + 1))
        {
            this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        float var5 = 0.25F;
        float var6 = 0.75F;
        float var7 = 0.25F;
        float var8 = 0.75F;
        float var9 = 0.25F;
        float var10 = 0.75F;

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2 - 1, var3, var4))
        {
            var5 = 0.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2 + 1, var3, var4))
        {
            var6 = 1.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 - 1, var4))
        {
            var7 = 0.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 + 1, var4))
        {
            var8 = 1.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 - 1))
        {
            var9 = 0.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 + 1))
        {
            var10 = 1.0F;
        }

        return AxisAlignedBB.getBoundingBoxFromPool((double)var2 + (double)var5, (double)var3 + (double)var7, (double)var4 + (double)var9, (double)var2 + (double)var6, (double)var3 + (double)var8, (double)var4 + (double)var10);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        return this.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6)
    {
        float var7 = 0.25F;
        float var8 = 0.75F;
        float var9 = 0.25F;
        float var10 = 0.75F;
        float var11 = 0.25F;
        float var12 = 0.75F;

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2 - 1, var3, var4))
        {
            var7 = 0.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2 + 1, var3, var4))
        {
            var8 = 1.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 - 1, var4))
        {
            var9 = 0.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3 + 1, var4))
        {
            var10 = 1.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 - 1))
        {
            var11 = 0.0F;
        }

        if (Utils.checkPipesConnections(var1, var2, var3, var4, var2, var3, var4 + 1))
        {
            var12 = 1.0F;
        }

        this.setBlockBounds(var7, var9, var11, var8, var10, var12);
        MovingObjectPosition var13 = super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return var13;
    }

    public static void removePipe(Pipe var0)
    {
        if (var0 != null)
        {
            World var1 = var0.worldObj;

            if (var1 != null)
            {
                int var2 = var0.xCoord;
                int var3 = var0.yCoord;
                int var4 = var0.zCoord;

                if (lastRemovedDate != var1.getWorldTime())
                {
                    lastRemovedDate = var1.getWorldTime();
                    pipeRemoved.clear();
                }

                pipeRemoved.put(new BlockIndex(var2, var3, var4), var0);
                PersistentWorld.getWorld(var1).removeTile(new BlockIndex(var2, var3, var4));
            }
        }
    }

    /**
     * Called whenever the block is removed.
     */
    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        Utils.preDestroyBlock(var1, var2, var3, var4);
        removePipe(getPipe(var1, var2, var3, var4));
        super.onBlockRemoval(var1, var2, var3, var4);
    }

    public String getTextureFile()
    {
        return BuildCraftCore.customBuildCraftTexture;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileGenericPipe();
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7)
    {
        if (!APIProxy.isClient(var1))
        {
            int var8 = this.quantityDropped(var1.rand);

            for (int var9 = 0; var9 < var8; ++var9)
            {
                if (var1.rand.nextFloat() <= var6)
                {
                    Pipe var10 = getPipe(var1, var2, var3, var4);

                    if (var10 == null)
                    {
                        var10 = (Pipe)pipeRemoved.get(new BlockIndex(var2, var3, var4));
                    }

                    if (var10 != null)
                    {
                        int var11 = var10.itemID;

                        if (var11 > 0)
                        {
                            var10.dropContents();
                            this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(var11, 1, this.damageDropped(var5)));
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return 0;
    }

    public boolean isPipeConnected(IBlockAccess var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        TileEntity var8 = var1.getBlockTileEntity(var5, var6, var7);
        Pipe var9 = getPipe(var1, var2, var3, var4);
        Pipe var10 = getPipe(var1, var5, var6, var7);
        return !isValid(var9) ? false : (isValid(var10) && !var9.transport.getClass().isAssignableFrom(var10.transport.getClass()) && !var10.transport.getClass().isAssignableFrom(var9.transport.getClass()) ? false : (var9 != null ? var9.isPipeConnected(var8) : false));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        super.onNeighborBlockChange(var1, var2, var3, var4, var5);
        Pipe var6 = getPipe(var1, var2, var3, var4);

        if (isValid(var6))
        {
            var6.onNeighborBlockChange();
        }
    }

    /**
     * Called when a block is placed using an item. Used often for taking the facing and figuring out how to position
     * the item. Args: x, y, z, facing
     */
    public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5)
    {
        super.onBlockPlaced(var1, var2, var3, var4, var5);
        Pipe var6 = getPipe(var1, var2, var3, var4);

        if (isValid(var6))
        {
            var6.onBlockPlaced();
        }
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        super.blockActivated(var1, var2, var3, var4, var5);
        Pipe var6 = getPipe(var1, var2, var3, var4);
        return isValid(var6) ? var6.blockActivated(var1, var2, var3, var4, var5) : false;
    }

    public void prepareTextureFor(IBlockAccess var1, int var2, int var3, int var4, Orientations var5)
    {
        Pipe var6 = getPipe(var1, var2, var3, var4);

        if (isValid(var6))
        {
            var6.prepareTextureFor(var5);
        }
    }

    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        Pipe var6 = getPipe(var1, var2, var3, var4);
        return isValid(var6) ? var6.getBlockTexture() : 0;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5)
    {
        super.onEntityCollidedWithBlock(var1, var2, var3, var4, var5);
        Pipe var6 = getPipe(var1, var2, var3, var4);

        if (isValid(var6))
        {
            var6.onEntityCollidedWithBlock(var5);
        }
    }

    /**
     * Is this block powering the block on the specified side
     */
    public boolean isPoweringTo(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        Pipe var6 = getPipe(var1, var2, var3, var4);
        return isValid(var6) ? var6.isPoweringTo(var5) : false;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Is this block indirectly powering the block on the specified side
     */
    public boolean isIndirectlyPoweringTo(World var1, int var2, int var3, int var4, int var5)
    {
        Pipe var6 = getPipe(var1, var2, var3, var4);
        return isValid(var6) ? var6.isIndirectlyPoweringTo(var5) : false;
    }

    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        Pipe var6 = getPipe(var1, var2, var3, var4);

        if (isValid(var6))
        {
            var6.randomDisplayTick(var5);
        }
    }

    public static Item registerPipe(int var0, Class var1)
    {
        ItemPipe var2 = new ItemPipe(var0);
        pipes.put(Integer.valueOf(var2.shiftedIndex), var1);
        return var2;
    }

    public static Pipe createPipe(int var0)
    {
        try
        {
            return (Pipe)((Class)pipes.get(Integer.valueOf(var0))).getConstructor(new Class[] {Integer.TYPE}).newInstance(new Object[] {Integer.valueOf(var0)});
        }
        catch (Throwable var2)
        {
            var2.printStackTrace();
            return null;
        }
    }

    public static Pipe createPipe(IBlockAccess var0, int var1, int var2, int var3, int var4)
    {
        Pipe var5 = createPipe(var4);
        var5.setPosition(var1, var2, var3);
        return (Pipe)PersistentWorld.getWorld(var0).createTile(var5, new BlockIndex(var1, var2, var3));
    }

    public static Pipe getPipe(IBlockAccess var0, int var1, int var2, int var3)
    {
        PersistentTile var4 = PersistentWorld.getWorld(var0).getTile(new BlockIndex(var1, var2, var3));
        return var4 != null && var4.isValid() && var4 instanceof Pipe ? (Pipe)var4 : null;
    }

    public static boolean isFullyDefined(Pipe var0)
    {
        return var0 != null && var0.transport != null && var0.logic != null;
    }

    public static boolean isValid(Pipe var0)
    {
        return isFullyDefined(var0) && var0.isValid();
    }
}
