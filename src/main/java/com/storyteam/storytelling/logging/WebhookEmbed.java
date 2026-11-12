package com.storyteam.storytelling.logging;

import java.util.ArrayList;
import java.util.List;

public class WebhookEmbed {
   public int color = 0;
   public String description = null;
   public String title = null;
   public List<EmbedField> fields = new ArrayList<>();

   public WebhookEmbed setColor(int color) {
      this.color = color;
      return this;
   }

   public WebhookEmbed setDescription(String description) {
      this.description = description;
      return this;
   }

   public WebhookEmbed setTitle(String title) {
      this.title = title;
      return this;
   }

   public WebhookEmbed addField(EmbedField field) {
      this.fields.add(field);
      return this;
   }
}
