package com.storyteam.storytelling.system.simulator.cognition_tab.client.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionPoseConfig;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionSystem;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.DrawUtils;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.gui.CognitionScreen;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.utils.FontUtils;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.C2SDowngradeCognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.C2SUpgradeCognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionTierInfo;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import java.util.Map;
import java.util.TreeSet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.Network;

@OnlyIn(Dist.CLIENT)
public class CognitionBlock {
   private final Minecraft mc;
   private final Cognition cognition;
   public float centerX;
   public float centerY;
   public float x;
   public float y;
   public float relX;
   public float relY;
   public float width;
   public float height;
   private float scale = 1.0F;
   private final TreeSet<CognitionUpgradeComponent> upgrades = new TreeSet<>();
   protected boolean isHover;
   private boolean isDragging;
   public final CognitionSystem system = CognitionSystem.INSTANCE;
   public final ResourceLocation BACKGROUND_BLOCKED = this.system.guiTex("card/base_blocked");
   public final ResourceLocation BACKGROUND_UNBLOCKED = this.system.guiTex("card/base_unblocked");
   public final ResourceLocation BACKGROUND_RESEARCHED = this.system.guiTex("card/base_res");
   public final ResourceLocation BACKGROUND_HOVER_OUTLINE = this.system.guiTex("card/base_hover_outline");

   public CognitionBlock(Cognition cognition) {
      this.cognition = cognition;
      this.mc = Minecraft.getInstance();
      this.width = 96.0F;
      this.height = 44.0F;
      Cognition.Pos pos = cognition.pos();
      this.relX = pos.x();
      this.relY = pos.y();

      for (int i = 2; i < 5; i++) {
         this.upgrades.add(new CognitionUpgradeComponent(cognition, i));
      }
   }

   public Cognition getCognition() {
      return this.cognition;
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
      mouseX = (int)((float)mouseX / this.scale);
      mouseY = (int)((float)mouseY / this.scale);
      this.x = this.centerX + this.relX;
      this.y = this.centerY + this.relY;
      this.isHover = (float)mouseX >= this.x && (float)mouseX <= this.x + this.width && (float)mouseY >= this.y && (float)mouseY <= this.y + this.height;
      boolean researchedCard = CognitionHelper.getTier(this.mc.player, this.cognition) > 0;
      boolean canResearch = CognitionHelper.canResearch(this.mc.player, this.cognition);
      ResourceLocation background = researchedCard ? this.BACKGROUND_RESEARCHED : (!canResearch ? this.BACKGROUND_BLOCKED : this.BACKGROUND_UNBLOCKED);
      DrawUtils.blit(poseStack, background, this.x, this.y, this.width, this.height);
      if (this.isHover) {
         DrawUtils.blit(poseStack, this.BACKGROUND_HOVER_OUTLINE, this.x - 2.0F, this.y - 2.0F, this.width + 4.0F, this.height + 4.0F);
      }

      DrawUtils.blit(poseStack, this.cognition.icon(), this.x + 9.0F, this.y + 9.0F, 16.0F, 16.0F);
      CognitionTierInfo tierInfo = CognitionHelper.getTierInfo(this.cognition, 1);
      if (tierInfo != null) {
         Map<AttributeType, Integer> types = tierInfo.types();

         for (AttributeType type : types.keySet()) {
            DrawUtils.blit(poseStack, this.system.guiTex("card/icon_outline_" + type.name().toLowerCase()), this.x + 6.0F, this.y + 6.0F, 22.0F, 22.0F);
         }

         float titleY = this.y + 8.0F;
         float textScale = 0.7F;

         for (String s : FontUtils.wrapText(this.cognition.title(), 58.0F, textScale)) {
            DrawUtils.drawString(poseStack, s, this.x + 36.0F, titleY, textScale, -1, false);
            titleY += 8.0F;
         }

         if (researchedCard) {
            int combineType = AttributeType.combine(types.keySet().toArray(new AttributeType[0]));
            DrawUtils.blit(poseStack, this.system.guiTex("card/upgraded_" + combineType), this.x - 8.0F, this.y - 14.0F, 112.0F, 72.0F);
         }

         float upgradeX = this.x + 5.0F;
         float upgradeY = this.y + 33.0F;

         for (CognitionUpgradeComponent upgrade : this.upgrades) {
            upgrade.setPos(upgradeX, upgradeY);
            upgrade.setScale(this.scale);
            upgrade.render(poseStack, mouseX, mouseY, partialTicks);
            upgradeX += 29.0F;
         }
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isHover) {
         if (button == 0) {
            this.isDragging = true;
            boolean upgradeHover = false;

            for (CognitionUpgradeComponent upgrade : this.upgrades) {
               if (upgrade.isHover()) {
                  upgradeHover = true;
               }

               upgrade.mouseClicked(mouseX, mouseY, button);
            }

            if (!upgradeHover) {
               boolean isCtrlDow = InputConstants.isKeyDown(this.mc.getWindow().getWindow(), 341);
               boolean isAltDow = InputConstants.isKeyDown(this.mc.getWindow().getWindow(), 342);
               if (CognitionPoseConfig.editorMod) {
                  if (isAltDow) {
                     Network.sendToServer(new C2SUpgradeCognition(this.cognition, 1, true));
                  } else if (isCtrlDow) {
                     Network.sendToServer(new C2SDowngradeCognition(this.cognition, 1));
                  }
               } else {
                  Network.sendToServer(new C2SUpgradeCognition(this.cognition, 1, false));
               }
            }
         }
      }
   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
      if (this.isHover) {
         if (!this.isUpgradeHover()) {
            this.isDragging = false;
            if (CognitionPoseConfig.editorMod && button == 0) {
               CognitionPoseConfig.save(this.cognition.id(), this.relX, this.relY);
            }
         }
      }
   }

   public void mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
      if (this.isHover) {
         if (CognitionScreen.INSTANCE.hoverBlock == this) {
            if (!this.isUpgradeHover()) {
               if (CognitionPoseConfig.editorMod && button == 0) {
                  this.relX = this.relX + (float)dragX / this.scale;
                  this.relY = this.relY + (float)dragY / this.scale;
               }
            }
         }
      }
   }

   public void setCenter(float x, float y) {
      this.centerX = x;
      this.centerY = y;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public boolean isHover() {
      return this.isHover;
   }

   public boolean isDragging() {
      return this.isDragging;
   }

   public boolean isUpgradeHover() {
      for (CognitionUpgradeComponent upgrade : this.upgrades) {
         if (upgrade.isHover()) {
            return true;
         }
      }

      return false;
   }

   public int getHoverTier() {
      boolean cardResearched = CognitionHelper.canResearch(this.mc.player, this.cognition);
      int hoverTier = 1;

      for (CognitionUpgradeComponent upgrade : this.upgrades) {
         if (upgrade.isHover()) {
            hoverTier = upgrade.getTier();
         }
      }

      return hoverTier;
   }
}
