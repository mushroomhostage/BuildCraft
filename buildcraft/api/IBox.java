package buildcraft.api;

import buildcraft.api.LaserKind;
import buildcraft.api.Position;
import net.minecraft.server.World;

public interface IBox
{
    Position p1();

    Position p2();

    void createLasers(World var1, LaserKind var2);

    void deleteLasers();
}
