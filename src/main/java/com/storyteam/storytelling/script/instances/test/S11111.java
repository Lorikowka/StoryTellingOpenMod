package com.storyteam.storytelling.script.instances.test;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(
   value = 11111,
   description = "Test script (markovav)"
)
public class S11111 extends ScriptInstance {
   public static final int ID = 11111;
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0(S11111 inst) {
         this.thenSetSpeechEntity(0, "???", "sky_eye");
         this.thenSpeechMessage(0, "О, а это у нас кто?");
         this.thenWaitForF8();
         this.thenSpeechMessage(0, "Формики, подойдите сюда.");
         this.thenWaitForF8();
         this.thenSpeechMessage(0, "Посмотрите, какой маленький.");
      }
   }).build(this::registerMainPerformer);

   public S11111(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }

   @Override
   public void handleStop() {
      this.getRepository().removeAll();
      super.handleStop();
   }

   @Override
   public void handleStart() {
      super.handleStart();
      this.handleEvent(EventType.ENTITY_CLICK, this::onClick);
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this).build();
   }

   public void onClick(EntityInteractionData ctx) {
   }
}
