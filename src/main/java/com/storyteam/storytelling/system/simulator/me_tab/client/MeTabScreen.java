package com.storyteam.storytelling.system.simulator.me_tab.client;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.client.screen.base.IAutoScaled;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.client.flowgui.GuiAttributeWidget;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.ButtonManager;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.EntityWidget;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.StretchButton;
import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiSpriteButtonObjectCopy;
import com.storyteam.storytelling.system.simulator.me_tab.client.history.HistoryScreen;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.utils.RenderUtils;
import com.storyteam.storytelling.system.simulator.me_tab.init.EntitiesMeTab;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiTextObject;
import org.zeith.hammerlib.util.math.Point;

public class MeTabScreen extends Screen implements IAutoScaled {
   protected EntityWidget entityWidget = null;
   protected GuiRootObject scene;
   protected final MeTabSystem sys = MeTabSystem.INSTANCE;
   protected ButtonManager buttonManager;
   GuiTextObject page;
   public final Map<AttributeType, GuiAttributeWidget> attributeWidgetMap = new HashMap<>();
   private Vector2f offset = new Vector2f(0.0F, 0.0F);

   public MeTabScreen() {
      super(Component.empty());
   }

   protected void init() {
      super.init();
      Minecraft MC = Minecraft.getInstance();
      Level level = MC.level;
      if (level != null) {
         int scale = 70;
         if (this.entityWidget == null) {
            HumanoidEntity entity = (HumanoidEntity)EntitiesMeTab.LO_ME_TAB_HEAD.create(level);
            float eyeHeight = entity.getEyeHeight(entity.getPose()) * (float)scale;
            this.entityWidget = new EntityWidget(
               this, entity, scale, new Vector2f((float)this.width / 2.0F, (float)this.height / 2.0F - eyeHeight / 4.0F - 15.0F)
            );
         } else {
            HumanoidEntity entity = this.entityWidget.getEntity();
            float eyeHeight = entity.getEyeHeight(entity.getPose()) * (float)scale;
            this.entityWidget.setBasePos(new Vector2f((float)this.width / 2.0F, (float)this.height / 2.0F - eyeHeight / 4.0F - 15.0F));
         }

         this.createButtons(false);
         this.scene = (GuiRootObject)this.addRenderableWidget(this.createRoot());
      }
   }

   public void createButtons(boolean rebuild) {
      if (this.buttonManager == null) {
         this.buttonManager = new ButtonManager(this.width / 2, this.height / 2, 120);
         this.buttonManager.createButtons(this.width, this.height, this);
      } else {
         this.buttonManager.setCenter(this.width / 2, this.height / 2);
         if (rebuild) {
            this.buttonManager.rebuildButtons(this.width, this.height, this);
         }
      }
   }

   public void tick() {
      super.tick();
      this.entityWidget.getEntity().getAnimationSystem().tick();
      if (this.scene != null) {
         this.scene.sendUpdate();
      }

      if (this.page != null) {
         if (!this.buttonManager.getPages().isEmpty()) {
            Component pageText = Component.literal(this.buttonManager.getCurrentPage() + 1 + "/" + this.buttonManager.getPages().size());
            this.page.setText(pageText);
         } else {
            this.page.setVisible(false);
         }
      }
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      super.render(graphics, mouseX, mouseY, partialTick);
      this.buttonManager.render(graphics, mouseX, mouseY, partialTick);
      Minecraft mc = Minecraft.getInstance();
      double realMouseX = mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth();
      double realMouseY = mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight();
      this.drawEntity(graphics, this.entityWidget.getScale(), (float)realMouseX, (float)realMouseY, this.entityWidget.getEntity());
      CursorParticles.spawnAtCursor((float)mouseX, (float)mouseY, this);
   }

