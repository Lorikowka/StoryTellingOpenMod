package com.storyteam.storytelling.system._core.morph.data;

import com.storyteam.storytelling.St;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import org.zeith.hammerlib.abstractions.props.Key;

public record PlayerPoseMap(Map<Pose, EntityDimensions> poses) {
   public static final Key<PlayerPoseMap> POSES = Key.of(St.id("poses"), PlayerPoseMap.class);

   public boolean hasPose(Pose pPose) {
      return this.poses.containsKey(pPose);
   }

   public EntityDimensions getPose(Pose pPose) {
      return this.poses.get(pPose);
   }

   @Generated
   public static PlayerPoseMap.PlayerPoseMapBuilder builder() {
      return new PlayerPoseMap.PlayerPoseMapBuilder();
   }

   @Generated
   public static class PlayerPoseMapBuilder {
      @Generated
      private ArrayList<Pose> poses$key;
      @Generated
      private ArrayList<EntityDimensions> poses$value;

      @Generated
      PlayerPoseMapBuilder() {
      }

      @Generated
      public PlayerPoseMap.PlayerPoseMapBuilder pose(Pose poseKey, EntityDimensions poseValue) {
         if (this.poses$key == null) {
            this.poses$key = new ArrayList<>();
            this.poses$value = new ArrayList<>();
         }

         this.poses$key.add(poseKey);
         this.poses$value.add(poseValue);
         return this;
      }

      @Generated
      public PlayerPoseMap.PlayerPoseMapBuilder poses(Map<? extends Pose, ? extends EntityDimensions> poses) {
         if (poses == null) {
            throw new NullPointerException("poses cannot be null");
         } else {
            if (this.poses$key == null) {
               this.poses$key = new ArrayList<>();
               this.poses$value = new ArrayList<>();
            }

            for (Entry<? extends Pose, ? extends EntityDimensions> $lombokEntry : poses.entrySet()) {
               this.poses$key.add($lombokEntry.getKey());
               this.poses$value.add($lombokEntry.getValue());
            }

            return this;
         }
      }

      @Generated
      public PlayerPoseMap.PlayerPoseMapBuilder clearPoses() {
         if (this.poses$key != null) {
            this.poses$key.clear();
            this.poses$value.clear();
         }

         return this;
      }

      @Generated
      public PlayerPoseMap build() {
         return new PlayerPoseMap(
            switch (this.poses$key == null ? 0 : this.poses$key.size()) {
               case 0 -> Collections.emptyMap();
               case 1 -> Collections.singletonMap(this.poses$key.get(0), this.poses$value.get(0));
               default -> {
                  Map<Pose, EntityDimensions> poses = new LinkedHashMap<>(
                     this.poses$key.size() < 1073741824 ? 1 + this.poses$key.size() + (this.poses$key.size() - 3) / 3 : Integer.MAX_VALUE
                  );

                  for (int $i = 0; $i < this.poses$key.size(); $i++) {
                     poses.put(this.poses$key.get($i), this.poses$value.get($i));
                  }

                  yield Collections.unmodifiableMap(poses);
               }
            }
         );
      }

      @Generated
      @Override
      public String toString() {
         return "PlayerPoseMap.PlayerPoseMapBuilder(poses$key=" + this.poses$key + ", poses$value=" + this.poses$value + ")";
      }
   }
}
