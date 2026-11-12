package com.storyteam.storytelling.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.script.story.npc.NpcPortraits;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.content.overlays.Message;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.RenderTools;
import com.storyteam.storytelling.util.RenderUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.zeith.hammerlib.client.utils.Scissors;

public class ChatLogScreen extends Screen {
   private static final int ICON_SIZE = 32;
   private ScriptEnvironment environment;
   public float ticker = 0.0F;
   public int scrollOffset = 0;
   public int sizeOffset = 0;

   public ChatLogScreen() {
      super(Component.empty());
   }

   protected void init() {
      super.init();
      IStoryServer storyServer = IStoryServer.get();
      ScriptEnvironment env = storyServer.getScriptEnv();
      this.environment = env;
      Minecraft.getInstance().options.hideGui = true;
   }

   public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      if (this.environment == null) {
         this.onClose();
      } else {
         float resolution = 1.1043478F;
         float bgX = (float)this.width / 2.0F - (float)this.height / 2.0F * resolution;
         guiGraphics.pose().pushPose();
         RenderSystem.setShaderTexture(0, St.id("textures/gui/chatlog/bg.png"));
         RenderTools.blitWithBlend(
            guiGraphics, bgX, 0.0F, 0.0F, 0.0F, (float)this.height * resolution, (float)this.height, (float)this.height * resolution, (float)this.height, 1.0F
         );
         guiGraphics.pose().popPose();
         this.renderChat(guiGraphics, pPartialTick);
         guiGraphics.pose().pushPose();
         RenderSystem.setShaderTexture(0, St.id("textures/gui/chatlog/bgframe.png"));
         RenderTools.blitWithBlend(
            guiGraphics, bgX, 0.0F, 0.0F, 0.0F, (float)this.height * resolution, (float)this.height, (float)this.height * resolution, (float)this.height, 1.0F
         );
         guiGraphics.pose().popPose();
      }
   }

   public void renderChat(GuiGraphics guiGraphics, float pPartialTick) {
      List<Message> list = this.environment.getChatHistory();
      this.ticker += pPartialTick;
      int index = 0;
      this.scrollOffset = Mth.clamp(this.scrollOffset, -6, this.sizeOffset + (int)((float)this.height * 0.075F) + 6);
      int yOffset = this.scrollOffset - this.sizeOffset;
      this.sizeOffset = -this.height + (int)((float)this.height * 0.048F) + 6;

      for (Message message : list) {
         float BLACKBOXALPHA = 0.7F;
         int MAXMESSAGEWIDTH = Math.round((float)Minecraft.getInstance().getWindow().getGuiScaledWidth() * 0.47F);
         int VERTICLAMARGINS = 2;
         int HORIZONTALMARGINS = 3;
         MutableComponent speakerName = message.speakerName().copy();
         MutableComponent flavor = message.flavor().copy();
         MutableComponent text = message.message().copy();
         MutableComponent comp = speakerName.append(flavor).append(Component.literal(":").withStyle(ChatFormatting.WHITE)).append(" ").append(text);
         Font font = Minecraft.getInstance().font;
         List<FormattedCharSequence> lines = font.split(comp, MAXMESSAGEWIDTH);
         int portraitHeight = NpcPortraits.height(message.speaker(), ICON_SIZE);
         int width = message.getBoxWidth(lines, font, MAXMESSAGEWIDTH, ICON_SIZE, 3);
         int height = message.getBoxHeight(lines, font, MAXMESSAGEWIDTH, portraitHeight, 2);
         int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - width / 2;
         int y = yOffset;
         guiGraphics.pose().pushPose();
         Scissors.begin(this.width / 2 - width / 2, (int)((float)this.height * 0.075F), width, (int)((float)this.height * 0.905F));
         float alpha = this.getAlpha(index * (30 / list.size()));
         Matrix4f m = guiGraphics.pose().last().pose();
         BufferBuilder vertex = Tesselator.getInstance().getBuilder();
         RenderSystem.setShader(GameRenderer::getPositionColorShader);
         RenderSystem.enableBlend();
         vertex.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
         vertex.vertex(m, (float)x, (float)(yOffset + height), 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.7F).endVertex();
         vertex.vertex(m, (float)(x + width), (float)(yOffset + height), 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.7F).endVertex();
         vertex.vertex(m, (float)(x + width), (float)yOffset, 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.7F).endVertex();
         vertex.vertex(m, (float)x, (float)yOffset, 0.0F).color(0.0F, 0.0F, 0.0F, alpha * 0.7F).endVertex();
         BufferUploader.drawWithShader(vertex.end());

         for (int i = 0; i < lines.size(); i++) {
            RenderUtils.drawString(
               guiGraphics,
               font,
               lines.get(i),
               x + ICON_SIZE + 6,
               y + height / 2 - 9 * lines.size() / 2 + 9 * i + 2 - 9 / 4,
               new Color(1.0F, 1.0F, 1.0F, alpha).getRGB()
            );
         }

         guiGraphics.pose().popPose();
         guiGraphics.pose().pushPose();
         RenderSystem.enableBlend();
         if (!NpcPortraits.draw(guiGraphics, message.speaker(), x + 3, y + height / 2 - portraitHeight / 2, ICON_SIZE, alpha)) {
            String icon = message.speaker().startsWith("glb:") ? "unknown" : message.speaker();
            ResourceLocation iconRL = St.id("textures/gui/chat/" + icon + ".png");
            RenderSystem.setShaderTexture(0, iconRL);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            guiGraphics.blit(iconRL, x + 3, y + height / 2 - ICON_SIZE / 2, 0.0F, 0.0F, ICON_SIZE, ICON_SIZE, 20, 20);
         }
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
         Scissors.end();
         guiGraphics.pose().popPose();
         index++;
         yOffset += height + 5;
         this.sizeOffset += height + 5;
      }
   }

   private float getAlpha(int delay) {
      return this.ticker < (float)(10 + delay) ? this.ticker / (float)(10 + delay) : 1.0F;
   }

   public void onClose() {
      super.onClose();
      Minecraft.getInstance().options.hideGui = false;
   }

   public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
      this.scrollOffset = Mth.clamp((int)((double)this.scrollOffset + pDelta * 16.0), -6, this.sizeOffset + (int)((float)this.height * 0.075F) + 6);
      return super.mouseScrolled(pMouseX, pMouseY, pDelta);
   }

   public boolean isPauseScreen() {
      return true;
   }
}
