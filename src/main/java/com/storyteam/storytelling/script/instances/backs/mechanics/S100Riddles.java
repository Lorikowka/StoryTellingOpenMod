package com.storyteam.storytelling.script.instances.backs.mechanics;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.content.entities.RiddleEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.riddles.data.RiddlesDef;
import com.storyteam.storytelling.system.riddles.storage.RiddlesStorage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.zeith.hammerlib.util.java.Cast;

@SaveOnClose
@Script(-100)
public class S100Riddles extends BackInstance {
   public static final int SCRIPT_ID = -100;
   private static final List<Character<RiddleEntity>> RIDDLES = IntStream.range(0, RiddlesStorage.allAsList().size())
      .mapToObj(i -> constructDefaultRiddle("s100_back_riddle_" + i, RiddlesStorage.allAsList().get(i)))
      .collect(Collectors.toList());
   private static final Map<Character<RiddleEntity>, RiddlesDef> RIDDLE_MAP = IntStream.range(0, RiddlesStorage.allAsList().size())
      .boxed()
      .collect(Collectors.toMap(RIDDLES::get, RiddlesStorage.allAsList()::get));
   public ScriptPerformer MAIN = (new ScriptPerformerBuilderReflective(this) {
      @ScriptPerformerBuilderReflective.Secondary("spawn")
      private void spawn(S100Riddles inst) {
         this.thenDynamicPerformer(b -> {
            for (Character<RiddleEntity> r : S100Riddles.RIDDLES) {
               b.positionEntity(r, S100Riddles.RIDDLE_MAP.get(r).getCoordinate());
            }
         });
      }
   }).build(this::registerMainPerformer);

   public S100Riddles(ScriptEnvironment env, int id) {
      super(env, id);
   }

   private static Character<RiddleEntity> constructDefaultRiddle(String refId, RiddlesDef riddle) {
      return Character.builder(refId, Cast.constant(EntitiesST.RIDDLE)).onCreatedFresh(e -> e.setRiddle(riddle)).name("Загадка").build();
   }
}
