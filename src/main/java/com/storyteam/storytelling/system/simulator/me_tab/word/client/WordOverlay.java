package com.storyteam.storytelling.system.simulator.me_tab.word.client;

import com.mojang.math.Axis;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.me_tab.word.AnimatedWordQueue;
import com.storyteam.storytelling.system.simulator.me_tab.word.data.AnimatedLetter;
import com.storyteam.storytelling.system.simulator.me_tab.word.data.AnimatedWordData;
import com.storyteam.storytelling.system.simulator.me_tab.word.net.FinishAnimWordDeleteQueuePacket;
import com.storyteam.storytelling.system.simulator.me_tab.word.net.FinishAnimWordPacket;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber({Dist.CLIENT})
public class WordOverlay implements IGuiOverlay {
   public static int ticker;
   private static AnimatedWordData wordData;
   public static Vector2f startPos;

   public static void clearWordData() {
      wordData = null;
   }

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (Minecraft.getInstance().screen == null) {
         renderWordOverlay(gui, guiGraphics, partialTick, screenWidth, screenHeight);
      }
   }

   public static void renderWordOverlay(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (wordData == null && gui.getMinecraft().screen == null) {
         if (!AnimatedWordQueue.getQueue().isEmpty()) {
            wordData = AnimatedWordQueue.getQueue().peek().copy();
            startWordAnimation(screenWidth, screenHeight);
         }
      } else {
         double totalWidth = wordData.getLetters().stream().mapToDouble(l -> (double)(l.getWidthChar() * wordData.getTargetScale())).sum();
         double maxHeight = wordData.getLetters().stream().mapToDouble(AnimatedLetter::getHeightChar).max().orElse(9.0);
         float offsetStartPosX = 0.0F;

         for (AnimatedLetter letter : wordData.getLetters()) {
            guiGraphics.pose().pushPose();
            float x = (float)((double)startPos.x() - totalWidth / 2.0 + (double)offsetStartPosX + (double)letter.getPosX());
            float y = (float)((double)startPos.y() - maxHeight * 2.0 + (double)letter.getPosY());
            guiGraphics.pose().translate(x, y, 0.0F);
            guiGraphics.pose().scale(letter.getScale(), letter.getScale(), 1.0F);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(letter.getRotationZ()));
            guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(letter.getRotationY()));
            int alpha = (int)(Math.max(0.1F, letter.getAlpha()) * 255.0F);
            FormattedCharSequence text = letter.getCharacter().getVisualOrderText();
            int color = alpha << 24 | 14155263;
            int outlineColor = alpha << 24 | 1125708;
            Matrix4f matrix = guiGraphics.pose().last().pose();
            MultiBufferSource buffer = guiGraphics.bufferSource();
            int packedLight = 15728880;
            gui.getFont().drawInBatch8xOutline(text, 0.0F, 0.0F, color, outlineColor, matrix, buffer, packedLight);
            guiGraphics.pose().popPose();
            offsetStartPosX += letter.getWidthChar() * letter.getScale();
         }
      }
   }

   private static void finishAnimWord() {
      if (wordData.getData() != null) {
         Network.sendToServer(new FinishAnimWordPacket(wordData.getData().getId()));
      }

      Network.sendToServer(new FinishAnimWordDeleteQueuePacket());
   }

   public static void startWordAnimation(int screenWidth, int screenHeight) {
      if (wordData != null) {
         String str = wordData.getText().getString();
         startPos = new Vector2f((float)screenWidth / 2.0F, (float)screenHeight / 2.0F);
         float textWidth = (float)Minecraft.getInstance().font.width(wordData.getText());
         float maxAllowedWidth = (float)screenWidth * 0.9F;
         float scale = Math.min(wordData.getTargetScale(), maxAllowedWidth / textWidth);
         wordData.setTargetScale(scale);

         for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            Component letterComp = Component.literal(String.valueOf(c)).setStyle(wordData.getText().getStyle());
            AnimatedLetter letter = new AnimatedLetter(letterComp, i);
            wordData.getLetters().add(letter);
            Tween letterTween = createLetterInTween(wordData, letter, (double)i * 0.05);
            if (i == str.length() - 1) {
               letterTween.tweenInterval(2.0);
               letterTween.tweenRunnable(() -> startWordOutAnimation(screenHeight));
            }

            letterTween.start();
         }
      }
   }

   public static Tween createLetterInTween(AnimatedWordData data, AnimatedLetter letter, double delaySeconds) {
      Tween tween = Tween.create().setEase(EaseType.EASE_OUT);
      tween.parallel().tweenMethod(letter::setAlpha, 0.0F, 1.0F, 0.3).setDelay(delaySeconds);
      float start_scale = data.getStartScale();
      float target_scale = data.getTargetScale();
      tween.parallel().tweenMethod(letter::setScale, start_scale, target_scale, 0.3).setDelay(delaySeconds);
      tween.parallel().tweenMethod(letter::setRotationZ, letter.getRotationZ(), 0.0F, 0.3).setDelay(delaySeconds);
      return tween;
   }

   public static void startWordOutAnimation(int screenHeight) {
      for (int i = 0; i < wordData.getLetters().size(); i++) {
         AnimatedLetter letter = wordData.getLetters().get(i);
         Tween tween = createLetterOutTween(letter, (double)i * 0.03, screenHeight);
         if (i == wordData.getLetters().size() - 1) {
            tween.tweenRunnable(() -> finishAnimWord());
         }

         tween.start();
      }
   }

   public static Tween createLetterOutTween(AnimatedLetter letter, double delaySeconds, int screenHeight) {
      Tween tween = Tween.create();
      Random rnd = new Random();
      double jumpHeight = rnd.nextDouble(20.0, 60.0);
      double rotationDelta = (double)rnd.nextInt(-60, 60);
      double horizontalShift = (double)rnd.nextInt(-40, 40);
      tween.parallel().tweenMethod(letter::setPosY, 0.0F, (float)(-jumpHeight), 0.3).setDelay(delaySeconds).setEaseType(EaseType.EASE_OUT);
      tween.parallel().tweenMethod(letter::setPosX, 0.0F, (float)horizontalShift, 0.3).setDelay(delaySeconds).setEaseType(EaseType.EASE_OUT);
      tween.parallel()
         .tweenMethod(letter::setRotationZ, letter.getRotationZ(), letter.getRotationZ() + (float)rotationDelta, 0.3)
         .setDelay(delaySeconds)
         .setEaseType(EaseType.EASE_OUT);
      tween.parallel()
         .tweenMethod(letter::setRotationY, letter.getRotationY(), letter.getRotationY() + (float)rotationDelta, 0.3)
         .setDelay(delaySeconds)
         .setEaseType(EaseType.EASE_OUT);
      float fallDistance = (float)(screenHeight + 20) + (float)(Math.random() * 20.0);
      tween.chain().tweenMethod(letter::setPosY, (float)(-jumpHeight), fallDistance, 0.6).setEaseType(EaseType.EASE_IN).setTransitionType(TransitionType.CIRC);
      tween.chain().tweenMethod(letter::setAlpha, 1.0F, 0.0F, 0.6).setEaseType(EaseType.EASE_IN);
      return tween;
   }
}
