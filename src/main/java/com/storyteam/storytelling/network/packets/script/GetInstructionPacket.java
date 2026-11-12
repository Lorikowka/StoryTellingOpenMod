package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.packets.RequestPacket;
import com.storyteam.storytelling.network.packets.ResponsePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;
import java.util.List;
import java.util.UUID;

public class GetInstructionPacket {
   public static record InstructionRow(String id, String line) {
   }

   @RegisterSTPacket
   public static class Request extends RequestPacket<GetInstructionPacket.Response> {
      private int script;
      private boolean debug;

      public Request() {
         super("getInstruction");
      }

      public Request(int script) {
         this();
         this.script = script;
      }

      public int getScript() {
         return this.script;
      }

      public boolean isDebug() {
         return this.debug;
      }
   }

   @RegisterSTPacket
   public static class Response extends ResponsePacket {
      private int script;
      private List<GetInstructionPacket.InstructionRow> instructions;

      public Response() {
         super("getInstruction", UUID.randomUUID());
      }

      public Response(UUID requestId) {
         super("getInstruction", requestId);
      }

      public Response(UUID requestId, int script, List<GetInstructionPacket.InstructionRow> instructions) {
         this(requestId);
         this.script = script;
         this.instructions = instructions;
      }

      public int getScript() {
         return this.script;
      }

      public List<GetInstructionPacket.InstructionRow> getInstructions() {
         return this.instructions;
      }
   }
}
