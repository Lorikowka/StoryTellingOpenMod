package com.storyteam.storytelling.system.riddles.data;

import com.storyteam.storytelling.system.riddles.storage.RiddlesStorage;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class RiddlesDef implements ICustomRegistrar {
   @NBTSerializable
   private String id;
   @NBTSerializable
   private String coordinate;
   @NBTSerializable
   private String text;
   @NBTSerializable
   private String answer;
   @NBTSerializable
   private int reward;
   @NBTSerializable
   private boolean visible;

   public RiddlesDef(String coordinate, String text, String answer, int reward) {
      this.coordinate = coordinate;
      this.text = text;
      this.answer = answer;
      this.reward = reward;
      this.visible = false;
   }

   public static RiddlesDef.Builder builder() {
      return new RiddlesDef.Builder();
   }

   public void performRegister(RegisterEvent event, ResourceLocation rl) {
      RiddlesStorage.register(rl.toString(), this);
   }

   @Override
   public String toString() {
      return String.format(
         "RiddleDef {\n  id='%s',\n  coordinate='%s',\n  text='%s',\n  answer='%s',\n  reward=%d,\n  visible=%b\n}",
         this.id,
         this.coordinate,
         this.text,
         this.answer,
         this.reward,
         this.visible
      );
   }

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public void setId(String id) {
      this.id = id;
   }

   @Generated
   public String getCoordinate() {
      return this.coordinate;
   }

   @Generated
   public void setCoordinate(String coordinate) {
      this.coordinate = coordinate;
   }

   @Generated
   public String getText() {
      return this.text;
   }

   @Generated
   public void setText(String text) {
      this.text = text;
   }

   @Generated
   public String getAnswer() {
      return this.answer;
   }

   @Generated
   public void setAnswer(String answer) {
      this.answer = answer;
   }

   @Generated
   public int getReward() {
      return this.reward;
   }

   @Generated
   public void setReward(int reward) {
      this.reward = reward;
   }

   @Generated
   public boolean isVisible() {
      return this.visible;
   }

   @Generated
   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public static class Builder {
      private String coordinate = "";
      private String text = "";
      private String answer = "";
      private int reward = 0;

      public RiddlesDef.Builder coordinate(String coordinate) {
         this.coordinate = coordinate;
         return this;
      }

      public RiddlesDef.Builder text(String text) {
         this.text = text;
         return this;
      }

      public RiddlesDef.Builder answer(String answer) {
         this.answer = answer;
         return this;
      }

      public RiddlesDef.Builder reward(int reward) {
         this.reward = reward;
         return this;
      }

      public RiddlesDef build() {
         return new RiddlesDef(this.coordinate, this.text, this.answer, this.reward);
      }
   }
}
