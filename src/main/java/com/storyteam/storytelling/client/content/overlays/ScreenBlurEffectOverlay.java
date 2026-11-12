package com.storyteam.storytelling.client.content.overlays;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.init.EffectsST;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Pre;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class ScreenBlurEffectOverlay {
   private static final ResourceLocation OVERLAY = St.tx("gui/overlay/blur_screen_layer");

   @SubscribeEvent
   public static void onOverlayRender(Pre event) {
      if (event.getOverlay() == VanillaGuiOverlay.VIGNETTE.type()) {
         GuiGraphics guiGraphics = event.getGuiGraphics();
         Minecraft mc = Minecraft.getInstance();
         Window window = mc.getWindow();
         LocalPlayer player = mc.player;
         if (player != null) {
            MobEffectInstance effect = player.getEffect(EffectsST.SCREEN_BLUR);
            if (effect != null) {
               guiGraphics.pose().pushPose();
               RenderSystem.enableBlend();
               RenderSystem.setShaderTexture(0, OVERLAY);
               RenderSystem.setShader(GameRenderer::getPositionTexShader);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               mc.getTextureManager().getTexture(OVERLAY).bind();
               guiGraphics.blit(
                  OVERLAY, 0, 0, 0.0F, 0.0F, window.getGuiScaledWidth(), window.getGuiScaledHeight(), window.getGuiScaledWidth(), window.getGuiScaledHeight()
               );
               RenderSystem.disableBlend();
               guiGraphics.pose().popPose();
            }
         }
      }
   }
}
