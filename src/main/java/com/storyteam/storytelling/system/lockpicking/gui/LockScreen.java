package com.storyteam.storytelling.system.lockpicking.gui;

import com.storyteam.storytelling.client.screen.base.IAutoScaled;
import com.storyteam.storytelling.events.ClientModEvents;
import com.storyteam.storytelling.system.lockpicking.LockpickingSystem;
import com.storyteam.storytelling.system.lockpicking.data.LockComplexity;
import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import com.storyteam.storytelling.system.lockpicking.net.PacketProgressLock;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.net.Network;

public class LockScreen extends Screen implements IAutoScaled {
   protected final GlobalPos pos;
   public LockRegistryEntry display;
   private final int w = 128;
   private final int h = 131;
   private int[] angles;
   private float speed = 8.0F;
   private int mod = 1;
   private float angle;
   protected GuiRootObject scene;

   public LockScreen(GlobalPos pos, LockRegistryEntry display) {
      super(Component.empty());
      this.pos = pos;
      this.display = display;
   }

   public int[] regenBrokenNotches() {
      this.speed = 8.0F * this.display.entry.getSpeed();
      int notches = this.display.entry.getNotches();
      int attempts = 32;

      do {
         int[] angles = new Random(this.display.entry.getSeed()).ints(0, 12).limit(1000000L).distinct().limit((long)notches).toArray();
         if (angles.length == notches) {
            for (int i = 0; i < angles.length; i++) {
               angles[i] *= 30;
            }

            this.angles = angles;
            return angles;
         }
      } while (attempts-- >= 0);

      return null;
   }

   public GuiRootObject createRoot() {
      GuiRootObject $root = GuiRootObject.root();
      $root.size(128.0F, 131.0F).centered(this.width, this.height).pivotAtCenter();
      int[] notches = this.regenBrokenNotches();
      if (notches == null) {
         return $root;
      } else {
         LockpickingSystem sys = LockpickingSystem.INSTANCE;
         GuiImageObject gui = GuiObject.create("gui").image(sys.guiTex("lockpick_gui"), 0.0F, 0.0F, 128.0F, 131.0F, 128.0F, 131.0F);
         $root.add(gui);
         LockComplexity complexity = this.display.entry.getComplexity();
         gui.addChild(
            GuiObject.create("complexity")
               .text(
                  Component.literal("Сложность замка: ")
                     .append(Component.literal(complexity.title).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(complexity.color))))
               )
               .pos(0.0F, 133.0F)
         );
         GuiObject center = GuiObject.create("center").empty().pos(gui.getUnscaledWidth() / 2.0F, gui.getUnscaledHeight() / 2.0F);
         GuiObject[] notchObjects = new GuiObject[notches.length];
         boolean[] prog = this.display.getProgress();
         int i = 0;

         for (int len = notches.length; i < len; i++) {
            boolean v = prog[i];
            GuiObject notchHolder = notchObjects[i] = GuiObject.create("notch" + i)
               .empty()
               .pos(-2.0F, 0.0F)
               .pivot(2.0F, 0.0F)
               .rotation((float)(180 + notches[i]));
            notchHolder.addChild(
               GuiImageObject.create("notch" + i)
                  .fullImage(LockpickingSystem.INSTANCE.guiTex(v ? "notch_closed" : "notch"), 4.0F, 52.0F)
                  .shader(ClientModEvents::noDiscardPositionColorTex)
                  .pos(0.0F, 8.0F)
            );
            center.addChild(notchObjects[i]);
         }

         gui.addChild(center);
         GuiObject lockpick = GuiImageObject.create("lockpick")
            .fullImage(LockpickingSystem.INSTANCE.guiTex("lockpick"), 52.0F, 52.0F)
            .shader(ClientModEvents::noDiscardPositionColorTex)
            .pos(-5.0F, -48.0F)
            .pivot(5.0F, 48.0F)
            .rotation(this.angle);
         $root.onPreRender((partialTime, mouse) -> lockpick.rotation(this.angle + partialTime * this.speed * (float)this.mod));
         center.addChild(lockpick);
         GuiObject ghost = GuiImageObject.create("lockpick")
            .fullImage(LockpickingSystem.INSTANCE.guiTex("lockpick_ghost"), 52.0F, 52.0F)
            .shader(ClientModEvents::noDiscardPositionColorTex)
            .pos(-5.0F, -48.0F)
            .pivot(5.0F, 48.0F)
            .rotation(95.0F);
         center.addChild(ghost);
         return $root;
      }
   }

   public void rebuildWidgets() {
      super.rebuildWidgets();
   }

   protected void init() {
      super.init();
      this.scene = (GuiRootObject)this.addRenderableOnly(this.createRoot());
   }

   public void tick() {
      this.scene.sendUpdate();
      super.tick();
      this.angle = Mth.wrapDegrees(this.angle + this.speed * (float)this.mod);
   }

   public void render(GuiGraphics gfx, int pMouseX, int pMouseY, float pPartialTick) {
      pPartialTick = this.minecraft.getPartialTick();
      super.render(gfx, pMouseX, pMouseY, pPartialTick);
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      Minecraft mc = Minecraft.getInstance();
      if (pButton != 0) {
         return super.mouseClicked(pMouseX, pMouseY, pButton);
      } else {
         for (int i = 0; i < this.angles.length; i++) {
            if (Mth.degreesDifferenceAbs(this.angle, (float)this.angles[i]) < 10.0F && !this.display.getProgress()[i]) {
               Network.sendToServer(new PacketProgressLock(this.pos, i));
               mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.STONE_BUTTON_CLICK_ON, 1.5F, 1.0F));
               this.mod *= -1;
               return true;
            }
         }

         Network.sendToServer(new PacketProgressLock(this.pos, -1));
         mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.SHIELD_BREAK, 1.5F, 1.0F));
         return true;
      }
   }

   public boolean isPauseScreen() {
      return false;
   }
}
