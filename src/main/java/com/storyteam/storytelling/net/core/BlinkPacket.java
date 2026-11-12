package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.client.content.overlays.BlinkOverlay;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class BlinkPacket implements IPacket {
   private int color;
   private int inTime;
   private int time;
   private int outTime;

   public BlinkPacket(int color, int inTime, int time, int outTime) {
      this.color = color;
      this.inTime = inTime;
      this.time = time;
      this.outTime = outTime;
   }

   public BlinkPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.color);
      buf.writeInt(this.inTime);
      buf.writeInt(this.time);
      buf.writeInt(this.outTime);
   }

   public void read(FriendlyByteBuf buf) {
      this.color = buf.readInt();
      this.inTime = buf.readInt();
      this.time = buf.readInt();
      this.outTime = buf.readInt();
   }

   public void clientExecute(PacketContext ctx) {
      BlinkOverlay.setCurrentBlackout(this.inTime, this.time, this.outTime, this.color);
   }
}
