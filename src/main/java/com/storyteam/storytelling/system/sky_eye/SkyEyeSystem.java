package com.storyteam.storytelling.system.sky_eye;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;

@SystemRoot("sky_eye")
public class SkyEyeSystem implements ISystem {
   public static SkyEyeSystem INSTANCE;

   public SkyEyeSystem() {
      INSTANCE = this;
   }
}
