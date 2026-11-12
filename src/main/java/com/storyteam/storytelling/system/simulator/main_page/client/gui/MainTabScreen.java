package com.storyteam.storytelling.system.simulator.main_page.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.ico.Ico;
import com.storyteam.storytelling.system.shaders.glitch.GlitchRendererPost;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.DrawUtils;
import com.storyteam.storytelling.system.simulator.cognition_tab.client.gui.CognitionScreen;
import com.storyteam.storytelling.system.simulator.main_page.MainTabStorage;
import com.storyteam.storytelling.system.simulator.main_page.MainTabSystem;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;
import com.storyteam.storytelling.system.simulator.main_page.client.gui.components.MainTabButton;
import com.storyteam.storytelling.system.simulator.me_tab.client.MeTabScreen;
import com.storyteam.storytelling.system.simulator.world_tab.ui.WorldMainTab;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MainTabScreen extends Screen {
   private final MainTabButton buttonMe;
   private final MainTabButton buttonMind;
   private final MainTabButton buttonWorld;
   private final MainTabButton unblockButton;
   private final MainTabButton controlButton;
   private final GlitchRendererPost glitch = new GlitchRendererPost();

   public MainTabScreen() {
      super(Component.literal("MainTabScreen"));
      this.buttonMind = new MainTabButton(595.0F, 365.0F, "button_mind", (b, id) -> CognitionScreen.open());
      this.buttonWorld = new MainTabButton(661.0F, 418.0F, "button_world", (b, id) -> this.minecraft.setScreen(new WorldMainTab()));
      this.buttonMe = new MainTabButton(551.0F, 412.0F, "button_me", (b, id) -> this.minecraft.setScreen(new MeTabScreen()));
      boolean hasEidos = CurrencyStorage.getEidos() >= 10;
      this.unblockButton = new MainTabButton(1036.0F, 157.0F, "button_unblock", (b, id) -> {
         if (hasEidos) {
            MainTabStorage.setBlocked(MainTabType.MAIN, false);
            CurrencyStorage.removeEidos(10);
         }
      });
      this.unblockButton.setTooltip(List.of(Component.literal("Разблокировать за " + (hasEidos ? "§a" : "§c") + "10 §r" + Ico.EIDOS.getSymbol())));
      this.controlButton = new MainTabButton(943.0F, 142.0F, "button_control", (b, id) -> {});
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      super.render(guiGraphics, mouseX, mouseY, partialTick);
      this.renderBackground(guiGraphics);
      if (this.minecraft != null) {
         Window window = this.minecraft.getWindow();
         MainTabSystem system = MainTabSystem.INSTANCE;
         PoseStack poseStack = guiGraphics.pose();
         float addedScale = (float)Math.round(Math.min((float)window.getWidth() / 2560.0F, (float)window.getHeight() / 1440.0F) / 0.1F) * 0.1F;
         float scale = (float)(1.0 / window.getGuiScale()) * addedScale;
         float width = 2560.0F * scale;
         float height = 1440.0F * scale;
         float x = (float)window.getGuiScaledWidth() / 2.0F - width / 2.0F;
         float y = (float)window.getGuiScaledHeight() / 2.0F - height / 2.0F;
         boolean isBlocked = MainTabStorage.isBlocked(MainTabType.MAIN);
         ResourceLocation bgTexture = isBlocked ? system.guiTex("background_off") : system.guiTex("background");
         DrawUtils.blit(poseStack, bgTexture, x, y, width, height);
         if (isBlocked) {
            this.unblockButton.setScale(scale).setPos(x, y, 762.0F, 637.0F).render(poseStack, mouseX, mouseY, partialTick);
            this.unblockButton.renderTooltip(guiGraphics, mouseX, mouseY);
         } else {
            this.buttonMind
               .setBlocked(MainTabStorage.isBlocked(MainTabType.MIND))
               .setScale(scale)
               .setPos(x, y, 239.0F, 470.0F)
               .render(poseStack, mouseX, mouseY, partialTick);
            this.buttonWorld
               .setBlocked(MainTabStorage.isBlocked(MainTabType.WORLD))
               .setScale(scale)
               .setPos(x, y, 990.0F, 318.0F)
               .render(poseStack, mouseX, mouseY, partialTick);
            this.buttonMe
               .setBlocked(MainTabStorage.isBlocked(MainTabType.ME))
               .setScale(scale)
               .setPos(x, y, 1758.0F, 405.0F)
               .render(poseStack, mouseX, mouseY, partialTick);
            this.controlButton
               .setBlocked(MainTabStorage.isBlocked(MainTabType.CONTROL))
               .setScale(scale)
               .setPos(x, y, 809.0F, 895.0F)
               .render(poseStack, mouseX, mouseY, partialTick);
         }

         this.glitch
            .render(
               poseStack,
               x - 1.0F + 126.0F * scale,
               y - 1.0F + 171.0F * scale,
               2321.0F * scale,
               1152.0F * scale,
               0.5F,
               3.0F,
               3.0F,
               22.0F,
               400.0F,
               20.0F,
               partialTick
            );
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      boolean isBlocked = MainTabStorage.isBlocked(MainTabType.MAIN);
      if (isBlocked) {
         this.unblockButton.mouseClicked(mouseX, mouseY, button);
      } else {
         this.buttonMind.mouseClicked(mouseX, mouseY, button);
         this.buttonWorld.mouseClicked(mouseX, mouseY, button);
         this.buttonMe.mouseClicked(mouseX, mouseY, button);
         this.controlButton.mouseClicked(mouseX, mouseY, button);
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }
}
