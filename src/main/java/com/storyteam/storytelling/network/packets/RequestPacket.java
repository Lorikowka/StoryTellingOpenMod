package com.storyteam.storytelling.network.packets;

import com.storyteam.storytelling.network.BasePacket;
import java.util.UUID;

public class RequestPacket<T extends ResponsePacket> extends BasePacket {
   private UUID requestId = UUID.randomUUID();

   public RequestPacket(String type) {
      super(type + "Request");
   }

   public UUID getRequestId() {
      return this.requestId;
   }
}
