package com.storyteam.storytelling.script.performer.entity;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.configurations.ILivingAnimationEntity;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.performer.entity.look.AbstractLookController;
import com.storyteam.storytelling.script.performer.entity.look.DefaultLookController;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.system.clothes.entity.VanityInventory;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.util.data.buffered.BooleanBuffer;
import com.storyteam.storytelling.util.world.Location;
import java.util.HashMap;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class EntityStateData<T extends Entity> implements IAutoNBTSerializable, Cloneable {
   private final HashMap<String, ConfiguredAnimation> animations = new HashMap<>();
   @NBTSerializable
   private double moveSpeed;
   @NBTSerializable
   @Nullable
   private Location pos;
   @NBTSerializable
   private boolean blockAnimRotateWalking;
   @NBTSerializable
   private float alphaFadeable;
   @NBTSerializable
   private boolean blockBody;
   @NBTSerializable
   private boolean lockHead;
   @NBTSerializable
   private boolean ignoreNavmesh;
   @NBTSerializable
   private boolean traversalNavigation;
   @NBTSerializable
   private ItemStack mainHandItem = ItemStack.EMPTY;
   @NBTSerializable
   private ItemStack offHandItem = ItemStack.EMPTY;
   @NBTSerializable
   private VanityInventory vanity = new VanityInventory();
   @NotNull
   private AbstractLookController<T> lookController = new DefaultLookController<>();
   @NBTSerializable
   private boolean goalsEnabled;
   @NBTSerializable
   private boolean freeSight = true;
   @NBTSerializable
   private Component displayName;
   @NBTSerializable
   EntityControlPriority priority;
   @NBTSerializable
   private ResourceLocation textureOverride;
   @NBTSerializable
   private boolean textureWasChanged;
   @NBTSerializable
   private boolean dontRotateOnInteraction;
   @NBTSerializable
   private final BooleanBuffer shouldUpdateChunk = new BooleanBuffer();
   @NBTSerializable
   @Nullable
   private ConfiguredAnimation overrideAnimationAll;
   private HashMap<MovementActionType, ConfiguredAnimation> overrideAnimations = new HashMap<>();

   public void setLookTarget(@Nullable ILookTarget<T> lookTarget) {
      this.lookController.setTarget(lookTarget);
   }

   public void setQuick(boolean isQuick) {
      this.lookController.setQuick(isQuick);
   }

   @Nullable
   public ILookTarget<T> getLookTarget() {
      return this.lookController.getTarget();
   }

   public void setHeldItem(InteractionHand hand, ItemStack stack) {
      stack = stack.copy();
      switch (hand) {
         case MAIN_HAND:
            this.mainHandItem = stack;
            break;
         case OFF_HAND:
            this.offHandItem = stack;
      }
   }

   public void setArmorItem(VanitySlot slot, ItemStack stack) {
      this.vanity = this.vanity.withItem(slot, stack);
   }

   public void setTextureOverride(ResourceLocation textureOverride) {
      this.textureOverride = textureOverride;
      this.textureWasChanged = true;
   }

   public void exchangeData(T entity, EntityController<T> controller) {
      if (entity instanceof LivingEntity le) {
         le.setItemInHand(InteractionHand.MAIN_HAND, this.getMainHandItem());
         le.setItemInHand(InteractionHand.OFF_HAND, this.getOffHandItem());
         if (entity instanceof AnimatedEntity he) {
            he.setInventory(this.vanity);
         }
      }

      if (entity instanceof ILivingAnimationEntity la) {
         la.setAnimationOverrideAll(this.getOverrideAnimationAll());
         this.overrideAnimations.forEach(la::setAnimationOverride);
      }

      if (this.getDisplayName() != null) {
         entity.setCustomName(this.getDisplayName());
      }

      if (this.isTextureWasChanged()) {
         String customTexture = this.getTextureOverride() == null ? null : this.getTextureOverride().toString();
         IStoryEntity.of(entity).setCustomTexture(customTexture);
         this.setTextureWasChanged(false);
      }
   }

   public EntityStateData<T> clone() {
      try {
         EntityStateData<T> clone = (EntityStateData<T>)super.clone();
         clone.setHeldItem(InteractionHand.MAIN_HAND, this.mainHandItem.copy());
         clone.setHeldItem(InteractionHand.OFF_HAND, this.offHandItem.copy());
         clone.displayName = this.displayName;
         clone.animations.putAll(this.animations);
         clone.shouldUpdateChunk.set(this.shouldUpdateChunk.get());
         clone.overrideAnimationAll = this.overrideAnimationAll;
         clone.lookController.setTarget(this.lookController.getTarget());
         clone.vanity = this.vanity.copy();
         return clone;
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError();
      }
   }

   @Generated
   public HashMap<String, ConfiguredAnimation> getAnimations() {
      return this.animations;
   }

   @Generated
   public double getMoveSpeed() {
      return this.moveSpeed;
   }

   @Nullable
   @Generated
   public Location getPos() {
      return this.pos;
   }

   @Generated
   public boolean isBlockAnimRotateWalking() {
      return this.blockAnimRotateWalking;
   }

   @Generated
   public float getAlphaFadeable() {
      return this.alphaFadeable;
   }

   @Generated
   public boolean isBlockBody() {
      return this.blockBody;
   }

   @Generated
   public boolean isLockHead() {
      return this.lockHead;
   }

   @Generated
   public boolean isIgnoreNavmesh() {
      return this.ignoreNavmesh;
   }

   @Generated
   public boolean isTraversalNavigation() {
      return this.traversalNavigation;
   }

   @Generated
   public ItemStack getMainHandItem() {
      return this.mainHandItem;
   }

   @Generated
   public ItemStack getOffHandItem() {
      return this.offHandItem;
   }

   @Generated
   public VanityInventory getVanity() {
      return this.vanity;
   }

   @NotNull
   @Generated
   public AbstractLookController<T> getLookController() {
      return this.lookController;
   }

   @Generated
   public boolean isGoalsEnabled() {
      return this.goalsEnabled;
   }

   @Generated
   public boolean isFreeSight() {
      return this.freeSight;
   }

   @Generated
   public Component getDisplayName() {
      return this.displayName;
   }

   @Generated
   public EntityControlPriority getPriority() {
      return this.priority;
   }

   @Generated
   public ResourceLocation getTextureOverride() {
      return this.textureOverride;
   }

   @Generated
   public boolean isTextureWasChanged() {
      return this.textureWasChanged;
   }

   @Generated
   public boolean isDontRotateOnInteraction() {
      return this.dontRotateOnInteraction;
   }

   @Generated
   public BooleanBuffer getShouldUpdateChunk() {
      return this.shouldUpdateChunk;
   }

   @Nullable
   @Generated
   public ConfiguredAnimation getOverrideAnimationAll() {
      return this.overrideAnimationAll;
   }

   @Generated
   public HashMap<MovementActionType, ConfiguredAnimation> getOverrideAnimations() {
      return this.overrideAnimations;
   }

   @Generated
   public void setMoveSpeed(double moveSpeed) {
      this.moveSpeed = moveSpeed;
   }

   @Generated
   public void setPos(@Nullable Location pos) {
      this.pos = pos;
   }

   @Generated
   public void setBlockAnimRotateWalking(boolean blockAnimRotateWalking) {
      this.blockAnimRotateWalking = blockAnimRotateWalking;
   }

   @Generated
   public void setAlphaFadeable(float alphaFadeable) {
      this.alphaFadeable = alphaFadeable;
   }

   @Generated
   public void setBlockBody(boolean blockBody) {
      this.blockBody = blockBody;
   }

   @Generated
   public void setLockHead(boolean lockHead) {
      this.lockHead = lockHead;
   }

   @Generated
   public void setIgnoreNavmesh(boolean ignoreNavmesh) {
      this.ignoreNavmesh = ignoreNavmesh;
   }

   @Generated
   public void setTraversalNavigation(boolean traversalNavigation) {
      this.traversalNavigation = traversalNavigation;
   }

   @Generated
   public void setMainHandItem(ItemStack mainHandItem) {
      this.mainHandItem = mainHandItem;
   }

   @Generated
   public void setOffHandItem(ItemStack offHandItem) {
      this.offHandItem = offHandItem;
   }

   @Generated
   public void setVanity(VanityInventory vanity) {
      this.vanity = vanity;
   }

   @Generated
   public void setLookController(@NotNull AbstractLookController<T> lookController) {
      if (lookController == null) {
         throw new NullPointerException("lookController is marked non-null but is null");
      } else {
         this.lookController = lookController;
      }
   }

   @Generated
   public void setGoalsEnabled(boolean goalsEnabled) {
      this.goalsEnabled = goalsEnabled;
   }

   @Generated
   public void setFreeSight(boolean freeSight) {
      this.freeSight = freeSight;
   }

   @Generated
   public void setDisplayName(Component displayName) {
      this.displayName = displayName;
   }

   @Generated
   public void setPriority(EntityControlPriority priority) {
      this.priority = priority;
   }

   @Generated
   public void setTextureWasChanged(boolean textureWasChanged) {
      this.textureWasChanged = textureWasChanged;
   }

   @Generated
   public void setDontRotateOnInteraction(boolean dontRotateOnInteraction) {
      this.dontRotateOnInteraction = dontRotateOnInteraction;
   }

   @Generated
   public void setOverrideAnimationAll(@Nullable ConfiguredAnimation overrideAnimationAll) {
      this.overrideAnimationAll = overrideAnimationAll;
   }

   @Generated
   public void setOverrideAnimations(HashMap<MovementActionType, ConfiguredAnimation> overrideAnimations) {
      this.overrideAnimations = overrideAnimations;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EntityStateData<?> other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (Double.compare(this.getMoveSpeed(), other.getMoveSpeed()) != 0) {
         return false;
      } else if (this.isBlockAnimRotateWalking() != other.isBlockAnimRotateWalking()) {
         return false;
      } else if (Float.compare(this.getAlphaFadeable(), other.getAlphaFadeable()) != 0) {
         return false;
      } else if (this.isBlockBody() != other.isBlockBody()) {
         return false;
      } else if (this.isLockHead() != other.isLockHead()) {
         return false;
      } else if (this.isIgnoreNavmesh() != other.isIgnoreNavmesh()) {
         return false;
      } else if (this.isTraversalNavigation() != other.isTraversalNavigation()) {
         return false;
      } else if (this.isGoalsEnabled() != other.isGoalsEnabled()) {
         return false;
      } else if (this.isFreeSight() != other.isFreeSight()) {
         return false;
      } else if (this.isTextureWasChanged() != other.isTextureWasChanged()) {
         return false;
      } else if (this.isDontRotateOnInteraction() != other.isDontRotateOnInteraction()) {
         return false;
      } else {
         Object this$animations = this.getAnimations();
         Object other$animations = other.getAnimations();
         if (this$animations == null ? other$animations == null : this$animations.equals(other$animations)) {
            Object this$pos = this.getPos();
            Object other$pos = other.getPos();
            if (this$pos == null ? other$pos == null : this$pos.equals(other$pos)) {
               Object this$mainHandItem = this.getMainHandItem();
               Object other$mainHandItem = other.getMainHandItem();
               if (this$mainHandItem == null ? other$mainHandItem == null : this$mainHandItem.equals(other$mainHandItem)) {
                  Object this$offHandItem = this.getOffHandItem();
                  Object other$offHandItem = other.getOffHandItem();
                  if (this$offHandItem == null ? other$offHandItem == null : this$offHandItem.equals(other$offHandItem)) {
                     Object this$vanity = this.getVanity();
                     Object other$vanity = other.getVanity();
                     if (this$vanity == null ? other$vanity == null : this$vanity.equals(other$vanity)) {
                        Object this$lookController = this.getLookController();
                        Object other$lookController = other.getLookController();
                        if (this$lookController == null ? other$lookController == null : this$lookController.equals(other$lookController)) {
                           Object this$displayName = this.getDisplayName();
                           Object other$displayName = other.getDisplayName();
                           if (this$displayName == null ? other$displayName == null : this$displayName.equals(other$displayName)) {
                              Object this$priority = this.getPriority();
                              Object other$priority = other.getPriority();
                              if (this$priority == null ? other$priority == null : this$priority.equals(other$priority)) {
                                 Object this$textureOverride = this.getTextureOverride();
                                 Object other$textureOverride = other.getTextureOverride();
                                 if (this$textureOverride == null ? other$textureOverride == null : this$textureOverride.equals(other$textureOverride)) {
                                    Object this$shouldUpdateChunk = this.getShouldUpdateChunk();
                                    Object other$shouldUpdateChunk = other.getShouldUpdateChunk();
                                    if (this$shouldUpdateChunk == null
                                       ? other$shouldUpdateChunk == null
                                       : this$shouldUpdateChunk.equals(other$shouldUpdateChunk)) {
                                       Object this$overrideAnimationAll = this.getOverrideAnimationAll();
                                       Object other$overrideAnimationAll = other.getOverrideAnimationAll();
                                       if (this$overrideAnimationAll == null
                                          ? other$overrideAnimationAll == null
                                          : this$overrideAnimationAll.equals(other$overrideAnimationAll)) {
                                          Object this$overrideAnimations = this.getOverrideAnimations();
                                          Object other$overrideAnimations = other.getOverrideAnimations();
                                          return this$overrideAnimations == null
                                             ? other$overrideAnimations == null
                                             : this$overrideAnimations.equals(other$overrideAnimations);
                                       } else {
                                          return false;
                                       }
                                    } else {
                                       return false;
                                    }
                                 } else {
                                    return false;
                                 }
                              } else {
                                 return false;
                              }
                           } else {
                              return false;
                           }
                        } else {
                           return false;
                        }
                     } else {
                        return false;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof EntityStateData;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      long $moveSpeed = Double.doubleToLongBits(this.getMoveSpeed());
      result = result * 59 + (int)($moveSpeed >>> 32 ^ $moveSpeed);
      result = result * 59 + (this.isBlockAnimRotateWalking() ? 79 : 97);
      result = result * 59 + Float.floatToIntBits(this.getAlphaFadeable());
      result = result * 59 + (this.isBlockBody() ? 79 : 97);
      result = result * 59 + (this.isLockHead() ? 79 : 97);
      result = result * 59 + (this.isIgnoreNavmesh() ? 79 : 97);
      result = result * 59 + (this.isTraversalNavigation() ? 79 : 97);
      result = result * 59 + (this.isGoalsEnabled() ? 79 : 97);
      result = result * 59 + (this.isFreeSight() ? 79 : 97);
      result = result * 59 + (this.isTextureWasChanged() ? 79 : 97);
      result = result * 59 + (this.isDontRotateOnInteraction() ? 79 : 97);
      Object $animations = this.getAnimations();
      result = result * 59 + ($animations == null ? 43 : $animations.hashCode());
      Object $pos = this.getPos();
      result = result * 59 + ($pos == null ? 43 : $pos.hashCode());
      Object $mainHandItem = this.getMainHandItem();
      result = result * 59 + ($mainHandItem == null ? 43 : $mainHandItem.hashCode());
      Object $offHandItem = this.getOffHandItem();
      result = result * 59 + ($offHandItem == null ? 43 : $offHandItem.hashCode());
      Object $vanity = this.getVanity();
      result = result * 59 + ($vanity == null ? 43 : $vanity.hashCode());
      Object $lookController = this.getLookController();
      result = result * 59 + ($lookController == null ? 43 : $lookController.hashCode());
      Object $displayName = this.getDisplayName();
      result = result * 59 + ($displayName == null ? 43 : $displayName.hashCode());
      Object $priority = this.getPriority();
      result = result * 59 + ($priority == null ? 43 : $priority.hashCode());
      Object $textureOverride = this.getTextureOverride();
      result = result * 59 + ($textureOverride == null ? 43 : $textureOverride.hashCode());
      Object $shouldUpdateChunk = this.getShouldUpdateChunk();
      result = result * 59 + ($shouldUpdateChunk == null ? 43 : $shouldUpdateChunk.hashCode());
      Object $overrideAnimationAll = this.getOverrideAnimationAll();
      result = result * 59 + ($overrideAnimationAll == null ? 43 : $overrideAnimationAll.hashCode());
      Object $overrideAnimations = this.getOverrideAnimations();
      return result * 59 + ($overrideAnimations == null ? 43 : $overrideAnimations.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "EntityStateData(animations="
         + this.getAnimations()
         + ", moveSpeed="
         + this.getMoveSpeed()
         + ", pos="
         + this.getPos()
         + ", blockAnimRotateWalking="
         + this.isBlockAnimRotateWalking()
         + ", alphaFadeable="
         + this.getAlphaFadeable()
         + ", blockBody="
         + this.isBlockBody()
         + ", lockHead="
         + this.isLockHead()
         + ", ignoreNavmesh="
         + this.isIgnoreNavmesh()
         + ", traversalNavigation="
         + this.isTraversalNavigation()
         + ", mainHandItem="
         + this.getMainHandItem()
         + ", offHandItem="
         + this.getOffHandItem()
         + ", vanity="
         + this.getVanity()
         + ", lookController="
         + this.getLookController()
         + ", goalsEnabled="
         + this.isGoalsEnabled()
         + ", freeSight="
         + this.isFreeSight()
         + ", displayName="
         + this.getDisplayName()
         + ", priority="
         + this.getPriority()
         + ", textureOverride="
         + this.getTextureOverride()
         + ", textureWasChanged="
         + this.isTextureWasChanged()
         + ", dontRotateOnInteraction="
         + this.isDontRotateOnInteraction()
         + ", shouldUpdateChunk="
         + this.getShouldUpdateChunk()
         + ", overrideAnimationAll="
         + this.getOverrideAnimationAll()
         + ", overrideAnimations="
         + this.getOverrideAnimations()
         + ")";
   }
}
