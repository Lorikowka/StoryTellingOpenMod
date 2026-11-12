package com.storyteam.storytelling.events.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.Event;

public class RenderScreenEffectEvent extends Event {
   Minecraft pMinecraft;
   PoseStack pPoseStack;

   public RenderScreenEffectEvent(Minecraft pMinecraft, PoseStack pPoseStack) {
      this.pMinecraft = pMinecraft;
      this.pPoseStack = pPoseStack;
   }

   public Minecraft getpMinecraft() {
      return this.pMinecraft;
   }

   public PoseStack getpPoseStack() {
      return this.pPoseStack;
   }
}
