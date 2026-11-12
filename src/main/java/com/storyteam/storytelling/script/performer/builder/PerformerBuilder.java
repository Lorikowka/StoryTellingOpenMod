package com.storyteam.storytelling.script.performer.builder;

import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.action.ChoosePerformerAction;
import com.storyteam.storytelling.script.performer.action.CustomTickAction;
import com.storyteam.storytelling.script.performer.action.DelayAction;
import com.storyteam.storytelling.script.performer.action.FinishAction;
import com.storyteam.storytelling.script.performer.action.GoToAction;
import com.storyteam.storytelling.script.performer.action.HandleStateAction;
import com.storyteam.storytelling.script.performer.action.LabelAction;
import com.storyteam.storytelling.script.performer.action.OnFastForwardAction;
import com.storyteam.storytelling.script.performer.action.ParallelPerformersAction;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.action.RandomDelayAction;
import com.storyteam.storytelling.script.performer.action.SavePointAction;
import com.storyteam.storytelling.script.performer.action.SingleTimeAction;
import com.storyteam.storytelling.script.performer.action.SwitchPerformersAction;
import com.storyteam.storytelling.script.performer.action.WaitUntilAction;
import com.storyteam.storytelling.script.performer.action.modificators.ChanceModifier;
import com.storyteam.storytelling.script.performer.action.modificators.FastForwardModifier;
import com.storyteam.storytelling.script.performer.action.modificators.IfOnlyModifier;
import com.storyteam.storytelling.script.performer.action.modificators.ParallelModifier;
import com.storyteam.storytelling.script.performer.action.modificators.PerformerModifier;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.minecraft.util.Mth;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammerlib.util.java.Cast;

public interface PerformerBuilder<P extends Performer<P>, T extends PerformerBuilder<P, T>> {
   T append(Function<P, PerformerAction<P>> var1);

   T appendModifier(Function<P, PerformerModifier<P>> var1);

   default T thenCustomTickAction(Runnable onTick, Runnable onStop, Runnable onFastForward, Supplier<Boolean> completionCallback) {
      return this.append(performer -> new CustomTickAction<>(performer, onTick, onStop, onFastForward, () -> {
         }, completionCallback));
   }

   default T thenCustomTickAction(Runnable onTick, Runnable onStop, Runnable onFastForward, Runnable onRewind, Supplier<Boolean> completionCallback) {
      return this.append(performer -> new CustomTickAction<>(performer, onTick, onStop, onFastForward, onRewind, completionCallback));
   }

   default T thenCustomTickAction(Runnable onTick, Supplier<Boolean> completionCallback) {
      return this.append(performer -> new CustomTickAction<>(performer, onTick, () -> {
         }, () -> {
         }, () -> {
         }, completionCallback));
   }

   default T appendBuilder(Consumer<T> builder) {
      builder.accept((T)this);
      return (T)this;
   }

   default T thenRun(Runnable runnable) {
      return this.append(performer -> new SingleTimeAction<>(performer, runnable));
   }

   default T thenPerform(Runnable action) {
      return this.append(performer -> new HandleStateAction<>(performer, () -> {
         }, action));
   }

   default T thenWaitUntil(Supplier<Boolean> condition) {
      return this.append(performer -> new WaitUntilAction<>(performer, condition, 0));
   }

   default T thenWaitUntilWithInfo(Supplier<Boolean> condition, Supplier<String> stHelpInfo) {
      return this.append(performer -> new WaitUntilAction<>(performer, condition, 0, stHelpInfo));
   }

   default T thenWaitUntil(Supplier<Boolean> condition, int checkInterval) {
      return this.append(performer -> new WaitUntilAction<>(performer, condition, checkInterval));
   }

   default T thenWaitUntilWithInfo(Supplier<Boolean> condition, int checkInterval, Supplier<String> stHelpInfo) {
      return this.append(performer -> new WaitUntilAction<>(performer, condition, checkInterval, stHelpInfo));
   }

   default T thenInfiniteSleep() {
      return this.append(performer -> new WaitUntilAction<>(performer, () -> false, 0));
   }

   default T thenHandleState(Runnable before, Runnable after) {
      return this.append(performer -> new HandleStateAction<>(performer, before, after));
   }

   default T thenOnFastForward(Runnable action) {
      return this.append(performer -> new OnFastForwardAction<>(performer, action));
   }

   default T thenHandleState(Consumer<Boolean> isPassed) {
      return this.append(performer -> new HandleStateAction<>(performer, isPassed));
   }

   default T thenHandleState(HandleStateAction.StateHandler handler) {
      return this.append(performer -> new HandleStateAction<>(performer, handler));
   }

   default T thenDelay(int ticks) {
      return this.append(performer -> new DelayAction<>(performer, () -> ticks));
   }

   default T thenDelay(IAnimationSource animation) {
      return this.thenDelay(animation.configure());
   }

   default T thenDelay(ConfiguredAnimation animation) {
      return this.append(
         performer -> new DelayAction<>(
               performer,
               () -> (int)(
                     (float)animation.animation.getData().getLength().minus(Duration.ofMillis((long)Mth.floor(animation.startTime * 1000.0F))).toMillis()
                        / 50.0F
                        / animation.speed
                  )
            )
      );
   }

