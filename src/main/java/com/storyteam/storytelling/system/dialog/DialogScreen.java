package com.storyteam.storytelling.system.dialog;

import com.mojang.blaze3d.platform.Window;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.init.SoundsST;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.system._core.st_cutscene.STCutsceneData;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.util.RenderTools;
import com.storyteam.storytelling.util.ServerScheduler;
import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.client.utils.Scissors;

public class DialogScreen extends Screen {
   public static ResourceLocation DIALOG_NAMEPLATE = St.tx("gui/dialog/dialog_name");
   public static ResourceLocation DIALOG_SHADOW = St.tx("gui/dialog/dialog_shadow");
   public int noRenderTicker = 0;
   public Dialog dialog;
   public List<AnswerButton> answerButtons = new ArrayList<>();
   public int ticker = 0;
   public Component toRender = Component.literal("");
   public Stack<TextInstance> texts = new Stack<>();
   public float maxScroll = 0.0F;
   public float currentScroll = 0.0F;
   public static int colorTextDialog = 4376575;

   public DialogScreen(Character<? extends AnimatedEntity> character) {
      super(Component.empty());
      Dialog dialog = DialogHandler.getDialog(character);
      if (dialog == null) {
         Player player = Minecraft.getInstance().player;
         player.sendSystemMessage(Component.literal("Couldn't find active dialog with character " + character.getReferenceId()));
      }

      this.dialog = dialog;
      Player player;
      Level level;
      if ((level = Minecraft.getInstance().level) != null && (player = Minecraft.getInstance().player) != null) {
         level.playSound(player, player.blockPosition(), SoundsST.OPEN_DIALOG, SoundSource.HOSTILE);
      }
   }

   protected void init() {
      super.init();
      this.noRenderTicker = 1;
      this.initializeDialog(this.dialog);
      if (Minecraft.getInstance().player != null) {
         SoundUtils.playVolumedSound(Minecraft.getInstance().player, SoundsST.DIALOG_OPEN);
      }
   }

   public void render(GuiGraphics graphics, int mx, int my, float pticks) {
      if (this.noRenderTicker == 0) {
         super.render(graphics, mx, my, pticks);
         this.renderDialogScreen(graphics, mx, my, pticks);
         this.renderText(graphics, mx, my, pticks);
      }
   }

   public void renderDialogScreen(GuiGraphics graphics, int mx, int my, float pticks) {
      float shadowHeight = 62.0F;
      float shadowGradientHeight = 20.0F;
      float alpha = 0.7F;
      Window window = Minecraft.getInstance().getWindow();
      float w = (float)window.getGuiScaledWidth();
      float h = (float)window.getGuiScaledHeight();
      RenderTools.bindText(DIALOG_SHADOW);
      RenderTools.blitWithBlend(graphics, 0.0F, h - 270.0F, 0.0F, 0.0F, w, 270.0F, w, 270.0F, 1.0F);
      RenderTools.bindText(DIALOG_NAMEPLATE);
      RenderTools.blitWithBlend(graphics, 28.5F, h - shadowHeight, 0.0F, 0.0F, 103.0F, 3.0F, 103.0F, 3.0F, 1.0F);
      Component name = Component.literal("");
      if (this.dialog != null && this.dialog.owner != null) {
         name = Component.literal(this.dialog.owner.getName());
      }

      graphics.drawCenteredString(this.font, name, 80, (int)(h - shadowHeight - 9.0F - 1.0F), colorTextDialog);
      boolean buttonPushesEmotion = false;
      DialogEmotion pushedEmotion = null;

      for (AnswerButton button : this.answerButtons) {
         if (button.isHovered() && button.answer.emotion != null) {
            buttonPushesEmotion = true;
            pushedEmotion = button.answer.emotion;
            break;
         }
      }

      if (!buttonPushesEmotion && this.dialog != null && this.dialog.emotion != null) {
         pushedEmotion = this.dialog.emotion;
      }

      if (pushedEmotion != null) {
         RenderTools.bindText(St.tx("system/dialog/emotions/" + pushedEmotion.getName()));
         RenderTools.blitWithBlend(graphics, 365.0F, h - shadowHeight - 85.0F, 0.0F, 0.0F, 98.0F, 98.0F, 98.0F, 98.0F, 1.0F);
      }
   }

   public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
      if (pKeyCode == 297) {
         this.pushToRenderText();
      }

