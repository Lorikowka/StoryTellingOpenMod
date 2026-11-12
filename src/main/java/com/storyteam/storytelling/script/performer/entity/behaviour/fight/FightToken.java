package com.storyteam.storytelling.script.performer.entity.behaviour.fight;

import java.util.Objects;
import lombok.Generated;

public class FightToken<O> {
   private final String name;
   private final Class<O> type;

   public FightToken(String name, Class<O> type) {
      this.name = name;
      this.type = type;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o instanceof FightToken<?> that) {
         return !Objects.equals(this.name, that.name) ? false : Objects.equals(this.type, that.type);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.name != null ? this.name.hashCode() : 0;
      return 31 * result + (this.type != null ? this.type.hashCode() : 0);
   }

   @Override
   public String toString() {
      return "FightToken{name='" + this.name + "', type=" + this.type + "}";
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Class<O> getType() {
      return this.type;
   }
}
