package com.storyteam.storytelling.script.instances.backs.characters;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

@SaveOnClose
@Script(
   value = -304,
   description = "Мини-игра (трава)"
)
public class S304GrassMinigame extends BackInstance {
   public static final int SCRIPT_ID = -304;
   private static final Character<AnimatedEntity> NEON_GRASS = Character.builder("s304_neon_grass", Cast.constant(EntitiesST.NEON_GRASS)).name("Трава").build();
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.ScriptPart(0)
      private void part0(S304GrassMinigame inst) {
         this.positionEntity(S304GrassMinigame.NEON_GRASS, "/execute in minecraft:overworld run tp @s -5.50 103.00 -408.50 0.00 0.00");
         this.entitySit(S304GrassMinigame.NEON_GRASS);
      }
   }).build(this::registerMainPerformer);

   public S304GrassMinigame(ScriptEnvironment env, int id) {
      super(env, id);
   }
}
