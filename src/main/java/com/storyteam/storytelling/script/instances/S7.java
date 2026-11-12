package com.storyteam.storytelling.script.instances;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.LuaStory;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(7)
public class S7 extends ScriptInstance {
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0(S7 inst) {
         LuaStory.loadBundled("s7").appendTo(this, "setup");
      }

      @ScriptPerformerBuilderReflective.ScriptPart(1)
      private void part1(S7 inst) {
         LuaStory.loadBundled("s7").appendTo(this, "main");
      }
   }).build(this::registerMainPerformer);

   public S7(ScriptEnvironment env, int id) {
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
