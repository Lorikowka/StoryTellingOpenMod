package com.storyteam.storytelling.content.entities;

import com.google.common.collect.ImmutableSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import javax.annotation.Nonnull;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public class StoryEntityType<T extends Entity> extends EntityType<T> {
   @Nonnull
   protected final StoryEntityType.Properties<T> properties;

   public StoryEntityType(
      EntityFactory<T> pFactory,
      MobCategory pCategory,
      boolean pSerialize,
      boolean pSummon,
      boolean pFireImmune,
      boolean pCanSpawnFarFromPlayer,
      ImmutableSet<Block> pImmuneTo,
      EntityDimensions pDimensions,
      int pClientTrackingRange,
      int pUpdateInterval,
      FeatureFlagSet pRequiredFeatures,
      @Nonnull StoryEntityType.Properties<T> properties
   ) {
      super(
         pFactory,
         pCategory,
         pSerialize,
         pSummon,
         pFireImmune,
         pCanSpawnFarFromPlayer,
         pImmuneTo,
         pDimensions,
         pClientTrackingRange,
         pUpdateInterval,
         pRequiredFeatures
      );
      this.properties = properties;
   }

   public StoryEntityType(
      EntityFactory<T> pFactory,
      MobCategory pCategory,
      boolean pSerialize,
      boolean pSummon,
      boolean pFireImmune,
      boolean pCanSpawnFarFromPlayer,
      ImmutableSet<Block> pImmuneTo,
      EntityDimensions pDimensions,
      int pClientTrackingRange,
      int pUpdateInterval,
      FeatureFlagSet pRequiredFeatures,
      Predicate<EntityType<?>> velocityUpdateSupplier,
      ToIntFunction<EntityType<?>> trackingRangeSupplier,
      ToIntFunction<EntityType<?>> updateIntervalSupplier,
      BiFunction<SpawnEntity, Level, T> customClientFactory,
      @Nonnull StoryEntityType.Properties<T> properties
   ) {
      super(
         pFactory,
         pCategory,
         pSerialize,
         pSummon,
         pFireImmune,
         pCanSpawnFarFromPlayer,
         pImmuneTo,
         pDimensions,
         pClientTrackingRange,
         pUpdateInterval,
         pRequiredFeatures,
         velocityUpdateSupplier,
         trackingRangeSupplier,
         updateIntervalSupplier,
         customClientFactory
      );
      this.properties = properties;
   }

   @Nonnull
   @Generated
   public StoryEntityType.Properties<T> getProperties() {
      return this.properties;
   }

   public static class Properties<T extends Entity> {
      @Nonnull
      protected final Function<T, IGeometryContainer> model;
      protected final float scale;

      @Generated
      private static <T extends Entity> Function<T, IGeometryContainer> $default$model() {
         return e -> null;
      }

      @Generated
      private static <T extends Entity> float $default$scale() {
         return 1.0F;
      }

      @Generated
      Properties(@Nonnull Function<T, IGeometryContainer> model, float scale) {
         if (model == null) {
            throw new NullPointerException("model is marked non-null but is null");
         } else {
            this.model = model;
            this.scale = scale;
         }
      }

      @Generated
      public static <T extends Entity> StoryEntityType.Properties.Builder<T> builder() {
         return new StoryEntityType.Properties.Builder<>();
      }

      @Nonnull
      @Generated
      public Function<T, IGeometryContainer> getModel() {
         return this.model;
      }

      @Generated
      public float getScale() {
         return this.scale;
      }

      public static class Builder<T extends Entity> {
         @Generated
         private boolean model$set;
         @Generated
         private Function<T, IGeometryContainer> model$value;
         @Generated
         private boolean scale$set;
         @Generated
         private float scale$value;

         public StoryEntityType.Properties.Builder<T> fixedModel(IGeometryContainer container) {
            return this.model(e -> container);
         }

         @Generated
         Builder() {
         }

         @Generated
         public StoryEntityType.Properties.Builder<T> model(@Nonnull Function<T, IGeometryContainer> model) {
            if (model == null) {
               throw new NullPointerException("model is marked non-null but is null");
            } else {
               this.model$value = model;
               this.model$set = true;
               return this;
            }
         }

         @Generated
         public StoryEntityType.Properties.Builder<T> scale(float scale) {
            this.scale$value = scale;
            this.scale$set = true;
            return this;
         }

         @Generated
         public StoryEntityType.Properties<T> build() {
            Function<T, IGeometryContainer> model$value = this.model$value;
            if (!this.model$set) {
               model$value = StoryEntityType.Properties.$default$model();
            }

            float scale$value = this.scale$value;
            if (!this.scale$set) {
               scale$value = StoryEntityType.Properties.$default$scale();
            }

            return new StoryEntityType.Properties<>(model$value, scale$value);
         }

         @Generated
         @Override
         public String toString() {
            return "StoryEntityType.Properties.Builder(model$value=" + this.model$value + ", scale$value=" + this.scale$value + ")";
         }
      }
   }
}