   default T thenDelay(int minValue, int maxValue, Random random) {
      return this.append(performer -> new DelayAction<>(performer, () -> minValue + 1 + random.nextInt(maxValue - minValue)));
   }

   default T thenDelayRandom(int minValue, int maxValue, Random random) {
      return this.append(performer -> new RandomDelayAction<>(performer, () -> minValue + 1 + random.nextInt(maxValue - minValue)));
   }

   default T thenDelay(IntSupplier ticks) {
      return this.append(performer -> new DelayAction<>(performer, ticks));
   }

   default T thenDelayInSeconds(double delay) {
      return this.append(performer -> new DelayAction<>(performer, () -> (int)Math.round(delay * 20.0)));
   }

   default T thenAllPerformers(Performer<P>... performers) {
      return this.append(performer -> new ParallelPerformersAction<>(performer, performers, ParallelPerformersAction.CompletionCondition.ALL_COMPLETED));
   }

   default T thenAllPerformers(Supplier<T> factory, Consumer<T>... performers) {
      return (T)Cast.cast(this.append(performer -> new ParallelPerformersAction<>(performer, Arrays.stream(performers).map(f -> {
            T builder = factory.get();
            f.accept(builder);
            return builder.build();
         }).toArray(Performer[]::new), ParallelPerformersAction.CompletionCondition.ALL_COMPLETED)));
   }

   default T thenAnyPerformer(Performer<P>... performers) {
      return this.append(performer -> new ParallelPerformersAction<>(performer, performers, ParallelPerformersAction.CompletionCondition.ANY_COMPLETED));
   }

   default T thenChoosePerformer(Performer<P>... performers) {
      return this.append(performer -> new ChoosePerformerAction<>(performer, performers));
   }

   default T thenIf(BooleanSupplier condition, Performer<?> onTrue) {
      return this.thenSwitch(() -> condition.getAsBoolean() ? 0 : -1, onTrue);
   }

   default T thenIfDynamic(BooleanSupplier condition, Performer<?> onTrue) {
      return this.thenSwitchDynamic(() -> condition.getAsBoolean() ? 0 : -1, onTrue);
   }

   default T thenIf(BooleanSupplier condition, Performer<?> onTrue, Performer<?> onFalse) {
      return this.thenSwitch(() -> condition.getAsBoolean() ? 0 : 1, onTrue, onFalse);
   }

   default T thenIfDynamic(BooleanSupplier condition, Performer<?> onTrue, Performer<?> onFalse) {
      return this.thenSwitchDynamic(() -> condition.getAsBoolean() ? 0 : 1, onTrue, onFalse);
   }

   default T thenSwitch(IntSupplier selector, Performer<?>... branches) {
      return this.append(performer -> new SwitchPerformersAction<>(performer, selector, false, branches));
   }

   default T thenSwitchDynamic(IntSupplier selector, Performer<?>... branches) {
      return this.append(performer -> new SwitchPerformersAction<>(performer, selector, true, branches));
   }

   default T label(String name) {
      return this.append(performer -> new LabelAction<>(performer, name, true));
   }

   default T label(String name, boolean sendDiscordLogs) {
      return this.append(performer -> new LabelAction<>(performer, name, sendDiscordLogs));
   }

   default T saveState(String name) {
      return this.append(performer -> new SavePointAction<>(performer, name));
   }

   default T gotoSaveState(String state) {
      return this.append(performer -> new GoToAction<>(performer, () -> state, true, false));
   }

   default T gotoLabel(Supplier<String> label) {
      return this.append(performer -> new GoToAction<>(performer, label, false, false));
   }

   default T gotoLabel(Supplier<String> label, boolean withoutFastRun) {
      return this.append(performer -> new GoToAction<>(performer, label, false, withoutFastRun));
   }

   default T gotoLabel(String label) {
      return this.append(performer -> new GoToAction<>(performer, () -> label, false, false));
   }

   default T gotoLabel(String label, boolean withoutFastRun) {
      return this.append(performer -> new GoToAction<>(performer, () -> label, false, withoutFastRun));
   }

   default T finish(boolean withoutFastRun) {
      return this.append(performer -> new FinishAction<>(performer, withoutFastRun));
   }

   default T _withChance(float chance) {
      return this.appendModifier(performer -> new ChanceModifier<>(chance));
   }

   default T _onlyIf(BooleanSupplier supplier) {
      return this.appendModifier(performer -> new IfOnlyModifier<>(supplier, false));
   }

   default T _onlyIfDynamic(BooleanSupplier supplier) {
      return this.appendModifier(performer -> new IfOnlyModifier<>(supplier, true));
   }

   default T _fastForwardOverwrite(boolean override, P perfomer) {
      return this.appendModifier(performer -> new FastForwardModifier<>(override, perfomer));
   }

   default T _fastForwardOverwrite(boolean override, Consumer<PerformerAction<P>> action) {
      return this.appendModifier(performer -> new FastForwardModifier<>(override, action));
   }

   default T _parallelAny() {
      return this.appendModifier(performer -> new ParallelModifier<>(ParallelPerformersAction.CompletionCondition.ANY_COMPLETED));
   }

   default T _parallelAll() {
      return this.appendModifier(performer -> new ParallelModifier<>(ParallelPerformersAction.CompletionCondition.ALL_COMPLETED));
   }

   P build();
}
