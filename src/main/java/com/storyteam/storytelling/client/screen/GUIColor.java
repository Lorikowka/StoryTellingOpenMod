package com.storyteam.storytelling.client.screen;

import lombok.Generated;

public enum GUIColor {
   WHITE(14932164),
   RED(13921881),
   GREEN(9619807);

   int hex;

   @Generated
   public int getHex() {
      return this.hex;
   }

   @Generated
   private GUIColor(int hex) {
      this.hex = hex;
   }
}
