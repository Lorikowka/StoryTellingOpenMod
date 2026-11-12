package com.storyteam.storytelling.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import lombok.Generated;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Since {
   Since.Season value();

   public static enum Season {
      GP("Игра Бога", "иб", false, 1),
      PW("Идеальный Мир", "им", false, 2),
      TV("Голос Времени", "гв", false, 3),
      TL("Тринадцать Огней", "то", false, 4),
      LR("Последняя Реальность", "пр", false, 5),
      DS("Сердце Вселенной", "св", false, 6),
      TN("Точка невозврата", "тн", false, 7),
      ST8("Что это блять", "чтоэтоблять", true, 8);

      final String displayName;
      final String hash;
      final boolean isCurrent;
      final int st;
      final String prefix;
      public static final Since.Season CURRENT = Arrays.stream(values()).filter(Since.Season::isCurrent).findFirst().orElseThrow();

      private Season(String displayName, String hash, boolean isCurrent, int st) {
         this.displayName = displayName;
         this.hash = hash;
         this.isCurrent = isCurrent;
         this.st = st;
         this.prefix = "st" + st + "/";
      }

      @Generated
      public String getDisplayName() {
         return this.displayName;
      }

      @Generated
      public boolean isCurrent() {
         return this.isCurrent;
      }

      @Generated
      public int getSt() {
         return this.st;
      }

      @Generated
      public String getPrefix() {
         return this.prefix;
      }

      @Generated
      public String getHash() {
         return this.hash;
      }
   }
}
