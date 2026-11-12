package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityLookAtLocAction<T extends Entity> extends EntityPerformerAction<T> {
   private final ILookTarget<T> target;
   private final boolean isQuick;
   private final boolean waitForEnd;

   public EntityLookAtLocAction(ScriptPerformer performer, Character<T> character, Supplier<Location> target, boolean waitForEnd, boolean isQuick) {
      super(performer, character);
      this.target = c -> target.get();
      this.isQuick = isQuick;
      this.waitForEnd = waitForEnd;
   }

   @Override
   public boolean isDone() {
      return !this.waitForEnd || this.getEntity().getData().getLookController().hasEndedRotation();
   }

   @Override
   public void tick() {
      this.fastForwardToStage();
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
      Vec3 target = this.target.getTarget(null);
      if (target != null) {
         builder.append("Поворот персонажа [%s] на позицию X:%d Y:%d Z:%d.", this.character.getName(), (int)target.x, (int)target.y, (int)target.z);
      }

      return builder;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("target", this.target).append("isQuick", this.isQuick).build();
   }
}
