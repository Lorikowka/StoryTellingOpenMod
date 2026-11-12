package com.storyteam.storytelling.compat.hl.io;

import net.minecraft.nbt.CompoundTag;
import org.joml.Vector2i;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;

@NBTSerializer({Vector2i.class})
public class Vector2iSerializer implements INBTSerializer<Vector2i> {
   public void serialize(CompoundTag nbt, String key, Vector2i loc) {
      if (loc != null) {
         CompoundTag sub = new CompoundTag();
         sub.putInt("x", loc.x);
         sub.putInt("y", loc.y);
         nbt.put(key, sub);
      }
   }

   public Vector2i deserialize(CompoundTag nbt, String key) {
      if (nbt.contains(key, 10)) {
         CompoundTag sub = nbt.getCompound(key);
         return new Vector2i(sub.getInt("x"), sub.getInt("y"));
      } else {
         return null;
      }
   }
}
