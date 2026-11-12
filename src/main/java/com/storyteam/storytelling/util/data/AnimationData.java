package com.storyteam.storytelling.util.data;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.apache.commons.lang3.tuple.Pair;

public class AnimationData {
   private final List<Pair<Integer, Integer>> frames = new ArrayList<>();

   public int getLength() {
      int time = 0;

      for (Pair<Integer, Integer> pair : this.frames) {
         time += pair.getRight();
      }

      return time;
   }

   public static AnimationData builder() {
      return new AnimationData();
   }

   public AnimationData frame(int index, int time) {
      this.frames.add(Pair.of(index, time));
      return this;
   }

   @Generated
   public List<Pair<Integer, Integer>> getFrames() {
      return this.frames;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof AnimationData other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$frames = this.getFrames();
         Object other$frames = other.getFrames();
         return this$frames == null ? other$frames == null : this$frames.equals(other$frames);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof AnimationData;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $frames = this.getFrames();
      return result * 59 + ($frames == null ? 43 : $frames.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "AnimationData(frames=" + this.getFrames() + ")";
   }
}
