package com.storyteam.storytelling.system.particle;

public class FogSettings {
   public static final FogSettings NO_FOG = new FogSettings(0.0F, 0.0F, 1.0F);
   public float NECR0MANTH_FOG_COEFF1;
   public float NECR0MANTH_FOG_COEFF2;
   public float NECR0MANTH_FOG_EXP;

   public FogSettings(float NECR0MANTH_FOG_COEFF1, float NECR0MANTH_FOG_COEFF2, float NECR0MANTH_FOG_EXP) {
      this.NECR0MANTH_FOG_COEFF1 = NECR0MANTH_FOG_COEFF1;
      this.NECR0MANTH_FOG_COEFF2 = NECR0MANTH_FOG_COEFF2;
      this.NECR0MANTH_FOG_EXP = NECR0MANTH_FOG_EXP;
   }
}
