package com.storyteam.storytelling.client.flowgui;

import com.google.common.base.MoreObjects;
import com.storyteam.storytelling.St;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiSpriteButtonObject;
import org.zeith.hammerlib.util.java.DirectStorage;
import org.zeith.hammerlib.util.java.itf.FloatConsumer;
import org.zeith.hammerlib.util.math.Point;

public class GuiHorizontalSliderObject extends GuiObject {
   protected boolean anchored;
   protected final Supplier<Component> label;
   protected final DirectStorage<Float> previewSlideValue;
   protected final FloatConsumer applySlideValue;
   protected GuiObject bgButton;
   protected GuiObject slideButton;
   protected GuiSpriteButtonObject labelComponent;

   public GuiHorizontalSliderObject(String name, Supplier<Component> label, DirectStorage<Float> previewSlideValue, FloatConsumer applySlideValue) {
      super(name);
      this.previewSlideValue = previewSlideValue;
      this.applySlideValue = applySlideValue;
      this.label = label;
      this.size(100.0F, 20.0F);
      this.addChild(this.bgButton = GuiObject.create("bg_button").button().enabled(false).build().size(100.0F, 20.0F));
      this.addChild(this.slideButton = GuiObject.create("slider_button").button().build().size(5.0F, 20.0F));
      this.addChild(this.labelComponent = GuiObject.create("label").spriteButton().customTexture(St.tx("empty")).enabled(false).build());
      this.labelComponent.size(100.0F, 20.0F);
   }

   protected float computeValue(MousePos pos) {
      return Mth.clamp((pos.relX() - 1.0F) * this.width / (this.width - 2.0F), 0.0F, this.width) / this.width;
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.slideButton
         .pos(1.0F + (Float)MoreObjects.firstNonNull((Float)this.previewSlideValue.get(), 0.0F) * (this.width - this.slideButton.getScaledWidth() - 1.0F), 0.0F)
         .size(5.0F, this.height);
      this.bgButton.size(this.width, this.height);
      this.labelComponent.size(this.width, this.height);
      Component lbl = this.label.get();
      if (lbl != null) {
         this.labelComponent.setMessage(lbl);
      } else {
         this.labelComponent.setMessage(Component.empty());
      }

      super.render(gfx, pos);
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      if (pos.isMouseWithin(this)) {
         if (!fake && button == 0) {
            this.anchored = true;
         }

         return true;
      } else {
         return super.onMouseClicked(globalMousePos, pos, button, fake);
      }
   }

   protected boolean onMouseDragged(Point globalMousePos, MousePos pos, int button, MousePos dragPos) {
      if (this.anchored) {
         this.previewSlideValue.set(this.computeValue(pos));
      }

      return super.onMouseDragged(globalMousePos, pos, button, dragPos);
   }

   protected boolean onMouseReleased(Point globalMousePos, MousePos pos, int button) {
      if (this.anchored) {
         this.anchored = false;
         float cv = this.computeValue(pos);
         this.previewSlideValue.set(cv);
         this.applySlideValue.accept(cv);
      }

      return super.onMouseReleased(globalMousePos, pos, button);
   }
}