   public GuiRootObject createRoot() {
      Font font = Minecraft.getInstance().font;
      int w = 480;
      int h = 270;
      GuiRootObject $root = GuiRootObject.root();
      $root.size((float)this.width, (float)this.height);
      GuiObject gui = GuiObject.create("gui")
         .image(this.sys.guiTex("background"), 0.0F, 0.0F, (float)this.width, (float)this.height, (float)this.width, (float)this.height)
         .centered(this.width, this.height)
         .pivotAtCenter()
         .size((float)this.width, (float)this.height);
      $root.add(gui);
      GuiObject frame = GuiObject.create("frame_lo")
         .image(this.sys.guiTex("frame_lo"), 0.0F, 0.0F, 480.0F, 270.0F, 480.0F, 270.0F)
         .centered(this.width, this.height);
      gui.addChild(frame);
      GuiAttributeWidget body = new GuiAttributeWidget("body", PlayerStats.getAttribute(AttributeType.BODY))
         .size(30.0F, 30.0F)
         .centered((Float)frame.elementWidth.get(), (Float)frame.elementHeight.get())
         .addPos(0, 82);
      frame.addChild(body);
      GuiAttributeWidget mind = new GuiAttributeWidget("mind", PlayerStats.getAttribute(AttributeType.MIND))
         .size(30.0F, 30.0F)
         .centered((Float)frame.elementWidth.get(), (Float)frame.elementHeight.get())
         .addPos(-50, 60);
      frame.addChild(mind);
      GuiAttributeWidget willpower = new GuiAttributeWidget("willpower", PlayerStats.getAttribute(AttributeType.WILLPOWER))
         .size(30.0F, 30.0F)
         .centered((Float)frame.elementWidth.get(), (Float)frame.elementHeight.get())
         .addPos(50, 60);
      frame.addChild(willpower);
      this.attributeWidgetMap.put(AttributeType.BODY, body);
      this.attributeWidgetMap.put(AttributeType.MIND, mind);
      this.attributeWidgetMap.put(AttributeType.WILLPOWER, willpower);
      Component lvlText = Component.literal("Ур: " + PlayerStats.getLevel());
      GuiObject lvl = GuiObject.create("lvl")
         .text(lvlText, -1, false)
         .pos(((Float)frame.elementWidth.get() - (float)font.width(lvlText)) / 2.0F, ((Float)frame.elementHeight.get() - 9.0F) / 2.0F - 90.0F);
      frame.addChild(lvl);
      if (!this.buttonManager.getPages().isEmpty()) {
         Component pageText = Component.literal(this.buttonManager.getCurrentPage() + 1 + "/" + this.buttonManager.getPages().size());
         this.page = (GuiTextObject)GuiObject.create("page")
            .text(pageText, -1, false)
            .pos(((Float)frame.elementWidth.get() - (float)font.width(pageText)) / 2.0F, ((Float)frame.elementHeight.get() + 9.0F) / 2.0F + 100.0F);
         frame.addChild(this.page);
      }

      GuiObject history = GuiSpriteButtonObjectCopy.of("history", this).customTexture(this.sys.guiTex("history/history_word_button")).callback(button -> {
         assert this.minecraft != null;

         this.minecraft.pushGuiLayer(new HistoryScreen(this, (Point)button.elementPosition.get(), 28.0F, 28.0F));
      }).build().size(28.0F, 28.0F).pos((float)(this.width - 28), (float)(this.height - 28));
      gui.addChild(history);
      $root.debugBoundaries = false;
      return $root;
   }

   public void drawEntity(GuiGraphics gui, int scale, float mouseX, float mouseY, AnimatedEntity entity) {
      if (entity != null) {
         if (!entity.getAnimationSystem().isActiveAnimation("action", AnimationsST8.FLOATING_POSE)) {
            entity.getAnimationSystem().startAnimationAt("action", AnimationsST8.FLOATING_POSE.configure().transitionTime(0.0F));
         }

         if (!entity.getAnimationSystem().isActiveAnimation("ANIMATION_1", AnimationsST8.FLOATING_CYCLE_LAYER)) {
            entity.getAnimationSystem().startAnimationAt("ANIMATION_1", AnimationsST8.FLOATING_CYCLE_LAYER.configure().transitionTime(0.0F));
         }

         float eyeHeight = entity.getEyeHeight(entity.getPose());
         Vector2f pos = this.entityWidget.getCurrentPosition();
         int entityScreenY = (int)(pos.y + eyeHeight * (float)scale);
         float offsetX = mouseX - pos.x;
         float offsetY = mouseY - pos.y;
         RenderUtils.renderEntityInInventoryFollowsMouse(gui, (int)pos.x, entityScreenY, scale, offsetX, offsetY, entity);
         if (entity instanceof HumanoidEntity humanoid) {
            double dx = Mth.clamp((double)offsetX / ((double)this.width / 2.0), -1.0, 1.0);
            double dy = Mth.clamp((double)offsetY / ((double)this.height / 2.0), -1.0, 1.0);
            Vec3 gazeTarget = new Vec3(dx, -dy, 0.0);
            humanoid.getGazeSystem().setGazePosition(gazeTarget);
            humanoid.getGazeSystem().updateClient(new Vec3(0.0, 0.0, 0.0), humanoid.getXRot(), humanoid.getYRot(), humanoid.getAnimationSystem());
         }
      }
   }

   public void onClose() {
      if (!this.buttonManager.isAnimAnyBtn()) {
         super.onClose();
      }
   }

   public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
      if (0 == pButton) {
         float newX = (float)((double)this.offset.x + pDragX);
         float newY = (float)((double)this.offset.y + pDragY);
         this.offset.set(newX, newY);
      }

      return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
      this.buttonManager.rotate((float)(delta * 0.1F));
      return true;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
      if (pButton == 0) {
         for (StretchButton button : this.buttonManager.getAllButtons()) {
            if (button.isMouseOver(pMouseX, pMouseY)) {
               button.mouseReleased(pMouseX, pMouseY, pButton);
            }
         }
      }

      if (pButton == 1 && this.buttonManager.getPages().size() != 1) {
         this.buttonManager.animatePageSwitch(this.width, this.height);
         return true;
      } else {
         return super.mouseReleased(pMouseX, pMouseY, pButton);
      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void rebuildButtons() {
      this.createButtons(true);
   }

   public void rebuildWidgets() {
      super.rebuildWidgets();
   }

   @Override
   public double defaultScale() {
      return 4.0;
   }

   @Generated
   public EntityWidget getEntityWidget() {
      return this.entityWidget;
   }

   @Generated
   public Vector2f getOffset() {
      return this.offset;
   }
}
