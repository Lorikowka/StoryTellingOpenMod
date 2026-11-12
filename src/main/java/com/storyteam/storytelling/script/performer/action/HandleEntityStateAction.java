package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ServerScheduler;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;

public class HandleEntityStateAction<T extends Entity> extends EntityPerformerAction<T> {
   private final HandleEntityStateAction.EntityStateHandler<T> handler;

   public HandleEntityStateAction(ScriptPerformer performer, Character<T> character, HandleEntityStateAction.EntityStateHandler<T> handler) {
      super(performer, character);
      this.handler = handler;
   }

   public HandleEntityStateAction(ScriptPerformer performer, Character<T> character, final Consumer<? super T> before, final Consumer<? super T> after) {
      super(performer, character);
      this.handler = new HandleEntityStateAction.EntityStateHandler<T>() {
         public void onPassed(T entity) {
            after.accept(entity);
         }

         public void onRewind(T entity) {
            before.accept(entity);
         }
      };
   }

   public HandleEntityStateAction(ScriptPerformer performer, Character<T> character, final BiConsumer<Boolean, ? super T> ifPassed) {
      super(performer, character);
      this.handler = new HandleEntityStateAction.EntityStateHandler<T>() {
         public void onPassed(T entity) {
            ifPassed.accept(true, entity);
         }

         public void onRewind(T entity) {
            ifPassed.accept(false, entity);
         }
      };
   }

   @Override
   public void fastForwardToStage() {
      ServerScheduler.executeWhen(
         () -> this.getEntity().optionalValidOnly().isPresent(), () -> this.getEntity().optionalValidOnly().ifPresent(this.handler::onPassed), 1200
      );
   }

   @Override
   public void rewindToStage() {
      this.getEntity().optionalValidOnly().ifPresent(this.handler::onRewind);
   }

   @Override
   public boolean isDone() {
      return this.getEntity().optionalValidOnly().map(e -> {
         if (e.isAlive() && e.isAddedToWorld()) {
            this.handler.onPassed((T)e);
            return true;
         } else {
            return false;
         }
      }).orElse(false);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Прогрузка персонажа \"%s\".", this.character.getName());
   }

   public interface EntityStateHandler<T> {
      void onPassed(T var1);

      void onRewind(T var1);
   }
}
