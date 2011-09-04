package buildcraft.transport;

import buildcraft.api.APIProxy;
import buildcraft.api.EntityPassiveItem;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.BlockIndex;
import buildcraft.core.CoreProxy;
import buildcraft.core.PacketIds;
import buildcraft.transport.TilePipe;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.BuildCraftCore;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;
import net.minecraft.server.mod_BuildCraftTransport;

public class TileDiamondPipe extends TilePipe implements IInventory, ISpecialInventory {

   ItemStack[] items = new ItemStack[54];


   public TileDiamondPipe() {
      this.items = new ItemStack[this.getSize()];
   }
   
   public ItemStack[] getContents() {
      return items;
   }

   public int getSize() {
      return this.items.length;
   }

   public ItemStack getItem(int var1) {
      return this.items[var1];
   }

   public ItemStack splitStack(int var1, int var2) {
      ItemStack var3 = this.items[var1].cloneItemStack();
      var3.count = var2;
      this.items[var1].count -= var2;
      if(this.items[var1].count == 0) {
         this.items[var1] = null;
      }

      if(APIProxy.isServerSide()) {
         CoreProxy.sendToPlayers((Packet230ModLoader)this.getDescriptionPacket(), this.x, this.y, this.z, 50, mod_BuildCraftTransport.instance);
      }

      return var3;
   }

   public void setItem(int var1, ItemStack var2) {
      this.items[var1] = var2;
      if(APIProxy.isServerSide()) {
         CoreProxy.sendToPlayers((Packet230ModLoader)this.getDescriptionPacket(), this.x, this.y, this.z, 50, mod_BuildCraftTransport.instance);
      }

   }

   public String getName() {
      return "Filters";
   }

   public int getMaxStackSize() {
      return 1;
   }

   public boolean a_(EntityHuman var1) {
      return true;
   }

   public LinkedList getPossibleMovements(Position var1, EntityPassiveItem var2) {
      LinkedList var3 = new LinkedList();
      LinkedList var4 = new LinkedList();
      LinkedList var5 = super.getPossibleMovements(new Position((double)this.x, (double)this.y, (double)this.z, var1.orientation), var2);
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         Orientations var7 = (Orientations)var6.next();
         boolean var8 = false;

         for(int var9 = 0; var9 < 9; ++var9) {
            ItemStack var10 = this.getItem(var7.ordinal() * 9 + var9);
            if(var10 != null) {
               var8 = true;
            }

            if(var10 != null && var10.id == var2.item.id) {
               if(CoreProxy.isDamageable(Item.byId[var2.item.id])) {
                  var3.add(var7);
               } else if(var10.getData() == var2.item.getData()) {
                  var3.add(var7);
               }
            }
         }

         if(!var8) {
            var4.add(var7);
         }
      }

      if(var3.size() != 0) {
         return var3;
      } else {
         return var4;
      }
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.l("items");

      for(int var3 = 0; var3 < var2.c(); ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.a(var3);
         int var5 = var4.e("index");
         this.items[var5] = new ItemStack(var4);
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.items.length; ++var3) {
         if(this.items[var3] != null && this.items[var3].count > 0) {
            NBTTagCompound var4 = new NBTTagCompound();
            var2.a(var4);
            var4.a("index", var3);
            this.items[var3].a(var4);
         }
      }

      var1.a("items", var2);
   }

   public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
      return false;
   }

   public ItemStack extractItem(boolean var1, Orientations var2) {
      return null;
   }

   public void initialize() {
      super.initialize();
      BlockIndex var1 = new BlockIndex(this.x, this.y, this.z);
      if(BuildCraftCore.bufferedDescriptions.containsKey(var1)) {
         Packet230ModLoader var2 = (Packet230ModLoader)BuildCraftCore.bufferedDescriptions.get(var1);
         BuildCraftCore.bufferedDescriptions.remove(var1);
         this.handlePacket(var2);
      }

   }

   public Packet getDescriptionPacket() {
      Packet230ModLoader var1 = new Packet230ModLoader();
      var1.modId = mod_BuildCraftTransport.instance.getId();
      var1.packetType = PacketIds.DiamondPipeContents.ordinal();
      var1.k = true;
      var1.dataInt = new int[3 + this.items.length * 2];
      var1.dataInt[0] = this.x;
      var1.dataInt[1] = this.y;
      var1.dataInt[2] = this.z;

      for(int var2 = 0; var2 < this.items.length; ++var2) {
         if(this.items[var2] == null) {
            var1.dataInt[3 + var2 * 2 + 0] = -1;
            var1.dataInt[3 + var2 * 2 + 1] = -1;
         } else {
            var1.dataInt[3 + var2 * 2 + 0] = this.items[var2].id;
            var1.dataInt[3 + var2 * 2 + 1] = this.items[var2].getData();
         }
      }

      return var1;
   }

   public void handlePacket(Packet230ModLoader var1) {
      if(var1.packetType == PacketIds.DiamondPipeContents.ordinal()) {
         for(int var2 = 0; var2 < this.items.length; ++var2) {
            if(var1.dataInt[3 + var2 * 2 + 0] == -1) {
               this.items[var2] = null;
            } else {
               this.items[var2] = new ItemStack(var1.dataInt[3 + var2 * 2 + 0], 1, var1.dataInt[3 + var2 * 2 + 1]);
            }
         }

      }
   }
}
