package com.storyteam.storytelling.system.simulator.cognition_tab;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;

@SystemRoot("simulator/cognition")
public class CognitionSystem implements ISystem {
   public static CognitionSystem INSTANCE;

   public CognitionSystem() {
      INSTANCE = this;
      Cognitions.init();
   }
}
