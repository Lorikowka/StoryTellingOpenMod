package com.storyteam.storytelling.mixins.minecraft;

import com.google.common.collect.ImmutableSet;
import com.storyteam.storytelling.api.entity.IStoryEntityBuilder;
import com.storyteam.storytelling.content.entities.StoryEntityType;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.java.Cast;

@Mixin({Builder.class})
@Implements({@Interface(
      iface = IStoryEntityBuilder.class,
      prefix = "steb$"
   )})
public class EntityTypeBuilderMixin<T extends Entity> {
   @Shadow
   @Final
   private EntityFactory<T> factory;
   @Shadow
   @Final
   private MobCategory category;
   @Shadow
   private boolean serialize;
   @Shadow
   private boolean summon;
   @Shadow
   private boolean fireImmune;
   @Shadow
   private boolean canSpawnFarFromPlayer;
   @Shadow
   private ImmutableSet<Block> immuneTo;
   @Shadow
   private EntityDimensions dimensions;
   @Shadow
   private int clientTrackingRange;
   @Shadow
   private int updateInterval;
   @Shadow
   private Predicate<EntityType<?>> velocityUpdateSupplier;
   @Shadow
   private ToIntFunction<EntityType<?>> trackingRangeSupplier;
   @Shadow
   private ToIntFunction<EntityType<?>> updateIntervalSupplier;
   @Shadow
   private BiFunction<SpawnEntity, Level, T> customClientFactory;
   @Shadow
   private FeatureFlagSet requiredFeatures;
   @Unique
   private StoryEntityType.Properties.Builder<T> st$properties;

   public <B extends Builder<T> & IStoryEntityBuilder<T>> B steb$applyProps(Consumer<StoryEntityType.Properties.Builder<T>> properties) {
      if (this.st$properties == null) {
         this.st$properties = StoryEntityType.Properties.builder();
      }

      properties.accept(this.st$properties);
      return (B)Cast.cast(this);
   }

   @Inject(
      method = {"build"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void build(String pKey, CallbackInfoReturnable<EntityType<T>> cir) {
      if (this.st$properties != null) {
         if (this.serialize) {
            Util.fetchChoiceType(References.ENTITY_TREE, pKey);
         }

         cir.setReturnValue(
            new StoryEntityType<>(
               this.factory,
               this.category,
               this.serialize,
               this.summon,
               this.fireImmune,
               this.canSpawnFarFromPlayer,
               this.immuneTo,
               this.dimensions,
               this.clientTrackingRange,
               this.updateInterval,
               this.requiredFeatures,
               this.velocityUpdateSupplier,
               this.trackingRangeSupplier,
               this.updateIntervalSupplier,
               this.customClientFactory,
               this.st$properties.build()
            )
         );
         cir.cancel();
      }
   }
}
