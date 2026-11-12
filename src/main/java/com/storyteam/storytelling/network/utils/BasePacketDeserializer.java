package com.storyteam.storytelling.network.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.packets.ChatMessagePacket;
import com.storyteam.storytelling.network.packets.CrashPacket;
import com.storyteam.storytelling.network.packets.DiscordLogPacket;
import com.storyteam.storytelling.network.packets.ErrorPacket;
import com.storyteam.storytelling.network.packets.ReportGameCrashPacket;
import com.storyteam.storytelling.network.packets.script.ClearLogLinesPacket;
import com.storyteam.storytelling.network.packets.script.GetInstructionPacket;
import com.storyteam.storytelling.network.packets.script.GetScriptPacket;
import com.storyteam.storytelling.network.packets.script.GetScriptsPacket;
import com.storyteam.storytelling.network.packets.script.GotoPacket;
import com.storyteam.storytelling.network.packets.script.PushLogLinePacket;
import com.storyteam.storytelling.network.packets.script.RemoveLogLinePacket;
import com.storyteam.storytelling.network.packets.script.ScriptUpdatePacket;
import com.storyteam.storytelling.network.packets.script.SetScriptPaused;
import com.storyteam.storytelling.network.packets.script.SkipScriptPacket;
import com.storyteam.storytelling.network.packets.script.StartScriptPacket;
import com.storyteam.storytelling.network.packets.script.StopScriptPacket;
import java.lang.reflect.Type;
import java.util.HashMap;

public class BasePacketDeserializer implements JsonDeserializer<BasePacket> {
   public static final HashMap<String, Class<? extends BasePacket>> packetTypes = new HashMap<>();
   private static final Class<?>[] packets = new Class[]{
      PushLogLinePacket.class,
      ClearLogLinesPacket.class,
      ErrorPacket.class,
      GetInstructionPacket.class,
      GetScriptPacket.class,
      GotoPacket.class,
      RemoveLogLinePacket.class,
      ScriptUpdatePacket.class,
      SetScriptPaused.class,
      SkipScriptPacket.class,
      StartScriptPacket.class,
      StopScriptPacket.class,
      ErrorPacket.class,
      ChatMessagePacket.class,
      CrashPacket.class,
      DiscordLogPacket.class,
      GetScriptsPacket.class,
      ReportGameCrashPacket.class
   };

   public BasePacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject jsonObject = json.getAsJsonObject();
      String type = jsonObject.get("type").getAsString();
      return packetTypes.containsKey(type) ? (BasePacket)context.deserialize(jsonObject, packetTypes.get(type)) : null;
   }

   static {
      for (Class<?> packet : packets) {
         if (BasePacket.class.isAssignableFrom(packet)) {
            try {
               if (packet.getConstructor().newInstance() instanceof BasePacket bp) {
                  packetTypes.put(bp.type, (Class<? extends BasePacket>)bp.getClass());
               }
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         } else {
            Class[] classes = packet.getClasses();

            for (Class aClass : classes) {
               if (BasePacket.class.isAssignableFrom(aClass)) {
                  try {
                     if (aClass.getConstructor().newInstance() instanceof BasePacket bp) {
                        packetTypes.put(bp.type, (Class<? extends BasePacket>)bp.getClass());
                     }
                  } catch (Exception var12) {
                     var12.printStackTrace();
                  }
               }
            }
         }
      }
   }
}
