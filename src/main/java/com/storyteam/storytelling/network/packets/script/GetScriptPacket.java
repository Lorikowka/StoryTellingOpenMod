package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.packets.RequestPacket;
import com.storyteam.storytelling.network.packets.ResponsePacket;
import com.storyteam.storytelling.network.types.Script;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;
import java.util.UUID;
import lombok.Generated;

public class GetScriptPacket {
   @RegisterSTPacket
   public static class Request extends RequestPacket<GetScriptPacket.Response> {
      private int script;

      public Request() {
         super("getScriptStorytelling");
      }

      public Request(int script) {
         this();
         this.script = script;
      }

      public int getScript() {
         return this.script;
      }
   }

   @RegisterSTPacket
   public static class Response extends ResponsePacket {
      private Script script;

      public Response() {
         super("getScriptStorytelling", UUID.randomUUID());
      }

      public Response(UUID requestId) {
         super("getScriptStorytelling", requestId);
      }

      public Response(UUID requestId, Script script) {
         this(requestId);
         this.script = script;
      }

      @Generated
      public Script getScript() {
         return this.script;
      }
   }
}
