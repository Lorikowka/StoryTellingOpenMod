package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.client.content.overlays.BlackoutBelowChatOverlay;
import com.storyteam.storytelling.client.content.overlays.BlackoutOverlay;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class BlackoutPacket implements IPacket {
   private int color;
   private int inTime;
   private int time;
   private int outTime;
   private boolean belowChat;

   public BlackoutPacket(int color, int inTime, int time, int outTime, boolean belowChat) {
      this.color = color;
      this.inTime = inTime;
      this.time = time;
      this.outTime = outTime;
      this.belowChat = belowChat;
   }

   public BlackoutPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.color);
      buf.writeInt(this.inTime);
      buf.writeInt(this.time);
      buf.writeInt(this.outTime);
      buf.writeBoolean(this.belowChat);
   }

   public void read(FriendlyByteBuf buf) {
      this.color = buf.readInt();
      this.inTime = buf.readInt();
      this.time = buf.readInt();
      this.outTime = buf.readInt();
      this.belowChat = buf.readBoolean();
   }

   public void clientExecute(PacketContext ctx) {
      if (!this.belowChat) {
         BlackoutOverlay.setCurrentBlackout(this.inTime, this.time, this.outTime, this.color);
      } else {
         BlackoutBelowChatOverlay.setCurrentBlackout(this.inTime, this.time, this.outTime, this.color);
      }
   }
}
