package com.storyteam.storytelling.script.instances;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(1111)
public class S1111 extends ScriptInstance {
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0(S1111 inst) {
         this.thenWaitForF8();
         this.setSkyEyeLookTarget("/execute in minecraft:overworld run tp @s -25.27 -59.00 -32.54 335.01 -33.45");
         this.thenWaitForF8();
         this.setSkyEyeLookTarget("player");
      }
   }).build(this::registerMainPerformer);

   public S1111(ScriptEnvironment env, int id) {
      super(env, id);
   }
}
