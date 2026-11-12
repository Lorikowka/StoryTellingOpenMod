package com.storyteam.storytelling.net.core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class ForceF1Packet implements IPacket {
   private boolean enable;

   public ForceF1Packet(boolean enable) {
      this.enable = enable;
   }

   public ForceF1Packet() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeBoolean(this.enable);
      IPacket.super.write(buf);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.enable = buf.readBoolean();
   }

   public void clientExecute(PacketContext ctx) {
      IPacket.super.clientExecute(ctx);
      Minecraft.getInstance().options.hideGui = this.enable;
   }
}
