package buildcraft.transport;

import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.LegacyBlock;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.TileEntity;

public class LegacyTile extends TileEntity
{

    public void q_()
    {
        int var1 = this.world.getData(this.x, this.y, this.z);
        int var2 = ((LegacyBlock)Block.byId[this.world.getTypeId(this.x, this.y, this.z)]).newPipeId;
        BlockGenericPipe.createPipe(this.world, this.x, this.y, this.z, var2);
        this.world.setTypeIdAndData(this.x, this.y, this.z, BuildCraftTransport.genericPipeBlock.id, var1);
    }
}
