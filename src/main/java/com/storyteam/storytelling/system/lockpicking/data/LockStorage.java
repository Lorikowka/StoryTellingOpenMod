package com.storyteam.storytelling.system.lockpicking.data;

import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.lockpicking.LockpickingSystem;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

public class LockStorage {
   public static final Map<GlobalPos, LockRegistryEntry> REGISTRY = new HashMap<>();
   private static final Map<GlobalPos, LockRegistryEntry> BY_POS = new HashMap<>();
   public static File REGISTRY_FILE;

   public static CompoundTag serialize() {
      CompoundTag tag = new CompoundTag();
      ListTag entries = new ListTag();
      for (LockRegistryEntry entry : REGISTRY.values()) {
         entries.add(entry.entry.serializeNBT());
      }
      tag.put("Registry", entries);
      return tag;
   }

   public static void recomputePos() {
      BY_POS.clear();

      for (LockRegistryEntry v : REGISTRY.values()) {
         v.positions().forEach(pos -> BY_POS.put(pos, v));
      }
   }

   @SubscribeEvent
   public static void load(ScriptIOEvent.Load load) {
      File dir = load.getSystemDirectory(LockpickingSystem.INSTANCE);
      REGISTRY_FILE = new File(dir, "registry.dat");
      REGISTRY.clear();
      BY_POS.clear();

      if (REGISTRY_FILE.isFile()) {
         CompoundTag tag = ScriptIO.readOrDefault(REGISTRY_FILE, CompoundTag::new);
         ListTag registry = tag.getList("Registry", 10);

         for (int i = 0; i < registry.size(); i++) {
            LockEntry data = new LockEntry();
            data.deserializeNBT(registry.getCompound(i));
            REGISTRY.put(data.getPos(), new LockRegistryEntry(data));
         }

         recomputePos();
         CompoundTag data = ScriptIO.readOrDefault(new File(dir, "core.dat"), CompoundTag::new);
         ListTag unlocks = data.getList("Unlocks", 10);

         for (int i = 0; i < unlocks.size(); i++) {
            CompoundTag tg = unlocks.getCompound(i);
            LockRegistryEntry reg = REGISTRY.get((new LockRegistryEntry(tg)).root);
            if (reg != null) {
               reg.deserializeNBT(tg);
            }
         }
      }
   }

   @SubscribeEvent
   public static void save(ScriptIOEvent.Save save) {
      File dir = save.getSystemDirectory(LockpickingSystem.INSTANCE);
      REGISTRY_FILE = new File(dir, "registry.dat");
      CompoundTag data = new CompoundTag();
      ListTag unlocks = new ListTag();

      for (LockRegistryEntry value : REGISTRY.values()) {
         unlocks.add(value.serializeNBT());
      }

      data.put("Unlocks", unlocks);
      ScriptIO.save(data, new File(dir, "core.dat"));
      saveRegistryLocally();
   }

   public static boolean saveRegistryLocally() {
      return REGISTRY_FILE != null && ScriptIO.save(serialize(), REGISTRY_FILE);
   }

   @Nullable
   public static LockRegistryEntry getLock(GlobalPos pos) {
      return BY_POS.get(pos);
   }

   public static void removeLock(GlobalPos pos) {
      LockRegistryEntry data = getLock(pos);
      if (data != null) {
         REGISTRY.remove(data.getPos());

         for (BlockPos blockPos : data.entry.getTotal()) {
            BY_POS.remove(GlobalPos.of(data.getPos().dimension(), blockPos));
         }
      }
   }

   public static void registerLock(LockEntry entry) {
      LockRegistryEntry reg = new LockRegistryEntry(entry);
      REGISTRY.put(entry.getPos(), reg);
      ResourceKey<Level> dim = entry.getPos().dimension();

      for (BlockPos blockPos : entry.getTotal()) {
         GlobalPos globalPos = GlobalPos.of(dim, blockPos);
         BY_POS.put(globalPos, reg);
      }
   }
}
