package com.storyteam.storytelling.api.animations;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.core.init.DefaultsHA;
import org.zeith.hammerlib.abstractions.props.Key;

public class AnimationWithCondition<CTX> {
   private final Predicate<CTX> canStart;
   private final Predicate<CTX> canContinue;
   private final IAnimationSource animation;
   private final Consumer<CTX> onStart;
   private final Consumer<CTX> onEnd;
   public static final Key<Boolean> STARTED_JUMPING = Key.of(St.id("initiated_jump"), Boolean.class);
   public static final Key<Boolean> DEAD = Key.of(St.id("dead"), Boolean.class);

   public void onStart(CTX player) {
      if (this.onStart != null) {
         this.onStart.accept(player);
      }
   }

   public void onEnd(CTX player) {
      if (this.onEnd != null) {
         this.onEnd.accept(player);
      }
   }

   public boolean canStart(CTX player) {
      return this.canStart.test(player);
   }

   public boolean canContinue(CTX player) {
      return this.canContinue.test(player);
   }

   public IAnimationSource getAnimation(CTX player) {
      return this.animation;
   }

   public AnimationWithCondition<CTX> or(AnimationWithCondition<CTX> b) {
      return combine(this, b);
   }

   public static <X> AnimationWithCondition<X> always(IAnimationSource anim) {
      return (AnimationWithCondition<X>)builder().activeWhile(p -> true).animation(anim).build();
   }

   public static AnimationWithCondition<Player> climbingUp(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().activeWhile(p -> p.onClimbable() && p.getDeltaMovement().y > 0.1).animation(anim).build();
   }

   public static AnimationWithCondition<Player> climbingDown(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().activeWhile(p -> p.onClimbable() && p.getDeltaMovement().y < -0.1).animation(anim).build();
   }

   public static AnimationWithCondition<Player> climbing(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().activeWhile(p -> p.onClimbable() && Math.abs(p.getDeltaMovement().y) > 0.1).animation(anim).build();
   }

   public static AnimationWithCondition<Player> walking(IAnimationSource anim, double minDist, double maxDist) {
      return AnimationWithCondition.<Player>builder().activeWhile(p -> {
         float move = new Vector3f(p.xxa, p.yya, p.zza).length() / 8.0F;
         double dMove = (double)Math.max(p.moveDist - p.walkDistO, move);
         return dMove >= minDist && dMove < maxDist && !p.isSprinting();
      }).animation(anim).build();
   }

   public static AnimationWithCondition<Player> running(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().activeWhile(Entity::isSprinting).animation(anim).build();
   }

   public static AnimationWithCondition<Player> sneaking(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().activeWhile(Entity::isShiftKeyDown).animation(anim).build();
   }

   public static AnimationWithCondition<Player> jumpStart(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder()
         .canStart(p -> ((IAnimatedPlayer)p).isPlayerJumping() && !p.onGround())
         .canContinue(p -> !p.onGround())
         .animation(anim)
         .onStart(p -> ((IAnimatedPlayer)p).getUnsavedProperties().put(STARTED_JUMPING, true))
         .build();
   }

   public static AnimationWithCondition<Player> jumpEnd(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder()
         .canStart(p -> ((IAnimatedPlayer)p).getUnsavedProperties().opt(STARTED_JUMPING).orElse(false) && p.onGround())
         .canContinue(Entity::onGround)
         .animation(anim)
         .onStart(p -> ((IAnimatedPlayer)p).getUnsavedProperties().put(STARTED_JUMPING, false))
         .build();
   }

   public static AnimationWithCondition<Player> swing(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().canStart(p -> p.swinging).canContinue(p -> p.swingTime != -1).animation(anim).build();
   }

   public static AnimationWithCondition<Player> hurt(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().canStart(p -> p.hurtTime > 0).canContinue(p -> p.hurtTime > 0).animation(anim).build();
   }

