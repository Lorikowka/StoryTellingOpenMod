package com.storyteam.storytelling.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.function.Function;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FadeableGlowingLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   final Function<T, ResourceLocation> texture;

   public FadeableGlowingLayer(RenderLayerParent<T, M> er, ResourceLocation texture) {
      this(er, (Function<T, ResourceLocation>)(ent -> texture));
   }

   public FadeableGlowingLayer(RenderLayerParent<T, M> er, Function<T, ResourceLocation> texture) {
      super(er);
      this.texture = texture;
   }

   public void render(
      PoseStack matrixStackIn,
      MultiBufferSource bufferIn,
      int packedLightIn,
      T entitylivingbaseIn,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      if (entitylivingbaseIn instanceof AnimatedEntity animatedEntity) {
         IStoryEntity ie = IStoryEntity.of(animatedEntity);
         if (ie.isStorytellingEntity()) {
            Character<?> character = Character.getById(ie.getStoryName());
            if (character == null) {
               return;
            }

            EntityState<?> state = IStoryServer.get().getEntityRepository().getOrCreate(character).getActiveState();
            if (state != null) {
               float alpha = state.getData().getAlphaFadeable();
               if (alpha != 0.0F) {
                  VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucentEmissive(this.texture.apply(entitylivingbaseIn)));
                  this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, alpha, alpha, alpha, alpha);
               }
            }
         }
      }
   }
}
