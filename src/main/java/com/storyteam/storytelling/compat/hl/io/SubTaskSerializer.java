package com.storyteam.storytelling.compat.hl.io;

import com.storyteam.storytelling.util.SubTask;
import net.minecraft.nbt.CompoundTag;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;

@NBTSerializer({SubTask.class})
public class SubTaskSerializer implements INBTSerializer<SubTask> {
   public void serialize(CompoundTag nbt, String key, SubTask loc) {
      if (loc != null) {
         CompoundTag sub = new CompoundTag();
         sub.putString("name", loc.name);
         sub.putBoolean("done", loc.done);
         nbt.put(key, sub);
      }
   }

   public SubTask deserialize(CompoundTag nbt, String key) {
      if (nbt.contains(key, 10)) {
         CompoundTag sub = nbt.getCompound(key);
         return new SubTask(sub.getString("name"), sub.getBoolean("done"));
      } else {
         return null;
      }
   }
}
