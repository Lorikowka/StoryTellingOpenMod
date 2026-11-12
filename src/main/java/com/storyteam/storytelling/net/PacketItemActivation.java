package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketItemActivation implements IPacket {
   private ItemStack stack;

   public PacketItemActivation() {
   }

   public PacketItemActivation(ItemStack stack) {
      this.stack = stack;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeItem(this.stack);
   }

   public void read(FriendlyByteBuf buf) {
      this.stack = buf.readItem();
   }

   public void clientExecute(PacketContext ctx) {
      Minecraft.getInstance().gameRenderer.displayItemActivation(this.stack);
   }
}