   public static AnimationWithCondition<Player> death(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder()
         .canStart(p -> ((IAnimatedPlayer)p).getUnsavedProperties().opt(DEAD).orElse(false))
         .canContinue(p -> ((IAnimatedPlayer)p).getUnsavedProperties().opt(DEAD).orElse(false))
         .animation(anim)
         .build();
   }

   public static AnimationWithCondition<Player> idle(IAnimationSource anim) {
      return AnimationWithCondition.<Player>builder().activeWhile(p -> !((IAnimatedPlayer)p).getUnsavedProperties().opt(DEAD).orElse(false)).animation(anim).build();
   }

   private static <CTX> AnimationWithCondition<CTX> combine(final AnimationWithCondition<CTX> a, final AnimationWithCondition<CTX> b) {
      return new AnimationWithCondition<CTX>(
         a.canStart.or(b.canStart), a.canContinue.or(b.canContinue), DefaultsHA.NULL_ANIMATION, a.onStart.andThen(b.onStart), a.onEnd.andThen(b.onEnd)
      ) {
         @Override
         public IAnimationSource getAnimation(CTX player) {
            if (a.canContinue.test(player)) {
               return a.getAnimation(player);
            } else {
               return b.canContinue.test(player) ? b.getAnimation(player) : super.getAnimation(player);
            }
         }
      };
   }

   @Generated
   AnimationWithCondition(Predicate<CTX> canStart, Predicate<CTX> canContinue, IAnimationSource animation, Consumer<CTX> onStart, Consumer<CTX> onEnd) {
      this.canStart = canStart;
      this.canContinue = canContinue;
      this.animation = animation;
      this.onStart = onStart;
      this.onEnd = onEnd;
   }

   @Generated
   public static <CTX> AnimationWithCondition.AnimationWithConditionBuilder<CTX> builder() {
      return new AnimationWithCondition.AnimationWithConditionBuilder<>();
   }

   public static class AnimationWithConditionBuilder<CTX> {
      @Generated
      private Predicate<CTX> canStart;
      @Generated
      private Predicate<CTX> canContinue;
      @Generated
      private IAnimationSource animation;
      @Generated
      private Consumer<CTX> onStart;
      @Generated
      private Consumer<CTX> onEnd;

      public AnimationWithConditionBuilder() {
         this.canStart(p -> false);
      }

      public AnimationWithCondition.AnimationWithConditionBuilder<CTX> activeWhile(Predicate<CTX> isActive) {
         return this.canStart(isActive).canContinue(isActive);
      }

      public AnimationWithCondition.AnimationWithConditionBuilder<CTX> canStart(Predicate<CTX> canStart) {
         if (this.canContinue == null) {
            this.canContinue = canStart;
         }

         this.canStart = canStart;
         return this;
      }

      @Generated
      public AnimationWithCondition.AnimationWithConditionBuilder<CTX> canContinue(Predicate<CTX> canContinue) {
         this.canContinue = canContinue;
         return this;
      }

      @Generated
      public AnimationWithCondition.AnimationWithConditionBuilder<CTX> animation(IAnimationSource animation) {
         this.animation = animation;
         return this;
      }

      @Generated
      public AnimationWithCondition.AnimationWithConditionBuilder<CTX> onStart(Consumer<CTX> onStart) {
         this.onStart = onStart;
         return this;
      }

      @Generated
      public AnimationWithCondition.AnimationWithConditionBuilder<CTX> onEnd(Consumer<CTX> onEnd) {
         this.onEnd = onEnd;
         return this;
      }

      @Generated
      public AnimationWithCondition<CTX> build() {
         return new AnimationWithCondition<>(this.canStart, this.canContinue, this.animation, this.onStart, this.onEnd);
      }

      @Generated
      @Override
      public String toString() {
         return "AnimationWithCondition.AnimationWithConditionBuilder(canStart="
            + this.canStart
            + ", canContinue="
            + this.canContinue
            + ", animation="
            + this.animation
            + ", onStart="
            + this.onStart
            + ", onEnd="
            + this.onEnd
            + ")";
      }
   }
}
