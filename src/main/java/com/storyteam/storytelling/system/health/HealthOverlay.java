package com.storyteam.storytelling.system.health;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.zeith.hammerlib.client.utils.Scissors;
import org.zeith.hammerlib.util.mcf.Resources;

public class HealthOverlay implements IGuiOverlay {
   public static final ResourceLocation HEALTH_OVERLAY_TEXTURE_LOCKED = Resources.location("storytelling", "textures/system/health/gui_hp_locked.png");
   public static final ResourceLocation HEALTH_OVERLAY_TEXTURE_BORDER = Resources.location("storytelling", "textures/system/health/gui_hp_frame.png");
   public static final ResourceLocation HEALTH_OVERLAY_TEXTURE_BACK = Resources.location("storytelling", "textures/system/health/gui_hp_background.png");
   public static final ResourceLocation HEALTH_OVERLAY_TEXTURE_SCALE = Resources.location("storytelling", "textures/system/health/gui_hp_scale.png");
   public static final ResourceLocation HEALTH_OVERLAY_TEXTURE_SCALE_FRAME = Resources.location("storytelling", "textures/system/health/gui_hp_frame_scale.png");
   public static final ResourceLocation HEALTH_OVERLAY_WAVES_LOCKED = Resources.location("storytelling", "textures/system/health/gui_hp_waves_back.png");
   public static final ResourceLocation HEALTH_OVERLAY_WAVES_SCALE = Resources.location("storytelling", "textures/system/health/gui_hp_waves_front.png");

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (Minecraft.getInstance().screen == null) {
         LocalPlayer player = Minecraft.getInstance().player;
         if (player != null && !player.isCreative() && !player.isSpectator()) {
            guiGraphics.pose().pushPose();
            int y = screenHeight - 90;
            guiGraphics.blit(HEALTH_OVERLAY_TEXTURE_LOCKED, 10, y, 0.0F, 0.0F, 68, 67, 68, 67);
            HealthSystem sys = HealthSystem.INSTANCE;
            float health = sys.getCurrentHealth(player);
            float maxHealth = player.getMaxHealth();
            float absoluteMaxHealth = (float)sys.getAbsoluteMaxHealth(player);
            float absoluteHealthScale = 1.0F - maxHealth / absoluteMaxHealth;
            float maxHealthScale = 1.0F - health / absoluteMaxHealth;
            Scissors.begin(0, (int)((float)y + 67.0F * absoluteHealthScale), 100, 100);
            guiGraphics.blit(HEALTH_OVERLAY_TEXTURE_BACK, 10, y, 0.0F, 0.0F, 68, 67, 68, 67);
            Scissors.end();
            this.startWaveScissors(maxHealthScale, y, true);
            this.renderWave(guiGraphics, HEALTH_OVERLAY_WAVES_LOCKED, y, partialTick, maxHealthScale, 1);
            Scissors.end();
            if (maxHealthScale < 0.2F) {
               this.startWaveScissors(maxHealthScale, y, false);
               this.renderWave(guiGraphics, HEALTH_OVERLAY_WAVES_LOCKED, y, partialTick, maxHealthScale, 1);
               Scissors.end();
            }

            Scissors.begin(0, (int)((float)y + 67.0F * maxHealthScale), 100, 100);
            guiGraphics.blit(HEALTH_OVERLAY_TEXTURE_SCALE, 10, y, 0.0F, 0.0F, 68, 67, 68, 67);
            Scissors.end();
            this.startWaveScissors(maxHealthScale, y, true);
            this.renderWave(guiGraphics, HEALTH_OVERLAY_WAVES_SCALE, y, partialTick, maxHealthScale, -1);
            Scissors.end();
            if (maxHealthScale < 0.2F) {
               this.startWaveScissors(maxHealthScale, y, false);
               this.renderWave(guiGraphics, HEALTH_OVERLAY_WAVES_SCALE, y, partialTick, maxHealthScale, -1);
               Scissors.end();
            }

            guiGraphics.blit(HEALTH_OVERLAY_TEXTURE_BORDER, 10, y, 0.0F, 0.0F, 68, 67, 68, 67);
            Scissors.begin(0, (int)((float)y + 67.0F * absoluteHealthScale), 100, 100);
            guiGraphics.blit(HEALTH_OVERLAY_TEXTURE_SCALE_FRAME, 10, y, 0.0F, 0.0F, 68, 67, 68, 67);
            Scissors.end();
            guiGraphics.pose().popPose();
         }
      }
   }

   private void startWaveScissors(float scale, int y, boolean isLeft) {
      if (scale >= 0.2F) {
         int sX = 10;
         int sW = 100;
         if (scale >= 0.95F) {
            sX += 30;
            sW -= 90;
         } else if (scale >= 0.9F) {
            sX += 25;
            sW -= 85;
         } else if (scale >= 0.85F) {
            sX += 23;
            sW -= 78;
         } else if (scale >= 0.8F) {
            sX += 20;
            sW -= 72;
         } else if (scale >= 0.75F) {
            sX += 17;
            sW -= 67;
         } else if (scale >= 0.7F) {
            sX += 11;
            sW -= 54;
         } else if (scale >= 0.65F) {
            sX += 10;
            sW -= 51;
         } else if (scale >= 0.6F) {
            sX += 9;
            sW -= 50;
         } else if (scale >= 0.55F) {
            sX += 5;
            sW -= 41;
         } else {
            sX += 3;
            sW -= 38;
         }

         Scissors.begin(sX, (int)((float)(y - 4) + 67.0F * scale), sW, 100);
      } else if (scale >= 0.15F) {
         if (isLeft) {
            Scissors.begin(12, (int)((float)(y - 4) + 67.0F * scale), 30, 100);
         } else {
            Scissors.begin(45, (int)((float)(y - 4) + 67.0F * scale), 30, 100);
         }
      } else if (scale >= 0.1F) {
         if (isLeft) {
            Scissors.begin(17, (int)((float)(y - 4) + 67.0F * scale), 19, 100);
         } else {
            Scissors.begin(52, (int)((float)(y - 4) + 67.0F * scale), 19, 100);
         }
      } else {
         Scissors.begin(0, 0, 1, 1);
      }
   }

   private void renderWave(GuiGraphics guiGraphics, ResourceLocation texture, int y, float partialTick, float scale, int direction) {
      float speedPxPerSec = 5.0F * (float)direction;
      long ticks = Minecraft.getInstance().level.getGameTime();
      float elapsedSec = ((float)ticks + partialTick) / 20.0F;
      int texWidth = 60;
      int texHeight = 4;
      int offsetX = (int)(elapsedSec * speedPxPerSec) % texWidth;
      int waveX = 10;
      int waveY = (int)((float)(y - 4) + 67.0F * scale);
      int targetWidth = 100;
      int seg1Width = Math.min(texWidth - offsetX, targetWidth);
      if (seg1Width > 0) {
         guiGraphics.blit(texture, waveX, waveY, (float)offsetX, 0.0F, seg1Width, texHeight, texWidth, texHeight);
      }

      int drawn;
      for (drawn = seg1Width; drawn + texWidth <= targetWidth; drawn += texWidth) {
         guiGraphics.blit(texture, waveX + drawn, waveY, 0.0F, 0.0F, texWidth, texHeight, texWidth, texHeight);
      }

      int tail = targetWidth - drawn;
      if (tail > 0) {
         guiGraphics.blit(texture, waveX + drawn, waveY, 0.0F, 0.0F, tail, texHeight, texWidth, texHeight);
      }
   }
}
