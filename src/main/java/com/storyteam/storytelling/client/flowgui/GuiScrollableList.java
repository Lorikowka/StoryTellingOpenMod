package com.storyteam.storytelling.client.flowgui;

import com.google.common.collect.Streams;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.util.ScrollData;
import org.zeith.hammerlib.util.math.Point;

public class GuiScrollableList extends GuiObject {
   private final String prefix;
   @Nullable
   private Consumer<Float> onScroll = null;

   public GuiScrollableList(
      String prefix,
      List<GuiObject> objects,
      int objectsPerRow,
      boolean hideNoScroll,
      float height,
      float width,
      float objHeight,
      float gap,
      float sliderWidth,
      Supplier<GuiImageObject> scrollBarBg,
      Supplier<GuiImageObject> scrollBar,
      float initialScroll
   ) {
      super(prefix + "_scrollable_list");
      this.prefix = prefix;
      this.pos(0.0F, 0.0F);
      this.size(width + 2.0F + sliderWidth, height);
      GuiObject list = new GuiObject(prefix + "_items");

      for (int i = 0; i < objects.size(); i++) {
         float rowHeight = this.calcRowStartY(i, objectsPerRow, list, gap);
         GuiObject obj = objects.get(i).pos((float)(i % objectsPerRow) * (objects.get(i).getScaledWidth() + gap), rowHeight);
         list.addChild(obj);
      }

      GuiObject scissors = new GuiScissorsObject(prefix + "_scissors").size(width + 2.0F, height);
      scissors.addChild(list);
      this.addChild(scissors);
      float listHeight = (float)Streams.stream(list.getChildren())
         .mapToDouble(e -> (double)(((Point)e.elementPosition.get()).y() + e.getScaledHeight()))
         .max()
         .orElse(0.0);
      float scrollHeight = listHeight > scissors.getScaledHeight()
         ? scissors.getScaledHeight() / (float)objects.size() / (float)objectsPerRow * (scissors.getScaledHeight() / objHeight) * 0.9F
         : scissors.getScaledHeight();
      GuiObject scroll = new GuiScrollBarObject(prefix + "_scrollbar", scrollBarBg, scrollBar, sliderWidth, scrollHeight)
         .pos(width + 2.0F, 0.0F)
         .size(sliderWidth, scissors.getScaledHeight());
      ((GuiScrollBarObject)scroll).setSliderValueY(initialScroll);
      list.onPreRender((p, m) -> {
         if (!(scissors.getScaledHeight() - scrollHeight <= 0.0F)) {
            float scrollPos = ((GuiScrollBarObject)scroll).getPosY();
            if (this.onScroll != null) {
               this.onScroll.accept(scrollPos);
            }

            float scrollPercent = scrollPos / (scissors.getScaledHeight() - scrollHeight);
            list.pos(0.0F, -scrollPercent * (listHeight - scissors.getScaledHeight()));
         }
      });
      if (listHeight > scissors.getScaledHeight() || !hideNoScroll) {
         this.addChild(scroll);
      }
   }

   public GuiScrollableList(
      String prefix,
      List<GuiObject> objects,
      int objectsPerRow,
      boolean hideNoScroll,
      float height,
      float width,
      float objHeight,
      float gap,
      float sliderWidth,
      Supplier<GuiImageObject> scrollBarBg,
      Supplier<GuiImageObject> scrollBar
   ) {
      this(prefix, objects, objectsPerRow, hideNoScroll, height, width, objHeight, gap, sliderWidth, scrollBarBg, scrollBar, 0.0F);
   }

   public GuiScrollableList setOnScroll(Consumer<Float> onScroll) {
      this.onScroll = onScroll;
      return this;
   }

   private float calcRowStartY(int curObjectIndex, int objectsPerRow, GuiObject list, float gap) {
      int curRow = curObjectIndex / objectsPerRow;
      return curRow == 0
         ? 0.0F
         : (float)Streams.stream(list.getChildren())
            .skip((long)(curRow - 1) * (long)objectsPerRow)
            .limit((long)objectsPerRow)
            .mapToDouble(e -> (double)(((Point)e.elementPosition.get()).y() + e.getScaledHeight() + gap))
            .max()
            .orElse(0.0);
   }

   protected boolean onMouseScrolled(Point globalMousePos, MousePos pos, ScrollData delta) {
      if (pos.isMouseWithin(this)) {
         GuiScrollBarObject scrollbar = (GuiScrollBarObject)this.getChild(this.prefix + "_scrollbar");
         if (scrollbar != null) {
            float newValue = (float)((double)scrollbar.getValueY() - delta.scrollY() / 8.0);
            scrollbar.setSliderValueY(newValue);
         }
      }

      return super.onMouseScrolled(globalMousePos, pos, delta);
   }
}
