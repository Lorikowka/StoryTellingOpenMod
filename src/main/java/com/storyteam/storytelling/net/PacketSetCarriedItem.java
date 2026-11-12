package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSetCarriedItem implements IPacket {
   ItemStack carried = ItemStack.EMPTY;

   public PacketSetCarriedItem(ItemStack carried) {
      this.carried = carried;
   }

   public PacketSetCarriedItem() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeItemStack(this.carried, false);
   }

   public void read(FriendlyByteBuf buf) {
      this.carried = buf.readItem();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         player.getInventory().setPickedItem(this.carried);
      }
   }
}
