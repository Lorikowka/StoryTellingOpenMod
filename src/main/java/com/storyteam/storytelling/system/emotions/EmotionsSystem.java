package com.storyteam.storytelling.system.emotions;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;

@SystemRoot("emotions")
public class EmotionsSystem implements ISystem {
   public static EmotionsSystem INSTANCE;

   public EmotionsSystem() {
      INSTANCE = this;
   }
}
