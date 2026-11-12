package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.CharacterTarget;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Entity;

public class EntityLookAtCharacterAction<T extends Entity, U extends Entity> extends EntityPerformerAction<T> {
   private final Character<U> targetCharacter;
   private final ILookTarget<T> target;
   private final boolean isQuick;
   private final boolean waitForEnd;

   public EntityLookAtCharacterAction(ScriptPerformer performer, Character<T> character, Character<U> target, boolean isQuick, boolean waitForEnd) {
      super(performer, character);
      this.targetCharacter = target;
      this.target = new CharacterTarget<>(target);
      this.isQuick = isQuick;
      this.waitForEnd = waitForEnd;
   }

   @Override
   public void tick() {
      this.fastForwardToStage();
   }

   @Override
   public boolean isDone() {
      return !this.waitForEnd || this.getEntity().getData().getLookController().hasEndedRotation();
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setLookTarget(this.target);
      this.getEntity().getData().setQuick(this.isQuick);
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setLookTarget(null);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Поворот персонажа [%s] на персонажа [%s].", this.character.getName(), this.targetCharacter.getName());
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("target", this.targetCharacter).append("isQuick", this.isQuick).build();
   }
}
