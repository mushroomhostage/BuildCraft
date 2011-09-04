package buildcraft.energy;

import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;

public class EngineStone extends Engine {

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
      switch(EngineStone.NamelessClass2021103299.$SwitchMap$net$minecraft$src$buildcraft$energy$Engine$EnergyStage[this.getEnergyStage().ordinal()]) {
      case 1:
         return 0.02F;
      case 2:
         return 0.04F;
      case 3:
         return 0.08F;
      case 4:
         return 0.16F;
      default:
         return 0.0F;
      }
   }

   public boolean isBurning() {
      return this.tile.burnTime > 0;
   }

   // $FF: synthetic class
   static class NamelessClass2021103299 {

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
