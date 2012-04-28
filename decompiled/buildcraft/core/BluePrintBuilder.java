package buildcraft.core;

import buildcraft.api.API;
import buildcraft.api.IAreaProvider;
import net.minecraft.server.World;

public class BluePrintBuilder implements IAreaProvider
{
    public BluePrint bluePrint;
    int x;
    int y;
    int z;
    public boolean done;

    public BluePrintBuilder(BluePrint var1, int var2, int var3, int var4)
    {
        this.bluePrint = var1;
        this.x = var2;
        this.y = var3;
        this.z = var4;
        this.done = false;
    }

    public BlockContents findNextBlock(World var1)
    {
        return this.findNextBlock(var1, BluePrintBuilder.Mode.Simple);
    }

    public BlockContents findNextBlock(World var1, BluePrintBuilder.Mode var2)
    {
        this.bluePrint.loadIfNeeded();

        for (int var3 = 0; var3 < this.bluePrint.sizeY; ++var3)
        {
            for (int var4 = 0; var4 < this.bluePrint.sizeX; ++var4)
            {
                for (int var5 = 0; var5 < this.bluePrint.sizeZ; ++var5)
                {
                    int var6 = var4 + this.x - this.bluePrint.anchorX;
                    int var7 = var3 + this.y - this.bluePrint.anchorY;
                    int var8 = var5 + this.z - this.bluePrint.anchorZ;

                    if (var7 > 0)
                    {
                        int var9 = var1.getBlockId(var6, var7, var8);
                        BlockContents var10 = this.bluePrint.contents[var4][var3][var5];

                        if (var10 != null)
                        {
                            if (var2 == BluePrintBuilder.Mode.Simple)
                            {
                                var10 = var10.clone();
                                var10.x = var6;
                                var10.y = var7;
                                var10.z = var8;

                                if (API.softBlock(var10.blockId))
                                {
                                    if (!API.softBlock(var9) && !API.unbreakableBlock(var9))
                                    {
                                        return var10;
                                    }
                                }
                                else if (var9 != var10.blockId && !API.unbreakableBlock(var9))
                                {
                                    return var10;
                                }
                            }
                            else if (var2 == BluePrintBuilder.Mode.Template && (var10.blockId != 0 && API.softBlock(var9) || var10.blockId == 0 && !API.softBlock(var9) && !API.unbreakableBlock(var9)))
                            {
                                var10 = new BlockContents();
                                var10.x = var6;
                                var10.y = var7;
                                var10.z = var8;
                                var10.blockId = var9;
                                return var10;
                            }
                        }
                    }
                }
            }
        }

        this.done = true;
        return null;
    }

    public int xMin()
    {
        this.bluePrint.loadIfNeeded();
        return this.x - this.bluePrint.anchorX;
    }

    public int yMin()
    {
        this.bluePrint.loadIfNeeded();
        return this.y - this.bluePrint.anchorY;
    }

    public int zMin()
    {
        this.bluePrint.loadIfNeeded();
        return this.z - this.bluePrint.anchorZ;
    }

    public int xMax()
    {
        this.bluePrint.loadIfNeeded();
        return this.x + this.bluePrint.sizeX - this.bluePrint.anchorX - 1;
    }

    public int yMax()
    {
        this.bluePrint.loadIfNeeded();
        return this.y + this.bluePrint.sizeY - this.bluePrint.anchorY - 1;
    }

    public int zMax()
    {
        this.bluePrint.loadIfNeeded();
        return this.z + this.bluePrint.sizeZ - this.bluePrint.anchorZ - 1;
    }

    public void removeFromWorld() {}

    public static enum Mode
    {
        Simple("Simple", 0),
        Template("Template", 1);

        private static final BluePrintBuilder.Mode[] $VALUES = new BluePrintBuilder.Mode[]{Simple, Template};

        private Mode(String var1, int var2) {}
    }
}
