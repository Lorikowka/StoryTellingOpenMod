package com.storyteam.storytelling.system.reputation.preferences;

import com.storyteam.storytelling.script.story.characters.Character;
import java.util.HashMap;
import java.util.Map;

public class PreferenceRegistry {
   private static final Map<String, CharacterPreferences> REGISTRY = new HashMap<>();

   public static CharacterPreferences register(Character<?> character, CharacterPreferences preferences) {
      REGISTRY.put(character.getReferenceId(), preferences);
      return preferences;
   }

   public static CharacterPreferences get(Character<?> character) {
      return REGISTRY.get(character.getReferenceId());
   }
}
