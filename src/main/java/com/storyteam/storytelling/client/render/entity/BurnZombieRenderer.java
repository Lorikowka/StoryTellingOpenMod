package com.storyteam.storytelling.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.ChangeTextureEmissiveLayer;
import com.storyteam.storytelling.client.render.entity.layer.FadeableGlowingLayer;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BurnZombieRenderer extends AnimatedEntityLikeHumanoidRenderer<BaseMob> {
   private final ResourceLocation[] TEXTURES = new ResourceLocation[]{
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie1.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie2.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie3.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie4.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie5.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie6.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie7.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_tex/burning_zombie8.png")
   };
   private final ResourceLocation[] TEXTURES_EMISSIVE = new ResourceLocation[]{
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission1.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission2.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission3.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission4.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission5.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission6.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission7.png"),
      St.id("textures/entity/mob/zombie_burn/zombie_burn_emissive/burning_zombie_emission8.png")
   };

   public BurnZombieRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, scale, false);
      this.addLayer(new ChangeTextureEmissiveLayer(this, this.TEXTURES_EMISSIVE));
      this.addLayer(new FadeableGlowingLayer(this, St.tx("entity/walter_white")));
   }

   public void render(
      @NotNull BaseMob entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight
   ) {
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull BaseMob entity) {
      int tickCount = entity.tickCount;
      int index = tickCount / 2 % this.TEXTURES.length;
      return this.TEXTURES[index];
   }
}
