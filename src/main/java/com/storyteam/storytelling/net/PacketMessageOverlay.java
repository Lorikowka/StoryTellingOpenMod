package com.storyteam.storytelling.net;

import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.client.content.overlays.MessageOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketMessageOverlay implements IPacket {
   Message message;

   public PacketMessageOverlay(Message message) {
      this.message = message;
   }

   public PacketMessageOverlay() {
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.message = Message.fromNetwork(buf);
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      this.message.toNetwork(buf);
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      MessageOverlay.setCurrentMessage(this.message);
   }
}
