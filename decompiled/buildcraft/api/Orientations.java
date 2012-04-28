package buildcraft.api;

public enum Orientations
{
    YNeg,
    YPos,
    ZNeg,
    ZPos,
    XNeg,
    XPos,
    Unknown;

    public Orientations reverse()
    {
        switch (Orientations.NamelessClass1253291554.$SwitchMap$net.minecraft.server$buildcraft$api$Orientations[this.ordinal()])
        {
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

    static class NamelessClass1253291554 {
        static final int[] $SwitchMap$net.minecraft.server$buildcraft$api$Orientations = new int[Orientations.values().length];

        static {
            try {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.YPos.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.YNeg.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.ZPos.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.ZNeg.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.XPos.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try {
                $SwitchMap$net.minecraft.server$buildcraft$api$Orientations[Orientations.XNeg.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
