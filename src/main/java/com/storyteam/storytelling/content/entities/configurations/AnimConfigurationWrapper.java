package com.storyteam.storytelling.content.entities.configurations;

import com.storyteam.storytelling.client.Animations;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;

public class AnimConfigurationWrapper {
   private static final Random RANDOM = new Random();
   @Nullable
   IDefaultAnimConfiguration configuration;
   MovementActionType lastAction;
   private final HashMap<MovementActionType, ConfiguredAnimation> overrides = new HashMap<>();
   ConfiguredAnimation overrideAll;
   private boolean wasChanged;

   public AnimConfigurationWrapper(@Nullable IDefaultAnimConfiguration configuration) {
      this.configuration = configuration;
   }

   public void setup(ILivingAnimationEntity object, double speedVariation) {
      String layerName = object.getIdleLayer();
      MovementActionType actionType = MovementActionType.getBySpeed((double)object.getWalkingSpeed());
      AnimationLayer layer = object.getAnimationSystem().getLayer(layerName);
      if (layer == null) {
         throw new IllegalStateException(String.format("ILivingAnimationEntity has unregistered %s layer.", layerName));
      } else {
         if (actionType != this.lastAction
            || this.wasChanged
            || layer.getCurrentAnimation() == null
            || layer.getCurrentAnimation().config == null
            || layer.getCurrentAnimation().getLocation() == Animations.NULL.getLocation()) {
            this.wasChanged = false;
            this.lastAction = actionType;
            ConfiguredAnimation result;
            if (this.overrideAll != null) {
               result = this.overrideAll;
            } else if (this.overrides.containsKey(actionType)) {
               result = this.overrides.get(actionType);
            } else if (this.configuration != null) {
               List<ConfiguredAnimation> choice = this.configuration.getAnimations(actionType);
               if (choice.isEmpty()) {
                  return;
               }

               result = choice.get(RANDOM.nextInt(choice.size()))
                  .speed((float)(1.0 - speedVariation / 2.0 + speedVariation * RANDOM.nextDouble()))
                  .transitionTime(0.3F);
            } else {
               result = Animations.NULL.configure().transitionTime(0.3F);
            }

            if (result != null) {
               object.getAnimationSystem().startAnimationAt(layerName, result);
            }
         }
      }
   }

   public void setOverride(MovementActionType type, ConfiguredAnimation override) {
      ConfiguredAnimation prevOverride = override == null ? this.overrides.remove(type) : this.overrides.put(type, override);
      this.wasChanged = prevOverride == null && override != null
         || prevOverride != null && override == null
         || prevOverride != null && !prevOverride.same(override);
   }

   public ConfiguredAnimation getOverride(MovementActionType type, ConfiguredAnimation override) {
      return this.overrides.get(type);
   }

   public void setOverrideAll(ConfiguredAnimation overrideAll) {
      this.wasChanged = this.overrideAll != null && overrideAll != null
         ? !this.overrideAll.animation.getLocation().equals(overrideAll.animation.getLocation())
         : overrideAll == null != (this.overrideAll == null);
      this.overrideAll = overrideAll;
   }

   @Nullable
   @Generated
   public IDefaultAnimConfiguration getConfiguration() {
      return this.configuration;
   }

   @Generated
   public HashMap<MovementActionType, ConfiguredAnimation> getOverrides() {
      return this.overrides;
   }

   @Generated
   public ConfiguredAnimation getOverrideAll() {
      return this.overrideAll;
   }
}
