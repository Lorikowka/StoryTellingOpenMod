package com.storyteam.storytelling.content.entities.configurations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

public interface ILivingAnimationEntity extends IAnimatedEntity {
   float getWalkingSpeed();

   @NotNull
   String getIdleLayer();

   void setAnimationOverride(MovementActionType var1, @Nullable ConfiguredAnimation var2);

   void setAnimationOverrideAll(@Nullable ConfiguredAnimation var1);

   @Nullable
   ConfiguredAnimation getAnimationOverride();
}
