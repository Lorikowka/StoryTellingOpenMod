package com.storyteam.storytelling.network.utils;

import com.storyteam.storytelling.network.StoryServer;
import com.storyteam.storytelling.network.packets.script.ClearLogLinesPacket;
import com.storyteam.storytelling.network.packets.script.PushLogLinePacket;
import com.storyteam.storytelling.network.packets.script.RemoveLogLinePacket;

public class WebMessageHandler {
   public static void sendInstructionLine(String id, int script, String text, boolean isDebug) {
      if (StoryServer.getInstance() != null) {
         StoryServer.getInstance().sendPacket(new PushLogLinePacket(id, script, text, isDebug));
      }
   }

   public static void removeInstructionLine(String id, int script) {
      if (StoryServer.getInstance() != null) {
         StoryServer.getInstance().sendPacket(new RemoveLogLinePacket(id, script));
      }
   }

   public static void clearNotices(int id) {
      if (StoryServer.getInstance() != null) {
         StoryServer.getInstance().sendPacket(new ClearLogLinesPacket(id));
      }
   }
}
