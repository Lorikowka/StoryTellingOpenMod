package com.storyteam.storytelling.compat.iris;

import org.jetbrains.annotations.Nullable;

public interface IrisProxy {
   @Nullable
   String getCurrentShader();

   void changeShader(@Nullable String var1);

   static IrisProxy getInstance() {
      return IrisProxy.Instance.instance;
   }

   public static class Instance {
      static IrisProxy instance = new IrisProxy() {
         @Override
         public String getCurrentShader() {
            return "";
         }

         @Override
         public void changeShader(String shaderZipFileName) {
         }
      };
   }
}
