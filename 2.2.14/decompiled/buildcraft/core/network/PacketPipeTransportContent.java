package buildcraft.core.network;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;

public class PacketPipeTransportContent extends PacketUpdate
{
    public PacketPipeTransportContent()
    {
        super(2);
    }

    public PacketPipeTransportContent(int var1, int var2, int var3, EntityPassiveItem var4, Orientations var5)
    {
        this();
        this.posX = var1;
        this.posY = var2;
        this.posZ = var3;
        this.payload = new PacketPayload(6, 4, 0);
        this.payload.intPayload[0] = var4.entityId;
        this.payload.intPayload[1] = var5.ordinal();
        this.payload.intPayload[2] = var4.item.id;
        this.payload.intPayload[3] = var4.item.count;
        this.payload.intPayload[4] = var4.item.getData();
        this.payload.intPayload[5] = var4.deterministicRandomization;
        this.payload.floatPayload[0] = (float)var4.posX;
        this.payload.floatPayload[1] = (float)var4.posY;
        this.payload.floatPayload[2] = (float)var4.posZ;
        this.payload.floatPayload[3] = var4.speed;
    }

    public int getEntityId()
    {
        return this.payload.intPayload[0];
    }

    public Orientations getOrientation()
    {
        return Orientations.values()[this.payload.intPayload[1]];
    }

    public int getItemId()
    {
        return this.payload.intPayload[2];
    }

    public int getStackSize()
    {
        return this.payload.intPayload[3];
    }

    public int getItemDamage()
    {
        return this.payload.intPayload[4];
    }

    public int getRandomization()
    {
        return this.payload.intPayload[5];
    }

    public double getPosX()
    {
        return (double)this.payload.floatPayload[0];
    }

    public double getPosY()
    {
        return (double)this.payload.floatPayload[1];
    }

    public double getPosZ()
    {
        return (double)this.payload.floatPayload[2];
    }

    public float getSpeed()
    {
        return this.payload.floatPayload[3];
    }
}
