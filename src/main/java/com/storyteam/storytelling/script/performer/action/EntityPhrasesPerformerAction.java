package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.phrases.Phrase;
import com.storyteam.storytelling.script.performer.entity.behaviour.phrases.PhraseOrder;
import com.storyteam.storytelling.script.performer.entity.behaviour.phrases.PhrasesBehaviour;
import com.storyteam.storytelling.script.story.characters.Character;
import org.jetbrains.annotations.NotNull;

public class EntityPhrasesPerformerAction<T extends AnimatedEntity> extends EntityPerformerAction<T> {
   final Phrase<T>[] phrases;
   boolean wasStarted;
   PhraseOrder order = PhraseOrder.LOOP;

   @SafeVarargs
   public EntityPhrasesPerformerAction(ScriptPerformer performer, @NotNull Character<T> character, Phrase<T>... phrases) {
      super(performer, character);
      this.phrases = phrases;
   }

   @SafeVarargs
   public EntityPhrasesPerformerAction(ScriptPerformer performer, @NotNull Character<T> character, PhraseOrder order, Phrase<T>... phrases) {
      super(performer, character);
      this.phrases = phrases;
      this.order = order;
   }

   @Override
   public void tick() {
      if (!this.wasStarted) {
         this.wasStarted = true;
         PhrasesBehaviour<T> phraseBehaviour = new PhrasesBehaviour<>(this.character.getName(), this.order, this.phrases);
         this.getEntity().addBehaviour(phraseBehaviour);
      }
   }

   @Override
   public void reset() {
      this.wasStarted = false;
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().addBehaviour(new PhrasesBehaviour<>(this.character.getName(), this.order, this.phrases));
   }
}
