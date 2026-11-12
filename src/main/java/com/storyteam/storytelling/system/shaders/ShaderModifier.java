package com.storyteam.storytelling.system.shaders;

import com.storyteam.storytelling.init.system.WeathersST;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.zeith.hammerlib.annotations.RegistryName;

public class ShaderModifier {
   public final List<String> shaderPacksBefore = new ArrayList<>();
   public final List<String> shaderPacksAfter = new ArrayList<>();
   public final List<String> modifiersBefore = new ArrayList<>();
   public final List<String> modifiersAfter = new ArrayList<>();
   public String name;
   public String shaderPackName;
   public float priority = 0.0F;
   public BooleanSupplier stillValid = () -> true;

   public ShaderModifier(String name, String shaderPackName) {
      this.name = name;
      this.shaderPackName = shaderPackName;
   }

   public ShaderModifier(String name) {
      this.name = name;
      this.shaderPackName = name;
   }

   private ShaderModifier() {
   }

   public static int compare(ShaderModifier m1, ShaderModifier m2) {
      int c1 = m1._compare(m2);
      int c2 = -m2._compare(m1);
      if (c1 == 0) {
         return c2;
      } else if (c2 == 0) {
         return c1;
      } else if (Math.signum((float)c1) == Math.signum((float)c2)) {
         return (int)Math.signum((float)c1);
      } else if (c1 == 1 && c2 == -2) {
         return -1;
      } else if (c1 == -1 && c2 == 2) {
         return 1;
      } else if (c1 == 2 && c2 == -1) {
         return 1;
      } else {
         return c1 == -2 && c2 == 1 ? -1 : 0;
      }
   }

   public ShaderModifier priority(float priority) {
      this.priority = priority;
      return this;
   }

   public ShaderModifier setStillValid(BooleanSupplier stillValid) {
      this.stillValid = stillValid;
      return this;
   }

   public ShaderModifier setStillValid(Predicate<ShaderModifier> stillValid) {
      this.stillValid = () -> stillValid.test(this);
      return this;
   }

   public boolean isValid() {
      return this.stillValid.getAsBoolean();
   }

   public ShaderModifier beforeModifier(String name) {
      this.modifiersAfter.add(name);
      return this;
   }

   public ShaderModifier afterModifier(String name) {
      this.modifiersBefore.add(name);
      return this;
   }

   public ShaderModifier afterWeather() {
      for (String tag : Arrays.stream(WeathersST.class.getDeclaredFields()).map(f -> ((RegistryName)f.getAnnotation(RegistryName.class)).value()).toList()) {
         this.afterModifier(tag);
      }

      return this;
   }

   public ShaderModifier beforeShaderPack(String name) {
      this.shaderPacksAfter.add(name);
      return this;
   }

   public ShaderModifier afterShaderPack(String name) {
      this.shaderPacksBefore.add(name);
      return this;
   }

   private int _compare(ShaderModifier other) {
      if (this.shaderPacksBefore.contains(other.shaderPackName) || this.modifiersBefore.contains(other.name)) {
         return -2;
      } else {
         return !this.shaderPacksAfter.contains(other.shaderPackName) && !this.modifiersAfter.contains(other.name)
            ? -Float.compare(this.priority, other.priority)
            : 2;
      }
   }
}
