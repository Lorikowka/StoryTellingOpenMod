package com.storyteam.storytelling.content.entities.base;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.storyteam.storytelling.api.entity.IStoryEntityBuilder;
import com.storyteam.storytelling.client.render.entity.def.RenderContext;
import com.storyteam.storytelling.client.render.entity.def.RenderDefinition;
import com.storyteam.storytelling.client.render.entity.def.impl.CommonRenderDefinition;
import com.storyteam.storytelling.client.render.entity.def.impl.EmptyRenderDefinition;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.util.java.Cast;

public class EntityRegistrar<T extends Entity> {
   protected final TypeToken<T> token = new TypeToken<T>(this.getClass()) {
   };
   protected final EntityRegistrar.EntityRegisrarHolder holder;
   protected final net.minecraft.world.entity.EntityType.Builder<T> builder;
   protected final Supplier<EntityType<T>> type;
   protected ResourceLocation texture;
   protected IGeometryContainer model;
   protected float scale = 1.0F;
   protected Function<RenderContext<T>, RenderDefinition<T>> renderer = EmptyRenderDefinition::new;
   protected final List<Consumer<RenderContext<T>>> operators = new ArrayList<>();
   protected Supplier<AttributeSupplier> attributes;

   public EntityRegistrar(EntityRegistrar.EntityRegisrarHolder holder, EntityFactory<T> factory, MobCategory category) {
      this.holder = holder;
      this.builder = net.minecraft.world.entity.EntityType.Builder.of(factory, category).sized(0.5F, 1.8F);
      this.type = Suppliers.memoize(() -> this.builder.build("storytelling"));
   }

   public EntityRegistrar<T> texture(ResourceLocation texture) {
      this.texture = texture;
      return this;
   }

   public EntityRegistrar<T> sized(float pWidth, float pHeight) {
      this.builder.sized(pWidth, pHeight);
      return this;
   }

   public EntityRegistrar<T> attributes(UnaryOperator<Builder> attrs) {
      return this.attributes((Supplier<AttributeSupplier>)(() -> attrs.apply(new Builder()).build()));
   }

   public EntityRegistrar<T> attributes(Supplier<AttributeSupplier> attrs) {
      this.attributes = attrs;
      return this;
   }

   public EntityRegistrar<T> model(IGeometryContainer model) {
      this.model = model;
      ((IStoryEntityBuilder<T>)this.builder).applyProps(b -> b.fixedModel(model));
      return this;
   }

   public EntityRegistrar<T> scale(float scale) {
      this.scale = scale;
      ((IStoryEntityBuilder<T>)this.builder).applyProps(b -> b.scale(scale));
      return this;
   }

   public EntityRegistrar<T> noSummon() {
      this.builder.noSummon();
      return this;
   }

   public EntityRegistrar<T> noSave() {
      this.builder.noSave();
      return this;
   }

   public EntityRegistrar<T> fireImmune() {
      this.builder.fireImmune();
      return this;
   }

   public EntityRegistrar<T> immuneTo(Block... pBlocks) {
      this.builder.immuneTo(pBlocks);
      return this;
   }

   public EntityRegistrar<T> canSpawnFarFromPlayer() {
      this.builder.canSpawnFarFromPlayer();
      return this;
   }

   public EntityRegistrar<T> clientTrackingRange(int pClientTrackingRange) {
      this.builder.clientTrackingRange(pClientTrackingRange);
      return this;
   }

   public EntityRegistrar<T> updateInterval(int pUpdateInterval) {
      this.builder.updateInterval(pUpdateInterval);
      return this;
   }

   public EntityRegistrar<T> setUpdateInterval(int interval) {
      this.builder.setUpdateInterval(interval);
      return this;
   }

   public EntityRegistrar<T> setTrackingRange(int range) {
      this.builder.setTrackingRange(range);
      return this;
   }

   public EntityRegistrar<T> setShouldReceiveVelocityUpdates(boolean value) {
      this.builder.setShouldReceiveVelocityUpdates(value);
      return this;
   }

   public EntityRegistrar<T> setCustomClientFactory(BiFunction<SpawnEntity, Level, T> customClientFactory) {
      this.builder.setCustomClientFactory(customClientFactory);
      return this;
   }

   public EntityRegistrar<T> renderer(Function<RenderContext<T>, RenderDefinition<T>> renderer) {
      this.renderer = renderer;
      return this;
   }

   public <P> EntityRegistrar<T> renderProp(Key<P> key, P value) {
      this.operators.add(c -> c.setProp(key, value));
      return this;
   }

   public EntityType<T> build() {
      this.holder.register(this);
      return this.type.get();
   }

   @OnlyIn(Dist.CLIENT)
   protected void registerRenderer(RegisterRenderers e) {
      RenderContext<T> chain = new RenderContext<>(this)
         .setProp(RenderContext.ENTITY_MODEL, this.model)
         .setProp(RenderContext.DEFAULT_TEXTURE, this.texture)
         .setProp(RenderContext.ENTITY_SCALE, this.scale);
      Class<? super T> rawType = this.token.getRawType();
      if (HumanoidEntity.class.isAssignableFrom(rawType)) {
         chain.setProp(CommonRenderDefinition.RENDERER, CommonRenderDefinition.Renderer.HumanoidRenderer);
      }

      this.operators.forEach(c -> c.accept(chain));
      e.registerEntityRenderer(this.type.get(), this.renderer.apply(chain)::create);
   }

   protected void attributes(EntityAttributeCreationEvent e) {
      AttributeSupplier attributes = this.attributes != null ? this.attributes.get() : null;
      if (this.attributes == null) {
         attributes = Mob.createMobAttributes().build();
      }

      if (LivingEntity.class.isAssignableFrom(this.token.getRawType())) {
         e.put((EntityType)Cast.cast(this.type.get()), attributes);
      }
   }

   public static <T extends Entity> EntityRegistrar<T> createGeneric(
      EntityRegistrar.EntityRegisrarHolder holder, EntityFactory<T> pFactory, MobCategory pCategory
   ) {
      return new EntityRegistrar<>(holder, pFactory, pCategory);
   }

   public static <T extends AnimatedEntity> AnimatedEntityRegistrar<T> createAnimated(
      EntityRegistrar.EntityRegisrarHolder holder, EntityFactory<T> pFactory, MobCategory pCategory, IDefaultAnimConfiguration animConfiguration
   ) {
      return new AnimatedEntityRegistrar<>(holder, pFactory, pCategory, animConfiguration);
   }

   public static record EntityRegisrarHolder(List<Consumer<RegisterRenderers>> renderListeners, List<Consumer<EntityAttributeCreationEvent>> attributeListeners) {
      public EntityRegisrarHolder() {
         this(Lists.newArrayList(), Lists.newArrayList());
      }

      public void register(EntityRegistrar<?> registrar) {
         IProxy.runSided(() -> () -> this.renderListeners.add(registrar::registerRenderer), () -> () -> {
            });
         this.attributeListeners.add(registrar::attributes);
      }

      public void attributes(EntityAttributeCreationEvent e) {
         for (Consumer<EntityAttributeCreationEvent> listener : this.attributeListeners) {
            listener.accept(e);
         }
      }

      public void register(RegisterRenderers e) {
         for (Consumer<RegisterRenderers> listener : this.renderListeners) {
            listener.accept(e);
         }
      }
   }
}
