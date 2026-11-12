package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketTimeSet implements IPacket {
   protected long time;

   public PacketTimeSet(long time) {
      this.time = time;
   }

   public PacketTimeSet() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeLong(this.time);
   }

   public void read(FriendlyByteBuf buf) {
      this.time = buf.readLong();
   }

   public void clientExecute(PacketContext ctx) {
      ClientLevel p = Minecraft.getInstance().level;
      if (p != null) {
         p.getLevelData().setDayTime(this.time);
      }
   }
}
