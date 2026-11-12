package com.storyteam.storytelling.script.story.characters;

import com.google.common.collect.Maps;
import com.storyteam.storytelling.api.CharacterPersistentDataHelper;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.net.PacketSyncEntityAdditionalData;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent.Save;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

public class Character<T extends Entity> {
   private static final HashMap<String, Character<?>> CHARACTERS_LOOKUP = Maps.newHashMap();
   private final String referenceId;
   public final Supplier<EntityType<T>> type;
   public final String name;
   public final List<Consumer<T>> onCreatedFresh = new ArrayList<>();
   public final double standardMoveSpeed;
   public final ResourceLocation textured;
   public final Outfit outfit;

   protected Character(
      String referenceId,
      Supplier<EntityType<T>> type,
      String name,
      List<Consumer<T>> onCreatedFresh,
      double standardMoveSpeed,
      ResourceLocation textured,
      Outfit outfit
   ) {
      this.referenceId = referenceId;
      this.type = type;
      this.name = name;
      this.onCreatedFresh.addAll(onCreatedFresh);
      this.standardMoveSpeed = standardMoveSpeed == 0.0 ? 0.3 : standardMoveSpeed;
      this.textured = textured;
      this.outfit = outfit;
      CHARACTERS_LOOKUP.put(this.referenceId, this);
   }

   public EntityType<T> getType() {
      return this.type.get();
   }

   public void onCreatedFreshEntity(T entity) {
      if (entity != null) {
         if (this.name != null && !this.name.isEmpty()) {
            entity.setCustomName(Component.literal(this.name));
         }

         StoryData data = StoryData.of(entity);
         if (data != null) {
            data.texture.set(this.textured);
         }

         if (!this.onCreatedFresh.isEmpty()) {
            this.onCreatedFresh.forEach(consumer -> consumer.accept(entity));
            Network.sendToTracking(entity, new PacketSyncEntityAdditionalData(entity));
         }

         if (data != null) {
            data.detectAndSendChanges();
         }
      }
   }

   public String getReportName() {
      return String.format("%s (%s)", this.getType().getDescription().getString(), this.getReferenceId());
   }

