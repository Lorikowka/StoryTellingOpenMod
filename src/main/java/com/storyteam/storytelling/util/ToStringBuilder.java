package com.storyteam.storytelling.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ToStringBuilder {
   private final StringBuilder builder = new StringBuilder();
   private int fieldIndex;

   public static ToStringBuilder builder(Object self) {
      return new ToStringBuilder(self);
   }

   public ToStringBuilder(Object self) {
      this.builder.append(self.getClass().getSimpleName());
      this.builder.append("{");
   }

   public ToStringBuilder append(String fieldName, Object value) {
      if (value != null && value.getClass().isArray()) {
         List<Object> lst = new ArrayList<>();
         int i = 0;

         for (int l = Array.getLength(value); i < l; i++) {
            lst.add(Array.get(value, i));
         }

         value = lst.toString();
      }

      if (this.fieldIndex++ != 0) {
         this.builder.append(",");
      }

      this.builder.append(fieldName).append('=').append(value);
      return this;
   }

   public String build() {
      this.builder.append('}');
      String out = this.builder.toString();
      this.builder.deleteCharAt(this.builder.length() - 1);
      return out;
   }
}