      return super.keyPressed(pKeyCode, pScanCode, pModifiers);
   }

   private void renderText(GuiGraphics graphics, int mx, int my, float pticks) {
      if (!this.toRender.getString().isEmpty()) {
         int offsetFromBottom = 50;
         int maxTextHeight = 45;
         Window window = Minecraft.getInstance().getWindow();
         int offsetFromEdges = 25;
         int w = window.getGuiScaledWidth();
         int h = window.getGuiScaledHeight();
         int rightEdge = 115;
         Scissors.begin(0, h - offsetFromBottom, w, maxTextHeight);
         int splitLength = w - offsetFromEdges * 2 - rightEdge;
         MutableComponent component = Component.empty();
         int index = 0;

         for (Component part : this.toRender.getSiblings()) {
            String partText = part.getString();
            if (index + partText.length() > this.ticker) {
               component.append(Component.literal(partText.substring(0, this.ticker - index)).withStyle(part.getStyle()));
               break;
            }

            component.append(part);
            index += partText.length();
         }

         int md = this.toRender.getString().charAt(this.ticker - 1) == '\n' ? -1 : 0;
         if (this.ticker != this.toRender.getString().length()) {
            component.append(Component.literal("d").withStyle(ChatFormatting.OBFUSCATED));
         }

         List<FormattedCharSequence> sequences = this.font.split(component, splitLength);

         for (int i = 0; i < sequences.size(); i++) {
            FormattedCharSequence sequence = sequences.get(i);
            int x = 36;
            int y = h - offsetFromBottom + 9 * i;
            graphics.drawString(this.font, sequence, (float)x, (float)y - this.currentScroll, colorTextDialog, false);
         }

         float prevScroll = this.maxScroll;
         this.maxScroll = (float)Mth.clamp((sequences.size() + md) * 9 - maxTextHeight, 0, Integer.MAX_VALUE);
         if (prevScroll != this.maxScroll && this.currentScroll == prevScroll) {
            this.currentScroll = this.maxScroll;
         }

         if (this.maxScroll != 0.0F) {
            int scrollBarWidth = 5;
            RenderTools.renderScrollBar(
               graphics,
               (float)(w - scrollBarWidth * 2),
               (float)(h - offsetFromBottom),
               (float)scrollBarWidth,
               (float)maxTextHeight,
               this.currentScroll,
               this.maxScroll,
               0.25F,
               0.25F,
               0.25F,
               1.0F,
               0.5F,
               0.5F,
               0.5F,
               1.0F
            );
         }

         Scissors.end();
      }
   }

   public boolean mouseScrolled(double mx, double my, double delta) {
      this.currentScroll = Mth.clamp(this.currentScroll - (float)delta * 3.0F, 0.0F, this.maxScroll);
      return super.mouseScrolled(mx, my, delta);
   }

   public void tick() {
      super.tick();
      this.noRenderTicker = Mth.clamp(this.noRenderTicker - 1, 0, Integer.MAX_VALUE);
      if (this.noRenderTicker == 0) {
         int speed = Math.round(2.0F + Minecraft.getInstance().getDeltaFrameTime());
         this.ticker = Mth.clamp(this.ticker + speed, 0, this.toRender.getString().length());
         if (this.ticker == this.toRender.getString().length() && this.texts.isEmpty()) {
            this.setAnswerButtonsState(true);
         }
      }
   }

   public void resetToRenderText() {
      this.ticker = 0;
      this.toRender = Component.literal("");
   }

   public void pushToRenderText() {
      if (!this.texts.isEmpty()) {
         this.setAnswerButtonsState(false);
         if (this.ticker == this.toRender.getString().length()) {
            TextInstance addition = this.texts.pop();
            AnimatedEntity entity = this.getTalkingToEntity();
            if (entity != null && addition.animation() != null) {
               entity.startAnimation("action", addition.animation());
            }

            Component add = addition.text().copy().append(Component.literal("\n"));
            this.toRender = this.toRender.copy().append(add);
         }
      } else if (this.ticker == this.toRender.getString().length()) {
         this.setAnswerButtonsState(true);
      }
   }

   public AnimatedEntity getTalkingToEntity() {
      String s = this.dialog.talkingToId;
      Character<? extends AnimatedEntity> character = Character.getById(s);
      return character == null ? null : IStoryServer.get().getEntityRepository().getEntity(character);
   }

   public void setAnswerButtonsState(boolean state) {
      for (AnswerButton button : this.answerButtons) {
         button.active = state;
         button.visible = state;
      }
   }

   protected void initializeDialog(Dialog dialog) {
      if (dialog != null) {
         this.resetToRenderText();
         this.texts.clear();
         this.answerButtons.forEach(x$0 -> this.removeWidget(x$0));
         this.answerButtons.clear();
         int btnOffset = 5;
         int accumulatedY = btnOffset;

         for (Answer answer : dialog.answers) {
            AnswerButton button = new AnswerButton(this, answer, btnOffset, accumulatedY, 1.0F, btn -> this.initializeDialog(answer.nextDialog));
            accumulatedY += button.getHeight() + btnOffset;
            this.addRenderableWidget(button);
            this.answerButtons.add(button);
         }

         if (dialog != this.dialog) {
            DialogHandler.currentDialogs.put(this.dialog.owner, dialog);
         }

         dialog.owner = this.dialog.owner;
         dialog.scriptId = this.dialog.scriptId;
         if (dialog.talkingToId == null) {
            dialog.talkingToId = this.dialog.talkingToId;
         } else {
            String flyTo = dialog.talkingToId;
            STCutsceneData data;
            if (flyTo.equals("player")) {
               data = this.constructFlyToEntityOrBackData(Minecraft.getInstance().player);
            } else if (flyTo.startsWith("/")) {
               data = new STCutsceneData();
               data.interpolationType = InterpolationType.EASE_IN_OUT_QUAD;
               data.lookInterpolationType = InterpolationType.LINEAR;
               data.splineType = SplineType.CATMULLROM;
               Player player = Minecraft.getInstance().player;
               Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
               Vec3 v = Minecraft.getInstance().gameRenderer.getMainCamera().position;
               Location init = new Location(player.level().dimension(), v, camera.getYRot(), camera.getXRot());
               if (ClientsideCutsceneHandler.currentData != null) {
                  init = ClientsideCutsceneHandler.getCurrentCameraLocation(
                     init,
                     ClientsideCutsceneHandler.spline,
                     ClientsideCutsceneHandler.timeInterpolation,
                     ClientsideCutsceneHandler.lookInterpolation,
                     ClientsideCutsceneHandler.cutsceneTime,
                     ClientsideCutsceneHandler.fullCutsceneTime,
                     0.0F
                  );
               } else {
                  init = init.add(player.getLookAngle().multiply(0.25, 0.25, 0.25));
               }

               init = init.subtract(0.0, (double)player.getEyeHeight(), 0.0);
               data.addLocation(init);
               Location end = new Location(flyTo);
               data.addLocation(end);
               double len = end.subtract(init).length();
               if (len <= 0.05) {
                  data = null;
               } else {
                  int time = (int)(len * Math.exp(-0.05 * len) * 5.0);
                  time = Mth.clamp(time, 1, Integer.MAX_VALUE);
                  data.time = time;
                  data.cameraOut = false;
               }
            } else {
               Character<? extends AnimatedEntity> ch = Character.getById(flyTo);
               AnimatedEntity entity = IStoryServer.get().getEntityRepository().getEntity(ch);
               data = this.constructFlyToEntityOrBackData(entity);
            }

            if (data != null) {
               this.noRenderTicker = data.time + 1;
               if (ClientsideCutsceneHandler.currentData != null) {
                  data.locations.remove(0);
                  ServersideCutsceneHandler.moveCamera(data);
               } else {
                  ServersideCutsceneHandler.initOrTpCamera(data);
               }
            }
         }

         for (int i = dialog.texts.size() - 1; i >= 0; i--) {
            this.texts.push(dialog.texts.get(i));
         }

         this.pushToRenderText();
      } else {
         DialogHandler.currentDialogs.remove(this.dialog.owner);
         this.terminateDialog();
      }

      this.dialog = dialog;
   }

   private void terminateDialog() {
      STCutsceneData data = this.constructFlyToEntityOrBackData(null);
      if (data != null) {
         if (ClientsideCutsceneHandler.currentData != null) {
            data.locations.remove(0);
            ServersideCutsceneHandler.moveCamera(data);
         } else {
            ServersideCutsceneHandler.initOrTpCamera(data);
         }

         ServerScheduler.executeWhen(
            () -> ClientsideCutsceneHandler.currentData == null ? true : ClientsideCutsceneHandler.currentData.time <= ClientsideCutsceneHandler.cutsceneTime,
            () -> ServersideCutsceneHandler.stopCamera(),
            Integer.MAX_VALUE
         );
      } else {
         ServerScheduler.schedule(1, ServersideCutsceneHandler::stopCamera);
      }

      Minecraft.getInstance().setScreen(null);
   }

   public void onClose() {
      this.terminateDialog();
      super.onClose();
   }

   public STCutsceneData constructFlyToEntityOrBackData(LivingEntity entity) {
      STCutsceneData data = new STCutsceneData();
      data.interpolationType = InterpolationType.EASE_IN_OUT_QUAD;
      data.lookInterpolationType = InterpolationType.LINEAR;
      data.splineType = SplineType.CATMULLROM;
      Player player = Minecraft.getInstance().player;
      Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
      Vec3 v = Minecraft.getInstance().gameRenderer.getMainCamera().position;
      Location init = new Location(player.level().dimension(), v, camera.getYRot(), camera.getXRot());
      if (ClientsideCutsceneHandler.currentData != null) {
         init = ClientsideCutsceneHandler.getCurrentCameraLocation(
            init,
            ClientsideCutsceneHandler.spline,
            ClientsideCutsceneHandler.timeInterpolation,
            ClientsideCutsceneHandler.lookInterpolation,
            ClientsideCutsceneHandler.cutsceneTime,
            ClientsideCutsceneHandler.fullCutsceneTime,
            0.0F
         );
      } else {
         init = init.add(player.getLookAngle().multiply(0.25, 0.25, 0.25));
      }

      init = init.subtract(0.0, (double)player.getEyeHeight(), 0.0);
      data.addLocation(init);
      Location end;
      if (entity != null) {
         Vec3 horizontalLookAngle = entity.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
         Vec3 angle = entity.getLookAngle().multiply(1.6, 1.6, 1.6);
         float entityEyeHeight = entity.getEyeHeight();
         float playerEyeHeight = player.getEyeHeight();
         float viewYRot = entity.getViewYRot(0.0F);
         end = new Location(
               entity.level().dimension(),
               entity.position().add(0.0, (double)(entityEyeHeight - playerEyeHeight), 0.0),
               viewYRot + 165.0F,
               -entity.getViewXRot(0.0F)
            )
            .add(angle)
            .add(horizontalLookAngle.multiply(0.1, 0.0, 0.1).yRot((float)Math.toRadians(90.0)));
         Vec3 between = end.subtract(init).multiply(1.0, 0.0, 1.0).normalize();
         double dot = between.dot(horizontalLookAngle);
         if (dot > 0.75) {
            Vec3 epos = entity.position();
            Vec3 toAdd1 = horizontalLookAngle.yRot((float)Math.toRadians(-90.0)).add(epos.x, epos.y, epos.z);
            Vec3 toAdd2 = horizontalLookAngle.yRot((float)Math.toRadians(90.0)).add(epos.x, epos.y, epos.z);
            if (init.distanceTo(toAdd1) > init.distanceTo(toAdd2)) {
               data.addLocation(
                  new Location(entity.level().dimension(), toAdd2, entity.getViewYRot(0.0F) + 90.0F, -entity.getViewXRot(0.0F))
                     .add(0.0, (double)(entityEyeHeight - playerEyeHeight), 0.0)
               );
            } else {
               data.addLocation(
                  new Location(entity.level().dimension(), toAdd1, entity.getViewYRot(0.0F) - 90.0F, -entity.getViewXRot(0.0F))
                     .add(0.0, (double)(entityEyeHeight - playerEyeHeight), 0.0)
               );
            }
         }
      } else {
         boolean detached = ClientsideCutsceneHandler.lastCameraType == CameraType.THIRD_PERSON_BACK;
         Camera copy = new Camera();
         copy.setup(Minecraft.getInstance().level, player, false, false, Minecraft.getInstance().getDeltaFrameTime());

         for (int i = 0; i < 100; i++) {
            copy.tick();
         }

         copy.setup(Minecraft.getInstance().level, player, detached, false, Minecraft.getInstance().getDeltaFrameTime());
         end = new Location(player.level().dimension(), copy.position.subtract(0.0, (double)player.getEyeHeight(), 0.0), copy.getYRot(), copy.getXRot());
         if (!detached) {
            end = end.add(player.getLookAngle().multiply(0.5, 0.5, 0.5));
         }
      }

      double len = end.subtract(init).length();
      if (len <= 0.05) {
         return null;
      } else {
         int time = (int)(len * Math.exp(-0.05 * len) * 5.0);
         time = Mth.clamp(time, 1, Integer.MAX_VALUE);
         data.time = time;
         data.addLocation(end);
         data.cameraOut = false;
         return data;
      }
   }

   public boolean isPauseScreen() {
      return false;
   }
}
