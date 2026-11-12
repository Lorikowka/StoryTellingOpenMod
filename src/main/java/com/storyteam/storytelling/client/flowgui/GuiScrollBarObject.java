package com.storyteam.storytelling.client.flowgui;

import java.util.function.Supplier;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.util.math.Point;

public class GuiScrollBarObject extends GuiObject {
   public float sliderHeight;
   public float sliderWidth;

   public GuiScrollBarObject(String name, Supplier<GuiImageObject> bgImage, Supplier<GuiImageObject> sliderImage, float sliderWidth, float sliderHeight) {
      super(name);
      this.sliderHeight = sliderHeight;
      this.sliderWidth = sliderWidth;
      GuiImageObject bg = (GuiImageObject)bgImage.get().pos(0.0F, 0.0F);
      bg.onPreRender((p, m) -> bg.scale(this.width / bg.getScaledWidth(), this.height / bg.getScaledHeight()));
      GuiImageObject sliderImg = (GuiImageObject)sliderImage.get().pos(0.0F, 0.0F);
      sliderImg.onPreRender((p, m) -> sliderImg.scale(this.sliderWidth / sliderImg.getScaledWidth(), this.sliderHeight / sliderImg.getScaledHeight()));
      GuiScrollBarObject.Slider slider = new GuiScrollBarObject.Slider("slider", 0.0F, 0.0F);
      slider.onPreRender((p, m) -> {
         slider.maxPosX = this.width - this.sliderWidth;
         slider.maxPosY = this.height - this.sliderHeight;
         slider.size(sliderImg.getScaledWidth(), sliderImg.getScaledHeight());
      });
      slider.addChild(sliderImg);
      this.addChild(bg);
      this.addChild(slider);
   }

   public Point getPos() {
      return (Point)this.getChild("slider").elementPosition.get();
   }

   public float getPosX() {
      return this.getPos().y();
   }

   public float getValueX() {
      return this.width == this.sliderWidth ? 0.0F : this.getPosX() / (this.width - this.sliderWidth);
   }

   public float getPosY() {
      return this.getPos().y();
   }

   public float getValueY() {
      return this.height == this.sliderHeight ? 0.0F : this.getPosY() / (this.height - this.sliderHeight);
   }

   public void setSliderPos(float x, float y) {
      this.getChild("slider").pos(x, y);
   }

   public void setSliderValue(float x, float y) {
      this.getChild("slider").pos(Mth.clamp(x, 0.0F, 1.0F) * (this.width - this.sliderWidth), Mth.clamp(y, 0.0F, 1.0F) * (this.height - this.sliderHeight));
   }

   public void setSliderPosX(float x) {
      this.setSliderPos(x, this.getPosY());
   }

   public void setSliderPosY(float y) {
      this.setSliderPos(this.getPosX(), y);
   }

   public void setSliderValueX(float x) {
      this.setSliderValue(x, this.getValueY());
   }

   public void setSliderValueY(float y) {
      this.setSliderValue(this.getValueX(), y);
   }

   public static class Slider extends GuiObject {
      public float maxPosX;
      public float maxPosY;
      private Point pinPoint;

      public Slider(String name, float maxPosX, float maxPosY) {
         super(name);
         this.maxPosX = maxPosX;
         this.maxPosY = maxPosY;
         this.onPreRender(
            (p, m) -> {
               if (this.pinPoint != null) {
                  this.pos(
                     Mth.clamp(this.pos.x() + m.relX() - this.pinPoint.x(), 0.0F, this.maxPosX),
                     Mth.clamp(this.pos.y() + m.relY() - this.pinPoint.y(), 0.0F, this.maxPosY)
                  );
               }
            }
         );
      }

      protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
         if (button == 0 && !fake && pos.isMouseWithin(this.width, this.height)) {
            this.pinPoint = new Point(pos.relX(), pos.relY());
         } else if (button == -25902376 && fake) {
            this.pinPoint = null;
         }

         return super.onMouseClicked(globalMousePos, pos, button, fake);
      }
   }
}
