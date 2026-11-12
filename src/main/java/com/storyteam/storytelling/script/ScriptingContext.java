package com.storyteam.storytelling.script;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.story.ScriptEnvironment;

public class ScriptingContext {
   public final ScriptInstance script;
   public final ScriptEnvironment env;
   public final IStoryServer server;

   public ScriptingContext(ScriptEnvironment env, ScriptInstance script) {
      this.script = script;
      this.env = env;
      this.server = env.server;
   }

   public <T> void postEvent(EventType<T> type, T event) {
      this.script.passEvent2Stage(type, event);
   }
}
