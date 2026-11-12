package com.storyteam.storytelling.system.simulator.me_tab.fragment.data;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.time.DurationData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class FragmentData implements ICustomRegistrar {
   private String id;
   private final Component word;
   private final Component thought;
   private IAnimationSource animation;
   private List<DescribedCondition> describedConditions;
   private DurationData durationData;
   private DescribedActionRollback acceptAction;
   private DescribedActionRollback rejectAction;

   private FragmentData(Component word, Component thought) {
      this.word = word;
      this.thought = thought;
   }

   protected FragmentData(
      Component word,
      Component thought,
      List<DescribedCondition> describedConditions,
      DurationData durationData,
      DescribedActionRollback acceptAction,
      DescribedActionRollback rejectAction,
      IAnimationSource animation
   ) {
      this.word = word;
      this.thought = thought;
      this.describedConditions = describedConditions;
      this.durationData = durationData;
      this.acceptAction = acceptAction;
      this.rejectAction = rejectAction;
      this.animation = animation;
   }

   public boolean checkDescribedConditions(ServerPlayer player) {
      for (DescribedCondition condition : this.describedConditions) {
         if (!condition.test(player)) {
            return false;
         }
      }

      return true;
   }

   public void performRegister(RegisterEvent event, ResourceLocation resourceLocation) {
      event.register(Registries.ITEM, registerHelper -> {
         this.id = resourceLocation.getPath().replace("system/simulator/me_tab/", "");
         FragmentStorage.register(this);
      });
   }

   @Generated
   public static FragmentData.FragmentDataBuilder builder() {
      return new FragmentData.FragmentDataBuilder();
   }

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public Component getWord() {
      return this.word;
   }

   @Generated
   public Component getThought() {
      return this.thought;
   }

   @Generated
   public IAnimationSource getAnimation() {
      return this.animation;
   }

   @Generated
   public List<DescribedCondition> getDescribedConditions() {
      return this.describedConditions;
   }

   @Generated
   public DurationData getDurationData() {
      return this.durationData;
   }

   @Generated
   public DescribedActionRollback getAcceptAction() {
      return this.acceptAction;
   }

   @Generated
   public DescribedActionRollback getRejectAction() {
      return this.rejectAction;
   }

   public static class FragmentDataBuilder {
      @Generated
      private Component word;
      @Generated
      private Component thought;
      private final List<DescribedCondition> describedConditions = new ArrayList<>();
      private DurationData durationData;
      private DescribedActionRollback acceptAction;
      private DescribedActionRollback rejectAction;
      private IAnimationSource animation = Animations.NULL;

      public FragmentData.FragmentDataBuilder durationData(int days, int hours, int minutes) {
         this.durationData = new DurationData(days, hours, minutes);
         return this;
      }

      public FragmentData.FragmentDataBuilder setAnimation(IAnimationSource animation) {
         this.animation = animation;
         return this;
      }

      public FragmentData.FragmentDataBuilder addRequirement(Predicate<ServerPlayer> condition, Component description) {
         this.describedConditions.add(new DescribedCondition(condition, description));
         return this;
      }

      public FragmentData.FragmentDataBuilder addRequirement(DescribedCondition requirement) {
         this.describedConditions.add(requirement);
         return this;
      }

      public FragmentData.FragmentDataBuilder acceptAction(Consumer<ServerPlayer> action, Component description) {
         this.acceptAction = new DescribedActionRollback(action, null, description);
         return this;
      }

      public FragmentData.FragmentDataBuilder rejectAction(Consumer<ServerPlayer> action, Component description) {
         this.rejectAction = new DescribedActionRollback(action, null, description);
         return this;
      }

      public FragmentData.FragmentDataBuilder acceptAction(Consumer<ServerPlayer> action, Consumer<ServerPlayer> rollback, Component description) {
         this.acceptAction = new DescribedActionRollback(action, rollback, description);
         return this;
      }

      public FragmentData.FragmentDataBuilder rejectAction(Consumer<ServerPlayer> action, Consumer<ServerPlayer> rollback, Component description) {
         this.rejectAction = new DescribedActionRollback(action, rollback, description);
         return this;
      }

      public FragmentData build() {
         return new FragmentData(this.word, this.thought, this.describedConditions, this.durationData, this.acceptAction, this.rejectAction, this.animation);
      }

      @Generated
      FragmentDataBuilder() {
      }

      @Generated
      public FragmentData.FragmentDataBuilder word(Component word) {
         this.word = word;
         return this;
      }

      @Generated
      public FragmentData.FragmentDataBuilder thought(Component thought) {
         this.thought = thought;
         return this;
      }

      @Generated
      @Override
      public String toString() {
         return "FragmentData.FragmentDataBuilder(word=" + this.word + ", thought=" + this.thought + ")";
      }
   }
}
