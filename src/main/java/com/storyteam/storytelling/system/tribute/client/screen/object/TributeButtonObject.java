package com.storyteam.storytelling.system.tribute.client.screen.object;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.currency.type.EidosCurrency;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.FlowguiShaderRegistry;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.util.math.Point;

public class TributeButtonObject extends GuiObject {
   private final ResourceLocation customTexture;
   private final TributeButtonObject.OnPress callback;
   public Supplier<ShaderInstance> shader = FlowguiShaderRegistry.GUI_SHADER;
   private Vec2 iconGlobalPosition = new Vec2(0.0F, 0.0F);

   public TributeButtonObject(@NotNull String name, ResourceLocation customTexture, TributeButtonObject.OnPress onPress) {
      super(name);
      this.customTexture = customTexture;
      this.callback = onPress;
   }

   public static TributeButtonObject of(String name, ResourceLocation customTexture, TributeButtonObject.OnPress onPress) {
      return new TributeButtonObject(name, customTexture, onPress);
   }

   protected void render(Graphics gfx, MousePos pos) {
      Font font = Minecraft.getInstance().font;
      int currentCountRaw = CurrencyStorage.getEidos();
      int requiredCountRaw = TributeOfferStorage.getOffer().getRequiredCount();
      String currentCount = String.valueOf(currentCountRaw);
      String requiredCount = "/" + requiredCountRaw;
      String fullText = currentCount + requiredCount;
      int textWidth = font.width(fullText);
      int totalWidth = 23 + textWidth;
      int availableWidth = (int)this.width;
      int availableHeight = (int)this.height;
      int contentX = (availableWidth - totalWidth) / 2;
      int iconY = availableHeight / 2 - 9;
      int textY = (availableHeight - 9) / 2 + 2;
      this.renderButtonBg(gfx, pos, (int)this.width);
      int iconPosX = (int)(((Point)this.elementPosition.get()).x() + (float)contentX);
      int iconPosY = (int)(((Point)this.elementPosition.get()).y() + (float)iconY);
      this.iconGlobalPosition = new Vec2((float)iconPosX, (float)iconPosY);
      EidosCurrency.renderTexture(gfx.gfx(), contentX, iconY);
      EidosCurrency.renderText(gfx.gfx(), currentCountRaw, requiredCountRaw, currentCount, requiredCount, (float)(contentX + 18 + 5), (float)textY, false);
   }

   protected void renderButtonBg(Graphics gfx, MousePos pos, int width) {
      RenderSystem.enableBlend();
      RenderSystem.enableDepthTest();
      gfx.setColor(1.0F, 1.0F, 1.0F, 1.0F);
      gfx.drawManaged(
         () -> {
            RenderSystem.setShaderTexture(0, this.customTexture);
            GuiImageObject.blitWithBlend(
               this.shader,
               gfx.gfx(),
               0.0F,
               (float)this.getTextureY(pos.isMouseWithin(this)),
               (float)width,
               this.height,
               (float)width,
               this.height * 3.0F,
               1.0F,
               new Vec3(1.0, 1.0, 1.0)
            );
         }
      );
      gfx.setColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void onPress() {
      this.callback.onPress(this);
   }

   public boolean isEnabled() {
      int offerDay = TributeOfferStorage.getOfferDay();
      int currentDay = TimeController.get().getCurrentDay();
      return currentDay >= offerDay;
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      if (button == 0 && this.enabled && pos.isMouseWithin(this)) {
         if (!fake) {
            this.onPress();
         }

         return true;
      } else {
         return fake && this.enabled && pos.isMouseWithin(this);
      }
   }

   protected int getTextureY(boolean hovered) {
      int state = 1;
      if (!this.isEnabled()) {
         state = 0;
      } else if (hovered) {
         state = 2;
      }

      return (int)((float)state * this.height);
   }

   @Generated
   public Vec2 getIconGlobalPosition() {
      return this.iconGlobalPosition;
   }

   @OnlyIn(Dist.CLIENT)
   public interface OnPress {
      void onPress(TributeButtonObject var1);
   }
}
