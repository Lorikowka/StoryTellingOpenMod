package com.storyteam.storytelling.client.render.gl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.mixins._client.minecraft.RenderTypeAccessor;
import net.minecraft.client.renderer.RenderType;
import org.lwjgl.opengl.GL11;

public class StencilingRenderType extends RenderType {
   public StencilingRenderType(RenderType type) {
      super(
         type.toString() + "+st.stencil",
         type.format(),
         type.mode(),
         type.bufferSize(),
         type.affectsCrumbling(),
         ((RenderTypeAccessor)type).isSortOnUpload(),
         type::setupRenderState,
         type::clearRenderState
      );
   }

   public void setupRenderState() {
      super.setupRenderState();
      GL11.glEnable(2960);
      RenderSystem.stencilFunc(514, 1, 255);
   }

   public void clearRenderState() {
      RenderSystem.stencilFunc(519, 1, 255);
      GL11.glDisable(2960);
      super.clearRenderState();
   }
}
