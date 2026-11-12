package com.storyteam.storytelling.logging;

public class EmbedField {
   private final String name;
   private final String value;
   private final boolean inline;

   public EmbedField(String name, String value, boolean inline) {
      this.name = name;
      this.value = value;
      this.inline = inline;
   }

   public EmbedField(boolean inline, String name, String value) {
      this.name = name;
      this.value = value;
      this.inline = inline;
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public boolean isInline() {
      return this.inline;
   }
}
