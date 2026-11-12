package com.storyteam.storytelling.client.flowgui;

import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.util.math.Point;

public class GuiMultilineTextInput extends GuiMultilineTextObject {
   protected String message;
   protected int maxRows = 10;
   protected int maxLength = Integer.MAX_VALUE;
   protected boolean active;
   @Nullable
   protected GuiMultilineTextInput nextInput = null;

   public GuiMultilineTextInput(String name, String initMessage, boolean active, Consumer<MultiLineTextWidget> initializer) {
      super(name, initializer);
      this.message = initMessage;
      this.active = active;
   }

   public GuiMultilineTextInput(String name, boolean active, Consumer<MultiLineTextWidget> initializer) {
      this(name, "", active, initializer);
   }

   public GuiMultilineTextInput(String name, Consumer<MultiLineTextWidget> initializer) {
      this(name, "", true, initializer);
   }

   public GuiMultilineTextInput(String name, boolean active) {
      this(name, "", active, null);
   }

   public GuiMultilineTextInput(String name) {
      this(name, "", true, null);
   }

   public GuiMultilineTextInput setMaxRows(int maxRows) {
      this.maxRows = maxRows;
      return this;
   }

   public GuiMultilineTextInput setMaxLength(int length) {
      this.maxLength = length;
      return this;
   }

   @Override
   protected boolean onCharTyped(char codePoint, int modifiers) {
      if (!this.active) {
         return false;
      } else {
         if (this.message.length() + 1 <= this.maxLength) {
            this.addChar(codePoint);
         }

         return super.onCharTyped(codePoint, modifiers);
      }
   }

   @Override
   protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
      if (!this.active) {
         return false;
      } else if ((modifiers & 2) == 2 && keyCode == 86) {
         String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
         this.addString(clipboard);
         return true;
      } else if (keyCode == 257 || keyCode == 335) {
         if (this.message.split("\n").length + 1 <= this.maxRows && this.message.length() + 1 <= this.maxLength) {
            this.addChar('\n');
         }

         return true;
      } else if (keyCode == 259) {
         this.deleteChars(1);
         return true;
      } else if (keyCode == 258 && this.nextInput != null) {
         this.nextInput.active = true;
         this.active = false;
         return true;
      } else {
         return super.onKeyPressed(keyCode, scanCode, modifiers);
      }
   }

   @Override
   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      if (!fake && button == 0) {
         this.active = pos.isMouseWithin(this);
      }

      return super.onMouseClicked(globalMousePos, pos, button, fake);
   }

   protected void addChar(char codePoint) {
      this.message = this.message + codePoint;
      this.wrapped.setMessage(ComponentUtils.mergeStyles(Component.literal(this.message), this.wrapped.getMessage().getStyle()));
   }

   protected void addString(String text) {
      this.message = this.message + text;
      this.wrapped.setMessage(ComponentUtils.mergeStyles(Component.literal(this.message), this.wrapped.getMessage().getStyle()));
   }

   protected void deleteChars(int count) {
      this.message = this.message.substring(0, Math.max(this.message.length() - count, 0));
      this.wrapped.setMessage(ComponentUtils.mergeStyles(Component.literal(this.message), this.wrapped.getMessage().getStyle()));
   }

   private boolean cursorVisible() {
      return this.active && System.currentTimeMillis() / 500L % 2L == 0L;
   }

   @Override
   protected void render(Graphics gfx, MousePos pos) {
      MutableComponent curComponent = ComponentUtils.mergeStyles(Component.literal(this.message), this.wrapped.getMessage().getStyle());
      if (this.cursorVisible()) {
         curComponent.append(Component.literal("|").withStyle(ChatFormatting.GRAY));
         this.wrapped.setMessage(curComponent);
      } else {
         this.wrapped.setMessage(curComponent);
      }

      super.render(gfx, pos);
   }

   @Generated
   public String getMessage() {
      return this.message;
   }

   @Generated
   public void setNextInput(@Nullable GuiMultilineTextInput nextInput) {
      this.nextInput = nextInput;
   }
}
