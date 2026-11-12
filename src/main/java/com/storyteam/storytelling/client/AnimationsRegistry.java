package com.storyteam.storytelling.client;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.animations.IAnimationGroup;
import com.storyteam.storytelling.client.core.AnimationStorage;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import com.storyteam.storytelling.util.Chirality;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammeranims.api.animation.IAnimationContainer;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class AnimationsRegistry implements AnimationStorage {
   public static final LinkedHashMap<ResourceLocation, IAnimationContainer> TO_REGISTER = new LinkedHashMap<>();

   @SubscribeEvent
   public static void registerAnimations(RegisterEvent event) {
      IForgeRegistry<IAnimationContainer> reg = HammerAnimationsApi.animations();
      if (event.getRegistryKey().equals(reg.getRegistryKey())) {
         for (Entry<ResourceLocation, IAnimationContainer> e : TO_REGISTER.entrySet()) {
            reg.register(e.getKey(), e.getValue());
         }
      }
   }

   @Override
   public SidedAnimationSet registerSidedSeparate(String rightPath, String leftPath) {
      return new SidedAnimationSet((Map<String, IAnimationSource>)Util.make(new HashMap(), m -> {
         m.put("right", this.register(rightPath).holder());
         m.put("left", this.register(leftPath));
      })).forSide(Chirality.RIGHT, "right").forSide(Chirality.LEFT, "left");
   }

   @Override
   public SidedAnimationSet registerSided(String rightVariant, String leftVariant, String path) {
      Map<String, IAnimationSource> animations = new HashMap<>();
      animations.put(leftVariant, getOrLoadAnimations(path).holder(leftVariant));
      animations.put(rightVariant, getOrLoadAnimations(path).holder(rightVariant));
      return new SidedAnimationSet(animations).forSide(Chirality.RIGHT, rightVariant).forSide(Chirality.LEFT, leftVariant);
   }

   @Override
   public IAnimationContainer register(String path) {
      return getOrLoadAnimations(path);
   }

   static IAnimationContainer getOrLoadAnimations(String path) {
      if (path.startsWith("animations/")) {
         path = path.substring(11);
      }

      if (path.endsWith(".json")) {
         throw new IllegalArgumentException("Animation key \"" + path + "\" must not contain .json suffix. Please remove it.");
      } else {
         return TO_REGISTER.computeIfAbsent(St.id(path), ___ -> IAnimationContainer.createNoSuffix());
      }
   }

   @SubscribeEvent
   public static void onSetup(FMLCommonSetupEvent event) {
   }

   public static class EyeGaze implements IAnimationGroup {
      public final IAnimationSource leftEyeLooksToLeft;
      public final IAnimationSource leftEyeLooksToRight;
      public final IAnimationSource rightEyeLooksToLeft;
      public final IAnimationSource rightEyeLooksToRight;
      public final IAnimationSource eyesLookUp;
      public final IAnimationSource eyesLookDown;
      private final Map<String, IAnimationSource> animations = new HashMap<>();

      public EyeGaze(SidedAnimationSet lookLeft, SidedAnimationSet lookRight, IAnimationSource lookUp, IAnimationSource lookDown) {
         this.animations.put("left_eye_to_left", this.leftEyeLooksToLeft = lookLeft.left());
         this.animations.put("left_eye_to_right", this.leftEyeLooksToRight = lookRight.left());
         this.animations.put("right_eye_to_left", this.rightEyeLooksToLeft = lookLeft.right());
         this.animations.put("right_eye_to_right", this.rightEyeLooksToRight = lookRight.right());
         this.animations.put("eyes_to_up", this.eyesLookUp = lookUp);
         this.animations.put("eyes_to_down", this.eyesLookDown = lookDown);
         this.animations.entrySet().removeIf(e -> e.getValue() == null);
      }

      @Override
      public Collection<String> getKeys() {
         return this.animations.keySet();
      }

      @Override
      public Optional<IAnimationSource> getAnimation(String key) {
         return Optional.ofNullable(this.animations.get(key));
      }
   }
}
