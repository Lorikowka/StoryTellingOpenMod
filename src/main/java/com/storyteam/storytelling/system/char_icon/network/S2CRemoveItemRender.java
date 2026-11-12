package com.storyteam.storytelling.system.char_icon.network;

import com.storyteam.storytelling.system.char_icon.RenderHolder;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CRemoveItemRender implements IPacket {
   private String refId;

   public S2CRemoveItemRender(String refId) {
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
