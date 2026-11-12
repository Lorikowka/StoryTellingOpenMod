package com.storyteam.storytelling.system.simulator.cognition_tab.client.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionPoseConfig;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionSystem;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.DrawUtils;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.C2SDowngradeCognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.network.C2SUpgradeCognition;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.net.Network;

public class CognitionUpgradeComponent implements Comparable<CognitionUpgradeComponent> {
   private final Minecraft mc;
   private final Cognition cognition;
   private final int tier;
   public float x;
   public float y;
   public float width;
   public float height;
   private float scale = 1.0F;
   protected boolean isHover;
   public final CognitionSystem system = CognitionSystem.INSTANCE;
   public final ResourceLocation BACKGROUND_BLOCKED;
   public final ResourceLocation BACKGROUND_RESEARCHED;
   public final ResourceLocation BACKGROUND_HOVER_OUTLINE_UNBLOCKED;
   public final ResourceLocation BACKGROUND_HOVER_OUTLINE;

   public CognitionUpgradeComponent(Cognition cognition, int tier) {
      this.cognition = cognition;
      this.tier = tier;
      this.BACKGROUND_BLOCKED = this.system.guiTex("upgrades/upgrade_" + (tier - 1));
      this.BACKGROUND_RESEARCHED = this.system.guiTex("upgrades/upgrade_" + (tier - 1) + "_outline_can");
      this.BACKGROUND_HOVER_OUTLINE_UNBLOCKED = this.system.guiTex("upgrades/upgrade_" + (tier - 1) + "_outline_can");
      this.BACKGROUND_HOVER_OUTLINE = this.system.guiTex("upgrades/upgrade_outline");
      this.mc = Minecraft.getInstance();
      this.width = 28.0F;
      this.height = 6.0F;
   }

   public Cognition getCognition() {
      return this.cognition;
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
      this.isHover = (float)mouseX >= this.x && (float)mouseX <= this.x + this.width && (float)mouseY >= this.y && (float)mouseY <= this.y + this.height;
      boolean researched = CognitionHelper.getTier(this.mc.player, this.cognition) >= this.tier;
      ResourceLocation background = researched ? this.BACKGROUND_RESEARCHED : this.BACKGROUND_BLOCKED;
      DrawUtils.blit(poseStack, background, this.x, this.y, this.width, this.height);
      boolean canResearch = CognitionHelper.canResearch(this.mc.player, this.cognition, this.tier);
      if (this.isHover) {
         ResourceLocation outline = canResearch ? this.BACKGROUND_HOVER_OUTLINE_UNBLOCKED : this.BACKGROUND_HOVER_OUTLINE;
         DrawUtils.blit(poseStack, outline, this.x - 1.0F, this.y - 1.0F, this.width + 2.0F, this.height + 2.0F);
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isHover) {
         if (button == 0) {
            boolean isCtrlDow = InputConstants.isKeyDown(this.mc.getWindow().getWindow(), 341);
            boolean isAltDow = InputConstants.isKeyDown(this.mc.getWindow().getWindow(), 342);
            if (CognitionPoseConfig.editorMod) {
               if (isAltDow) {
                  Network.sendToServer(new C2SUpgradeCognition(this.cognition, this.tier, true));
               } else if (isCtrlDow) {
                  Network.sendToServer(new C2SDowngradeCognition(this.cognition, this.tier));
               }
            } else {
               Network.sendToServer(new C2SUpgradeCognition(this.cognition, this.tier, false));
            }
         }
      }
   }

   public void setPos(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public boolean isHover() {
      return this.isHover;
   }

   public int getTier() {
      return this.tier;
   }

   public int compareTo(@NotNull CognitionUpgradeComponent o) {
      return this.tier - o.tier;
   }
}
