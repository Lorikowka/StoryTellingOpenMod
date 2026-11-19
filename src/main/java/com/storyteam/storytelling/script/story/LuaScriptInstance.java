package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.data.ScriptFlagStorage;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.PositionEntityAction;
import com.storyteam.storytelling.script.performer.action.RemoveEntityAction;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.zeith.hammerlib.api.io.NBTSerializable;

/** One shared Java wrapper for all externally registered Lua stories. */
@SaveOnClose
public final class LuaScriptInstance extends ScriptInstance {
   public final String definitionHash;
   public String runToken = UUID.randomUUID().toString();
   @NBTSerializable
   public final ScriptPerformer mainFlow;
   @NBTSerializable
   public final ScriptFlagStorage sceneVariables = new ScriptFlagStorage();
   private final Set<String> startedParallelBranches = new LinkedHashSet<>();

   public LuaScriptInstance(ScriptEnvironment env, int id) {
      super(env, id);
      LuaStory story = env.luaStories.get(id);
      if (story == null) {
         throw new IllegalArgumentException("Unregistered Lua story " + id);
      }
      this.definitionHash = story.fingerprint();
      ScriptPerformerBuilder builder = new ScriptPerformerBuilder(this);
      var characters = story.createNpcCharacters();
      if (story.hasPart("setup")) {
         story.appendTo(builder, "setup", characters);
      }
      story.appendTo(builder, "main", characters);
      this.mainFlow = builder.build(this::registerMainPerformer);
   }

   @Override public CompoundTag serializeNBT() {
      CompoundTag tag = super.serializeNBT();
      tag.putString("luaRunToken", runToken);
      ListTag branches = new ListTag();
      for (String key : startedParallelBranches) {
         CompoundTag branch = new CompoundTag();
         branch.putString("key", key);
         ScriptPerformer performer = getParallelPerformer(key);
         branch.putString("status", performer == null ? "done" : "running");
         if (performer != null) branch.put("performer", performer.serializeNBT());
         branches.add(branch);
      }
      tag.put("luaParallelBranches", branches);
      return tag;
   }

   @Override public void deserializeNBT(CompoundTag tag) {
      runToken = readRunToken(tag, runToken);
      super.deserializeNBT(tag);
      if (!tag.contains("luaParallelBranches", Tag.TAG_LIST)) return;
      ListTag branches = tag.getList("luaParallelBranches", Tag.TAG_COMPOUND);
      for (int i = 0; i < branches.size(); i++) {
         CompoundTag branch = branches.getCompound(i);
         String key = branch.getString("key");
         if (!startedParallelBranches.add(key)) {
            throw new IllegalStateException("Duplicate Lua parallel branch in save: " + key);
         }
         String status = branch.getString("status");
         if (status.equals("done")) continue;
         if (!status.equals("running") || !branch.contains("performer", Tag.TAG_COMPOUND)) {
            throw new IllegalStateException("Invalid saved Lua parallel branch: " + key);
         }
         ScriptPerformer performer = createParallelBranch(key);
         performer.deserializeNBT(branch.getCompound("performer"));
      }
   }

   /** Starts a checkpointed wait-only part for the Lua parallel_start action. */
   void startParallelBranch(String key) {
      if (!startedParallelBranches.add(key)) {
         throw new IllegalStateException("Lua parallel branch already started: " + key);
      }
      try {
         createParallelBranch(key);
      } catch (RuntimeException error) {
         startedParallelBranches.remove(key);
         throw error;
      }
   }

   boolean isParallelBranchDone(String key) {
      if (!startedParallelBranches.contains(key)) {
         throw new IllegalArgumentException("Lua parallel branch was not started: " + key);
      }
      ScriptPerformer performer = getParallelPerformer(key);
      return performer == null || performer.isCompleted();
   }

   public boolean hasStartedParallelBranches() {
      return !startedParallelBranches.isEmpty();
   }

   private ScriptPerformer createParallelBranch(String key) {
      LuaStory story = env.luaStories.get(getId());
      if (key.equals("main") || key.equals("setup") || !story.isWaitOnlyPart(key)) {
         throw new IllegalArgumentException("Lua parallel branch must be a wait_ticks-only part: " + key);
      }
      ScriptPerformerBuilder builder = new ScriptPerformerBuilder(this);
      story.appendTo(builder, key);
      ScriptPerformer performer = builder.build();
      addParallelPerformer(key, performer);
      return performer;
   }

   static String readRunToken(CompoundTag tag, String fallback) {
      String token = tag.getString("luaRunToken");
      return token.isBlank() ? fallback : token;
   }

   /** Reattach NPCs that were spawned before the saved action index. */
   public void restoreNpcEntities() {
      LuaStory story = env.luaStories.get(getId());
      String prefix = "lua_" + getId() + "_";
      Map<String, Location> spawnedPositions = new HashMap<>();
      int completed = Math.min(mainFlow.getActionIndex(), mainFlow.getActionsAmount());
      for (int i = 0; i < completed; i++) {
         var action = mainFlow.getActions().get(i);
         if (action instanceof PositionEntityAction<?> spawn) {
            String referenceId = spawn.getCharacter().getReferenceId();
            if (referenceId.startsWith(prefix)) spawnedPositions.put(referenceId, spawn.getTargetPosition());
         } else if (action instanceof RemoveEntityAction<?> remove) {
            spawnedPositions.remove(remove.getCharacter().getReferenceId());
         }
      }
      story.createNpcCharacters().values().forEach(character -> {
         String referenceId = character.getReferenceId();
         var controller = env.server.getEntityRepository().get(character);
         Location position = controller == null ? null : controller.getLastRegisteredPos();
         if (position == null) position = spawnedPositions.get(referenceId);
         if (position == null) return;
         var state = getRepository().getOrCreate(character);
         state.setPosition(position);
         state.markForReposition();
         getRepository().getOrCreate(character, true);
         if (controller == null) Storytelling.LOG.info("Restored Lua NPC {} from the last completed spawn", referenceId);
      });
   }
}
