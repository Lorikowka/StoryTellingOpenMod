package com.storyteam.storytelling.client.render.gl;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class BatchingRenderTypeBuffer implements MultiBufferSource {
   private final Map<RenderType, CollectingVertexBuilder> builders = new HashMap<>();

   @Nonnull
   public VertexConsumer getBuffer(@Nonnull RenderType type) {
      return (VertexConsumer)this.builders.computeIfAbsent(type, s -> new CollectingVertexBuilder());
   }

   public void pipe(MultiBufferSource out) {
      Collection<RenderType> delay = ImmutableList.of(RenderType.glint(), RenderType.entityGlint());

      for (Entry<RenderType, CollectingVertexBuilder> e : this.builders.entrySet()) {
         if (!delay.contains(e.getKey())) {
            e.getValue().pipeAndClear(out.getBuffer(e.getKey()));
         }
      }

      for (RenderType rt : delay) {
         if (this.builders.containsKey(rt)) {
            this.builders.get(rt).pipeAndClear(out.getBuffer(rt));
         }
      }

      this.builders.clear();
   }
}
