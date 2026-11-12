package com.storyteam.storytelling.system._core.recipe_displays.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.world.item.crafting.RecipeType;

public record RecipeLayoutCollection(Map<RecipeType<?>, RecipeDrawLayout> layouts) {
   @Generated
   public static RecipeLayoutCollection.RecipeLayoutCollectionBuilder builder() {
      return new RecipeLayoutCollection.RecipeLayoutCollectionBuilder();
   }

   @Generated
   public static class RecipeLayoutCollectionBuilder {
      @Generated
      private ArrayList<RecipeType<?>> layouts$key;
      @Generated
      private ArrayList<RecipeDrawLayout> layouts$value;

      @Generated
      RecipeLayoutCollectionBuilder() {
      }

      @Generated
      public RecipeLayoutCollection.RecipeLayoutCollectionBuilder layout(RecipeType<?> layoutKey, RecipeDrawLayout layoutValue) {
         if (this.layouts$key == null) {
            this.layouts$key = new ArrayList<>();
            this.layouts$value = new ArrayList<>();
         }

         this.layouts$key.add(layoutKey);
         this.layouts$value.add(layoutValue);
         return this;
      }

      @Generated
      public RecipeLayoutCollection.RecipeLayoutCollectionBuilder layouts(Map<? extends RecipeType<?>, ? extends RecipeDrawLayout> layouts) {
         if (layouts == null) {
            throw new NullPointerException("layouts cannot be null");
         } else {
            if (this.layouts$key == null) {
               this.layouts$key = new ArrayList<>();
               this.layouts$value = new ArrayList<>();
            }

            for (Entry<? extends RecipeType<?>, ? extends RecipeDrawLayout> $lombokEntry : layouts.entrySet()) {
               this.layouts$key.add((RecipeType<?>)$lombokEntry.getKey());
               this.layouts$value.add($lombokEntry.getValue());
            }

            return this;
         }
      }

      @Generated
      public RecipeLayoutCollection.RecipeLayoutCollectionBuilder clearLayouts() {
         if (this.layouts$key != null) {
            this.layouts$key.clear();
            this.layouts$value.clear();
         }

         return this;
      }

      @Generated
      public RecipeLayoutCollection build() {
         return new RecipeLayoutCollection(
            switch (this.layouts$key == null ? 0 : this.layouts$key.size()) {
               case 0 -> Collections.emptyMap();
               case 1 -> Collections.singletonMap(this.layouts$key.get(0), this.layouts$value.get(0));
               default -> {
                  Map<RecipeType<?>, RecipeDrawLayout> layouts = new LinkedHashMap<>(
                     this.layouts$key.size() < 1073741824 ? 1 + this.layouts$key.size() + (this.layouts$key.size() - 3) / 3 : Integer.MAX_VALUE
                  );

                  for (int $i = 0; $i < this.layouts$key.size(); $i++) {
                     layouts.put(this.layouts$key.get($i), this.layouts$value.get($i));
                  }

                  yield Collections.unmodifiableMap(layouts);
               }
            }
         );
      }

      @Generated
      @Override
      public String toString() {
         return "RecipeLayoutCollection.RecipeLayoutCollectionBuilder(layouts$key=" + this.layouts$key + ", layouts$value=" + this.layouts$value + ")";
      }
   }
}
