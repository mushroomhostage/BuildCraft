package buildcraft.api;


public enum Orientations {

   YNeg("YNeg", 0),
   YPos("YPos", 1),
   ZNeg("ZNeg", 2),
   ZPos("ZPos", 3),
   XNeg("XNeg", 4),
   XPos("XPos", 5),
   Unknown("Unknown", 6);
   // $FF: synthetic field
   private static final Orientations[] $VALUES = new Orientations[]{YNeg, YPos, ZNeg, ZPos, XNeg, XPos, Unknown};


   private Orientations(String var1, int var2) {}

   public Orientations reverse() {
      switch(Orientations.NamelessClass1844901915.$SwitchMap$buildcraft$api$Orientations[this.ordinal()]) {
      case 1:
         return YNeg;
      case 2:
         return YPos;
      case 3:
         return ZNeg;
      case 4:
         return ZPos;
      case 5:
         return XNeg;
      case 6:
         return XPos;
      default:
         return Unknown;
      }
   }


   // $FF: synthetic class
   static class NamelessClass1844901915 {

      // $FF: synthetic field
      static final int[] $SwitchMap$buildcraft$api$Orientations = new int[Orientations.values().length];


      static {
         try {
            $SwitchMap$buildcraft$api$Orientations[Orientations.YPos.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$buildcraft$api$Orientations[Orientations.YNeg.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$buildcraft$api$Orientations[Orientations.ZPos.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$buildcraft$api$Orientations[Orientations.ZNeg.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$buildcraft$api$Orientations[Orientations.XPos.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$buildcraft$api$Orientations[Orientations.XNeg.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
