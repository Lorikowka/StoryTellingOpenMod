package com.storyteam.storytelling.net;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.base.LeftClickEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketLeftClick implements IPacket {
   public void serverExecute(PacketContext ctx) {
      ServerPlayer sender = ctx.getSender();
      if (sender != null) {
         IStoryServer.get().getScriptEnv().postEventToInstances(EventType.LEFT_CLICK_AIR, sender);
         MinecraftForge.EVENT_BUS.post(new LeftClickEvent(sender));
      } else {
         Storytelling.LOG.fatal("Left click skipped, unknown sender!");
      }
   }
}
