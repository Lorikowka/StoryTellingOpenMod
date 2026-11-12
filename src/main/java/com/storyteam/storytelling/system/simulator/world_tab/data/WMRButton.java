package com.storyteam.storytelling.system.simulator.world_tab.data;

import com.storyteam.storytelling.system.currency.CurrencySystem;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.flowgui.objects.GuiButtonObject.OnPress;

public class WMRButton {
   private final String name;
   private boolean enabled;
   private final Consumer<WorldMapRegion> action;
   private final ResourceLocation texture;
   private int price;

   public WMRButton(boolean enabled, Consumer<WorldMapRegion> action, ResourceLocation texture, String name, int price) {
      this.enabled = enabled;
      this.action = action;
      this.texture = texture;
      this.name = name;
      this.price = price;
      if (!WorldMapRegionsStorage.WORLD_MAP_BUTTONS.containsKey(name)) {
         WorldMapRegionsStorage.WORLD_MAP_BUTTONS.put(name, this);
      }
   }

   public OnPress callback(WorldMapRegion region) {
      return gui -> {
         if (this.hasEnoughEidos()) {
            CurrencyStorage.removeCurrency(CurrencySystem.EIDOS_CURRENCY, this.price);
            this.action.accept(region);
         }
      };
   }

   public boolean hasEnoughEidos() {
      return CurrencyStorage.getEidos() >= this.price;
   }

   public WMRButton copy() {
      return this.copy(this.enabled);
   }

   public WMRButton copy(boolean enabled) {
      return new WMRButton(enabled, this.action, this.texture, this.name, this.price);
   }

   @Generated
   public static WMRButton.WMRButtonBuilder builder() {
      return new WMRButton.WMRButtonBuilder();
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public Consumer<WorldMapRegion> getAction() {
      return this.action;
   }

   @Generated
   public ResourceLocation getTexture() {
      return this.texture;
   }

   @Generated
   public int getPrice() {
      return this.price;
   }

   @Generated
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Generated
   public static class WMRButtonBuilder {
      @Generated
      private boolean enabled;
      @Generated
      private Consumer<WorldMapRegion> action;
      @Generated
      private ResourceLocation texture;
      @Generated
      private String name;
      @Generated
      private int price;

      @Generated
      WMRButtonBuilder() {
      }

      @Generated
      public WMRButton.WMRButtonBuilder enabled(boolean enabled) {
         this.enabled = enabled;
         return this;
      }

      @Generated
      public WMRButton.WMRButtonBuilder action(Consumer<WorldMapRegion> action) {
         this.action = action;
         return this;
      }

      @Generated
      public WMRButton.WMRButtonBuilder texture(ResourceLocation texture) {
         this.texture = texture;
         return this;
      }

      @Generated
      public WMRButton.WMRButtonBuilder name(String name) {
         this.name = name;
         return this;
      }

      @Generated
      public WMRButton.WMRButtonBuilder price(int price) {
         this.price = price;
         return this;
      }

      @Generated
      public WMRButton build() {
         return new WMRButton(this.enabled, this.action, this.texture, this.name, this.price);
      }

      @Generated
      @Override
      public String toString() {
         return "WMRButton.WMRButtonBuilder(enabled="
            + this.enabled
            + ", action="
            + this.action
            + ", texture="
            + this.texture
            + ", name="
            + this.name
            + ", price="
            + this.price
            + ")";
      }
   }
}
