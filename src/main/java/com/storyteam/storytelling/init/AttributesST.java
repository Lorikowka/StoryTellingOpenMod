package com.storyteam.storytelling.init;

import lombok.Generated;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface AttributesST {
   @RegistryName("leveling_vigor")
   Attribute LEVELING_VIGOR = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.leveling.vigor").defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("leveling_endurance")
   Attribute LEVELING_ENDURANCE = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.leveling.endurance").defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("leveling_intelligence")
   Attribute LEVELING_INTELLIGENCE = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.leveling.intelligence").defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("leveling_strength")
   Attribute LEVELING_STRENGTH = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.leveling.strength").defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("leveling_agility")
   Attribute LEVELING_AGILITY = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.leveling.agility").defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("leveling_luck")
   Attribute LEVELING_LUCK = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.leveling.luck").defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("stamina")
   Attribute MAX_STAMINA = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.max_stamina").defaultValue(200.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("mana")
   Attribute MAX_MANA = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.max_mana").defaultValue(200.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("stamina_regen")
   Attribute STAMINA_REGEN = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.stamina_regen").defaultValue(8.0).minValue(0.0).maxValue(1024.0).build()
   );
   @RegistryName("poise")
   Attribute POISE = register(AttributesST.AttributeBuilder.builder().key("storytelling.poise").defaultValue(0.5).minValue(0.0).maxValue(1024.0).build());
   @RegistryName("affinity")
   Attribute AFFINITY = register(
      AttributesST.AttributeBuilder.builder().key("storytelling.leveling.affinity").defaultValue(0.0).minValue(0.0).maxValue(1024.0).build()
   );

   private static Attribute register(AttributesST.AttributeBuilder builder) {
      return new RangedAttribute(builder.key, builder.defaultValue, builder.minValue, builder.maxValue);
   }

   @Setup
   static void setup() {
   }

   public static class AttributeBuilder {
      private String key;
      private double defaultValue;
      private double minValue;
      private double maxValue;

      public AttributeBuilder(String key, double defaultValue, double minValue, double maxValue) {
         this.key = key;
         this.defaultValue = defaultValue;
         this.minValue = minValue;
         this.maxValue = maxValue;
      }

      @Generated
      public static AttributesST.AttributeBuilder.AttributeBuilderBuilder builder() {
         return new AttributesST.AttributeBuilder.AttributeBuilderBuilder();
      }

      @Generated
      public String getKey() {
         return this.key;
      }

      @Generated
      public double getDefaultValue() {
         return this.defaultValue;
      }

      @Generated
      public double getMinValue() {
         return this.minValue;
      }

      @Generated
      public double getMaxValue() {
         return this.maxValue;
      }

      @Generated
      public static class AttributeBuilderBuilder {
         @Generated
         private String key;
         @Generated
         private double defaultValue;
         @Generated
         private double minValue;
         @Generated
         private double maxValue;

         @Generated
         AttributeBuilderBuilder() {
         }

         @Generated
         public AttributesST.AttributeBuilder.AttributeBuilderBuilder key(String key) {
            this.key = key;
            return this;
         }

         @Generated
         public AttributesST.AttributeBuilder.AttributeBuilderBuilder defaultValue(double defaultValue) {
            this.defaultValue = defaultValue;
            return this;
         }

         @Generated
         public AttributesST.AttributeBuilder.AttributeBuilderBuilder minValue(double minValue) {
            this.minValue = minValue;
            return this;
         }

         @Generated
         public AttributesST.AttributeBuilder.AttributeBuilderBuilder maxValue(double maxValue) {
            this.maxValue = maxValue;
            return this;
         }

         @Generated
         public AttributesST.AttributeBuilder build() {
            return new AttributesST.AttributeBuilder(this.key, this.defaultValue, this.minValue, this.maxValue);
         }

         @Generated
         @Override
         public String toString() {
            return "AttributesST.AttributeBuilder.AttributeBuilderBuilder(key="
               + this.key
               + ", defaultValue="
               + this.defaultValue
               + ", minValue="
               + this.minValue
               + ", maxValue="
               + this.maxValue
               + ")";
         }
      }
   }
}
