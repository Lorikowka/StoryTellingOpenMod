package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.NearestTarget;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityLookAtNearest<T extends Entity> extends EntityPerformerAction<T> {
   private final boolean waitForEnd;
   private final ILookTarget<T> lookTarget;
   private final boolean storytellingTarget;

   public EntityLookAtNearest(ScriptPerformer performer, @NotNull Character<T> character, double range, boolean storytellingTarget, boolean waitForEnd) {
      super(performer, character);
      this.waitForEnd = waitForEnd;
      this.storytellingTarget = storytellingTarget;
      this.lookTarget = new NearestTarget<>(range, storytellingTarget);
   }

   @Override
   public void tick() {
      this.fastForwardToStage();
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setLookTarget(this.lookTarget);
   }

   @Override
   public boolean isDone() {
      return !this.waitForEnd || this.getEntity().getData().getLookController().hasEndedRotation();
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setLookTarget(null);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      builder.append(this.storytellingTarget ? "Поворот персонажа на ближайшего персонажа." : "Поворот персонажа на ближайшую сущность.");
      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("target", this.lookTarget).build();
   }
}
