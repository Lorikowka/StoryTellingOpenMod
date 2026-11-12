package com.storyteam.storytelling.system.simulator.me_tab.word.data;

import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class AnimatedLetter {
   private final Component character;
   private final int index;
   private float scale;
   private float alpha = 0.0F;
   private float rotationZ;
   private float rotationY;
   private float posX = 0.0F;
   private float posY = 0.0F;

   public AnimatedLetter(Component character, int index) {
      this.character = character;
      this.index = index;
   }

   public float getWidthChar() {
      return (float)Minecraft.getInstance().font.width(this.character);
   }

   public float getHeightChar() {
      return 9.0F;
   }

   public AnimatedLetter copy() {
      Component copiedChar = this.character.copy();
      AnimatedLetter copy = new AnimatedLetter(copiedChar, this.index);
      copy.setScale(this.scale);
      copy.setAlpha(this.alpha);
      copy.setRotationZ(this.rotationZ);
      copy.setRotationY(this.rotationY);
      copy.setPosX(this.posX);
      copy.setPosY(this.posY);
      return copy;
   }

   @Generated
   public Component getCharacter() {
      return this.character;
   }

   @Generated
   public int getIndex() {
      return this.index;
   }

   @Generated
   public float getScale() {
      return this.scale;
   }

   @Generated
   public float getAlpha() {
      return this.alpha;
   }

   @Generated
   public float getRotationZ() {
      return this.rotationZ;
   }

   @Generated
   public float getRotationY() {
      return this.rotationY;
   }

   @Generated
   public float getPosX() {
      return this.posX;
   }

   @Generated
   public float getPosY() {
      return this.posY;
   }

   @Generated
   public void setScale(float scale) {
      this.scale = scale;
   }

   @Generated
   public void setAlpha(float alpha) {
      this.alpha = alpha;
   }

   @Generated
   public void setRotationZ(float rotationZ) {
      this.rotationZ = rotationZ;
   }

   @Generated
   public void setRotationY(float rotationY) {
      this.rotationY = rotationY;
   }

   @Generated
   public void setPosX(float posX) {
      this.posX = posX;
   }

   @Generated
   public void setPosY(float posY) {
      this.posY = posY;
   }
}
