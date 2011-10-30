package buildcraft.api;

import net.minecraft.server.*;

public class FakePlayer {
	private static EntityPlayer fakePlayer = null;
	
	public static EntityPlayer get(World world) {
		if (fakePlayer == null) {
			fakePlayer = new EntityPlayer(ModLoader.getMinecraftServerInstance(), world, 
				"[BuildCraft]", new ItemInWorldManager(world));
		}
		return fakePlayer;
	}
}
