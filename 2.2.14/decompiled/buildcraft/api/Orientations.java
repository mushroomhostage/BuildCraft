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
        switch (this.ordinal())
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
}
