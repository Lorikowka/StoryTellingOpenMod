package com.storyteam.storytelling.system.gating;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.storyteam.storytelling.system.gating.io.LockedEntriesDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class LockedEntry implements ICustomRegistrar {
   private static final BiMap<ResourceLocation, LockedEntry> REGISTRY = HashBiMap.create();
   public static final Map<ResourceLocation, LockedEntry> BY_ID = Collections.unmodifiableMap(REGISTRY);
   private ResourceLocation id;
   private final Set<ResourceLocation> recipes;
   private final Set<ItemLike> items;
   private final Set<String> tags;
   private final boolean unlockedByDefault;

   public LockedEntry(Set<ResourceLocation> recipes, Set<ItemLike> items, Set<String> tags, boolean unlockedByDefault) {
      this.recipes = Collections.unmodifiableSet(recipes);
      this.items = Collections.unmodifiableSet(items);
      this.tags = Collections.unmodifiableSet(tags);
      this.unlockedByDefault = unlockedByDefault;
   }

   public static Stream<LockedEntry> stream() {
      return BY_ID.values().stream();
   }

   public static Stream<LockedEntry> streamByTag(String tag) {
      return BY_ID.values().stream().filter(le -> le.getTags().contains(tag));
   }

   private void checkRegistry() {
      Objects.requireNonNull(this.id, this + " is not registered!");
   }

   public boolean isUnlocked() {
      this.checkRegistry();
      return !LockedEntriesDB.isKnownEntry(this.id) ? this.unlockedByDefault : LockedEntriesDB.isUnlockedEntry(this.id);
   }

   public boolean setUnlocked(boolean unlocked) {
      boolean currentlyUnlocked = this.isUnlocked();
      if (currentlyUnlocked == unlocked) {
         return false;
      } else {
         LockedEntriesDB.recordChange(op -> {
            op.modifyKnownEntries(list -> list.add(this.id));
            op.modifyUnlockedEntries(list -> {
               if (unlocked) {
                  list.add(this.id);
               } else {
                  list.remove(this.id);
               }
            });
         });
         return true;
      }
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      event.register(Registries.ITEM, h -> {
         REGISTRY.put(id, this);
         this.id = id;
      });
   }

   @Generated
   public static LockedEntry.LockedEntryBuilder builder() {
      return new LockedEntry.LockedEntryBuilder();
   }

   @Generated
   @Override
   public String toString() {
      return "LockedEntry(id="
         + this.getId()
         + ", recipes="
         + this.getRecipes()
         + ", items="
         + this.getItems()
         + ", tags="
         + this.getTags()
         + ", unlockedByDefault="
         + this.unlockedByDefault
         + ")";
   }

   @Generated
   public ResourceLocation getId() {
      return this.id;
   }

   @Generated
   public Set<ResourceLocation> getRecipes() {
      return this.recipes;
   }

   @Generated
   public Set<ItemLike> getItems() {
      return this.items;
   }

   @Generated
   public Set<String> getTags() {
      return this.tags;
   }

   @Generated
   public static class LockedEntryBuilder {
      @Generated
      private ArrayList<ResourceLocation> recipes;
      @Generated
      private ArrayList<ItemLike> items;
      @Generated
      private ArrayList<String> tags;
      @Generated
      private boolean unlockedByDefault;

      @Generated
      LockedEntryBuilder() {
      }

      @Generated
      public LockedEntry.LockedEntryBuilder recipe(ResourceLocation recipe) {
         if (this.recipes == null) {
            this.recipes = new ArrayList<>();
         }

         this.recipes.add(recipe);
         return this;
      }

      @Generated
      public LockedEntry.LockedEntryBuilder recipes(Collection<? extends ResourceLocation> recipes) {
         if (recipes == null) {
            throw new NullPointerException("recipes cannot be null");
         } else {
            if (this.recipes == null) {
               this.recipes = new ArrayList<>();
            }

            this.recipes.addAll(recipes);
            return this;
         }
      }

      @Generated
      public LockedEntry.LockedEntryBuilder clearRecipes() {
         if (this.recipes != null) {
            this.recipes.clear();
         }

         return this;
      }

      @Generated
      public LockedEntry.LockedEntryBuilder item(ItemLike item) {
         if (this.items == null) {
            this.items = new ArrayList<>();
         }

         this.items.add(item);
         return this;
      }

      @Generated
      public LockedEntry.LockedEntryBuilder items(Collection<? extends ItemLike> items) {
         if (items == null) {
            throw new NullPointerException("items cannot be null");
         } else {
            if (this.items == null) {
               this.items = new ArrayList<>();
            }

            this.items.addAll(items);
            return this;
         }
      }

      @Generated
      public LockedEntry.LockedEntryBuilder clearItems() {
         if (this.items != null) {
            this.items.clear();
         }

         return this;
      }

      @Generated
      public LockedEntry.LockedEntryBuilder tag(String tag) {
         if (this.tags == null) {
            this.tags = new ArrayList<>();
         }

         this.tags.add(tag);
         return this;
      }

      @Generated
      public LockedEntry.LockedEntryBuilder tags(Collection<? extends String> tags) {
         if (tags == null) {
            throw new NullPointerException("tags cannot be null");
         } else {
            if (this.tags == null) {
               this.tags = new ArrayList<>();
            }

            this.tags.addAll(tags);
            return this;
         }
      }

      @Generated
      public LockedEntry.LockedEntryBuilder clearTags() {
         if (this.tags != null) {
            this.tags.clear();
         }

         return this;
      }

      @Generated
      public LockedEntry.LockedEntryBuilder unlockedByDefault(boolean unlockedByDefault) {
         this.unlockedByDefault = unlockedByDefault;
         return this;
      }

      @Generated
      public LockedEntry build() {
         Set var4 = switch (this.recipes == null ? 0 : this.recipes.size()) {
            case 0 -> Collections.emptySet();
            case 1 -> Collections.singleton(this.recipes.get(0));
            default -> {
               Set<ResourceLocation> recipes = new LinkedHashSet<>(
                  this.recipes.size() < 1073741824 ? 1 + this.recipes.size() + (this.recipes.size() - 3) / 3 : Integer.MAX_VALUE
               );
               recipes.addAll(this.recipes);
               yield Collections.unmodifiableSet(recipes);
            }
         };

         Set var5 = switch (this.items == null ? 0 : this.items.size()) {
            case 0 -> Collections.emptySet();
            case 1 -> Collections.singleton(this.items.get(0));
            default -> {
               Set<ItemLike> items = new LinkedHashSet<>(
                  this.items.size() < 1073741824 ? 1 + this.items.size() + (this.items.size() - 3) / 3 : Integer.MAX_VALUE
               );
               items.addAll(this.items);
               yield Collections.unmodifiableSet(items);
            }
         };

         return new LockedEntry(var4, var5, switch (this.tags == null ? 0 : this.tags.size()) {
            case 0 -> Collections.emptySet();
            case 1 -> Collections.singleton(this.tags.get(0));
            default -> {
               Set<String> tags = new LinkedHashSet<>(this.tags.size() < 1073741824 ? 1 + this.tags.size() + (this.tags.size() - 3) / 3 : Integer.MAX_VALUE);
               tags.addAll(this.tags);
               yield Collections.unmodifiableSet(tags);
            }
         }, this.unlockedByDefault);
      }

      @Generated
      @Override
      public String toString() {
         return "LockedEntry.LockedEntryBuilder(recipes="
            + this.recipes
            + ", items="
            + this.items
            + ", tags="
            + this.tags
            + ", unlockedByDefault="
            + this.unlockedByDefault
            + ")";
      }
   }
}
