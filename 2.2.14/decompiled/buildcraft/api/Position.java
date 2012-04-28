package buildcraft.api;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class Position
{
    public double x;
    public double y;
    public double z;
    public Orientations orientation;

    public Position(double var1, double var3, double var5)
    {
        this.x = var1;
        this.y = var3;
        this.z = var5;
        this.orientation = Orientations.Unknown;
    }

    public Position(double var1, double var3, double var5, Orientations var7)
    {
        this.x = var1;
        this.y = var3;
        this.z = var5;
        this.orientation = var7;
    }

    public Position(Position var1)
    {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
        this.orientation = var1.orientation;
    }

    public Position(NBTTagCompound var1)
    {
        this.x = var1.getDouble("i");
        this.y = var1.getDouble("j");
        this.z = var1.getDouble("k");
        this.orientation = Orientations.Unknown;
    }

    public Position(TileEntity var1)
    {
        this.x = (double)var1.x;
        this.y = (double)var1.y;
        this.z = (double)var1.z;
    }

    public void moveRight(double var1)
    {
        switch (this.orientation.ordinal())
        {
            case 1:
                this.x -= var1;
                break;

            case 2:
                this.x += var1;
                break;

            case 3:
                this.z += var1;
                break;

            case 4:
                this.z -= var1;
        }
    }

    public void moveLeft(double var1)
    {
        this.moveRight(-var1);
    }

    public void moveForwards(double var1)
    {
        switch (this.orientation.ordinal())
        {
            case 1:
                this.z += var1;
                break;

            case 2:
                this.z -= var1;
                break;

            case 3:
                this.x += var1;
                break;

            case 4:
                this.x -= var1;
                break;

            case 5:
                this.y += var1;
                break;

            case 6:
                this.y -= var1;
        }
    }

    public void moveBackwards(double var1)
    {
        this.moveForwards(-var1);
    }

    public void moveUp(double var1)
    {
        switch (this.orientation.ordinal())
        {
            case 1:
            case 2:
            case 3:
            case 4:
                this.y += var1;

            default:
        }
    }

    public void moveDown(double var1)
    {
        this.moveUp(-var1);
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        var1.setDouble("i", this.x);
        var1.setDouble("j", this.y);
        var1.setDouble("k", this.z);
    }

    public String toString()
    {
        return "{" + this.x + ", " + this.y + ", " + this.z + "}";
    }

    public Position min(Position var1)
    {
        return new Position(var1.x > this.x ? this.x : var1.x, var1.y > this.y ? this.y : var1.y, var1.z > this.z ? this.z : var1.z);
    }

    public Position max(Position var1)
    {
        return new Position(var1.x < this.x ? this.x : var1.x, var1.y < this.y ? this.y : var1.y, var1.z < this.z ? this.z : var1.z);
    }

}
