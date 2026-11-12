package com.storyteam.storytelling.system.lockpicking.net;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;

abstract class PacketGlopos implements IPacket {
   protected GlobalPos pos;

   public PacketGlopos(GlobalPos pos) {
      this.pos = pos;
   }

   public PacketGlopos() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeGlobalPos(this.pos);
   }

   public void read(FriendlyByteBuf buf) {
      this.pos = buf.readGlobalPos();
   }
}
