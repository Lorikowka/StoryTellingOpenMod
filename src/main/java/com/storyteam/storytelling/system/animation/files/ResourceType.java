package com.storyteam.storytelling.system.animation.files;

import lombok.Generated;

public enum ResourceType {
   ASSETS("assets"),
   DATA("data");

   private final String dir;

   @Generated
   public String getDir() {
      return this.dir;
   }

   @Generated
   private ResourceType(String dir) {
      this.dir = dir;
   }
}
