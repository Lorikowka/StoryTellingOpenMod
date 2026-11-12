package com.storyteam.storytelling.system.emotions.network;

import com.storyteam.storytelling.system.emotions.RenderHolder;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CClearCharacterEmotions implements IPacket {
   private String refId;

   public S2CClearCharacterEmotions() {
   }

   public S2CClearCharacterEmotions(String refId) {
      this.refId = refId;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.refId);
   }

   public void read(FriendlyByteBuf buf) {
      this.refId = buf.readUtf();
   }

   public void clientExecute(PacketContext ctx) {
      RenderHolder.removeRenderedUUID(this.refId);
   }
}
