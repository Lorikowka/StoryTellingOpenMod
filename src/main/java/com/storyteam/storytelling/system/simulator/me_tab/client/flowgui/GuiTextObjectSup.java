package com.storyteam.storytelling.system.simulator.me_tab.client.flowgui;

import lombok.Generated;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;

public class GuiTextObjectSup extends GuiObject {
   protected Font font;
   protected FormattedCharSequence text;
   public int color;
   public boolean shadow;

   public GuiTextObjectSup(String name, Font font, FormattedCharSequence text, int color, boolean shadow) {
      super(name);
      this.font = font;
      this.text = text;
      this.color = color;
      this.shadow = shadow;
      this.updateDimensions();
   }

   public GuiTextObjectSup setColor(int color) {
      this.color = color;
      return this;
   }

   public GuiTextObjectSup setShadow(boolean shadow) {
      this.shadow = shadow;
      return this;
   }

   public GuiTextObjectSup setFont(Font font) {
      this.font = font;
      this.updateDimensions();
      return this;
   }

   public GuiTextObjectSup setText(Component text) {
      return this.setText(text.getVisualOrderText());
   }

   public GuiTextObjectSup setText(FormattedCharSequence text) {
      this.text = text;
      this.updateDimensions();
      return this;
   }

   public void updateDimensions() {
      this.width = (float)this.font.width(this.text);
      this.height = 9.0F;
   }

   protected void render(Graphics gfx, MousePos pos) {
      gfx.drawString(this.font, this.text, 0, 0, this.color, this.shadow);
   }

   @Generated
   public Font getFont() {
      return this.font;
   }

   @Generated
   public FormattedCharSequence getText() {
      return this.text;
   }
}
