package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.client.CameraHandler;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

@MainThreaded
public class ForceStopCameraPathPacket implements IPacket {
   public static void call() {
      if (!LogicalSidePredictor.getCurrentLogicalSide().isServer()) {
         throw new IllegalStateException("Can not send ForceStopCameraPathPacket from non-server thread.");
      } else {
         Network.sendToAll(new ForceStopCameraPathPacket());
      }
   }

   public void clientExecute(PacketContext ctx) {
      CameraHandler.startPos = null;
      CameraHandler.targetPos = null;
   }
}
