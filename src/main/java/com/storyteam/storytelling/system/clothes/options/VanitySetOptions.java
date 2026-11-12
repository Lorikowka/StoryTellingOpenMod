package com.storyteam.storytelling.system.clothes.options;

import com.storyteam.storytelling.system.clothes.VanityModels;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.client.VanityRenderOptions;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.system.clothes.item.IVanityArmorItemFactory;
import com.storyteam.storytelling.system.clothes.item.IVanityArmorMaterialFactory;
import java.util.EnumMap;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.world.item.Item.Properties;
import org.jetbrains.annotations.NotNull;

public class VanitySetOptions {
   private final VanityParts parts;
   private final IVanityTextureProvider textures;
   private final Supplier<Properties> armorProps;
   private final EnumMap<VanitySlot, String> nameOverrides;
   private final BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions;
   @NotNull
   private final VanityModels geometry;
   private final boolean useBuiltinItemModels;
   private final IVanityArmorItemFactory itemFactory;
   private final IVanityArmorMaterialFactory materialFactory;

   @Generated
   private static VanityParts $default$parts() {
      return VanityParts.ALL;
   }

   @Generated
   private static IVanityTextureProvider $default$textures() {
      return IVanityTextureProvider.UNIFIED_TEXTURE;
   }

   @Generated
   private static Supplier<Properties> $default$armorProps() {
      return () -> new Properties();
   }

   @Generated
   private static EnumMap<VanitySlot, String> $default$nameOverrides() {
      return new EnumMap<>(VanitySlot.class);
   }

   @Generated
   private static BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> $default$renderOptions() {
      return (s, b) -> b;
   }

   @Generated
   private static boolean $default$useBuiltinItemModels() {
      return true;
   }

   @Generated
   private static IVanityArmorItemFactory $default$itemFactory() {
      return IVanityArmorItemFactory.DEFAULT;
   }

   @Generated
   private static IVanityArmorMaterialFactory $default$materialFactory() {
      return IVanityArmorMaterialFactory.DEFAULT;
   }

   @Generated
   VanitySetOptions(
      VanityParts parts,
      IVanityTextureProvider textures,
      Supplier<Properties> armorProps,
      EnumMap<VanitySlot, String> nameOverrides,
      BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions,
      @NotNull VanityModels geometry,
      boolean useBuiltinItemModels,
      IVanityArmorItemFactory itemFactory,
      IVanityArmorMaterialFactory materialFactory
   ) {
      if (geometry == null) {
         throw new NullPointerException("geometry is marked non-null but is null");
      } else {
         this.parts = parts;
         this.textures = textures;
         this.armorProps = armorProps;
         this.nameOverrides = nameOverrides;
         this.renderOptions = renderOptions;
         this.geometry = geometry;
         this.useBuiltinItemModels = useBuiltinItemModels;
         this.itemFactory = itemFactory;
         this.materialFactory = materialFactory;
      }
   }

   @Generated
   public static VanitySetOptions.VanitySetOptionsBuilder builder() {
      return new VanitySetOptions.VanitySetOptionsBuilder();
   }

   @Generated
   public VanityParts getParts() {
      return this.parts;
   }

   @Generated
   public IVanityTextureProvider getTextures() {
      return this.textures;
   }

   @Generated
   public Supplier<Properties> getArmorProps() {
      return this.armorProps;
   }

   @Generated
   public EnumMap<VanitySlot, String> getNameOverrides() {
      return this.nameOverrides;
   }

   @Generated
   public BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> getRenderOptions() {
      return this.renderOptions;
   }

   @NotNull
   @Generated
   public VanityModels getGeometry() {
      return this.geometry;
   }

   @Generated
   public boolean isUseBuiltinItemModels() {
      return this.useBuiltinItemModels;
   }

   @Generated
   public IVanityArmorItemFactory getItemFactory() {
      return this.itemFactory;
   }

