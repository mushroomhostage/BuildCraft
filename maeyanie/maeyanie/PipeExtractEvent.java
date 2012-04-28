package maeyanie;

import net.minecraft.server.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class PipeExtractEvent extends BlockEvent implements Cancellable {

   private static final HandlerList handlers = new HandlerList();
   protected Block pipe;
   protected ItemStack item;
   protected boolean cancelled = false;


   public PipeExtractEvent(Block var1, Block var2, ItemStack var3) {
      super(var1);
      this.pipe = var2;
      this.item = var3;
   }

   public Block getPipe() {
      return this.pipe;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

}
