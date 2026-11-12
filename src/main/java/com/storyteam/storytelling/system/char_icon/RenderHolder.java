package com.storyteam.storytelling.system.char_icon;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RenderHolder {
   private static final List<IconRenderData> renderedData = new ArrayList<>();

   public static List<IconRenderData> getRenderedMap() {
      return renderedData;
   }

   public static ItemStack getRenderItem(String uuid) {
      return renderedData.stream().filter(data -> data.getUuid().equals(uuid)).findFirst().orElse(IconRenderData.empty(uuid)).getItem();
   }

   public static void addRenderedUUID(String uuid, ItemStack stack) {
      renderedData.add(IconRenderData.of(uuid, stack));
   }

   public static void addRenderedUUID(String uuid, ItemStack stack, Vec3 offset) {
      removeRenderedUUID(uuid);
      renderedData.add(IconRenderData.of(uuid, stack, offset));
   }

   public static void removeRenderedUUID(String uuid) {
      renderedData.removeIf(data -> data.getUuid().equals(uuid));
   }
}
