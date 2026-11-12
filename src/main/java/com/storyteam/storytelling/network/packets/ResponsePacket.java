package com.storyteam.storytelling.network.packets;

import com.storyteam.storytelling.network.BasePacket;
import java.util.UUID;

public class ResponsePacket extends BasePacket {
   private UUID responseId;

   public ResponsePacket(String type, UUID requestId) {
      super(type + "Response");
      this.responseId = requestId;
   }

   public UUID getResponseId() {
      return this.responseId;
   }
}
