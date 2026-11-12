package com.storyteam.storytelling.system.particle;

public abstract class AbstractParticleType {
   public abstract void setup();

   @Override
   public final boolean equals(Object other) {
      if (other instanceof AbstractParticleType type && this.equals(type)) {
         return true;
      }

      return false;
   }

   public abstract boolean equals(AbstractParticleType var1);

   @Override
   public int hashCode() {
      return 0;
   }
}
