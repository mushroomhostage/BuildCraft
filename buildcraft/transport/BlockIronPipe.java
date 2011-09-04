package buildcraft.transport;

import buildcraft.api.Orientations;
import buildcraft.transport.BlockPipe;
import buildcraft.transport.TileIronPipe;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockIronPipe extends BlockPipe {

   public BlockIronPipe(int var1) {
      super(var1, Material.ORE);
      this.textureId = 18;
   }

   public void postPlace(World var1, int var2, int var3, int var4, int var5) {
      super.postPlace(var1, var2, var3, var3, var5);
      TileIronPipe var6 = (TileIronPipe)var1.getTileEntity(var2, var3, var4);
      var1.setRawData(var2, var3, var4, 1);
      var6.switchPosition();
   }

   public boolean interact(World var1, int var2, int var3, int var4, EntityHuman var5) {
      if(var5.G() != null && var5.G().getItem() == BuildCraftCore.wrenchItem) {
         TileIronPipe var6 = (TileIronPipe)var1.getTileEntity(var2, var3, var4);
         var6.switchPosition();
         var1.notify(var2, var3, var4);
         return true;
      } else {
         return false;
      }
   }

   public void doPhysics(World var1, int var2, int var3, int var4, int var5) {
      TileIronPipe var6 = (TileIronPipe)var1.getTileEntity(var2, var3, var4);
      var6.switchPower();
   }

   protected TileEntity a_() {
      return new TileIronPipe();
   }

   public int getTextureForConnection(Orientations var1, int var2) {
      return var2 != var1.ordinal()?BuildCraftTransport.plainIronTexture:this.textureId;
   }
}
