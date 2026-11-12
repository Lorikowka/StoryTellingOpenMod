package com.storyteam.storytelling.system.emotions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public class RenderHolder {
   private static final List<EmotionRenderData> renderedData = new ArrayList<>();

   public static List<EmotionRenderData> getRenderedMap() {
      return renderedData;
   }

   public static Map<Emotion, Integer> getRenderEmotions(String uuid) {
      return renderedData.stream()
         .filter(data -> data.getUuid().equals(uuid))
         .collect(Collectors.toMap(EmotionRenderData::getEmotion, EmotionRenderData::getTicks));
   }

   public static void add(EmotionRenderData data) {
      data.setAppearTick(Minecraft.getInstance().level.getGameTime());
      renderedData.add(data);
   }

   public static void addRenderedUUID(String uuid, Emotion emotion) {
      add(EmotionRenderData.of(uuid, emotion));
   }

   public static void addRenderedUUID(String uuid, Emotion emotion, Vec3 offset, BubbleType bubbleType) {
      add(EmotionRenderData.of(uuid, emotion, offset, bubbleType));
   }

   public static void addRenderedUUID(String uuid, Emotion emotion, Vec3 offset) {
      add(EmotionRenderData.of(uuid, emotion, offset));
   }

   public static void addRenderedUUID(String uuid, Emotion emotion, Vec3 offset, int ticks) {
      add(EmotionRenderData.of(uuid, emotion, ticks, offset));
   }

   public static void addRenderedUUID(String uuid, Emotion emotion, int ticks) {
      add(EmotionRenderData.of(uuid, emotion, ticks));
   }

   public static void removeRenderedUUID(String uuid) {
      renderedData.removeIf(data -> data.getUuid().equals(uuid));
   }

   public static void hideRenderedPlayer() {
      for (EmotionRenderData data : renderedData) {
         if (data.getUuid().equals("player")) {
            data.setHiddenOnWorld(true);
         }
      }
   }

   public static void removeRenderedUUID(String uuid, Emotion emotion) {
      renderedData.removeIf(data -> data.getUuid().equals(uuid) && data.getEmotion().equals(emotion));
   }

   public static boolean hasRenderedUUID(String uuid) {
      return renderedData.stream().anyMatch(data -> data.getUuid().equals(uuid));
   }

   public static EmotionRenderData getOldestRenderedUUID(String uuid) {
      return renderedData.stream()
         .filter(data -> data.getUuid().equals(uuid))
         .sorted(Comparator.comparingLong(EmotionRenderData::getAppearTick))
         .findFirst()
         .orElse(EmotionRenderData.empty(uuid));
   }
}
