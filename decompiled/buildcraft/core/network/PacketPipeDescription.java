package buildcraft.core.network;

public class PacketPipeDescription extends PacketUpdate
{
    public PacketPipeDescription() {}

    public PacketPipeDescription(int var1, int var2, int var3, int var4)
    {
        super(1);
        PacketPayload var5 = new PacketPayload(1, 0, 0);
        this.posX = var1;
        this.posY = var2;
        this.posZ = var3;
        var5.intPayload[0] = var4;
        this.payload = var5;
    }
}
