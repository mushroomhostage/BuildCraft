package buildcraft.transport;

import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;
import forge.ITextureProvider;
import net.minecraft.server.Block;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.BuildCraftTransport;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemPipe extends Item implements ITextureProvider {

   Pipe dummyPipe;


   protected ItemPipe(int var1) {
      super(var1);
   }

   public boolean a(ItemStack var1, EntityHuman var2, World var3, int var4, int var5, int var6, int var7) {
      int var8 = BuildCraftTransport.genericPipeBlock.id;
      if(var3.getTypeId(var4, var5, var6) == Block.SNOW.id) {
         var7 = 0;
      } else {
         if(var7 == 0) {
            --var5;
         }

         if(var7 == 1) {
            ++var5;
         }

         if(var7 == 2) {
            --var6;
         }

         if(var7 == 3) {
            ++var6;
         }

         if(var7 == 4) {
            --var4;
         }

         if(var7 == 5) {
            ++var4;
         }
      }

      if(var1.count == 0) {
         return false;
      } else if(var5 == 127 && Block.byId[var8].material.isBuildable()) {
         return false;
      } else if(var3.a(var8, var4, var5, var6, false, var7)) {
         BlockGenericPipe.createPipe(var4, var5, var6, this.id);
         if(var3.setTypeIdAndData(var4, var5, var6, var8, 0)) {
            Block.byId[var8].postPlace(var3, var4, var5, var6, var7);
            Block.byId[var8].postPlace(var3, var4, var5, var6, var2);
            --var1.count;
         }

         return true;
      } else {
         return false;
      }
   }

   public String getTextureFile() {
      return BuildCraftCore.customBuildCraftTexture;
   }

   public int getTextureIndex() {
      if(this.dummyPipe == null) {
         this.dummyPipe = BlockGenericPipe.createPipe(this.id);
      }

      return this.dummyPipe.getBlockTexture();
   }
}
