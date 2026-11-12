package com.storyteam.storytelling.system.simulator.cognition_tab.network;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class C2SSyncCognitionCustomReques implements IPacket {
   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
   }

   public void serverExecute(PacketContext ctx) {
      CognitionHelper.syncCustomReques(ctx.getSender());
   }
}
