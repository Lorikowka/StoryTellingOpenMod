package com.storyteam.storytelling.system.char_icon;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;

@SystemRoot("char_icon")
public class CharIconSystem implements ISystem {
   public static CharIconSystem INSTANCE;

   public CharIconSystem() {
      INSTANCE = this;
   }
}
