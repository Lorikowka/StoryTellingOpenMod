package com.storyteam.storytelling.script.performer.action;

import net.minecraft.nbt.CompoundTag;

/** State of the active action, stored alongside the performer's action index. */
public interface ActionCheckpoint {
   CompoundTag saveActionState();

   void restoreActionState(CompoundTag state);
}
