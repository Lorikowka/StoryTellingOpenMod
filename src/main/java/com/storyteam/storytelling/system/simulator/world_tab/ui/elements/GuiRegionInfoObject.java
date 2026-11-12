package com.storyteam.storytelling.system.simulator.world_tab.ui.elements;

import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.octospizdel.util.OctoColor;
import com.storyteam.storytelling.system.simulator.world_tab.data.WMRButton;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegion;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegionInfo;
import com.storyteam.storytelling.system.simulator.world_tab.ui.WorldTabConstants;
import com.storyteam.storytelling.system.simulator.world_tab.ui.abstractions.GuiFittingMultiLineTextWidget;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiTextObject;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.math.Point;

public class GuiRegionInfoObject extends GuiImageObject {
   private final WorldMapRegion region;
   private final WorldMapRegionInfo regionInfo;
   private final GuiImageObject image;
   public Tween tween;
   public Point startPoint;
   private int buttonsPage = 0;

   public GuiRegionInfoObject(WorldMapRegion region) {
      super("info", Cast.constant(WorldTabConstants.BACKGROUND_INFO_IMAGE), 0.0F, 0.0F, 214.0F, 235.0F, 214.0F, 235.0F);
      this.startPoint = new Point(
         ((float)region.getWorldMapRegionIcon().XPos() + (float)region.getWorldMapRegionIcon().width() / 2.0F) * 0.5F,
         ((float)region.getWorldMapRegionIcon().YPos() + (float)region.getWorldMapRegionIcon().height() / 2.0F) * 0.5F
      );
      this.region = region;
      this.regionInfo = region.getWorldMapRegionInfo();
      this.pos = this.startPoint;
      this.scale = Vec3.ZERO;
      this.alpha = 0.0F;
      this.zOffset = 3.0F;
      GuiImageObject image = GuiObject.create("logo_base").image(WorldTabConstants.DEFAULT_INFO_IMAGE, 0.0F, 0.0F, 116.0F, 64.0F, 116.0F, 64.0F);
      image.alpha = 0.0F;
      image.pos(49.0F, 122.0F).zOffset(-1.0F);
      this.image = image;
      this.addChild(image);
   }

   private void generateInfo() {
      Tween infoTween = Tween.create().setTransitionType(TransitionType.SINE).setEase(EaseType.EASE_OUT).setLoops(1);
      GuiTextObject name = GuiObject.create("name").text(this.regionInfo.getName(), 862774774, true);
      name.scale(1.5F + (123.0F - name.getUnscaledWidth()) / 70.0F);
      name.pos(107.0F - name.getScaledWidth() / 2.0F, 0.0F);
      name.setShadow(true);
      name.setVisible(false);
      infoTween.tweenMethod(x -> {
         name.setColor(x.getARGB());
         name.setVisible(true);
      }, new OctoColor(292349430), new OctoColor(-9640458), 1.0);
      infoTween.parallel().tweenMethod(y -> name.pos(107.0F - name.getScaledWidth() / 2.0F, y), 0.0F, 16.0F, 1.0);
      MultiLineTextWidget mltw = new MultiLineTextWidget(Component.empty(), Minecraft.getInstance().font);
      mltw.setFGColor(16711680);
      GuiFittingMultiLineTextWidget description = new GuiFittingMultiLineTextWidget("description", widget -> {
         widget.setMultilineWidget(mltw);
         widget.setWidth(201);
         widget.setHeight(55);
         widget.setScrollColor(5452543, 8392692);
      });
      description.scale(0.88F).pos(19.0F, 72.0F);
      GuiImageObject image = GuiObject.create("logo").image(this.regionInfo.getImage(), 0.0F, 0.0F, 116.0F, 64.0F, 116.0F, 64.0F);
      image.alpha = 0.0F;
      image.pos(49.0F, 122.0F).zOffset(-0.5F);
      infoTween.tweenMethod(x -> image.alpha = x, 0.0F, 1.0F, 1.0);
      infoTween.parallel()
         .tweenMethod(x -> {
            String string = this.regionInfo.getDescription().substring(0, x.intValue());
            mltw.setMessage(Component.literal(string));
         }, 0.0F, (float)this.regionInfo.getDescription().length(), (double)((float)this.regionInfo.getDescription().length() / 100.0F))
         .setTransitionType(TransitionType.LINEAR)
         .setDelay(0.3);
      this.addChild(name);
      this.addChild(description);
      this.addChild(image);
      this.addChild(this.generateButtons());
      infoTween.start();
   }

