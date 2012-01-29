package buildcraft.energy;

import buildcraft.api.APIProxy;
import buildcraft.api.Orientations;
import buildcraft.api.TileNetworkData;
import buildcraft.energy.ContainerEngine;
import buildcraft.energy.TileEngine;
import net.minecraft.server.Entity;
import net.minecraft.server.ICrafting;
import net.minecraft.server.NBTTagCompound;

public abstract class Engine
{
    public int maxEnergy;
    @TileNetworkData
    public float progress;
    @TileNetworkData
    public Orientations orientation;
    public int energy;
    @TileNetworkData
    public Engine.EnergyStage energyStage;
    public int maxEnergyExtracted;
    protected TileEngine tile;

    public Engine(TileEngine var1)
    {
        this.energyStage = Engine.EnergyStage.Blue;
        this.maxEnergyExtracted = 1;
        this.tile = var1;
    }

    protected void computeEnergyStage()
    {
        if ((double)this.energy / (double)this.maxEnergy * 100.0D <= 25.0D)
        {
            this.energyStage = Engine.EnergyStage.Blue;
        }
        else if ((double)this.energy / (double)this.maxEnergy * 100.0D <= 50.0D)
        {
            this.energyStage = Engine.EnergyStage.Green;
        }
        else if ((double)this.energy / (double)this.maxEnergy * 100.0D <= 75.0D)
        {
            this.energyStage = Engine.EnergyStage.Yellow;
        }
        else if ((double)this.energy / (double)this.maxEnergy * 100.0D <= 100.0D)
        {
            this.energyStage = Engine.EnergyStage.Red;
        }
        else
        {
            this.energyStage = Engine.EnergyStage.Explosion;
        }
    }

    public final Engine.EnergyStage getEnergyStage()
    {
        if (!APIProxy.isClient(this.tile.world))
        {
            this.computeEnergyStage();
        }

        return this.energyStage;
    }

    public void update()
    {
        if (!this.tile.world.isBlockIndirectlyPowered(this.tile.x, this.tile.y, this.tile.z) && this.energy > 1)
        {
            --this.energy;
        }
    }

    public abstract String getTextureFile();

    public abstract int explosionRange();

    public abstract int maxEnergyReceived();

    public abstract float getPistonSpeed();

    public abstract boolean isBurning();

    public abstract void delete();

    public void addEnergy(int var1)
    {
        this.energy += var1;
        if (this.getEnergyStage() == Engine.EnergyStage.Explosion)
        {
            this.tile.world.a((Entity)null, (double)this.tile.x, (double)this.tile.y, (double)this.tile.z, (float)this.explosionRange());
        }

        if (this.energy > this.maxEnergy)
        {
            this.energy = this.maxEnergy;
        }
    }

    public int extractEnergy(int var1, int var2, boolean var3)
    {
        if (this.energy < var1)
        {
            return 0;
        }
        else
        {
            int var4;
            if (var2 > this.maxEnergyExtracted)
            {
                var4 = this.maxEnergyExtracted;
            }
            else
            {
                var4 = var2;
            }

            int var5;
            if (this.energy >= var4)
            {
                var5 = var4;
                if (var3)
                {
                    this.energy -= var4;
                }
            }
            else
            {
                var5 = this.energy;
                if (var3)
                {
                    this.energy = 0;
                }
            }

            return var5;
        }
    }

    public abstract int getScaledBurnTime(int var1);

    public abstract void burn();

    public void readFromNBT(NBTTagCompound var1) {}

    public void writeToNBT(NBTTagCompound var1) {}

    public void getGUINetworkData(int var1, int var2) {}

    public void sendGUINetworkData(ContainerEngine var1, ICrafting var2) {}

    public static enum EnergyStage
    {
        Blue("Blue", 0),
        Green("Green", 1),
        Yellow("Yellow", 2),
        Red("Red", 3),
        Explosion("Explosion", 4);

        private static final Engine.EnergyStage[] $VALUES = new Engine.EnergyStage[]{Blue, Green, Yellow, Red, Explosion};

        private EnergyStage(String var1, int var2) {}
    }
}
