package com.storyteam.storytelling.system.octospizdel.particle;

import lombok.Generated;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

public class Transform {
   private Vector2f position;
   private float roll;
   private Vector2f size;
   private Vector2f oldPosition;
   private float oldRoll;
   private Vector2f oldSize;

   public Transform(Vector2f position, float roll, Vector2f size) {
      this.position = position;
      this.roll = roll;
      this.size = size;
      this.oldPosition = new Vector2f(position);
      this.oldRoll = roll;
      this.oldSize = new Vector2f(size);
   }

   public void setRoll(float angle) {
      angle %= 360.0F;
      if (angle < 0.0F) {
         angle += 360.0F;
      }

      this.roll = angle;
   }

   public void updateOldValues() {
      this.oldPosition = new Vector2f(this.position);
      this.oldRoll = this.roll;
      this.oldSize = new Vector2f(this.size);
   }

   public Vector2f getInterpolatedPosition(float partialTicks) {
      return new Vector2f(Mth.lerp(partialTicks, this.oldPosition.x, this.position.x), Mth.lerp(partialTicks, this.oldPosition.y, this.position.y));
   }

   public float getInterpolatedRoll(float partialTicks) {
      return lerpAngle(partialTicks, this.oldRoll, this.roll);
   }

   public Vector2f getInterpolatedSize(float partialTicks) {
      return new Vector2f(Mth.lerp(partialTicks, this.oldSize.x, this.size.x), Mth.lerp(partialTicks, this.oldSize.y, this.size.y));
   }

   private static float lerpAngle(float partialTicks, float start, float end) {
      float delta = (end - start + 540.0F) % 360.0F - 180.0F;
      return (start + delta * partialTicks + 360.0F) % 360.0F;
   }

   @Generated
   public Vector2f getPosition() {
      return this.position;
   }

   @Generated
   public float getRoll() {
      return this.roll;
   }

   @Generated
   public Vector2f getSize() {
      return this.size;
   }

   @Generated
   public Vector2f getOldPosition() {
      return this.oldPosition;
   }

   @Generated
   public float getOldRoll() {
      return this.oldRoll;
   }

   @Generated
   public Vector2f getOldSize() {
      return this.oldSize;
   }

   @Generated
   public void setPosition(Vector2f position) {
      this.position = position;
   }

   @Generated
   public void setSize(Vector2f size) {
      this.size = size;
   }
}
