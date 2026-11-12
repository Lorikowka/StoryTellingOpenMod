package com.storyteam.storytelling.system._core.structio.structure.v3;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import org.zeith.hammerlib.api.io.ICompoundSerializable;

public class StructureAttachments implements ICompoundSerializable {
   private final Map<String, StrAttachment> attachmentMap = new HashMap<>();

   public StrAttachment getOrCreate(String key) {
      return this.attachmentMap.computeIfAbsent(key, v -> new StrAttachment());
   }

   public StrAttachment get(String key) {
      return this.attachmentMap.get(key);
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      CompoundTag attachments = new CompoundTag();

      for (Entry<String, StrAttachment> s : this.attachmentMap.entrySet()) {
         attachments.put(s.getKey(), s.getValue().serializeNBT());
      }

      nbt.put("All", attachments);
      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.attachmentMap.clear();
      CompoundTag attachments = nbt.getCompound("All");

      for (String key : attachments.getAllKeys()) {
         if (attachments.getTagType(key) == 10) {
            StrAttachment att = new StrAttachment();
            att.deserializeNBT(attachments.getCompound(key));
            this.attachmentMap.put(key, att);
         }
      }
   }
}
