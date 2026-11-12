package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.packets.RequestPacket;
import com.storyteam.storytelling.network.packets.ResponsePacket;
import com.storyteam.storytelling.network.types.Script;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;
import java.util.List;
import java.util.UUID;

public class GetScriptsPacket {
   @RegisterSTPacket
   public static class Request extends RequestPacket<GetScriptsPacket.Response> {
      public Request() {
         super("getScriptsStorytelling");
      }
   }

   @RegisterSTPacket
   public static class Response extends ResponsePacket {
      private List<Script> scripts;

      public Response() {
         super("getScriptsStorytelling", UUID.randomUUID());
      }

      public Response(UUID requestId, List<Script> scripts) {
         super("getScriptsStorytelling", requestId);
         this.scripts = scripts;
      }

      public List<Script> getScripts() {
         return this.scripts;
      }
   }
}
