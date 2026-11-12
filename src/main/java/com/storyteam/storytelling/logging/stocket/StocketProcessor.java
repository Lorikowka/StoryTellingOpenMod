package com.storyteam.storytelling.logging.stocket;

import java.util.HashMap;
import java.util.Map;

public class StocketProcessor {
   private final Map<String, StocketProcessor.IStocketProcessor> processorMap = new HashMap<>();

   public void registerProcessor(String method, StocketProcessor.IStocketProcessor processor) {
      this.processorMap.put(method, processor);
   }

   public void handle(StocketRequest request, StocketResponse response) {
      StocketProcessor.IStocketProcessor p = this.processorMap.get(request.getMethod());
      if (p != null) {
         p.handle(request, response);
      }
   }

   public interface IStocketProcessor {
      void handle(StocketRequest var1, StocketResponse var2);
   }
}
