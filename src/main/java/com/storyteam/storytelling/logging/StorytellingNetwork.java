package com.storyteam.storytelling.logging;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.logging.stocket.StocketProcessor;
import com.storyteam.storytelling.logging.stocket.StocketRequest;
import com.storyteam.storytelling.logging.stocket.StocketResponse;
import com.storyteam.storytelling.logging.stocket.StocketServer;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;

public class StorytellingNetwork {
   public static StocketServer server;

   public static void setup() {
      try {
         int port = 42687;
         server = new StocketServer(port);
         registerPackets(server.getProcessor());
         server.start();
         Storytelling.LOG.warn("Starting StorytellingNetwork Stocket on port " + port);
      } catch (IOException var1) {
         Storytelling.LOG.fatal("Unable to start StorytellingNetwork!", var1);
      }
   }

   public static void registerPackets(StocketProcessor $) {
      $.registerProcessor("sthelp", StorytellingNetwork::stHelp);
   }

   private static void stHelp(StocketRequest req, StocketResponse resp) {
      IStoryServer.optionally().ifPresentOrElse(server -> {
         Int2ObjectArrayMap<ScriptInstance> activeScripts = server.getScriptEnv().scriptInstances;
         resp.setReply("OK");
         ObjectIterator var3 = activeScripts.values().iterator();

         while (var3.hasNext()) {
            ScriptInstance script = (ScriptInstance)var3.next();
            ScriptPerformer mainPerformer = script.getMainPerformer();
            String id = Integer.toString(script.getId());
            if (mainPerformer == null) {
               resp.addData(id, "Отсутствует главный исполнитель.");
            } else {
               String report = mainPerformer.gatherHelpReport();
               resp.addData(id, report);
            }
         }
      }, () -> {
         resp.setReply("WARN");
         resp.addData("msg", "no_server");
      });
   }
}
