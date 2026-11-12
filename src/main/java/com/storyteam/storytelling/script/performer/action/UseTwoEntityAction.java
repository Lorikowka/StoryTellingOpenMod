package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import org.zeith.hammerlib.util.java.consumers.Consumer2;

public class UseTwoEntityAction<A extends Entity, B extends Entity> extends ScriptPerformerAction {
   private final Character<A> charA;
   private final Character<B> charB;
   private final Consumer2<? super A, ? super B> entityConsumer;

   public UseTwoEntityAction(ScriptPerformer performer, Character<A> charA, Character<B> charB, Consumer2<? super A, ? super B> entityConsumer) {
      super(performer);
      this.charA = charA;
      this.charB = charB;
      this.entityConsumer = entityConsumer;
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Прогрузка персонажей \"%s\" и \"%s\".", this.charA.getName(), this.charB.getName());
   }

   @Override
   public boolean isDone() {
      Optional<A> first = this.getEntity(this.charA).optionalValidOnly();
      Optional<B> second = this.getEntity(this.charB).optionalValidOnly();
      if (first.isPresent() && second.isPresent()) {
         this.entityConsumer.accept(first.get(), second.get());
         return true;
      } else {
         return false;
      }
   }

   public <T extends Entity> EntityState<T> getEntity(Character<T> character) {
      return this.getOrCreateEntity(character);
   }

   public <T extends Entity> EntityState<T> getOrCreateEntity(Character<T> character) {
      return this.script.getRepository().getOrCreate(character);
   }
}
