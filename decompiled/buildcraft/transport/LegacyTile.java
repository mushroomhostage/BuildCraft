package buildcraft.transport;

import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.TileEntity;

public class LegacyTile extends TileEntity
{
    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        int var1 = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        int var2 = ((LegacyBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)]).newPipeId;
        BlockGenericPipe.createPipe(this.worldObj, this.xCoord, this.yCoord, this.zCoord, var2);
        this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, BuildCraftTransport.genericPipeBlock.blockID, var1);
    }
}
