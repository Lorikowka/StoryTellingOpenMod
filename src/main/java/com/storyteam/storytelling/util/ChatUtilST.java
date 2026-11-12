package com.storyteam.storytelling.util;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.net.PacketMessageOverlay;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.zeith.hammerlib.net.Network;

public class ChatUtilST {
   public static void sendMessageLololowka(Component message) {
      MutableComponent spkrName = Component.empty().append(Component.literal("[Lololowka]").withStyle(Style.EMPTY.withColor(16750070)));
      sendMessage("timeless_lololoshka_icon", spkrName, message);
   }

   public static void sendMessage(String speaker, MutableComponent speakerName, Component message) {
      sendMessage(speaker, speakerName, Component.empty(), 0, 200, 15, message);
   }

   public static void sendMessage(String speaker, MutableComponent speakerName, MutableComponent flavor, int inTime, int time, int outTime, Component message) {
      MinecraftServer srv = ServerLifecycleHooks.getCurrentServer();
      if (srv != null) {
         srv.execute(
            () -> {
               Message msg = new Message(speaker, speakerName, flavor, inTime, time, outTime, message.copy());
               Network.sendToAll(new PacketMessageOverlay(msg));
               IStoryServer storyServer = IStoryServer.get(srv);
               ScriptEnvironment env = storyServer.getScriptEnv();
               env.addChatMessage(msg);
            }
         );
      }
   }
}
