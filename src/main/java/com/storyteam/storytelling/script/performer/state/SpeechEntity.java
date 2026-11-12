package com.storyteam.storytelling.script.performer.state;

import com.storyteam.storytelling.script.story.characters.Character;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public class SpeechEntity {
   public static final int SEASON_BRACKET_COLOR = 16750070;
   private final String name;
   private final int color;
   private final String icon;

   public SpeechEntity(String name, int color, String icon) {
      this.name = name;
      this.color = color;
      this.icon = icon;
   }

   public SpeechEntity(String name, int color) {
      this.name = name;
      this.color = color;
      this.icon = "";
   }

   public SpeechEntity(String name) {
      this.name = name;
      this.color = 16750070;
      this.icon = "";
   }

   public SpeechEntity(@NotNull Character<?> character) {
      this.name = character.getName();
      this.color = 16750070;
      this.icon = character.getReferenceId();
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public int getColor() {
      return this.color;
   }

   @Generated
   public String getIcon() {
      return this.icon;
   }
}
