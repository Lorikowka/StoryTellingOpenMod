package com.storyteam.storytelling.util.mixin;

import com.storyteam.storytelling.net.PacketSetCustomTexture;
import org.zeith.hammerlib.net.Network;

public class HLAccessor {
   public static void setTexture(int id, String texture) {
      Network.sendToAll(new PacketSetCustomTexture(id, texture));
   }
}
