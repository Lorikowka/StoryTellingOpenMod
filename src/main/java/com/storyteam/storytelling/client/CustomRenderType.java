package com.storyteam.storytelling.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.resources.ResourceLocation;

public class CustomRenderType extends RenderType {
   public static final RenderType DEATH_RAYS = RenderType.create(
      "death_rays",
      DefaultVertexFormat.POSITION_COLOR,
      Mode.QUADS,
      256,
      false,
      true,
      CompositeState.builder()
         .setShaderState(RenderStateShard.RENDERTYPE_LIGHTNING_SHADER)
         .setWriteMaskState(RenderStateShard.COLOR_WRITE)
         .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
         .setCullState(RenderStateShard.NO_CULL)
         .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
         .createCompositeState(false)
   );
   public static final RenderType DEATH_RAYS1 = RenderType.create(
      "death_rays",
      DefaultVertexFormat.POSITION_COLOR,
      Mode.QUADS,
      256,
      false,
      true,
      CompositeState.builder()
         .setShaderState(RenderStateShard.RENDERTYPE_LIGHTNING_SHADER)
         .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
         .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
         .setCullState(RenderStateShard.NO_CULL)
         .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
         .createCompositeState(false)
   );
   private static final Function<ResourceLocation, RenderType> TRANSLUCENT_ARMOR_NO_CULL = Util.memoize(
      texture -> {
         CompositeState state = CompositeState.builder()
            .setShaderState(RenderType.RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER)
            .setTextureState(new TextureStateShard(texture, false, false))
            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setCullState(RenderType.NO_CULL)
            .setLightmapState(RenderType.LIGHTMAP)
            .setOverlayState(RenderType.OVERLAY)
            .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(true);
         return RenderType.create("translucent_armor_no_cull", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, true, false, state);
      }
   );

   public CustomRenderType(
      String pName,
      VertexFormat pFormat,
      Mode pMode,
      int pBufferSize,
      boolean pAffectsCrumbling,
      boolean pSortOnUpload,
      Runnable pSetupState,
      Runnable pClearState
   ) {
      super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
   }
}
