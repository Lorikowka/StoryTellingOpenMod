package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class StopCameraPathPacket implements IPacket {
   public void serverExecute(PacketContext ctx) {
      IStoryServer.get().postStoryEvent(EventType.CAMERA_PATH_END, IStoryServer.get());
   }
}
