package com.storyteam.storytelling.system.simulator.me_tab.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.zeith.hammerlib.client.flowgui.FlowguiShaderRegistry;

public class ImageWidget extends AbstractWidget {
   public float uOffset;
   public float vOffset;
   public float txWidth;
   public float txHeight;
   public Supplier<ResourceLocation> tex;
   public Supplier<Tooltip> tooltip;
   public int shaderTexture = 0;
   public Supplier<ShaderInstance> shader = FlowguiShaderRegistry.GUI_SHADER;
   public Vec3 color = Vec3.fromRGB24(-1);

   public ImageWidget(Supplier<ResourceLocation> tex, int pX, int pY, float uOffset, float vOffset, int pWidth, int pHeight) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.tex = tex;
      this.uOffset = uOffset;
      this.vOffset = vOffset;
      this.txWidth = (float)pWidth;
      this.txHeight = (float)pHeight;
   }

   protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
      if (this.tooltip.get() != null) {
         this.setTooltip(this.tooltip.get());
      }

      RenderSystem.setShaderTexture(this.shaderTexture, this.tex.get());
      blitWithBlend(
         this.shader,
         guiGraphics,
         this.uOffset,
         this.vOffset,
         this.txWidth,
         this.txHeight,
         (float)this.getWidth(),
         (float)this.getHeight(),
         this.alpha,
         this.color
      );
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   public static void blitWithBlend(
      Supplier<ShaderInstance> shader,
      GuiGraphics gfx,
      float texPosX,
      float texPosY,
      float width,
      float height,
      float texWidth,
      float texHeight,
      float alpha,
      Vec3 rgb
   ) {
      blitWithBlend(shader, gfx, texPosX, texPosY, width, height, width, height, texWidth, texHeight, alpha, rgb);
   }

   public static void blitWithBlend(
      Supplier<ShaderInstance> shader,
      GuiGraphics gfx,
      float texPosX,
      float texPosY,
      float renderWidth,
      float renderHeight,
      float spriteWidth,
      float spriteHeight,
      float texWidth,
      float texHeight,
      float alpha,
      Vec3 rgb
   ) {
      float u1 = texPosX / texWidth;
      float u2 = (texPosX + spriteWidth) / texWidth;
      float v1 = texPosY / texHeight;
      float v2 = (texPosY + spriteHeight) / texHeight;
      Matrix4f pose = gfx.pose().last().pose();
      BufferBuilder buf = Tesselator.getInstance().getBuilder();
      if (buf.building()) {
         RenderedBuffer batch = buf.endOrDiscardIfEmpty();
         if (batch != null) {
            BufferUploader.drawWithShader(batch);
         }
      }

      RenderSystem.enableBlend();
      RenderSystem.setShader(shader);
      buf.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      float r = (float)rgb.x();
      float g = (float)rgb.y();
      float b = (float)rgb.z();
      buf.vertex(pose, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).uv(u1, v1).endVertex();
      buf.vertex(pose, 0.0F, renderHeight, 0.0F).color(r, g, b, alpha).uv(u1, v2).endVertex();
      buf.vertex(pose, renderWidth, renderHeight, 0.0F).color(r, g, b, alpha).uv(u2, v2).endVertex();
      buf.vertex(pose, renderWidth, 0.0F, 0.0F).color(r, g, b, alpha).uv(u2, v1).endVertex();
      BufferUploader.drawWithShader(buf.end());
      RenderSystem.disableBlend();
   }

   @Generated
   public void setUOffset(float uOffset) {
      this.uOffset = uOffset;
   }

   @Generated
   public void setVOffset(float vOffset) {
      this.vOffset = vOffset;
   }

   @Generated
   public void setTxWidth(float txWidth) {
      this.txWidth = txWidth;
   }

   @Generated
   public void setTxHeight(float txHeight) {
      this.txHeight = txHeight;
   }

   @Generated
   public void setTooltip(Supplier<Tooltip> tooltip) {
      this.tooltip = tooltip;
   }

   @Generated
   public void setShaderTexture(int shaderTexture) {
      this.shaderTexture = shaderTexture;
   }

   @Generated
   public void setShader(Supplier<ShaderInstance> shader) {
      this.shader = shader;
   }
}
