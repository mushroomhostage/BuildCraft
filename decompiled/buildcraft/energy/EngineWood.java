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
        switch (EngineWood.NamelessClass774541535.$SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[this.getEnergyStage().ordinal()])
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

        if (this.tile.worldObj.isBlockIndirectlyGettingPowered(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord) && this.tile.worldObj.getWorldTime() % 20L == 0L)
        {
            ++this.energy;
        }
    }

    public boolean isBurning()
    {
        return this.tile.worldObj.isBlockIndirectlyGettingPowered(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord);
    }

    public int getScaledBurnTime(int var1)
    {
        return 0;
    }

    public void delete() {}

    public void burn() {}

    static class NamelessClass774541535
    {
        static final int[] $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage = new int[Engine.EnergyStage.values().length];

        static
        {
            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Blue.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Green.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Yellow.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$net.minecraft.server$buildcraft$energy$Engine$EnergyStage[Engine.EnergyStage.Red.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
