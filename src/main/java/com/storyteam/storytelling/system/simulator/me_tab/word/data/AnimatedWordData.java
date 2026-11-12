package com.storyteam.storytelling.system.simulator.me_tab.word.data;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentStorage;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;

public class AnimatedWordData {
   private FragmentData data = null;
   private Component text;
   private List<AnimatedLetter> letters = new ArrayList<>();
   private float startScale = 4.0F;
   private float targetScale = 2.5F;

   public AnimatedWordData() {
   }

   public AnimatedWordData(Component text) {
      this.text = text;
   }

   public AnimatedWordData(FragmentData data) {
      this.text = data.getWord();
      this.data = data;
   }

   public AnimatedWordData copy() {
      AnimatedWordData copy = new AnimatedWordData();
      copy.setData(this.data);
      if (this.text != null) {
         copy.setText(this.text.copy());
      }

      List<AnimatedLetter> lettersCopy = new ArrayList<>();

      for (AnimatedLetter letter : this.letters) {
         lettersCopy.add(letter.copy());
      }

      copy.setLetters(lettersCopy);
      copy.setStartScale(this.startScale);
      copy.setTargetScale(this.targetScale);
      return copy;
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      if (this.data != null) {
         tag.putString("fragment_data", this.data.getId());
      }

      tag.putString("text", Serializer.toJson(this.text));
      return tag;
   }

   public AnimatedWordData deserializeNBT(CompoundTag tag) {
      Component text = Serializer.fromJson(tag.getString("text"));
      AnimatedWordData data;
      if (tag.contains("fragment_data")) {
         String fragId = tag.getString("fragment_data");
         data = new AnimatedWordData(FragmentStorage.FRAGMENTS_MAP.get(fragId));
      } else {
         data = new AnimatedWordData(text);
      }

      return data;
   }

   @Generated
   public FragmentData getData() {
      return this.data;
   }

   @Generated
   public Component getText() {
      return this.text;
   }

   @Generated
   public List<AnimatedLetter> getLetters() {
      return this.letters;
   }

   @Generated
   public float getStartScale() {
      return this.startScale;
   }

   @Generated
   public float getTargetScale() {
      return this.targetScale;
   }

   @Generated
   public void setData(FragmentData data) {
      this.data = data;
   }

   @Generated
   public void setText(Component text) {
      this.text = text;
   }

   @Generated
   public void setLetters(List<AnimatedLetter> letters) {
      this.letters = letters;
   }

   @Generated
   public void setStartScale(float startScale) {
      this.startScale = startScale;
   }

   @Generated
   public void setTargetScale(float targetScale) {
      this.targetScale = targetScale;
   }
}
