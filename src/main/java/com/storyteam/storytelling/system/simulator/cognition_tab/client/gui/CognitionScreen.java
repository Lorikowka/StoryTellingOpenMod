package com.storyteam.storytelling.system.simulator.cognition_tab.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.client.screen.base.IAutoScaled;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.ico.Ico;
import com.storyteam.storytelling.system.shaders.glitch.GlitchRendererPost;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHolder;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionPoseConfig;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionSystem;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.DrawUtils;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.gui.components.CognitionBlock;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.C2SSyncCognitionCustomReques;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.C2SSyncCognitionData;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionCustomRequement;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionTierInfo;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CustomUpgradeHolder;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.net.Network;

public class CognitionScreen extends Screen implements IAutoScaled {
   public static final CognitionScreen INSTANCE = new CognitionScreen();
   private boolean init = false;
   private float offsetX;
   private float offsetY;
   private float finOffsetX;
   private float finOffsetY;
   private float finScrolledScale = 2.5F;
   private float scrolledScale = 2.5F;
   private float fullScale = 1.0F;
   private final float backWidth = 10240.0F;
   private final float backHeight = 5760.0F;
   private float tooltipDelayTimer;
   private final float minScale = 1.0F;
   private final float maxScale = 2.5F;
   private final int tooltipDelay = 10;
   private final Set<CognitionBlock> cognitionBlocks = new HashSet<>();
   public CognitionBlock hoverBlock = null;
   private final GlitchRendererPost glitch = new GlitchRendererPost();

   private CognitionScreen() {
      super(Component.literal("Cognition"));
   }

   public void init() {
      CognitionPoseConfig.reloadCache();
      this.cognitionBlocks.clear();

      for (Cognition cognition : CognitionHolder.INSTANCE.getCognitions()) {
         this.cognitionBlocks.add(new CognitionBlock(cognition));
      }

      Network.sendToServer(new C2SSyncCognitionCustomReques());
      this.init = true;
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      super.render(guiGraphics, mouseX, mouseY, partialTick);
      if (this.minecraft != null) {
         if (!this.init) {
            this.init();
         }

         PoseStack poseStack = guiGraphics.pose();
         Window window = this.minecraft.getWindow();
         CognitionSystem sys = CognitionSystem.INSTANCE;
         float SMOOTHING_FACTOR = 0.1F;
         this.finScrolledScale = this.finScrolledScale + (this.scrolledScale - this.finScrolledScale) * SMOOTHING_FACTOR;
         this.finOffsetX = this.finOffsetX + (this.offsetX - this.finOffsetX) * SMOOTHING_FACTOR;
         this.finOffsetY = this.finOffsetY + (this.offsetY - this.finOffsetY) * SMOOTHING_FACTOR;
         float addedScale = (float)Math.round(Math.min((float)window.getWidth() / 1920.0F, (float)window.getHeight() / 1080.0F) / 0.1F) * 0.1F;
         this.fullScale = (float)(1.0 / window.getGuiScale()) * addedScale * this.finScrolledScale;
         float backScale = (float)Math.sqrt((double)(this.fullScale / 12.0F));
         poseStack.pushPose();
         poseStack.scale(backScale, backScale, 1.0F);
         float backX = (float)window.getGuiScaledWidth() / 2.0F / backScale - 5120.0F + this.finOffsetX / 3.0F;
         float backY = (float)window.getGuiScaledHeight() / 2.0F / backScale - 2880.0F + this.finOffsetY / 3.0F;
         DrawUtils.blit(poseStack, sys.guiTex("background4"), backX, backY, 10240.0F, 5760.0F);
         poseStack.popPose();
         poseStack.pushPose();
         poseStack.scale(this.fullScale, this.fullScale, 1.0F);
         float centerX = (float)window.getGuiScaledWidth() / 2.0F / this.fullScale;
         float centerY = (float)window.getGuiScaledHeight() / 2.0F / this.fullScale;
         float circleSize = 3000.0F;
         float circleX = centerX - circleSize / 2.0F + this.finOffsetX;
         float circleY = centerY - circleSize / 2.0F + this.finOffsetY;
         DrawUtils.blit(poseStack, sys.guiTex("circle2"), circleX, circleY, circleSize, circleSize);
         this.hoverBlock = null;

         for (CognitionBlock block : this.cognitionBlocks) {
            block.setCenter(circleX + circleSize / 2.0F, circleY + circleSize / 2.0F);
            block.setScale(this.fullScale);
            block.render(poseStack, mouseX, mouseY, partialTick);
            if (block.isHover() && (this.hoverBlock == null || !this.hoverBlock.isDragging())) {
               this.hoverBlock = block;
            }
         }

         for (CognitionBlock blockx : this.cognitionBlocks) {
            Cognition cognition = blockx.getCognition();

            for (Cognition dep : cognition.dependencies()) {
               boolean cardResearched = CognitionHelper.isCardResearched(this.minecraft.player, cognition);
               DrawUtils.LineInfo lineInfo = DrawUtils.getLineInfo(cognition, dep, blockx.width, blockx.height, -2.0F, -8.0F);
               float startX = lineInfo.startX() + centerX + this.finOffsetX;
               float startY = lineInfo.startY() + centerY + this.finOffsetY;
               float endX = lineInfo.endX() + centerX + this.finOffsetX;
               float endY = lineInfo.endY() + centerY + this.finOffsetY;
               Color lineColor = cardResearched ? new Color(138, 34, 174) : new Color(65, 65, 93);
               DrawUtils.drawLine(poseStack, startX, startY, endX, endY, 4.0F * this.fullScale, lineColor);
               poseStack.pushPose();
               float arrowX = lineInfo.offsetX() - 0.5F + centerX + this.finOffsetX;
               float arrowY = lineInfo.offsetY() - 0.5F + centerY + this.finOffsetY;
               poseStack.translate(arrowX, arrowY, 0.0F);
               poseStack.rotateAround(Axis.ZP.rotation(lineInfo.angle()), 0.0F, 0.0F, 0.0F);
               float arrowWidth = 7.0F;
               float arrowHeight = 14.0F;
               ResourceLocation arrowTexture = cardResearched ? sys.guiTex("arrow_active") : sys.guiTex("arrow_unactive");
               DrawUtils.blit(poseStack, arrowTexture, 0.0F - arrowWidth / 2.0F, 0.0F - arrowHeight / 2.0F, arrowWidth, arrowHeight);
               poseStack.popPose();
            }
         }

         poseStack.popPose();
         this.renderTooltip(guiGraphics, mouseX, mouseY, partialTick);
         if (CognitionPoseConfig.editorMod) {
            DrawUtils.fill(
               poseStack,
               0.0F,
               (float)(window.getGuiScaledHeight() - 12),
               (float)window.getGuiScaledWidth(),
               (float)window.getScreenHeight(),
               new Color(0, 0, 0, 120)
            );
            String editorText = "EDITOR MOD";
            DrawUtils.drawString(
               poseStack,
               editorText,
               (float)window.getGuiScaledWidth() / 2.0F - (float)this.font.width(editorText) / 2.0F,
               (float)(window.getGuiScaledHeight() - 10),
               1.0F,
               -65536,
               false
            );
         }

         this.glitch
            .render(
               poseStack, 0.0F, 0.0F, (float)window.getGuiScaledWidth(), (float)window.getGuiScaledWidth(), 0.5F, 3.0F, 3.0F, 22.0F, 400.0F, 20.0F, partialTick
            );
      }
   }

