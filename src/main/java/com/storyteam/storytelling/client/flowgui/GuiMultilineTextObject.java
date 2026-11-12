package com.storyteam.storytelling.client.flowgui;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.util.math.Point;

public class GuiMultilineTextObject extends GuiObject {
   public MultiLineTextWidget wrapped;
   public final Consumer<MultiLineTextWidget> initializer;

   public GuiMultilineTextObject(String name, Consumer<MultiLineTextWidget> initializer) {
      super(name);
      this.initializer = initializer;
      this.wrapped = this.createWrapped();
   }

   protected MultiLineTextWidget createWrapped() {
      MultiLineTextWidget multiLineText = new MultiLineTextWidget(Component.empty(), Minecraft.getInstance().font);
      if (this.initializer != null) {
         this.initializer.accept(multiLineText);
      }

      this.size((float)multiLineText.getWidth(), (float)multiLineText.getHeight());
      return multiLineText;
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.wrapped.setWidth((int)this.width);
      this.wrapped.setHeight((int)this.height);
      this.wrapped.render(gfx.gfx(), (int)pos.relX(), (int)pos.relY(), gfx.partialTime());
   }

   protected boolean onMouseDragged(Point globalMousePos, MousePos pos, int button, MousePos dragPos) {
      return this.wrapped.mouseDragged((double)pos.relX(), (double)pos.relY(), button, (double)dragPos.relX(), (double)dragPos.relY());
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

   public GuiMultilineTextObject initializer(Consumer<MultiLineTextWidget> initializer) {
      if (this.wrapped != null) {
         initializer.accept(this.wrapped);
      }

      return this;
   }
}
