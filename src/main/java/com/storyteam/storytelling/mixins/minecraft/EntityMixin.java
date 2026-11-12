package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.util.mixin.HLAccessor;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.util.java.Cast;

@Mixin({Entity.class})
@Implements({@Interface(
      iface = IStoryEntity.class,
      prefix = "i$"
   )})
public abstract class EntityMixin implements IStoryEntity {
   @Shadow
   private Level level;
   @Shadow
   private CompoundTag persistentData;
   @Shadow
   @Nullable
   private RemovalReason removalReason;
   @Unique
   private String STT_CustomTexture;

   @Shadow
   @Override
   public abstract boolean equals(Object var1);

   @Shadow
   protected abstract void addAdditionalSaveData(CompoundTag var1);

   @Shadow
   protected abstract void readAdditionalSaveData(CompoundTag var1);

   @Shadow
   public abstract int getId();

   @Shadow
   public abstract boolean isAlive();

   @Shadow
   public abstract EntityType<?> getType();

   public boolean i$isStorytellingEntity() {
      return this.persistentData != null && this.persistentData.contains("Storytelling", 10);
   }

   public boolean i$isUndespawnable() {
      return this.persistentData != null && this.persistentData.getBoolean("STT_Undespawnable");
   }

   public boolean i$isSEVulnerable() {
      return this.persistentData != null && this.persistentData.getCompound("Storytelling").getBoolean("Vulnerable");
   }

   public String i$getStoryName() {
      String name = this.isStorytellingEntity() ? this.persistentData.getCompound("Storytelling").getString("Id") : null;
      if (name == null && this.isStorytellingEntity()) {
         throw new IllegalStateException(String.format("Storytelling entity of type %s doesn't have story name.", this.getType()));
      } else {
         return name;
      }
   }

   public String i$getCustomTexture() {
      return this.persistentData != null && this.persistentData.contains("STT_CustomTexture") ? this.persistentData.getString("STT_CustomTexture") : null;
   }

   public void i$setCustomTexture(String s) {
      boolean changed = false;
      if (s == null) {
         if (this.persistentData != null) {
            this.persistentData.remove("STT_CustomTexture");
            changed = true;
         }
      } else {
         String prevCustomTexture = this.persistentData.getString("STT_CustomTexture");
         if (!prevCustomTexture.equals(s)) {
            this.persistentData.putString("STT_CustomTexture", s);
            changed = true;
         }
      }

      if (!this.level.isClientSide && changed) {
         HLAccessor.setTexture(this.getId(), s);
      }
   }

   public void i$storytellingTick() {
      if (!this.level.isClientSide()) {
         if (this.isStorytellingEntity()) {
            CompoundTag storytelling = this.persistentData.getCompound("Storytelling");
            IStoryServer story = IStoryServer.get(this.level.getServer());
            EntityRepository repo = story.getEntityRepository();
            repo.reportStoryEntity((Entity)(Object)this, storytelling);
         }

         if (this.persistentData != null && this.persistentData.contains("STT_CustomTexture")) {
            String tex = this.persistentData.getString("STT_CustomTexture");
            if (!Objects.equals(tex, this.STT_CustomTexture)) {
               this.STT_CustomTexture = tex;
               HLAccessor.setTexture(this.getId(), tex);
            }
         }
      }

      if (this.isStorytellingEntity()) {
         StoryData sd = StoryData.of((Entity)Cast.cast(this));
         if (sd != null) {
            sd.tick(this.level);
         }
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tickStorytelling(CallbackInfo ci) {
      this.storytellingTick();
   }

   @Inject(
      method = {"onAddedToWorld"},
      at = {@At("TAIL")},
      remap = false
   )
   public void onEntityLoad(CallbackInfo ci) {
      this.onEntityLoad((Entity)(Object)this);
   }

   public CompoundTag i$gatherAdditionalNbtData_ST() {
      CompoundTag nbt = new CompoundTag();
      this.addAdditionalSaveData(nbt);
      return nbt;
   }

   public void i$applyAdditionalNbtData_ST(CompoundTag nbt) {
      this.readAdditionalSaveData(nbt);
   }
}
