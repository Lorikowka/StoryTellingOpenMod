package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.packets.RequestPacket;
import com.storyteam.storytelling.network.packets.ResponsePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;
import java.util.UUID;
import lombok.Generated;

@RegisterSTPacket
public class SetScriptPaused {
   @RegisterSTPacket
   public static class Request extends RequestPacket<SetScriptPaused.Response> {
      private int id;
      private boolean state;

      public Request() {
         super("setScriptPausedStorytelling");
      }

      public Request(int id, boolean state) {
         this();
         this.id = id;
         this.state = state;
      }

      @Generated
      public int getId() {
         return this.id;
      }

      @Generated
      public boolean isState() {
         return this.state;
      }
   }

   @RegisterSTPacket
   public static class Response extends ResponsePacket {
      private int id;
      private boolean state;

      public Response() {
         super("getScriptStorytelling", UUID.randomUUID());
      }

      public Response(UUID requestId) {
         super("getScriptStorytelling", requestId);
      }

      public Response(UUID requestId, int id, boolean state) {
         this(requestId);
         this.id = id;
         this.state = state;
      }

      @Generated
      public int getId() {
         return this.id;
      }

      @Generated
      public boolean isState() {
         return this.state;
      }
   }
}
