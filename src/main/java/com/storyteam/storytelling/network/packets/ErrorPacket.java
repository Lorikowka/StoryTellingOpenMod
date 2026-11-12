package com.storyteam.storytelling.network.packets;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class ErrorPacket extends BasePacket {
   public int code;
   public String message;

   public ErrorPacket() {
      super("error");
   }

   public ErrorPacket(int code, String message) {
      super("error");
      this.code = code;
      this.message = message;
   }
}