   private GuiObject generateButtons() {
      GuiObject buttons = GuiObject.create("buttons").empty();
      int counter = 0;

      for (int i = 0; i < 3; i++) {
         WMRButton button = null;

         try {
            button = this.regionInfo.getButtons().get(this.buttonsPage * 3 + i);
         } catch (Exception var6) {
         }

         if (button == null) {
            break;
         }

         GuiObject guiButton = GuiRegionButtonObject.regionButtonBuilder()
            .name(this.region.getId().getPath() + "_button_" + button.getName())
            .message(Component.empty())
            .enabled(button.isEnabled())
            .customTexture(button.getTexture())
            .callback(button.callback(this.region))
            .button(button)
            .alpha(1.0F)
            .build()
            .size(32.0F, 32.0F)
            .pos((float)(i * 48), 0.0F);
         buttons.addChild(guiButton);
         counter++;
      }

      buttons.scale(1.0F);
      buttons.centered((float)(214 - 16 * (2 + 3 * (counter - 1))), 376.0F);
      return buttons;
   }

   protected void render(Graphics gfx, MousePos pos) {
      if (this.tween == null) {
         float centerX = 133.0F;
         float centerY = 17.5F;
         this.tween = Tween.create().setTransitionType(TransitionType.CIRC).setEase(EaseType.EASE_IN_OUT).setLoops(1);
         this.tween.tweenMethod(this::setAlpha, 0.0F, 1.0F, 0.8).setTransitionType(TransitionType.CUBIC).setEaseType(EaseType.EASE_OUT);
         this.tween.parallel().tweenMethod(this::setSize, 0.0, 1.0, 0.6).setTransitionType(TransitionType.CUBIC).setEaseType(EaseType.EASE_OUT);
         this.tween.parallel().tweenMethod(this::setXPos, this.pos.x(), centerX, 0.6).setTransitionType(TransitionType.CUBIC).setEaseType(EaseType.EASE_OUT);
         this.tween.parallel().tweenMethod(this::setYPos, this.pos.y(), centerY, 0.6).setTransitionType(TransitionType.CUBIC).setEaseType(EaseType.EASE_OUT);
         this.tween.tweenRunnable(this::generateInfo);
         this.tween.tweenInterval(1.0);
         this.tween.start();
      }

      super.render(gfx, pos);
   }

   public void setSize(double size) {
      this.scale = new Vec3(size, size, size);
   }

   public void setXPos(float x) {
      this.pos = new Point(x, this.pos.y());
   }

   public void setYPos(float y) {
      this.pos = new Point(this.pos.x(), y);
   }

   public void setAlpha(float alpha) {
      this.alpha = alpha;
      this.image.alpha = alpha;
   }

   public Point getPos() {
      return this.pos;
   }

   public double getScale() {
      return this.scale.x;
   }

   @Generated
   public static GuiRegionInfoObject.GuiRegionInfoObjectBuilder regionInfoBuilder() {
      return new GuiRegionInfoObject.GuiRegionInfoObjectBuilder();
   }

   @Generated
   public static class GuiRegionInfoObjectBuilder {
      @Generated
      private WorldMapRegion region;

      @Generated
      GuiRegionInfoObjectBuilder() {
      }

      @Generated
      public GuiRegionInfoObject.GuiRegionInfoObjectBuilder region(WorldMapRegion region) {
         this.region = region;
         return this;
      }

      @Generated
      public GuiRegionInfoObject build() {
         return new GuiRegionInfoObject(this.region);
      }

      @Generated
      @Override
      public String toString() {
         return "GuiRegionInfoObject.GuiRegionInfoObjectBuilder(region=" + this.region + ")";
      }
   }
}
