package com.storyteam.storytelling.system.simulator.me_tab.client.button;

import com.storyteam.storytelling.content.entities.HumanoidEntity;
import java.awt.Rectangle;
import lombok.Generated;
import net.minecraft.client.gui.screens.Screen;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class EntityWidget {
   private final HumanoidEntity entity;
   private Vector2f basePos;
   private int scale;
   private Vector4f padding = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);
   Screen screen;

   public EntityWidget(Screen screen, HumanoidEntity entity, int scale, Vector2f position) {
      this.entity = entity;
      this.scale = scale;
      this.basePos = position;
      this.screen = screen;
      this.padding = new Vector4f(20.0F, 20.0F, 20.0F, 0.0F);
   }

   public Vector2f getCurrentPosition() {
      return new Vector2f(this.basePos.x, this.basePos.y);
   }

   public Rectangle getLocalBounds() {
      float w = this.entity.getBbWidth() * (float)this.scale;
      float h = this.entity.getBbHeight() * (float)this.scale;
      float eyeHeight = this.entity.getEyeHeight(this.entity.getPose()) * (float)this.scale;
      float x = this.basePos.x - w / 2.0F;
      float y = this.basePos.y;
      int offsetY = (int)(h - eyeHeight);
      return new Rectangle((int)x, (int)y - offsetY, (int)w, (int)h);
   }

   public Rectangle getScreenBounds(Vector2f offset, float zoom) {
      Rectangle local = this.getLocalBounds();
      int x = (int)((float)local.x * zoom + offset.x);
      int y = (int)((float)local.y * zoom + offset.y);
      int w = (int)((float)local.width * zoom);
      int h = (int)((float)local.height * zoom);
      return new Rectangle(x, y, w, h);
   }

   @Generated
   public HumanoidEntity getEntity() {
      return this.entity;
   }

   @Generated
   public Vector2f getBasePos() {
      return this.basePos;
   }

   @Generated
   public int getScale() {
      return this.scale;
   }

   @Generated
   public Vector4f getPadding() {
      return this.padding;
   }

   @Generated
   public Screen getScreen() {
      return this.screen;
   }

   @Generated
   public void setBasePos(Vector2f basePos) {
      this.basePos = basePos;
   }

   @Generated
   public void setScale(int scale) {
      this.scale = scale;
   }

   @Generated
   public void setPadding(Vector4f padding) {
      this.padding = padding;
   }
}
