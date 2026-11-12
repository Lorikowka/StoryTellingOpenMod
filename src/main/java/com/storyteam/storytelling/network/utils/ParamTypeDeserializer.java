package com.storyteam.storytelling.network.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class ParamTypeDeserializer implements JsonDeserializer<ParamType>, JsonSerializer<ParamType> {
   public ParamType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return ParamType.valueOf(json.getAsString());
   }

   public JsonElement serialize(ParamType src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src.name());
   }
}
