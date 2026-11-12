package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.behaviour.BehaviourAction;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.entity.behaviour.complex.EntityPerformerBehaviour;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;

public class EntityPerformerInvokerAction<T extends Entity> extends BehaviourAction<T, EntityPerformerBehaviour<T>> {
   final Consumer<ScriptPerformerBuilder> action;
   final String[] layers;

   public EntityPerformerInvokerAction(
      ScriptPerformer performer, Character<T> character, Consumer<ScriptPerformerBuilder> action, boolean parallel, String... layers
   ) {
      super(performer, character, parallel, true);
      this.layers = layers;
      this.action = action;
   }

   @Override
   public void fastForwardToStage() {
      super.fastForwardToStage();
   }

   public EntityPerformerBehaviour<T> createBehaviour() {
      return new EntityPerformerBehaviour<>(this.action, this.layers);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      EntityPerformerBehaviour<T> beh = this.getBehavior();
      return beh != null && beh.getPerformer() != null ? builder.append(beh.getPerformer().gatherHelpReport()) : builder;
   }
}
