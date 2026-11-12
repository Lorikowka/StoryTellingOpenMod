package com.storyteam.storytelling.system.shaders;

import java.nio.file.Files;
import java.nio.file.Path;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.gui.GuiUtil;
import net.irisshaders.iris.gui.element.ShaderPackSelectionList;
import net.irisshaders.iris.gui.element.ShaderPackSelectionList.BaseEntry;
import net.irisshaders.iris.gui.element.ShaderPackSelectionList.ShaderPackEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public class AddShaderSettingsButton extends BaseEntry {
   public static String newSettings = null;
   private final ShaderPackSelectionList list;
   private ScreenRectangle bounds = ScreenRectangle.empty();

   public AddShaderSettingsButton(ShaderPackSelectionList list) {
      this.list = list;
   }

   public ScreenRectangle getRectangle() {
      return this.bounds;
   }

   public boolean isApplied() {
      return false;
   }

   public boolean isSelected() {
      return this.list.getSelected() == this;
   }

   public void render(
      GuiGraphics guiGraphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta
   ) {
      this.bounds = new ScreenRectangle(x, y, entryWidth, entryHeight);
      Font font = Minecraft.getInstance().font;
      int color = 16777215;
      String name = "Create new settings for currently selected shaderpack";
      if (hovered) {
         GuiUtil.bindIrisWidgetsTexture();
         GuiUtil.drawButton(guiGraphics, x - 2, y - 2, entryWidth, entryHeight + 4, hovered, false);
      }

      boolean shadersEnabled = this.list.getTopButtonRow().shadersEnabled;
      if (font.width(Component.literal(name).withStyle(ChatFormatting.BOLD)) > this.list.getRowWidth() - 3) {
         String var10000 = font.plainSubstrByWidth(name, this.list.getRowWidth() - 8);
         name = var10000 + "...";
      }

      MutableComponent text = Component.literal(name);
      if (this.isMouseOver((double)mouseX, (double)mouseY)) {
         text = text.withStyle(ChatFormatting.BOLD);
      }

      if (shadersEnabled && this.isApplied()) {
         color = 16773731;
      }

      if (!shadersEnabled && !this.isMouseOver((double)mouseX, (double)mouseY)) {
         color = 10658466;
      }

      guiGraphics.drawCenteredString(font, text, x + entryWidth / 2 - 2, y + (entryHeight - 11) / 2, color);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return button != 0 ? false : this.doThing();
   }

   public boolean keyPressed(int keycode, int pInt1, int pInt2) {
      return keycode != 257 ? false : this.doThing();
   }

   private boolean doThing() {
      int i = 0;

      Path path;
      do {
         path = Iris.getShaderpacksDirectory().resolve("New shaderpack settings" + (i == 0 ? "" : "(%s)".formatted(i)) + ".txt");
         i++;
      } while (Files.exists(path));

      try {
         Files.createFile(path);
         String shaderPackName;
         if (this.list.getSelected() instanceof ShaderPackEntry shaderPackEntry) {
            shaderPackName = ShaderSystem.getConfiguredShaderPack(shaderPackEntry.getPackName()).shaderPackName;
         } else {
            shaderPackName = ShaderSystem.getCurrentConfiguredShaderPack().shaderPackName;
         }

         Files.writeString(path, "shaderpack: " + shaderPackName);
         newSettings = path.getFileName().toString();
      } catch (Exception var6) {
      }

      return true;
   }

   @Nullable
   public ComponentPath nextFocusPath(FocusNavigationEvent pGuiEventListener0) {
      return !this.isFocused() ? ComponentPath.leaf(this) : null;
   }

   public boolean isFocused() {
      return this.list.getFocused() == this;
   }
}
