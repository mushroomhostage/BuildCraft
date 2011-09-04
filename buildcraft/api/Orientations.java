package buildcraft.api;


public enum Orientations {
   YNeg,	// 0
   YPos,	// 1
   ZNeg,	// 2
   ZPos,	// 3
   XNeg,	// 4
   XPos,	// 5
   Unknown;	// 6

   public Orientations reverse() {
      switch(this) {
      case YPos:
         return YNeg;
      case YNeg:
         return YPos;
      case ZPos:
         return ZNeg;
      case ZNeg:
         return ZPos;
      case XPos:
         return XNeg;
      case XNeg:
         return XPos;
      default:
         return Unknown;
      }
   }
}
