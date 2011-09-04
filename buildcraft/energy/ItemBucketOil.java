package buildcraft.energy;

import net.minecraft.server.forge.ITextureProvider;
import net.minecraft.server.BuildCraftEnergy;
import net.minecraft.server.ItemBucket;

public class ItemBucketOil extends ItemBucket implements ITextureProvider {

   public ItemBucketOil(int var1) {
      super(var1, BuildCraftEnergy.oilMoving.id);
      this.textureId = 1;
   }

   public String getTextureFile() {
      return "/net/minecraft/src/buildcraft/energy/gui/item_textures.png";
   }
}
