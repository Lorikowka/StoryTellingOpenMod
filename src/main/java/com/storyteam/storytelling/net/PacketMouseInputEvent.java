package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.base.MouseInputEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketMouseInputEvent implements IPacket {
   int key;

   public PacketMouseInputEvent(int key) {
      this.key = key;
   }

   public PacketMouseInputEvent() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeVarInt(this.key);
   }

   public void read(FriendlyByteBuf buf) {
      this.key = buf.readVarInt();
   }

   public void serverExecute(PacketContext ctx) {
      IStoryServer.optionally().ifPresent(server -> server.getScriptEnv().postEventToInstances(EventType.MOUSE_PRESS, this.key));
      MinecraftForge.EVENT_BUS.post(new MouseInputEvent(this.key));
   }
}
