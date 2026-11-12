package com.storyteam.storytelling.util.data;

import com.storyteam.storytelling.script.story.characters.Character;
import net.minecraft.world.entity.Entity;

public interface IEntityInfoPack<T extends Entity> {
   Character<T> getCharacter();

   T getEntity();

   default boolean isOf(Character<?> character) {
      return this.getCharacter() == character;
   }
}
