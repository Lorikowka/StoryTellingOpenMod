package com.storyteam.storytelling.system.simulator.main_page.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.DrawUtils;
import com.storyteam.storytelling.system.simulator.main_page.MainTabSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MainTabButton {
   private final Minecraft mc;
   public float scaledX;
   public float scaledY;
   public float offsetX;
   public float offsetY;
   public float width;
   public float height;
   private float scale = 1.0F;
   private List<Component> tooltip = new ArrayList<>();
   public final MainTabButton.IPressable onPress;
   private boolean isHover;
   private boolean isBlocked;
   public final ResourceLocation texture;

   public MainTabButton(float width, float height, String texture, MainTabButton.IPressable onPress) {
      this.width = width;
      this.height = height;
      this.onPress = onPress;
      MainTabSystem system = MainTabSystem.INSTANCE;
      this.texture = system.guiTex(texture);
      this.mc = Minecraft.getInstance();
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
      float x = this.scaledX + this.offsetX * this.scale;
      float y = this.scaledY + this.offsetY * this.scale;
      float width = this.width * this.scale;
      float height = this.height * this.scale;
      this.isHover = (float)mouseX >= x && (float)mouseX <= x + width && (float)mouseY >= y && (float)mouseY <= y + height;
      DrawUtils.blit(poseStack, this.texture, x, y, width, height, 0.0F, this.getTextureY(), width, height * 3.0F);
   }

   public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
      if (this.isHover) {
         guiGraphics.renderComponentTooltip(this.mc.font, this.tooltip, mouseX, mouseY);
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (!this.isBlocked) {
         if (this.isHover) {
            this.onPress.onPress(this, button);
         }
      }
   }

   private float getTextureY() {
      if (this.isBlocked) {
         return 0.0F;
      } else {
         return this.isHover ? this.height * this.scale : this.height * this.scale * 2.0F;
      }
   }

   public void setTooltip(List<Component> tooltip) {
      this.tooltip = tooltip;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public MainTabButton setPos(float scaledX, float scaledY, float offsetX, float offsetY) {
      this.scaledX = scaledX;
      this.scaledY = scaledY;
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      return this;
   }

   public MainTabButton setScale(float scale) {
      this.scale = scale;
      return this;
   }

   public MainTabButton setBlocked(boolean blocked) {
      this.isBlocked = blocked;
      return this;
   }

   public boolean isBlocked() {
      return this.isBlocked;
   }

   public boolean isHover() {
      return this.isHover;
   }

   @OnlyIn(Dist.CLIENT)
   public interface IPressable {
      void onPress(MainTabButton var1, int var2);
   }
}
