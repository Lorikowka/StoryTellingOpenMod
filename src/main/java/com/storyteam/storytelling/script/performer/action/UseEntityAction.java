package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;

public class UseEntityAction<T extends Entity> extends EntityPerformerAction<T> {
   private final Consumer<? super T> entityConsumer;

   public UseEntityAction(ScriptPerformer performer, Character<T> character, Consumer<? super T> entityConsumer) {
      super(performer, character);
      this.entityConsumer = entityConsumer;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Прогрузка персонажа \"%s\".", this.character.getName());
   }

   @Override
   public boolean isDone() {
      return this.getEntity().optionalValidOnly().map(e -> {
         this.entityConsumer.accept(e);
         return true;
      }).orElse(false);
   }
}
