package com.storyteam.storytelling.system.animation.backend;

import com.storyteam.storytelling.script.story.characters.RenderCharacter;
import com.storyteam.storytelling.script.story.characters.RenderCharacters;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.Util;

public class RenderCharacterBackend {
   public static final Map<String, RenderCharacter> CHARACTERS = Collections.unmodifiableMap(
      (Map<? extends String, ? extends RenderCharacter>)Util.make(new HashMap(), m -> {
         for (Field f : RenderCharacters.class.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod) && RenderCharacter.class.equals(f.getType())) {
               try {
                  m.put(f.getName(), (RenderCharacter)f.get(null));
               } catch (IllegalAccessException var7) {
                  throw new RuntimeException(var7);
               }
            }
         }
      })
   );
   public static final String DEFAULT_HUMAN = findName(RenderCharacters.DEFAULT_CHARACTER);

   private static String findName(RenderCharacter character) {
      return CHARACTERS.entrySet().stream().filter(e -> e.getValue() == character).findFirst().map(Entry::getKey).orElseThrow();
   }
}