   @Generated
   public IVanityArmorMaterialFactory getMaterialFactory() {
      return this.materialFactory;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof VanitySetOptions other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.isUseBuiltinItemModels() != other.isUseBuiltinItemModels()) {
         return false;
      } else {
         Object this$parts = this.getParts();
         Object other$parts = other.getParts();
         if (this$parts == null ? other$parts == null : this$parts.equals(other$parts)) {
            Object this$textures = this.getTextures();
            Object other$textures = other.getTextures();
            if (this$textures == null ? other$textures == null : this$textures.equals(other$textures)) {
               Object this$armorProps = this.getArmorProps();
               Object other$armorProps = other.getArmorProps();
               if (this$armorProps == null ? other$armorProps == null : this$armorProps.equals(other$armorProps)) {
                  Object this$nameOverrides = this.getNameOverrides();
                  Object other$nameOverrides = other.getNameOverrides();
                  if (this$nameOverrides == null ? other$nameOverrides == null : this$nameOverrides.equals(other$nameOverrides)) {
                     Object this$renderOptions = this.getRenderOptions();
                     Object other$renderOptions = other.getRenderOptions();
                     if (this$renderOptions == null ? other$renderOptions == null : this$renderOptions.equals(other$renderOptions)) {
                        Object this$geometry = this.getGeometry();
                        Object other$geometry = other.getGeometry();
                        if (this$geometry == null ? other$geometry == null : this$geometry.equals(other$geometry)) {
                           Object this$itemFactory = this.getItemFactory();
                           Object other$itemFactory = other.getItemFactory();
                           if (this$itemFactory == null ? other$itemFactory == null : this$itemFactory.equals(other$itemFactory)) {
                              Object this$materialFactory = this.getMaterialFactory();
                              Object other$materialFactory = other.getMaterialFactory();
                              return this$materialFactory == null ? other$materialFactory == null : this$materialFactory.equals(other$materialFactory);
                           } else {
                              return false;
                           }
                        } else {
                           return false;
                        }
                     } else {
                        return false;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof VanitySetOptions;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + (this.isUseBuiltinItemModels() ? 79 : 97);
      Object $parts = this.getParts();
      result = result * 59 + ($parts == null ? 43 : $parts.hashCode());
      Object $textures = this.getTextures();
      result = result * 59 + ($textures == null ? 43 : $textures.hashCode());
      Object $armorProps = this.getArmorProps();
      result = result * 59 + ($armorProps == null ? 43 : $armorProps.hashCode());
      Object $nameOverrides = this.getNameOverrides();
      result = result * 59 + ($nameOverrides == null ? 43 : $nameOverrides.hashCode());
      Object $renderOptions = this.getRenderOptions();
      result = result * 59 + ($renderOptions == null ? 43 : $renderOptions.hashCode());
      Object $geometry = this.getGeometry();
      result = result * 59 + ($geometry == null ? 43 : $geometry.hashCode());
      Object $itemFactory = this.getItemFactory();
      result = result * 59 + ($itemFactory == null ? 43 : $itemFactory.hashCode());
      Object $materialFactory = this.getMaterialFactory();
      return result * 59 + ($materialFactory == null ? 43 : $materialFactory.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "VanitySetOptions(parts="
         + this.getParts()
         + ", textures="
         + this.getTextures()
         + ", armorProps="
         + this.getArmorProps()
         + ", nameOverrides="
         + this.getNameOverrides()
         + ", renderOptions="
         + this.getRenderOptions()
         + ", geometry="
         + this.getGeometry()
         + ", useBuiltinItemModels="
         + this.isUseBuiltinItemModels()
         + ", itemFactory="
         + this.getItemFactory()
         + ", materialFactory="
         + this.getMaterialFactory()
         + ")";
   }

   public static class VanitySetOptionsBuilder {
      @Generated
      private boolean parts$set;
      @Generated
      private VanityParts parts$value;
      @Generated
      private boolean textures$set;
      @Generated
      private IVanityTextureProvider textures$value;
      @Generated
      private boolean armorProps$set;
      @Generated
      private Supplier<Properties> armorProps$value;
      @Generated
      private boolean nameOverrides$set;
      @Generated
      private EnumMap<VanitySlot, String> nameOverrides$value;
      @Generated
      private boolean renderOptions$set;
      @Generated
      private BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions$value;
      @Generated
      private VanityModels geometry;
      @Generated
      private boolean useBuiltinItemModels$set;
      @Generated
      private boolean useBuiltinItemModels$value;
      @Generated
      private boolean itemFactory$set;
      @Generated
      private IVanityArmorItemFactory itemFactory$value;
      @Generated
      private boolean materialFactory$set;
      @Generated
      private IVanityArmorMaterialFactory materialFactory$value;

      public VanitySetOptions.VanitySetOptionsBuilder rename(VanitySlot slot, String name) {
         if (this.nameOverrides$value == null || !this.nameOverrides$set) {
            this.nameOverrides$value = new EnumMap<>(VanitySlot.class);
            this.nameOverrides$set = true;
         }

         this.nameOverrides$value.put(slot, name);
         return this;
      }

      public VanitySetOptions.VanitySetOptionsBuilder addParts(VanitySlot... slots) {
         this.parts$value = this.parts$value.or(VanityParts.of(slots));
         this.parts$set = true;
         return this;
      }

      public VanitySetOptions.VanitySetOptionsBuilder setParts(VanitySlot... slots) {
         Set<VanitySlot> es = Set.of(slots);
         this.parts$value = VanityParts.ALL.filter(es::contains);
         this.parts$set = true;
         return this;
      }

      public VanitySetOptions.VanitySetOptionsBuilder use(Consumer<VanitySetOptions.VanitySetOptionsBuilder> handler) {
         handler.accept(this);
         return this;
      }

      public VanitySet create() {
         return new VanitySet(this.build());
      }

      @Generated
      VanitySetOptionsBuilder() {
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder parts(VanityParts parts) {
         this.parts$value = parts;
         this.parts$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder textures(IVanityTextureProvider textures) {
         this.textures$value = textures;
         this.textures$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder armorProps(Supplier<Properties> armorProps) {
         this.armorProps$value = armorProps;
         this.armorProps$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder nameOverrides(EnumMap<VanitySlot, String> nameOverrides) {
         this.nameOverrides$value = nameOverrides;
         this.nameOverrides$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder renderOptions(
         BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions
      ) {
         this.renderOptions$value = renderOptions;
         this.renderOptions$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder geometry(@NotNull VanityModels geometry) {
         if (geometry == null) {
            throw new NullPointerException("geometry is marked non-null but is null");
         } else {
            this.geometry = geometry;
            return this;
         }
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder useBuiltinItemModels(boolean useBuiltinItemModels) {
         this.useBuiltinItemModels$value = useBuiltinItemModels;
         this.useBuiltinItemModels$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder itemFactory(IVanityArmorItemFactory itemFactory) {
         this.itemFactory$value = itemFactory;
         this.itemFactory$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions.VanitySetOptionsBuilder materialFactory(IVanityArmorMaterialFactory materialFactory) {
         this.materialFactory$value = materialFactory;
         this.materialFactory$set = true;
         return this;
      }

      @Generated
      public VanitySetOptions build() {
         VanityParts parts$value = this.parts$value;
         if (!this.parts$set) {
            parts$value = VanitySetOptions.$default$parts();
         }

         IVanityTextureProvider textures$value = this.textures$value;
         if (!this.textures$set) {
            textures$value = VanitySetOptions.$default$textures();
         }

         Supplier<Properties> armorProps$value = this.armorProps$value;
         if (!this.armorProps$set) {
            armorProps$value = VanitySetOptions.$default$armorProps();
         }

         EnumMap<VanitySlot, String> nameOverrides$value = this.nameOverrides$value;
         if (!this.nameOverrides$set) {
            nameOverrides$value = VanitySetOptions.$default$nameOverrides();
         }

         BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions$value = this.renderOptions$value;
         if (!this.renderOptions$set) {
            renderOptions$value = VanitySetOptions.$default$renderOptions();
         }

         boolean useBuiltinItemModels$value = this.useBuiltinItemModels$value;
         if (!this.useBuiltinItemModels$set) {
            useBuiltinItemModels$value = VanitySetOptions.$default$useBuiltinItemModels();
         }

         IVanityArmorItemFactory itemFactory$value = this.itemFactory$value;
         if (!this.itemFactory$set) {
            itemFactory$value = VanitySetOptions.$default$itemFactory();
         }

         IVanityArmorMaterialFactory materialFactory$value = this.materialFactory$value;
         if (!this.materialFactory$set) {
            materialFactory$value = VanitySetOptions.$default$materialFactory();
         }

         return new VanitySetOptions(
            parts$value,
            textures$value,
            armorProps$value,
            nameOverrides$value,
            renderOptions$value,
            this.geometry,
            useBuiltinItemModels$value,
            itemFactory$value,
            materialFactory$value
         );
      }

      @Generated
      @Override
      public String toString() {
         return "VanitySetOptions.VanitySetOptionsBuilder(parts$value="
            + this.parts$value
            + ", textures$value="
            + this.textures$value
            + ", armorProps$value="
            + this.armorProps$value
            + ", nameOverrides$value="
            + this.nameOverrides$value
            + ", renderOptions$value="
            + this.renderOptions$value
            + ", geometry="
            + this.geometry
            + ", useBuiltinItemModels$value="
            + this.useBuiltinItemModels$value
            + ", itemFactory$value="
            + this.itemFactory$value
            + ", materialFactory$value="
            + this.materialFactory$value
            + ")";
      }
   }
}
