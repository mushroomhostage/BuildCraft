package maeyanie;

import net.minecraft.server.ItemStack;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class PipeExtractEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected Block pipe;
	protected boolean cancelled = false;
	
	public PipeExtractEvent(Block block, Block pipe) {
		super(block);
		this.pipe = pipe;
	}
	
	public Block getPipe() { return this.pipe; }
	
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
}



