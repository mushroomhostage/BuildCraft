package buildcraft.energy;

public class EngineWood extends Engine
{
    public EngineWood(TileEngine var1)
    {
        super(var1);
        this.maxEnergy = 1000;
    }

    public String getTextureFile()
    {
        return "/net.minecraft.server/buildcraft/energy/gui/base_wood.png";
    }

    public int explosionRange()
    {
        return 1;
    }

    public int maxEnergyReceived()
    {
        return 50;
    }

    public float getPistonSpeed()
    {
        switch (this.getEnergyStage().ordinal())
        {
            case 1:
                return 0.01F;

            case 2:
                return 0.02F;

            case 3:
                return 0.04F;

            case 4:
                return 0.08F;

            default:
                return 0.0F;
        }
    }

    public void update()
    {
        super.update();

        if (this.tile.world.isBlockIndirectlyPowered(this.tile.x, this.tile.y, this.tile.z) && this.tile.world.getTime() % 20L == 0L)
        {
            ++this.energy;
        }
    }

    public boolean isBurning()
    {
        return this.tile.world.isBlockIndirectlyPowered(this.tile.x, this.tile.y, this.tile.z);
    }

    public int getScaledBurnTime(int var1)
    {
        return 0;
    }

    public void delete() {}

    public void burn() {}

}
