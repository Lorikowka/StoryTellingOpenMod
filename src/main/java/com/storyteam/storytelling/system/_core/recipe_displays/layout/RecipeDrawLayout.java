package com.storyteam.storytelling.system._core.recipe_displays.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.math.Point;

public record RecipeDrawLayout(@Nullable ResourceLocation background, float width, float height, List<Point> inputSlots, List<Point> outputSlots) {
   @Generated
   public static RecipeDrawLayout.RecipeDrawLayoutBuilder builder() {
      return new RecipeDrawLayout.RecipeDrawLayoutBuilder();
   }

   @Generated
   public static class RecipeDrawLayoutBuilder {
      @Generated
      private ResourceLocation background;
      @Generated
      private float width;
      @Generated
      private float height;
      @Generated
      private ArrayList<Point> inputSlots;
      @Generated
      private ArrayList<Point> outputSlots;

      @Generated
      RecipeDrawLayoutBuilder() {
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder background(@Nullable ResourceLocation background) {
         this.background = background;
         return this;
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder width(float width) {
         this.width = width;
         return this;
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder height(float height) {
         this.height = height;
         return this;
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder inputSlot(Point inputSlot) {
         if (this.inputSlots == null) {
            this.inputSlots = new ArrayList<>();
         }

         this.inputSlots.add(inputSlot);
         return this;
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder inputSlots(Collection<? extends Point> inputSlots) {
         if (inputSlots == null) {
            throw new NullPointerException("inputSlots cannot be null");
         } else {
            if (this.inputSlots == null) {
               this.inputSlots = new ArrayList<>();
            }

            this.inputSlots.addAll(inputSlots);
            return this;
         }
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder clearInputSlots() {
         if (this.inputSlots != null) {
            this.inputSlots.clear();
         }

         return this;
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder outputSlot(Point outputSlot) {
         if (this.outputSlots == null) {
            this.outputSlots = new ArrayList<>();
         }

         this.outputSlots.add(outputSlot);
         return this;
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder outputSlots(Collection<? extends Point> outputSlots) {
         if (outputSlots == null) {
            throw new NullPointerException("outputSlots cannot be null");
         } else {
            if (this.outputSlots == null) {
               this.outputSlots = new ArrayList<>();
            }

            this.outputSlots.addAll(outputSlots);
            return this;
         }
      }

      @Generated
      public RecipeDrawLayout.RecipeDrawLayoutBuilder clearOutputSlots() {
         if (this.outputSlots != null) {
            this.outputSlots.clear();
         }

         return this;
      }

      @Generated
      public RecipeDrawLayout build() {
         List inputSlots = switch (this.inputSlots == null ? 0 : this.inputSlots.size()) {
            case 0 -> Collections.emptyList();
            case 1 -> Collections.singletonList(this.inputSlots.get(0));
            default -> Collections.unmodifiableList(new ArrayList<>(this.inputSlots));
         };

         return new RecipeDrawLayout(this.background, this.width, this.height, inputSlots, switch (this.outputSlots == null ? 0 : this.outputSlots.size()) {
            case 0 -> Collections.emptyList();
            case 1 -> Collections.singletonList(this.outputSlots.get(0));
            default -> Collections.unmodifiableList(new ArrayList<>(this.outputSlots));
         });
      }

      @Generated
      @Override
      public String toString() {
         return "RecipeDrawLayout.RecipeDrawLayoutBuilder(background="
            + this.background
            + ", width="
            + this.width
            + ", height="
            + this.height
            + ", inputSlots="
            + this.inputSlots
            + ", outputSlots="
            + this.outputSlots
            + ")";
      }
   }
}
