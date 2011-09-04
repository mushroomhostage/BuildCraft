package buildcraft.energy;

import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;

public class EngineIron extends Engine {

   public EngineIron(TileEngine var1) {
      super(var1);
      this.maxEnergy = 100000;
      this.maxEnergyExtracted = 500;
   }

   public String getTextureFile() {
      return "/net/minecraft/src/buildcraft/energy/gui/base_iron.png";
   }

   public int explosionRange() {
      return 8;
   }

   public int maxEnergyReceived() {
      return 2000;
   }

   public float getPistonSpeed() {
      switch(EngineIron.NamelessClass35216364.$SwitchMap$net$minecraft$src$buildcraft$energy$Engine$EnergyStage[this.getEnergyStage().ordinal()]) {
      case 1:
         return 0.04F;
      case 2:
         return 0.08F;
      case 3:
         return 0.16F;
      case 4:
         return 0.32F;
      default:
         return 0.0F;
      }
   }

   public boolean isBurning() {
      return this.tile.burnTime > 0 && this.tile.world.isBlockIndirectlyPowered(this.tile.x, this.tile.y, this.tile.z);
   }

   // $FF: synthetic class
   static class NamelessClass35216364 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$src$buildcraft$energy$Engine$EnergyStage = new int[Engine.EnergyStage.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$src$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Blue.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Green.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Yellow.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Red.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
