package com.storyteam.storytelling.script.instances.backs.mechanics;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import java.util.UUID;
import java.util.function.Consumer;
import org.zeith.hammerlib.api.io.NBTSerializable;

@Script(-1300)
public class S1300GiftPerformers extends BackInstance {
   public static final int SCRIPT_ID = -1300;
   @NBTSerializable
   public final ScriptPerformer mainFlow = new ScriptPerformerBuilder(this).thenInfiniteSleep().build(this::registerMainPerformer);

   public S1300GiftPerformers(ScriptEnvironment env, int id) {
      super(env, id);
   }

   public static void perform(Consumer<ScriptPerformerBuilder> builder) {
      perform(System.currentTimeMillis() + "." + System.nanoTime() + "." + UUID.randomUUID(), builder);
   }

   public static void perform(String id, Consumer<ScriptPerformerBuilder> builder) {
      IStoryServer srv = IStoryServer.get();
      if (srv != null) {
         ScriptEnvironment env = srv.getScriptEnv();
         env.start(-1300);
         ScriptInstance si = env.getScript(-1300);
         if (si == null) {
            Storytelling.LOG.error("Unable to obtain active instance of {}. Failing to start parallel performer.", S1300GiftPerformers.class.getName());
         } else {
            ScriptPerformerBuilder sb = new ScriptPerformerBuilder(si).appendBuilder(builder);
            si.addParallelPerformer(id, sb.build());
         }
      }
   }
}
