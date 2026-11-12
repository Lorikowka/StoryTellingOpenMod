package com.storyteam.storytelling.system._core.character_data;

import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

public class CharacterDataAttachment implements IAutoNBTSerializable {
   public final String name;

   public CharacterDataAttachment(String name) {
      this.name = name;
   }
}
