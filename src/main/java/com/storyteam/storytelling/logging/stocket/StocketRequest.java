package com.storyteam.storytelling.logging.stocket;

import java.util.Map;

public class StocketRequest {
   private final String method;
   private final Map<String, String> data;

   public StocketRequest(String method, Map<String, String> data) {
      this.method = method;
      this.data = data;
   }

   public String getMethod() {
      return this.method;
   }

   public Map<String, String> getData() {
      return this.data;
   }

   @Override
   public String toString() {
      return "StocketRequest{method='" + this.method + "', data=" + this.data + "}";
   }
}
