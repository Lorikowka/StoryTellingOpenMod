package com.storyteam.storytelling.client.content.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.script.story.npc.NpcPortraits;
import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.RenderUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Matrix4f;

@EventBusSubscriber({Dist.CLIENT})
public class MessageOverlay implements IGuiOverlay {
   public static Message currentMessage = null;
   public static int ticker;
   public static boolean SV = false;
   private static final int ICON_SIZE = 32;
   private static final float BLACKBOXALPHA = 0.5F;
   private static final int VERTICLAMARGINS = 2;
   private static final int HORIZONTALMARGINS = 3;
   private static final float MESSAGEWIDTH_RATIO = 0.6F;
   private static final float CHAT_SCALE = 0.8F;

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (Minecraft.getInstance().screen == null) {
         renderMessageOverlay(gui, guiGraphics, partialTick, screenWidth, screenHeight);
      }
   }

   public static void renderMessageOverlay(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (ticker == 0) {
         currentMessage = null;
      }

      if (currentMessage != null) {
         guiGraphics.pose().pushPose();
         float alpha = Mth.clamp(getAlpha(), 0.0F, 1.0F);
         Matrix4f m = guiGraphics.pose().last().pose();
         BufferBuilder vertex = Tesselator.getInstance().getBuilder();
         RenderSystem.setShader(GameRenderer::getPositionColorShader);
         RenderSystem.enableBlend();
         vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
         int MAXMESSAGEWIDTH = Math.round((float)Minecraft.getInstance().getWindow().getGuiScaledWidth() * 0.6F);
         MutableComponent speakerName = currentMessage.speakerName().copy();
         MutableComponent flavor = currentMessage.flavor().copy();
         MutableComponent message = currentMessage.message().copy();
         MutableComponent comp = speakerName.append(flavor).append(Component.literal(":").withStyle(ChatFormatting.WHITE)).append(" ").append(message);
         List<FormattedCharSequence> lines = gui.getFont().split(comp, MAXMESSAGEWIDTH);
         int width = currentMessage.getBoxWidth(lines, gui.getFont(), MAXMESSAGEWIDTH, ICON_SIZE, 3);
         int height = currentMessage.getBoxHeight(lines, gui.getFont(), MAXMESSAGEWIDTH, ICON_SIZE, 2);
         int yOffset = SV ? 60 : (ClientsideCutsceneHandler.currentData == null ? 50 : 25);
         int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - width / 2;
         int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() - yOffset - height;
         vertex.vertex(m, (float)x, (float)(y + height), 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.5F).endVertex();
         vertex.vertex(m, (float)(x + width), (float)(y + height), 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.5F).endVertex();
         vertex.vertex(m, (float)(x + width), (float)y, 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.5F).endVertex();
         vertex.vertex(m, (float)x, (float)y, 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.5F).endVertex();
         BufferUploader.drawWithShader(vertex.end());

         for (int i = 0; i < lines.size(); i++) {
            RenderUtils.drawString(
               guiGraphics,
               gui.getFont(),
               lines.get(i),
               x + ICON_SIZE + 6,
               y + height / 2 - 9 * lines.size() / 2 + 9 * i + 2 - 9 / 4,
               new Color(1.0F, 1.0F, 1.0F, alpha).getRGB()
            );
         }

         guiGraphics.pose().scale(1.0F, 1.0F, 1.0F);
         guiGraphics.pose().popPose();
         guiGraphics.pose().pushPose();
         RenderSystem.enableBlend();
         if (!NpcPortraits.draw(guiGraphics, currentMessage.speaker(), x + 3, y + height / 2 - ICON_SIZE / 2, ICON_SIZE, alpha)) {
            String icon = currentMessage.speaker().startsWith("glb:") ? "unknown" : currentMessage.speaker();
            ResourceLocation iconRL = St.id("textures/gui/chat/" + icon + ".png");
            RenderSystem.setShaderTexture(0, iconRL);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            guiGraphics.blit(iconRL, x + 3, y + height / 2 - ICON_SIZE / 2, 0.0F, 0.0F, ICON_SIZE, ICON_SIZE, 20, 20);
         }
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
         guiGraphics.pose().popPose();
      }
   }

   @SubscribeEvent
   public static void tick(PlayerTickEvent event) {
      if (event.phase == Phase.END && event.player.level().isClientSide) {
         ticker = Mth.clamp(ticker - 1, 0, Integer.MAX_VALUE);
      }
   }

   private static float getAlpha() {
      int t = currentMessage.inTime() + currentMessage.time() + currentMessage.outTime() - ticker;
      float tick = Mth.lerp(Minecraft.getInstance().getDeltaFrameTime(), (float)t, (float)(t + 1));
      if (tick < (float)currentMessage.inTime() && currentMessage.inTime() != 0) {
         return tick / (float)currentMessage.inTime();
      } else {
         return t - currentMessage.inTime() < currentMessage.time()
            ? 1.0F
            : 1.0F - (tick - (float)currentMessage.inTime() - (float)currentMessage.time()) / (float)currentMessage.outTime();
      }
   }

   public static void setCurrentMessage(Message message) {
      currentMessage = message;
      ticker = message.inTime() + message.time() + message.outTime();
   }
}
