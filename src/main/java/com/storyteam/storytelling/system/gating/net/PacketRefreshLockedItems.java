package com.storyteam.storytelling.system.gating.net;

import com.storyteam.storytelling.system.gating.event.RequestLockedItemUpdateEvent;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketRefreshLockedItems implements IPacket {
   public void clientExecute(PacketContext ctx) {
      RequestLockedItemUpdateEvent.request();
   }
}
