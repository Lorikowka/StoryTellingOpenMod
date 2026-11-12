package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.client.screen.knocked_down_screen.KnockedDownScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class KnockDownPacket implements IPacket {
   private int id = 1;

   public KnockDownPacket(int id) {
      this.id = id;
   }

   public KnockDownPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
   }

   public void clientExecute(PacketContext ctx) {
      IPacket.super.clientExecute(ctx);
      Minecraft.getInstance().setScreen(new KnockedDownScreen(this.id));
   }
}
