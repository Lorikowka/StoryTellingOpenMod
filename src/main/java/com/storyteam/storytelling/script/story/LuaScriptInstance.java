package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.PositionEntityAction;
import com.storyteam.storytelling.script.performer.action.RemoveEntityAction;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.HashMap;
import java.util.Map;
import org.zeith.hammerlib.api.io.NBTSerializable;

/** One shared Java wrapper for all externally registered Lua stories. */
@SaveOnClose
public final class LuaScriptInstance extends ScriptInstance {
   public final String definitionHash;
   @NBTSerializable
   public final ScriptPerformer mainFlow;

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
