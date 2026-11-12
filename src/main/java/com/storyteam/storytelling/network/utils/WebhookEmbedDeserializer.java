package com.storyteam.storytelling.network.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.storyteam.storytelling.logging.EmbedField;
import com.storyteam.storytelling.logging.WebhookEmbed;
import java.lang.reflect.Type;

public class WebhookEmbedDeserializer implements JsonDeserializer<WebhookEmbed>, JsonSerializer<WebhookEmbed> {
   public WebhookEmbed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      WebhookEmbed embed = new WebhookEmbed();
      json.getAsJsonObject().entrySet().forEach(e -> {
         String var2x = (String)e.getKey();
         switch (var2x) {
            case "color":
               embed.setColor(((JsonElement)e.getValue()).getAsInt());
               break;
            case "description":
               embed.setDescription(((JsonElement)e.getValue()).getAsString());
               break;
            case "title":
               embed.setTitle(((JsonElement)e.getValue()).getAsString());
               break;
            case "fields":
               ((JsonElement)e.getValue()).getAsJsonArray().forEach(f -> {
                  JsonObject field = f.getAsJsonObject();
                  embed.addField(new EmbedField(field.get("name").getAsString(), field.get("value").getAsString(), field.get("inline").getAsBoolean()));
               });
         }
      });
      return embed;
   }

   public JsonElement serialize(WebhookEmbed src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      json.addProperty("color", src.color);
      json.addProperty("description", src.description);
      json.addProperty("title", src.title);
      JsonArray fields = new JsonArray();
      src.fields.forEach(f -> {
         JsonObject field = new JsonObject();
         field.addProperty("name", f.getName());
         field.addProperty("value", f.getValue());
         field.addProperty("inline", f.isInline());
         fields.add(field);
      });
      json.add("fields", fields);
      return json;
   }
}