   @Nullable
   public T repositoryEntity() {
      T entity = IStoryServer.get().getEntityRepository().getEntity(this);
      if (entity != null && this.name != null && !this.name.isEmpty()) {
         entity.setCustomName(Component.literal(this.name));
      }

      return entity;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this)
         .append("referenceId", this.referenceId)
         .append("type", ForgeRegistries.ENTITY_TYPES.getKey(this.type.get()))
         .append("chatName", this.name)
         .append("texture", this.textured)
         .build();
   }

   public static <T extends Entity> Character.CharacterBuilder<T> builder(String referenceId, Supplier<EntityType<T>> typeSupplier) {
      return (Character.CharacterBuilder<T>)Cast.cast(
         new Character.CharacterBuilder().referenceId(referenceId).type((Supplier<EntityType<T>>)Cast.cast(typeSupplier))
      );
   }

   public static <T extends Entity> Character.CharacterBuilder<T> builder(String referenceId, EntityType<T> type) {
      return builder(referenceId, (Supplier<EntityType<T>>)(() -> type));
   }

   public static <T extends Entity> Character.CharacterBuilder<T> builder(String referenceId, ResourceLocation type) {
      return (Character.CharacterBuilder<T>)Cast.cast(
         new Character.CharacterBuilder().referenceId(referenceId).type(() -> (EntityType<T>)Cast.cast(ForgeRegistries.ENTITY_TYPES.getValue(type)))
      );
   }

   public static <T extends Entity> Character.CharacterBuilder<T> builder(String referenceId, String type) {
      return builder(referenceId, Resources.location(type));
   }

   public static <T extends Entity> Character<T> getById(String referenceId) {
      return (Character<T>)Cast.cast(CHARACTERS_LOOKUP.get(referenceId));
   }

   public static HashMap<String, Character<?>> getCharacters() {
      return CHARACTERS_LOOKUP;
   }

   public boolean is(EntityInteractionData entity) {
      return entity.isOf(this);
   }

   public boolean is(Character<?> entity) {
      return Objects.equals(entity.getReferenceId(), this.getReferenceId());
   }

   public boolean is(Entity entity) {
      Entity re = this.repositoryEntity();
      return re != null && re.is(entity);
   }

   @Generated
   public Character.CharacterBuilder<T> toBuilder() {
      Character.CharacterBuilder<T> builder = new Character.CharacterBuilder<T>()
         .referenceId(this.referenceId)
         .type(this.type)
         .name(this.name)
         .standardMoveSpeed(this.standardMoveSpeed)
         .textured(this.textured)
         .outfit(this.outfit);
      if (this.onCreatedFresh != null) {
         builder.onCreatedFresh(this.onCreatedFresh);
      }

      return builder;
   }

   @Generated
   public String getReferenceId() {
      return this.referenceId;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public List<Consumer<T>> getOnCreatedFresh() {
      return this.onCreatedFresh;
   }

   @Generated
   public double getStandardMoveSpeed() {
      return this.standardMoveSpeed;
   }

   @Generated
   public ResourceLocation getTextured() {
      return this.textured;
   }

   @Generated
   public Outfit getOutfit() {
      return this.outfit;
   }

   public static class CharacterBuilder<T extends Entity> {
      @Generated
      private String referenceId;
      @Generated
      private Supplier<EntityType<T>> type;
      @Generated
      private String name;
      @Generated
      private ArrayList<Consumer<T>> onCreatedFresh;
      @Generated
      private double standardMoveSpeed;
      @Generated
      private ResourceLocation textured;
      @Generated
      private Outfit outfit;

      @Generated
      CharacterBuilder() {
      }

      @Generated
      public Character.CharacterBuilder<T> referenceId(String referenceId) {
         this.referenceId = referenceId;
         return this;
      }

      @Generated
      public Character.CharacterBuilder<T> type(Supplier<EntityType<T>> type) {
         this.type = type;
         return this;
      }

      @Generated
      public Character.CharacterBuilder<T> name(String name) {
         this.name = name;
         return this;
      }

      @Generated
      public Character.CharacterBuilder<T> onCreatedFresh(Consumer<T> onCreatedFresh) {
         if (this.onCreatedFresh == null) {
            this.onCreatedFresh = new ArrayList<>();
         }

         this.onCreatedFresh.add(onCreatedFresh);
         return this;
      }

      @Generated
      public Character.CharacterBuilder<T> onCreatedFresh(Collection<? extends Consumer<T>> onCreatedFresh) {
         if (onCreatedFresh == null) {
            throw new NullPointerException("onCreatedFresh cannot be null");
         } else {
            if (this.onCreatedFresh == null) {
               this.onCreatedFresh = new ArrayList<>();
            }

            this.onCreatedFresh.addAll(onCreatedFresh);
            return this;
         }
      }

      @Generated
      public Character.CharacterBuilder<T> clearOnCreatedFresh() {
         if (this.onCreatedFresh != null) {
            this.onCreatedFresh.clear();
         }

         return this;
      }

      @Generated
      public Character.CharacterBuilder<T> standardMoveSpeed(double standardMoveSpeed) {
         this.standardMoveSpeed = standardMoveSpeed;
         return this;
      }

      @Generated
      public Character.CharacterBuilder<T> textured(ResourceLocation textured) {
         this.textured = textured;
         return this;
      }

      @Generated
      public Character.CharacterBuilder<T> outfit(Outfit outfit) {
         this.outfit = outfit;
         return this;
      }

      @Generated
      public Character<T> build() {
         return new Character<>(this.referenceId, this.type, this.name, switch (this.onCreatedFresh == null ? 0 : this.onCreatedFresh.size()) {
            case 0 -> Collections.emptyList();
            case 1 -> Collections.singletonList(this.onCreatedFresh.get(0));
            default -> Collections.unmodifiableList(new ArrayList<>(this.onCreatedFresh));
         }, this.standardMoveSpeed, this.textured, this.outfit);
      }

      @Generated
      @Override
      public String toString() {
         return "Character.CharacterBuilder(referenceId="
            + this.referenceId
            + ", type="
            + this.type
            + ", name="
            + this.name
            + ", onCreatedFresh="
            + this.onCreatedFresh
            + ", standardMoveSpeed="
            + this.standardMoveSpeed
            + ", textured="
            + this.textured
            + ", outfit="
            + this.outfit
            + ")";
      }
   }

   @EventBusSubscriber
   public static class SaveHandler {
      @SubscribeEvent
      public static void onSave(Save e) {
         if (e.getLevel() instanceof ServerLevel) {
            ResourceKey<Level> dimension = ((ServerLevel)e.getLevel()).dimension();
            if (Level.OVERWORLD == dimension) {
               for (Character<?> character : Character.CHARACTERS_LOOKUP.values()) {
                  EntityController<?> ref = IStoryServer.get().getEntityRepository().get(character.referenceId);
                  if (ref != null && ref.getEntity() != null) {
                     Entity entity = ref.getEntity();
                     CharacterPersistentDataHelper.save(character.getReferenceId(), entity);
                  }
               }
            }
         }
      }
   }
}
