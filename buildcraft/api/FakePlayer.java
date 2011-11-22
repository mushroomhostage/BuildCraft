package buildcraft.api;

import net.minecraft.server.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class FakePlayer {
	enum Method { NULL, FAKEPLAYER };
	
	private static Method method = Method.FAKEPLAYER;
	private static EntityPlayer fakePlayer = null;
	
	public static void setMethod(String value) {
		if (value.equalsIgnoreCase("null")) {
			method = Method.NULL;
		} else if (value.equalsIgnoreCase("fakeplayer")) {
			method = Method.FAKEPLAYER;
		} else {
			System.err.println("Unknown blocks.placedby type '"+value+"'");
		}
	}
	
	public static EntityPlayer get(World world) {
		switch (method) {
		case NULL:
			return null;
		case FAKEPLAYER:
			if (fakePlayer == null) {
				fakePlayer = new EntityPlayer(ModLoader.getMinecraftServerInstance(), world, 
					"[BuildCraft]", new ItemInWorldManager(world));
			}
			return fakePlayer;
		}
		return null;
	}
	
	public static CraftPlayer getBukkitEntity(World world) {
		EntityPlayer player = get(world);
		if (player != null) return (CraftPlayer)player.getBukkitEntity();
		return null;
	}
}
