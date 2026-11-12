package com.storyteam.storytelling.system.simulator.me_tab.client.flowgui;

import com.storyteam.storytelling.system.simulator.me_tab.client.ImageWidget;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.util.java.DirectStorage;

public class GuiImageObjectWithTooltip extends GuiObject {
   public Supplier<ResourceLocation> tex;
   public float uOffset;
   public float vOffset;
   public float imgWidth;
   public float imgHeight;
   public float txWidth;
   public float txHeight;
   public final DirectStorage<Float> textureUOffset = DirectStorage.create(p -> this.uOffset = p, () -> this.uOffset);
   public final DirectStorage<Float> textureVOffset = DirectStorage.create(p -> this.vOffset = p, () -> this.vOffset);
   public final DirectStorage<Float> imageWidth = DirectStorage.create(p -> this.imgWidth = p, () -> this.imgWidth);
   public final DirectStorage<Float> imageHeight = DirectStorage.create(p -> this.imgHeight = p, () -> this.imgHeight);
   public final DirectStorage<Float> fileWidth = DirectStorage.create(p -> this.txWidth = p, () -> this.txWidth);
   public final DirectStorage<Float> fileHeight = DirectStorage.create(p -> this.txHeight = p, () -> this.txHeight);
   public final ImageWidget wrapped;

   public GuiImageObjectWithTooltip(
      String name, Supplier<ResourceLocation> tex, float uOffset, float vOffset, float imgWidth, float imgHeight, float txWidth, float txHeight
   ) {
      super(name);
      this.wrapped = new ImageWidget(tex, 0, 0, uOffset, vOffset, (int)txWidth, (int)txHeight);
      this.size(txWidth, txHeight);
      this.tex = tex;
      this.uOffset = uOffset;
      this.vOffset = vOffset;
      this.txWidth = txWidth;
      this.txHeight = txHeight;
   }

   public GuiImageObjectWithTooltip tooltip(Supplier<Tooltip> tooltipSupplier) {
      this.wrapped.setTooltip(tooltipSupplier);
      return this;
   }

   public GuiImageObjectWithTooltip tooltip(Tooltip tooltipSupplier) {
      this.wrapped.setTooltip(() -> tooltipSupplier);
      return this;
   }

   public GuiImageObjectWithTooltip alpha(float alpha) {
      this.wrapped.setAlpha(alpha);
      return this;
   }

   public GuiImageObjectWithTooltip shader(Supplier<ShaderInstance> shader) {
      this.wrapped.setShader(shader);
      return this;
   }

   public GuiImageObjectWithTooltip shaderTexture(int shaderTexture) {
      this.wrapped.setShaderTexture(shaderTexture);
      return this;
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.wrapped.setUOffset((Float)this.textureUOffset.get());
      this.wrapped.setVOffset((Float)this.textureVOffset.get());
      this.wrapped.setTxWidth((float)((Float)this.elementWidth.get()).intValue());
      this.wrapped.setTxHeight((float)((Float)this.elementHeight.get()).intValue());
      this.wrapped.render(gfx.gfx(), (int)pos.relX(), (int)pos.relY(), gfx.partialTime());
      gfx.drawManaged(() -> {
      });
   }
}
