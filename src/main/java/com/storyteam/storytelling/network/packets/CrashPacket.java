package com.storyteam.storytelling.network.packets;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class CrashPacket extends BasePacket {
   public CrashPacket() {
      super("crashStorytelling");
   }
}
