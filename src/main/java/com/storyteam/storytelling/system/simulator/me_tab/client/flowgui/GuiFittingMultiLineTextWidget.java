package com.storyteam.storytelling.system.simulator.me_tab.client.flowgui;

import com.storyteam.storytelling.system.simulator.me_tab.client.TFittingMultiLineTextWidget;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.util.ScrollData;
import org.zeith.hammerlib.util.math.Point;

public class GuiFittingMultiLineTextWidget extends GuiObject {
   public TFittingMultiLineTextWidget wrapped;
   public final Consumer<TFittingMultiLineTextWidget> initializer;

   public GuiFittingMultiLineTextWidget(String name, Consumer<TFittingMultiLineTextWidget> initializer) {
      super(name);
      this.initializer = initializer;
      this.wrapped = this.createWrapped();
   }

   protected TFittingMultiLineTextWidget createWrapped() {
      TFittingMultiLineTextWidget multiLineText = new TFittingMultiLineTextWidget(0, 0, 100, 20, Component.empty(), Minecraft.getInstance().font, this);
      if (this.initializer != null) {
         this.initializer.accept(multiLineText);
      }

      this.size((float)multiLineText.getWidth(), (float)multiLineText.getHeight());
      return multiLineText;
   }

   public GuiObject pos(float x, float y) {
      return super.pos(x, y);
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.wrapped.setWidth((int)this.width);
      this.wrapped.setHeight((int)this.height);
      this.wrapped.render(gfx.gfx(), (int)pos.relX(), (int)pos.relY(), gfx.partialTime());
   }

   protected boolean onMouseDragged(Point globalMousePos, MousePos pos, int button, MousePos dragPos) {
      return this.wrapped.mouseDragged((double)pos.relX(), (double)pos.relY(), button, (double)dragPos.globalPos().x(), (double)dragPos.globalPos().y());
   }

   protected boolean onMouseReleased(Point globalMousePos, MousePos pos, int button) {
      return this.wrapped.mouseReleased((double)pos.relX(), (double)pos.relY(), button);
   }

   protected boolean onMouseScrolled(Point globalMousePos, MousePos pos, ScrollData delta) {
      return this.wrapped.mouseScrolled((double)pos.relX(), (double)pos.relY(), delta.scrollY());
   }

   protected boolean onCharTyped(char codePoint, int modifiers) {
      return this.wrapped.charTyped(codePoint, modifiers);
   }

   protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
      return this.wrapped.keyPressed(keyCode, scanCode, modifiers);
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      return this.wrapped.mouseClicked((double)pos.relX(), (double)pos.relY(), button);
   }

   public GuiFittingMultiLineTextWidget initializer(Consumer<TFittingMultiLineTextWidget> initializer) {
      if (this.wrapped != null) {
         initializer.accept(this.wrapped);
      }

      return this;
   }
}
