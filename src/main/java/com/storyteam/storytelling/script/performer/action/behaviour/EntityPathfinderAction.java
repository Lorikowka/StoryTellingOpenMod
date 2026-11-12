package com.storyteam.storytelling.script.performer.action.behaviour;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityPathfinderBehaviour;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;

public class EntityPathfinderAction<T extends Mob> extends BehaviourAction<T, EntityPathfinderBehaviour<T>> {
   Supplier<List<Location>> positions;
   protected final double speed;
   boolean parallel;

   public EntityPathfinderAction(ScriptPerformer performer, Character<T> character, double speed, Supplier<List<Location>> poses) {
      super(performer, character);
      this.speed = speed;
      this.positions = poses;
   }

   public EntityPathfinderAction(ScriptPerformer performer, Character<T> character, double speed, Supplier<List<Location>> poses, boolean parallel) {
      super(performer, character);
      this.speed = speed;
      this.positions = poses;
      this.parallel = parallel;
   }

   @Override
   public boolean isDone() {
      return this.parallel || super.isDone();
   }

   @Override
   public void fastForwardToStage() {
      if (this.behavior != null) {
         this.optionalState().ifPresent(s -> s.stopBehaviour(this.behavior.getType()));
      }

      EntityState<T> entity = this.getEntity();
      List<Location> poses = this.positions.get();
      if (poses != null && !poses.isEmpty()) {
         entity.setPosition(poses.get(poses.size() - 1), true);
      }

      entity.markForReposition();
   }

   public EntityPathfinderBehaviour<T> createBehaviour() {
      return new EntityPathfinderBehaviour<>(this.positions, this.speed);
   }

   @Override
   public void rewindToStage() {
      super.rewindToStage();
      this.getEntity().setPosition(null);
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Путь персонажа [%s] (%s).".formatted(this.character.getName(), this.character.getReferenceId()));
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("speed", this.speed).build();
   }
}
