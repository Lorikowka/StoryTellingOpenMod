package com.storyteam.storytelling.system.videotest.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.init.BindsST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.system.videotest.GatherVideotestReportEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Post;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VideotestHandler {
   @SubscribeEvent
   public static void onOverlayRender(Post event) {
      if (event.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type() && BindsST.showVideotestInfo) {
         Minecraft MC = Minecraft.getInstance();
         LocalPlayer player = MC.player;
         if (player != null) {
            GuiGraphics gfx = event.getGuiGraphics();
            PoseStack pose = gfx.pose();
            IStoryServer storyServer = IStoryServer.get();
            ScriptEnvironment env = storyServer.getScriptEnv();
            List<Integer> indexes = env.scriptInstances.values().stream().map(ScriptInstance::getId).sorted().collect(Collectors.toList());
            List<Integer> backScripts = indexes.stream().filter(entry -> entry < 0).toList();
            indexes.removeAll(backScripts);
            int x = 4;
            int y = 80;
            int yOff = 0;
            int xOff = 0;
            pose.pushPose();
            pose.scale(0.5F, 0.5F, 0.5F);
            gfx.drawString(MC.font, "§nАктивные скрипты:§r", x, y + yOff, 16777215, true);
            yOff += 11;

            for (int entry : indexes) {
               int var24 = 16;
               ScriptInstance script = (ScriptInstance)env.scriptInstances.get(entry);
               if (script != null) {
                  ScriptPerformer performer = script.getMainPerformer();
                  if (performer != null) {
                     for (FormattedCharSequence line : MC.font.split(Component.literal("§e#" + entry + "§r: " + performer.gatherHelpReport()), 400)) {
                        gfx.drawString(MC.font, line, x + var24, y + yOff, 16777215, true);
                        yOff += 8;
                     }

                     var24 = 40;
                     gfx.drawString(MC.font, "§7● Последний чекпоинт: §a" + performer.getStage(), x + var24, y + yOff, 16777215, true);
                     yOff += 8;
                     gfx.drawString(
                        MC.font,
                        "§7● Прогресс скрипта: §e"
                           + new DecimalFormat("#.#").format((double)((float)performer.getActionIndex() / ((float)performer.getActionsAmount() / 100.0F)))
                           + "%",
                        x + var24,
                        y + yOff,
                        16777215,
                        true
                     );
                  }
               }
            }

            yOff += 11;
            int var26 = 0;
            gfx.drawString(MC.font, "§nАктивные бэкскрипты:§r", x + var26, y + yOff, 16777215, true);
            yOff += 11;
            var26 = 16;
            if (!player.isShiftKeyDown()) {
               for (int i = 0; i < backScripts.size(); i++) {
                  int index = backScripts.get(i);
                  ScriptInstance script = (ScriptInstance)env.scriptInstances.get(index);
                  if (script != null) {
                     String color = "§" + (script.isPaused() ? "8" : "7");
                     gfx.drawString(MC.font, color + "#" + index, x + var26, y + yOff, 16777215, true);
                     var26 += 40;
                     if ((i + 1) % 5 == 0) {
                        yOff += 8;
                        var26 = 16;
                     }
                  }
               }
            } else {
               for (int entryx : backScripts) {
                  ScriptInstance script = (ScriptInstance)env.scriptInstances.get(entryx);
                  if (script != null) {
                     String color = "§" + (script.isPaused() ? "8" : "7");
                     gfx.drawString(MC.font, color + "#" + entryx + "§r: " + script.getDescription(), x + var26, y + yOff, 16777215, true);
                     yOff += 8;
                  }
               }
            }

            yOff += 11;
            var26 = 0;
            yOff += 11;
            Entity targetEntity = MC.crosshairPickEntity;
            if (targetEntity != null) {
               IStoryEntity storyEntity = IStoryEntity.of(targetEntity);
               if (storyEntity.isStorytellingEntity()) {
                  gfx.drawString(MC.font, "§nID сущности:§r §e" + storyEntity.getStoryName(), x + var26, y + yOff, 16777215, true);
               }

               yOff += 22;
            }

            GatherVideotestReportEvent evt = new GatherVideotestReportEvent(player);
            Storytelling.BUS.post(evt);
            pose.translate((float)var26, (float)yOff, 0.0F);
            evt.components().forEach(com -> {
               gfx.drawString(MC.font, com, x, y, 16777215, true);
               pose.translate(0.0F, 11.0F, 0.0F);
            });
            pose.popPose();
         }
      }
   }
}
