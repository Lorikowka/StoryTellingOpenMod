package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
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
}
