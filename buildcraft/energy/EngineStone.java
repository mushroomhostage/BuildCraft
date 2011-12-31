package buildcraft.energy;

import buildcraft.core.CoreProxy;
import buildcraft.core.Utils;
import buildcraft.energy.ContainerEngine;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import net.minecraft.server.Block;
import net.minecraft.server.ICrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;

public class EngineStone extends Engine {

   int burnTime = 0;
   int totalBurnTime = 0;


   public EngineStone(TileEngine var1) {
      super(var1);
      this.maxEnergy = 10000;
      this.maxEnergyExtracted = 100;
   }

   public String getTextureFile() {
      return "/net/minecraft/src/buildcraft/energy/gui/base_stone.png";
   }

   public int explosionRange() {
      return 4;
   }

   public int maxEnergyReceived() {
      return 200;
   }

   public float getPistonSpeed() {
      switch(this.getEnergyStage()) {
      case Blue:
         return 0.02F;
      case Green:
         return 0.04F;
      case Yellow:
         return 0.08F;
      case Red:
         return 0.16F;
      default:
         return 0.0F;
      }
   }

   public boolean isBurning() {
      return this.burnTime > 0;
   }

   public void burn() {
      if(this.burnTime > 0) {
         --this.burnTime;
         this.addEnergy(1);
      }

      if(this.burnTime == 0 && this.tile.world.isBlockIndirectlyPowered(this.tile.x, this.tile.y, this.tile.z)) {
         this.burnTime = this.totalBurnTime = this.getItemBurnTime(this.tile.getItem(0));
         if(this.burnTime > 0) {
            ItemStack var1 = this.tile.splitStack(1, 1);
            if(var1.getItem().i() != null) {
               this.tile.setItem(1, new ItemStack(var1.getItem().i(), 1));
            }
         }
      }

   }

   public int getScaledBurnTime(int var1) {
      return (int)((float)this.burnTime / (float)this.totalBurnTime * (float)var1);
   }

   private int getItemBurnTime(ItemStack var1) {
      if(var1 == null) {
         return 0;
      } else {
         int var2 = var1.getItem().id;
         return var2 < Block.byId.length && Block.byId[var2].material == Material.WOOD?300:(var2 == Item.STICK.id?100:(var2 == Item.COAL.id?1600:(var2 == Item.LAVA_BUCKET.id?20000:(var2 == Block.SAPLING.id?100:CoreProxy.addFuel(var2, var1.getData())))));
      }
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.burnTime = var1.getInt("burnTime");
      this.totalBurnTime = var1.getInt("totalBurnTime");
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.setInt("burnTime", this.burnTime);
      var1.setInt("totalBurnTime", this.totalBurnTime);
   }

   public void delete() {
      ItemStack var1 = this.tile.getItem(0);
      if(var1 != null) {
         Utils.dropItems(this.tile.world, var1, this.tile.x, this.tile.y, this.tile.z);
      }

   }

   public void getGUINetworkData(int var1, int var2) {
      if(var1 == 0) {
         this.burnTime = var2;
      } else if(var1 == 1) {
         this.totalBurnTime = var2;
      }

   }

   public void sendGUINetworkData(ContainerEngine var1, ICrafting var2) {
      var2.a(var1, 0, this.burnTime);
      var2.a(var1, 1, this.totalBurnTime);
    }
}
