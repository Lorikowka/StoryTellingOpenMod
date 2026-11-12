package com.storyteam.storytelling.system.gating.jei;

public interface IJeiContext {
   static IJeiContext getInstance() {
      return IJeiContext.Instance.context;
   }

   public static class Instance {
      static IJeiContext context;
   }
}
