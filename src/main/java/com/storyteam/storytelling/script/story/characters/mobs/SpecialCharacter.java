package com.storyteam.storytelling.script.story.characters.mobs;

import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.fight.mob_drop.MobDropData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.util.mcf.Resources;

public class SpecialCharacter<T extends Entity> extends Character<T> {
   private List<MobDropData> mobDropList = new ArrayList<>();

   public SpecialCharacter(
      String referenceId,
      Supplier<EntityType<T>> type,
      String name,
      List<Consumer<T>> onCreatedFresh,
      double standardMoveSpeed,
      ResourceLocation textured,
      Outfit outfit,
      List<MobDropData> mobDropList
   ) {
      super(referenceId, type, name, onCreatedFresh, standardMoveSpeed, textured, outfit);
      this.mobDropList = mobDropList;
   }

   public List<MobDropData> getmobDropList() {
      return this.mobDropList;
   }

   public static <T extends Entity> SpecialCharacter.SpecialCharacterBuilder<T> specialCharacterBuilder(
      String referenceId, Supplier<EntityType<T>> typeSupplier
   ) {
      return new SpecialCharacter.SpecialCharacterBuilder<T>().referenceId(referenceId).type(typeSupplier);
   }

   public static <T extends Entity> SpecialCharacter.SpecialCharacterBuilder<T> specialCharacterBuilder(String referenceId, EntityType<T> type) {
      return specialCharacterBuilder(referenceId, (Supplier<EntityType<T>>)(() -> type));
   }

   public static <T extends Entity> SpecialCharacter.SpecialCharacterBuilder<T> specialCharacterBuilder(String referenceId, ResourceLocation type) {
      return specialCharacterBuilder(referenceId, (Supplier<EntityType<T>>)(() -> (EntityType<T>)ForgeRegistries.ENTITY_TYPES.getValue(type)));
   }

   public static <T extends Entity> SpecialCharacter.SpecialCharacterBuilder<T> specialCharacterBuilder(String referenceId, String type) {
      return specialCharacterBuilder(referenceId, Resources.location(type));
   }

   @Generated
   public static <T extends Entity> SpecialCharacter.SpecialCharacterBuilder<T> builder() {
      return new SpecialCharacter.SpecialCharacterBuilder<>();
   }

   public static class SpecialCharacterBuilder<T extends Entity> {
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
      private List<MobDropData> mobDropList;

      @Generated
      SpecialCharacterBuilder() {
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> referenceId(String referenceId) {
         this.referenceId = referenceId;
         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> type(Supplier<EntityType<T>> type) {
         this.type = type;
         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> name(String name) {
         this.name = name;
         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> onCreatedFresh(Consumer<T> onCreatedFresh) {
         if (this.onCreatedFresh == null) {
            this.onCreatedFresh = new ArrayList<>();
         }

         this.onCreatedFresh.add(onCreatedFresh);
         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> onCreatedFresh(Collection<? extends Consumer<T>> onCreatedFresh) {
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
      public SpecialCharacter.SpecialCharacterBuilder<T> clearOnCreatedFresh() {
         if (this.onCreatedFresh != null) {
            this.onCreatedFresh.clear();
         }

         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> standardMoveSpeed(double standardMoveSpeed) {
         this.standardMoveSpeed = standardMoveSpeed;
         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> textured(ResourceLocation textured) {
         this.textured = textured;
         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> outfit(Outfit outfit) {
         this.outfit = outfit;
         return this;
      }

      @Generated
      public SpecialCharacter.SpecialCharacterBuilder<T> mobDropList(List<MobDropData> mobDropList) {
         this.mobDropList = mobDropList;
         return this;
      }

      @Generated
      public SpecialCharacter<T> build() {
         return new SpecialCharacter<>(this.referenceId, this.type, this.name, switch (this.onCreatedFresh == null ? 0 : this.onCreatedFresh.size()) {
            case 0 -> Collections.emptyList();
            case 1 -> Collections.singletonList(this.onCreatedFresh.get(0));
            default -> Collections.unmodifiableList(new ArrayList<>(this.onCreatedFresh));
         }, this.standardMoveSpeed, this.textured, this.outfit, this.mobDropList);
      }

      @Generated
      @Override
      public String toString() {
         return "SpecialCharacter.SpecialCharacterBuilder(referenceId="
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
            + ", mobDropList="
            + this.mobDropList
            + ")";
      }
   }
}
