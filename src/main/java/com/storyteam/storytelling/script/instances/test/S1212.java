package com.storyteam.storytelling.script.instances.test;

import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.Characters;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammeranims.api.animation.IAnimationSource;

@Script(
   value = 1212,
   description = "Test script (Даша)"
)
public class S1212 extends ScriptInstance {
   public static final int ID = 1212;
   private static final CharacterList<AntEntity> ANTS = Characters.ANT_FAC.createCharacterList("s1_ants", 5);
   public ScriptPerformer MAIN = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0() {
         this.clearCharacterEmotion(Characters.ANT);
         this.positionEntity(S1212.ANTS.get(0), "/execute in minecraft:overworld run tp @s 57.48 82.00 45.88 -629.23 3.90");
         this.positionEntity(S1212.ANTS.get(1), "/execute in minecraft:overworld run tp @s 57.48 82.00 47.88 -629.23 3.90");
         this.thenWaitForF8();
         this.action(S1212.ANTS.get(0), new IAnimationSource[]{AnimationsST8.ANT_RUN});
         this.thenWaitForF8();
         this.stopAction(S1212.ANTS.get(0));
         this.thenWaitForF8();
         this.thenEntityPath(S1212.ANTS.get(0), new String[]{"/execute in minecraft:overworld run tp @s 41.52 82.00 45.88 -629.23 4.95"});
         this.thenWaitForF8();
         this.entityLookAt(Characters.ANT, "player");
      }
   }).build(this::registerMainPerformer);

   public S1212(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }
}
