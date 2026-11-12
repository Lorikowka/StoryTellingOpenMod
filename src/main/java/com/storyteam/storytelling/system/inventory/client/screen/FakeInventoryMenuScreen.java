package com.storyteam.storytelling.system.inventory.client.screen;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.system.inventory.InventorySystem;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.client.flowgui.GuiAttributeWidget;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.EntityWidget;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.utils.RenderUtils;
import com.storyteam.storytelling.system.simulator.me_tab.init.EntitiesMeTab;
import com.storyteam.storytelling.util.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.client.screen.ScreenWTFMojang;
import org.zeith.hammerlib.util.math.Point;

public class FakeInventoryMenuScreen extends ScreenWTFMojang<FakeInventoryMenu> implements IAdvancedGui<FakeInventoryMenuScreen> {
   private final FakeInventoryMenu menu;
   private GuiRootObject root;
   protected EntityWidget entityWidget = null;
   public final Map<AttributeType, GuiAttributeWidget> attributeWidgetMap = new HashMap<>();

   public FakeInventoryMenuScreen(FakeInventoryMenu menu, Inventory inventory, Component label) {
      super(menu, inventory, label);
      this.menu = menu;
   }

   protected void renderBackground(GuiGraphics pose, float partialTime, int mouseX, int mouseY) {
   }

   public void init() {
      super.init();
      if (this.minecraft != null) {
         this.root = (GuiRootObject)this.addRenderableWidget(GuiObject.root());
         GuiObject canva;
         this.root.addChild(canva = GuiObject.create("canva").empty().scale(0.85F).pos((float)this.width / 2.0F, (float)this.height / 2.0F));
         GuiObject bg;
         canva.addChild(
            bg = GuiObject.create("bg").image(InventorySystem.INSTANCE.guiTex("inventory_base"), 0.0F, 0.0F, 191.0F, 258.0F, 191.0F, 258.0F).centered(0, 0)
         );
         Vec2[] slotposes = new Vec2[]{
            new Vec2(15.0F, 101.0F),
            new Vec2(15.0F, 74.0F),
            new Vec2(15.0F, 47.0F),
            new Vec2(42.0F, 20.0F),
            new Vec2(69.0F, 20.0F),
            new Vec2(96.0F, 20.0F),
            new Vec2(123.0F, 20.0F),
            new Vec2(150.0F, 47.0F),
            new Vec2(150.0F, 74.0F)
         };

         for (int i = 0; i < slotposes.length; i++) {
            Vec2 slotpos = slotposes[i];
            bg.addChild(
               GuiObject.create("texslot" + i)
                  .fullImage(
                     InventorySystem.INSTANCE.guiTex(InventorySystem.getHotBarSize(this.minecraft.player) > i ? "inventory_slot_on" : "inventory_slot_off"),
                     26.0F,
                     26.0F
                  )
                  .pos(slotpos.x, slotpos.y)
            );
            bg.addChild(GuiObject.create("slot" + i).slot(this.menu.getSlot(i)).size(20.0F, 20.0F).pos(slotpos.x + 3.0F, slotpos.y + 3.0F));
         }

         if (InventorySystem.isSecondHandAvailable(this.minecraft.player)) {
            bg.addChild(GuiObject.create("texslotleft").fullImage(InventorySystem.INSTANCE.guiTex("inventory_slot_pink_on"), 26.0F, 26.0F).pos(150.0F, 101.0F));
            bg.addChild(GuiObject.create("slotleft").slot(this.menu.getSlot(9)).size(20.0F, 20.0F).pos(153.0F, 104.0F));
         }

         int scale = 46;
         HumanoidEntity entity = (HumanoidEntity)EntitiesMeTab.LO_ME_TAB_HEAD.create(this.minecraft.level);
         float eyeHeight = entity.getEyeHeight(entity.getPose()) * (float)scale;
         this.entityWidget = new EntityWidget(this, entity, scale, new Vector2f((float)this.width / 2.0F, (float)this.height / 2.0F - eyeHeight / 4.0F - 23.0F));
         this.root.pos(0.0F, -7.0F);
         GuiAttributeWidget body = new GuiAttributeWidget("body", PlayerStats.getAttribute(AttributeType.BODY)).size(30.0F, 30.0F).pos(48.0F, 186.0F);
         bg.addChild(body);
         GuiAttributeWidget mind = new GuiAttributeWidget("mind", PlayerStats.getAttribute(AttributeType.MIND))
            .size(30.0F, 30.0F)
            .pos(((Point)body.elementPosition.get()).x() + (Float)body.elementWidth.get() + 2.0F, 186.0F);
         bg.addChild(mind);
         GuiAttributeWidget willpower = new GuiAttributeWidget("willpower", PlayerStats.getAttribute(AttributeType.WILLPOWER))
            .size(30.0F, 30.0F)
            .pos(((Point)mind.elementPosition.get()).x() + (Float)mind.elementWidth.get() + 2.0F, 186.0F);
         bg.addChild(willpower);
         this.attributeWidgetMap.put(AttributeType.BODY, body);
         this.attributeWidgetMap.put(AttributeType.MIND, mind);
         this.attributeWidgetMap.put(AttributeType.WILLPOWER, willpower);
      }
   }

   public void containerTick() {
      this.entityWidget.getEntity().getAnimationSystem().tick();
      if (this.root != null) {
         this.root.sendUpdate();
      }
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      super.render(graphics, mouseX, mouseY, partialTick);
      Minecraft mc = Minecraft.getInstance();
      double realMouseX = mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth();
      double realMouseY = mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight();
      this.drawEntity(graphics, this.entityWidget.getScale(), (float)realMouseX, (float)realMouseY, this.entityWidget.getEntity());
      graphics.pose().pushPose();
      int y = 10;





      graphics.pose().popPose();
   }

   public void drawEntity(GuiGraphics gui, int scale, float mouseX, float mouseY, AnimatedEntity entity) {
      if (entity != null) {
         if (!entity.getAnimationSystem().isActiveAnimation("action", AnimationsST8.FLOATING_POSE)) {
            entity.getAnimationSystem().startAnimationAt("action", AnimationsST8.FLOATING_POSE.configure().transitionTime(0.0F));
         }

         if (!entity.getAnimationSystem().isActiveAnimation("emotion", AnimationsST8.FLOATING_CYCLE_LAYER)) {
            entity.getAnimationSystem().startAnimationAt("emotion", AnimationsST8.FLOATING_CYCLE_LAYER.configure().transitionTime(0.0F));
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

}
