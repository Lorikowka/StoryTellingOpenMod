package com.storyteam.storytelling.system.gating.io;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.base.ScriptIOEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system.gating.LockedEntry;
import com.storyteam.storytelling.system.gating.event.GatherHiddenItemsEvent;
import com.storyteam.storytelling.system.gating.net.PacketRefreshLockedItems;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.event.player.PlayerLoadedInEvent;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.mcf.Resources;

public class LockedEntriesDB {
   private static final Set<ResourceLocation> KNOWN_ENTRIES = new HashSet<>();
   private static final Set<ResourceLocation> UNLOCKED_ENTRIES = new HashSet<>();
   private static LockedContentCache cache;

   public static boolean isKnownEntry(ResourceLocation id) {
      return KNOWN_ENTRIES.contains(id);
   }

   public static boolean isUnlockedEntry(ResourceLocation id) {
      return UNLOCKED_ENTRIES.contains(id);
   }

   public static LockedContentCache getContent() {
      if (cache == null) {
         Set<Item> items = new HashSet<>();
         Set<ResourceLocation> recipes = new HashSet<>();
         LockedEntry.stream().filter(e -> !e.isUnlocked()).forEach(le -> {
            items.addAll(le.getItems().stream().map(ItemLike::asItem).toList());
            recipes.addAll(le.getRecipes());
         });
         cache = new LockedContentCache(Set.copyOf(items), Set.copyOf(recipes));
      }

      return cache;
   }

   public static void recordChange(Consumer<WriteOp> operation) {
      operation.accept(new WriteOp(c -> c.accept(KNOWN_ENTRIES), c -> c.accept(UNLOCKED_ENTRIES)));
      cache = null;
      Network.sendToAll(new PacketRefreshLockedItems());
   }

   @SubscribeEvent
   public static void gatherHiddenContent(GatherHiddenItemsEvent e) {
      LockedContentCache content = getContent();
      content.hiddenItems().forEach(e::hide);
      e.hideAllRecipes(content.hiddenRecipes());
   }

   @SubscribeEvent
   public static void playerReady(PlayerLoadedInEvent e) {
      Network.sendTo(e.getEntity(), new PacketRefreshLockedItems());
   }

   @SubscribeEvent
   public static void save(ScriptIOEvent.Save e) {
      save(e.getSystemDirectory("content-gating"));
   }

   @SubscribeEvent
   public static void load(ScriptIOEvent.Load e) {
      load(e.getSystemDirectory("content-gating"));
   }

   private static void save(File dir) {
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();

      for (ResourceLocation UNLOCKED_ENTRY : UNLOCKED_ENTRIES) {
         list.add(StringTag.valueOf(UNLOCKED_ENTRY.toString()));
      }

      tag.put("Unlocks", list);
      list = new ListTag();

      for (ResourceLocation rl : KNOWN_ENTRIES) {
         list.add(StringTag.valueOf(rl.toString()));
      }

      tag.put("Known", list);
      ScriptIO.save(tag, new File(dir, "locked-entries.dat"));
      Storytelling.LOG.info("[LockedEntriesDB] Saved {} known entries, {} of which are unlocked.", KNOWN_ENTRIES.size(), UNLOCKED_ENTRIES.size());
   }

   private static void load(File dir) {
      CompoundTag tag = ScriptIO.readOrDefault(new File(dir, "locked-entries.dat"), CompoundTag::new);
      Set<ResourceLocation> knownEntries = new HashSet<>();
      Set<ResourceLocation> unlockedEntries = new HashSet<>();
      ListTag list = tag.getList("Unlocks", 8);
      int i = 0;

      for (int j = list.size(); i < j; i++) {
         unlockedEntries.add(Resources.location(list.getString(i)));
      }

      list = tag.getList("Known", 8);
      i = 0;

      for (int j = list.size(); i < j; i++) {
         knownEntries.add(Resources.location(list.getString(i)));
      }

      recordChange(op -> op.modifyKnownEntries(ke -> {
            ke.clear();
            ke.addAll(knownEntries);
         }).modifyUnlockedEntries(ue -> {
            ue.clear();
            ue.addAll(unlockedEntries);
         }));
      Storytelling.LOG.info("[LockedEntriesDB] Loaded {} known entries, {} of which are unlocked.", knownEntries.size(), unlockedEntries.size());
   }
}