   public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      if (this.minecraft != null) {
         if (this.hoverBlock == null) {
            this.tooltipDelayTimer = 0.0F;
         } else {
            this.tooltipDelayTimer += partialTick;
            float ratio = Mth.clamp(this.tooltipDelayTimer / 5.0F, 0.0F, 1.0F);
            int alpha = (int)(ratio * 255.0F);
            float percent = Mth.clamp(this.tooltipDelayTimer / 10.0F, 0.0F, 1.0F);
            float barWidth = this.hoverBlock.width * this.fullScale;
            float barHeight = 3.0F * this.fullScale;
            float barX = this.hoverBlock.x * this.fullScale;
            float barY = this.hoverBlock.y * this.fullScale + this.hoverBlock.height * this.fullScale + 3.0F * this.fullScale;
            PoseStack poseStack = guiGraphics.pose();
            DrawUtils.fill(poseStack, barX, barY, barWidth * percent, barHeight, new Color(250, 18, 249, alpha));
            if (!(this.tooltipDelayTimer < 10.0F)) {
               LocalPlayer player = this.minecraft.player;
               if (player != null) {
                  alpha = CognitionHelper.getTier(player, this.hoverBlock.getCognition());
                  CognitionTierInfo tierInfo = CognitionHelper.getTierInfo(this.hoverBlock.getCognition(), this.hoverBlock.getHoverTier());
                  if (tierInfo != null) {
                     List<Component> tooltip = new ArrayList<>();
                     tooltip.add(Component.literal("§n§d" + this.hoverBlock.getCognition().title()));
                     tooltip.add(Component.literal(" "));
                     tooltip.add(Component.literal(tierInfo.description()));
                     tooltip.add(Component.literal(" "));
                     if (alpha >= this.hoverBlock.getHoverTier()) {
                        tooltip.add(Component.literal("§aПознание получено!"));
                     } else {
                        tooltip.add(Component.literal("Требования:"));
                        int level = tierInfo.level();
                        String lvlColor = PlayerStats.getLevel() >= level && level >= 0 ? "§a" : "§c";
                        String sLevel = level < 0 ? "?" : String.valueOf(level);
                        tooltip.add(Component.literal("  - Уровень: " + lvlColor + sLevel));
                        int eidos = tierInfo.eidos();
                        String eidosColor = CurrencyStorage.getEidos() >= eidos && eidos >= 0 ? "§a" : "§c";
                        String sEidos = eidos < 0 ? "?" : String.valueOf(eidos);
                        tooltip.add(Component.literal("  - Эйдосы: " + eidosColor + sEidos + "§r" + Ico.EIDOS.getSymbol()));
                        StringBuilder attS = new StringBuilder("  - Атрибуты: ");
                        Map<AttributeType, Integer> types = tierInfo.types();

                        for (Entry<AttributeType, Integer> entry : types.entrySet()) {
                           AttributeType attr = entry.getKey();
                           int value = entry.getValue();
                           boolean isEnough = value >= PlayerStats.getAttribute(attr).getValue();
                           String color = isEnough ? "§a" : "§c";
                           String sVal = value < 0 ? "?" : String.valueOf(value);
                           attS.append(color).append(sVal).append("§r").append(attr.ico().getSymbol());
                        }

                        tooltip.add(Component.literal(attS.toString()));

                        for (CognitionCustomRequement reque : tierInfo.customReques()) {
                           String requeColor = CustomUpgradeHolder.getCustomUpgradeState(reque.getId()) ? "§a" : "§c";
                           tooltip.add(Component.literal("  - " + requeColor + reque.getDescription()));
                        }
                     }

                     if (CognitionPoseConfig.editorMod) {
                        tooltip.add(Component.literal(" "));
                        tooltip.add(Component.literal("§cAlt + ЛКМ - Прокачать познание без учета условий"));
                        tooltip.add(Component.literal("§cCtrl + ЛКМ - Понизить тир познания"));
                     }

                     guiGraphics.renderComponentTooltip(this.minecraft.font, tooltip, mouseX, mouseY);
                  }
               }
            }
         }
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      for (CognitionBlock block : this.cognitionBlocks) {
         block.mouseClicked(mouseX, mouseY, button);
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      for (CognitionBlock block : this.cognitionBlocks) {
         block.mouseReleased(mouseX, mouseY, button);
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
      if (this.hoverBlock != null) {
         this.hoverBlock.mouseDragged(mouseX, mouseY, button, dragX, dragY);
         return false;
      } else {
         float clampFactor = Math.max(0.0F, this.scrolledScale - 0.02F);
         this.offsetX = Mth.clamp(this.offsetX + (float)dragX / this.fullScale, -9216.0F * clampFactor, 9216.0F * clampFactor);
         this.offsetY = Mth.clamp(this.offsetY + (float)dragY / this.fullScale, -5184.0F * clampFactor, 5184.0F * clampFactor);
         return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
      if (this.minecraft == null) {
         return false;
      } else {
         Window window = this.minecraft.getWindow();
         float centerX = (float)window.getGuiScaledWidth() / 2.0F;
         float centerY = (float)window.getGuiScaledHeight() / 2.0F;
         float newScrolledScale = Mth.clamp(this.scrolledScale + (float)delta / 20.0F, 1.0F, 2.5F);
         float percentX = Math.abs((float)((mouseX - (double)centerX) / (double)((float)window.getGuiScaledWidth() / 2.0F)) * 100.0F);
         float percentY = Math.abs((float)((mouseY - (double)centerY) / (double)((float)window.getGuiScaledHeight() / 2.0F)) * 100.0F);
         float distance = (float)Math.sqrt((double)(percentX * percentX + percentY * percentY));
         if (distance > 15.0F) {
            float offsetFactor = 4.0F - distance / 50.0F;
            if (delta > 0.0 && newScrolledScale != this.scrolledScale) {
               this.offsetX += (centerX - (float)mouseX) / offsetFactor;
               this.offsetY += (centerY - (float)mouseY) / offsetFactor;
            }
         }

         this.scrolledScale = newScrolledScale;
         return true;
      }
   }

   public void reinit() {
      this.init = false;
   }

   public void onClose() {
      super.onClose();
      this.init = false;
      this.finOffsetX = 0.0F;
      this.finOffsetY = 0.0F;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      this.finScrolledScale = 2.5F;
      this.scrolledScale = 2.5F;
      this.fullScale = 1.0F;
      this.tooltipDelayTimer = 0.0F;
   }

   public static void open() {
      Minecraft minecraft = Minecraft.getInstance();
      Network.sendToServer(new C2SSyncCognitionData());
      minecraft.setScreen(INSTANCE);
   }
}
