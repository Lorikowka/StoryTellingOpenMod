package com.storyteam.storytelling.compat.hl.io;

import com.storyteam.storytelling.util.PickupList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;

@NBTSerializer({PickupList.class})
public class PickupListSerializer implements INBTSerializer<PickupList> {
   public void serialize(CompoundTag $, String key, PickupList lst) {
      if (lst != null) {
         CompoundTag root = new CompoundTag();
         root.put("Waiting", lst.serializeNBT());
         ListTag initial = new ListTag();
         ObjectIterator var6 = lst.getInitial().object2IntEntrySet().iterator();

         while (var6.hasNext()) {
            Entry<PickupList.TaggedItem> e = (Entry<PickupList.TaggedItem>)var6.next();
            CompoundTag tag = ((PickupList.TaggedItem)e.getKey()).serializeNBT();
            tag.putInt("Count", e.getIntValue());
            initial.add(tag);
         }

         root.put("Initial", initial);
         $.put(key, root);
      }
   }

   public PickupList deserialize(CompoundTag $, String key) {
      CompoundTag root = $.getCompound(key);
      PickupList lst = new PickupList();
      Object2IntArrayMap<PickupList.TaggedItem> initial = lst.getInitial();
      ListTag initialNbt = root.getList("Initial", 10);

      for (int i = 0; i < initialNbt.size(); i++) {
         CompoundTag tag = initialNbt.getCompound(i);
         initial.put(PickupList.TaggedItem.read(tag), tag.getInt("Count"));
      }

      lst.deserializeNBT(root.getList("Waiting", 10));
      return lst;
   }
}
