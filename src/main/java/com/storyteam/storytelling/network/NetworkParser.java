package com.storyteam.storytelling.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.storyteam.storytelling.logging.WebhookEmbed;
import com.storyteam.storytelling.network.utils.BasePacketDeserializer;
import com.storyteam.storytelling.network.utils.InstantDeserializer;
import com.storyteam.storytelling.network.utils.ParamType;
import com.storyteam.storytelling.network.utils.ParamTypeDeserializer;
import com.storyteam.storytelling.network.utils.WebhookEmbedDeserializer;
import java.time.Instant;

public class NetworkParser {
   private static Gson gson = new GsonBuilder()
      .registerTypeAdapter(BasePacket.class, new BasePacketDeserializer())
      .registerTypeAdapter(Instant.class, new InstantDeserializer())
      .registerTypeAdapter(ParamType.class, new ParamTypeDeserializer())
      .registerTypeAdapter(WebhookEmbed.class, new WebhookEmbedDeserializer())
      .create();

   public static String serialize(BasePacket packet) {
      return gson.toJson(packet);
   }

   public static BasePacket deserialize(String json) {
      return (BasePacket)gson.fromJson(json, BasePacket.class);
   }

   public static <T extends BasePacket> T deserialize(String json, Class<T> clazz) {
      return (T)gson.fromJson(json, clazz);
   }

   public static void process(String json, Object provider) {
   }
}
