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
import com.storyteam.storytelling.script.story.characters.Characters;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.api.io.NBTSerializable;

@ForgetOnClose
@Script(
   value = -8800,
   description = "Test script (sleepless)"
)
public class SSleep extends ScriptInstance {
   public static final int ID = -8800;
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0(SSleep inst) {
         this.positionEntity(Characters.ZEITHERON, "/execute in minecraft:overworld run tp @s -140.15 64.00 104.74 330.90 6.60");
         this.positionEntity(Characters.ARCHMAGE, "/execute in minecraft:overworld run tp @s -139.05 64.00 109.98 183.15 8.70");
         this.thenDelay(20);
         this.playerEmotion(new Emotion[]{Emotion.EYES, Emotion.THINKING_FACE, Emotion.SLIGHTLY_SMILING_FACE});
         this.thenWaitForF8();
         this.playerEmotion(new Emotion[]{Emotion.THUMBS_DOWN, Emotion.THUMBS_DOWN, Emotion.GRINNING_FACE_WITH_SMILING_EYES});
      }
   }).build(this::registerMainPerformer);

   public SSleep(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
      ScriptUtils.getLevel().setDayTime(18000L);
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
