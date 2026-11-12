package com.storyteam.storytelling.api.animations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;

public class LivingAnimationSet<CTX> {
   private final Map<String, AnimationWithCondition<CTX>> animations;

   @Generated
   LivingAnimationSet(Map<String, AnimationWithCondition<CTX>> animations) {
      this.animations = animations;
   }

   @Generated
   public static <CTX> LivingAnimationSet.LivingAnimationSetBuilder<CTX> builder() {
      return new LivingAnimationSet.LivingAnimationSetBuilder<>();
   }

   @Generated
   public Map<String, AnimationWithCondition<CTX>> getAnimations() {
      return this.animations;
   }

   public static class LivingAnimationSetBuilder<CTX> {
      @Generated
      private ArrayList<String> animations$key;
      @Generated
      private ArrayList<AnimationWithCondition<CTX>> animations$value;

      public LivingAnimationSet.LivingAnimationSetBuilder<CTX> addAnimation(AnimationWithCondition<CTX> anim) {
         int k = (this.animations$key == null ? 0 : this.animations$key.size()) + 1;
         return this.animation("AG_" + k, anim);
      }

      @Generated
      LivingAnimationSetBuilder() {
      }

      @Generated
      public LivingAnimationSet.LivingAnimationSetBuilder<CTX> animation(String animationKey, AnimationWithCondition<CTX> animationValue) {
         if (this.animations$key == null) {
            this.animations$key = new ArrayList<>();
            this.animations$value = new ArrayList<>();
         }

         this.animations$key.add(animationKey);
         this.animations$value.add(animationValue);
         return this;
      }

      @Generated
      public LivingAnimationSet.LivingAnimationSetBuilder<CTX> animations(Map<? extends String, ? extends AnimationWithCondition<CTX>> animations) {
         if (animations == null) {
            throw new NullPointerException("animations cannot be null");
         } else {
            if (this.animations$key == null) {
               this.animations$key = new ArrayList<>();
               this.animations$value = new ArrayList<>();
            }

            for (Entry<? extends String, ? extends AnimationWithCondition<CTX>> $lombokEntry : animations.entrySet()) {
               this.animations$key.add($lombokEntry.getKey());
               this.animations$value.add((AnimationWithCondition<CTX>)$lombokEntry.getValue());
            }

            return this;
         }
      }

      @Generated
      public LivingAnimationSet.LivingAnimationSetBuilder<CTX> clearAnimations() {
         if (this.animations$key != null) {
            this.animations$key.clear();
            this.animations$value.clear();
         }

         return this;
      }

      @Generated
      public LivingAnimationSet<CTX> build() {
         return new LivingAnimationSet<>(
            switch (this.animations$key == null ? 0 : this.animations$key.size()) {
               case 0 -> Collections.emptyMap();
               case 1 -> Collections.singletonMap(this.animations$key.get(0), this.animations$value.get(0));
               default -> {
                  Map<String, AnimationWithCondition<CTX>> animations = new LinkedHashMap<>(
                     this.animations$key.size() < 1073741824 ? 1 + this.animations$key.size() + (this.animations$key.size() - 3) / 3 : Integer.MAX_VALUE
                  );

                  for (int $i = 0; $i < this.animations$key.size(); $i++) {
                     animations.put(this.animations$key.get($i), this.animations$value.get($i));
                  }

                  yield Collections.unmodifiableMap(animations);
               }
            }
         );
      }

      @Generated
      @Override
      public String toString() {
         return "LivingAnimationSet.LivingAnimationSetBuilder(animations$key=" + this.animations$key + ", animations$value=" + this.animations$value + ")";
      }
   }
}
