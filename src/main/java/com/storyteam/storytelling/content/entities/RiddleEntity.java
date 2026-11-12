package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.system.persona.PersonaHandler;
import com.storyteam.storytelling.system.riddles.data.RiddlesDef;
import com.storyteam.storytelling.system.riddles.storage.RiddlesStorage;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RiddleEntity extends AnimatedEntity {
   public static final double TRIGGER_DISTANCE = 5.0;
   public static final float MAX_BUBBLE_SCALE = 1.0F;
   public static final float MIN_BUBBLE_SCALE = 0.0F;
   public static final float MAX_QUESTION_SCALE = 1.5F;
   public static final float MIN_QUESTION_SCALE = 0.0F;
   public static final float EPSILON = 0.01F;
   private boolean showText = false;
   private float bubbleScale = 0.0F;
   private float questionScale = 1.5F;
   private static final EntityDataAccessor<String> RIDDLE_ID = SynchedEntityData.defineId(RiddleEntity.class, EntityDataSerializers.STRING);

   public RiddleEntity(EntityType<? extends AnimatedEntity> type, Level level) {
      super(type, level);
      this.noPhysics = true;
   }

   @Override
   public void tick() {
      super.tick();
      this.setDeltaMovement(Vec3.ZERO);
      RiddlesDef def = this.getRiddle();
      if (def != null && def.isVisible()) {
         Player player = this.level().getNearestPlayer(this, 5.0);
         this.showText = player != null;
         if (this.showText && this.bubbleScale < 1.0F && this.questionScale <= 0.0F) {
            this.bubbleScale = this.bubbleScale + (1.0F - this.bubbleScale) * 0.25F;
            if (1.0F - this.bubbleScale < 0.01F) {
               this.bubbleScale = 1.0F;
            }
         } else if (!this.showText && this.bubbleScale > 0.0F) {
            this.bubbleScale = this.bubbleScale - (this.bubbleScale - 0.0F) * 0.25F;
            if (this.bubbleScale - 0.0F < 0.01F) {
               this.bubbleScale = 0.0F;
            }
         }

         this.bubbleScale = Mth.clamp(this.bubbleScale, 0.0F, 1.0F);
         if (!this.shouldShowText()) {
            this.questionScale = this.questionScale + (1.5F - this.questionScale) * 0.25F;
            if (1.5F - this.questionScale < 0.01F) {
               this.questionScale = 1.5F;
            }
         } else {
            this.questionScale = this.questionScale - (this.questionScale - 0.0F) * 0.25F;
            if (this.questionScale - 0.0F < 0.01F) {
               this.questionScale = 0.0F;
            }
         }

         this.questionScale = Mth.clamp(this.questionScale, 0.0F, 1.5F);
      } else {
         this.showText = false;
         this.bubbleScale = 0.0F;
         this.questionScale = 1.5F;
      }
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public boolean isAlive() {
      return true;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(RIDDLE_ID, "");
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      String id = (String)this.entityData.get(RIDDLE_ID);
      if (id != null && !id.isEmpty()) {
         tag.putString("RiddleId", id);
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("RiddleId")) {
         String id = tag.getString("RiddleId");
         this.entityData.set(RIDDLE_ID, id);
      }
   }

   public RiddlesDef getRiddle() {
      String id = (String)this.entityData.get(RIDDLE_ID);
      return id != null && !id.isEmpty() ? RiddlesStorage.get(id) : null;
   }

   public void setRiddle(RiddlesDef riddle) {
      if (!this.level().isClientSide()) {
         this.entityData.set(RIDDLE_ID, riddle.getId());
      }
   }

   public boolean shouldShowText() {
      return this.showText || this.bubbleScale > 0.0F;
   }

   public boolean canReadText() {
      return RiddlesStorage.canReadByPersona(PersonaHandler.getCurrentPersona());
   }

   @Generated
   public float getBubbleScale() {
      return this.bubbleScale;
   }

   @Generated
   public float getQuestionScale() {
      return this.questionScale;
   }
}
