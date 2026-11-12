package com.storyteam.storytelling.system.rts.handlers;

import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.rts.common.RTSData;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.level.LevelEvent.Save;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DataHandler {
   @SubscribeEvent
   public static void load(Save event) {
      if (!event.getLevel().isClientSide()) {
         CompoundTag tag = ScriptIO.getSubTag("rts").getCompound("data");
         if (!tag.isEmpty()) {
            RTSData.get().deserializeNBT(tag);
         }
      }
   }

   @SubscribeEvent
   public static void save(Save event) {
      if (!event.getLevel().isClientSide()) {
         ScriptIO.getSubTag("rts").put("data", RTSData.get().serializeNBT());
      }
   }
}
