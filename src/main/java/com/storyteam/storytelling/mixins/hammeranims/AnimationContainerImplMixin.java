package com.storyteam.storytelling.mixins.hammeranims;

import com.storyteam.storytelling.Storytelling;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammeranims.api.animation.IAnimationContainer;
import org.zeith.hammeranims.api.animation.data.IReadAnimationHolder;
import org.zeith.hammeranims.core.impl.api.animation.AnimationContainerImpl;

@Mixin({AnimationContainerImpl.class})
public abstract class AnimationContainerImplMixin implements IAnimationContainer {
   @Shadow
   protected IReadAnimationHolder animations;

   @Inject(
      method = {"lambda$reload$1"},
      at = {@At(
         value = "INVOKE",
         target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"
      )}
   )
   private static void causeCrash(ResourceLocation key, ResourceLocation path, CallbackInfoReturnable<IReadAnimationHolder> cir) {
      Storytelling.MISSING_FILES.add(path.toString());
   }
}
