package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketKeyInputEvent implements IPacket {
   int key;

   public PacketKeyInputEvent(int key) {
      this.key = key;
   }

   public PacketKeyInputEvent() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeVarInt(this.key);
   }

   public void read(FriendlyByteBuf buf) {
      this.key = buf.readVarInt();
   }

   public void serverExecute(PacketContext ctx) {
      IStoryServer.optionally().ifPresent(server -> server.getScriptEnv().postEventToInstances(EventType.KEY_PRESS, this.key));
   }
}
