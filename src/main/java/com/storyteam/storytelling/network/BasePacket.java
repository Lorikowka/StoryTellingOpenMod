package com.storyteam.storytelling.network;

public abstract class BasePacket {
   public String type;

   public BasePacket(String type) {
      this.type = type;
   }
}
