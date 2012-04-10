package maeyanie;

import net.minecraft.server.ItemStack;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class PipeExitEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected Block pipe;
	protected ItemStack item;
	protected boolean cancelled = false;
	
	public PipeExitEvent(Block block, Block pipe, ItemStack item) {
		super(block);
		this.pipe = pipe;
		this.item = item;
	}
	
	public Block getPipe() { return this.pipe; }
	public ItemStack getItem() { return this.item; }
	
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
}



